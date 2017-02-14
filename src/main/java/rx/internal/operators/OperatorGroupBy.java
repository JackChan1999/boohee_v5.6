package rx.internal.operators;

import android.support.v4.media.session.PlaybackStateCompat;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Observable$Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public class OperatorGroupBy<T, K, R> implements Observable$Operator<GroupedObservable<K, R>, T> {
    private static final Func1<Object, Object> IDENTITY = new Func1<Object, Object>() {
        public Object call(Object t) {
            return t;
        }
    };
    private static final Object NULL_KEY = new Object();
    final Func1<? super T, ? extends K> keySelector;
    final Func1<? super T, ? extends R> valueSelector;

    static final class GroupBySubscriber<K, T, R> extends Subscriber<T> {
        static final AtomicLongFieldUpdater<GroupBySubscriber> BUFFERED_COUNT = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "bufferedCount");
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> COMPLETION_EMITTED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "completionEmitted");
        private static final int MAX_QUEUE_SIZE = 1024;
        static final AtomicLongFieldUpdater<GroupBySubscriber> REQUESTED = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "requested");
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> TERMINATED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "terminated");
        private static final int TERMINATED_WITH_COMPLETED = 1;
        private static final int TERMINATED_WITH_ERROR = 2;
        private static final int UNTERMINATED = 0;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> WIP_FOR_UNSUBSCRIBE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "wipForUnsubscribe");
        private static final NotificationLite<Object> nl = NotificationLite.instance();
        volatile long bufferedCount;
        final Subscriber<? super GroupedObservable<K, R>> child;
        volatile int completionEmitted;
        final Func1<? super T, ? extends R> elementSelector;
        private final ConcurrentHashMap<Object, GroupState<K, T>> groups = new ConcurrentHashMap();
        final Func1<? super T, ? extends K> keySelector;
        volatile long requested;
        final GroupBySubscriber<K, T, R> self = this;
        volatile int terminated = 0;
        volatile int wipForUnsubscribe = 1;

        private static class GroupState<K, T> {
            private final Queue<Object> buffer;
            private final AtomicLong count;
            private final AtomicLong requested;
            private final Subject<T, T> s;

            private GroupState() {
                this.s = BufferUntilSubscriber.create();
                this.requested = new AtomicLong();
                this.count = new AtomicLong();
                this.buffer = new ConcurrentLinkedQueue();
            }

            public Observable<T> getObservable() {
                return this.s;
            }

            public Observer<T> getObserver() {
                return this.s;
            }
        }

        public GroupBySubscriber(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> elementSelector, Subscriber<? super GroupedObservable<K, R>> child) {
            this.keySelector = keySelector;
            this.elementSelector = elementSelector;
            this.child = child;
            child.add(Subscriptions.create(new Action0() {
                public void call() {
                    if (GroupBySubscriber.WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(GroupBySubscriber.this.self) == 0) {
                        GroupBySubscriber.this.self.unsubscribe();
                    }
                }
            }));
        }

        public void onStart() {
            REQUESTED.set(this, PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            request(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        }

        public void onCompleted() {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 1)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.completed());
                }
                if (this.groups.isEmpty() && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                    this.child.onCompleted();
                }
            }
        }

        public void onError(Throwable e) {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 2)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.error(e));
                }
                try {
                    this.child.onError(e);
                } finally {
                    unsubscribe();
                }
            }
        }

        void requestFromGroupedObservable(long n, GroupState<K, T> group) {
            BackpressureUtils.getAndAddRequest(group.requested, n);
            if (group.count.getAndIncrement() == 0) {
                pollQueue(group);
            }
        }

        private Object groupedKey(K key) {
            return key == null ? OperatorGroupBy.NULL_KEY : key;
        }

        private K getKey(Object groupedKey) {
            return groupedKey == OperatorGroupBy.NULL_KEY ? null : groupedKey;
        }

        public void onNext(T t) {
            try {
                Object key = groupedKey(this.keySelector.call(t));
                GroupState<K, T> group = (GroupState) this.groups.get(key);
                if (group == null) {
                    if (!this.child.isUnsubscribed()) {
                        group = createNewGroup(key);
                    } else {
                        return;
                    }
                }
                if (group != null) {
                    emitItem(group, nl.next(t));
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this, t);
            }
        }

        private GroupState<K, T> createNewGroup(final Object key) {
            final GroupState<K, T> groupState = new GroupState();
            GroupedObservable<K, R> go = GroupedObservable.create(getKey(key), new Observable$OnSubscribe<R>() {
                public void call(final Subscriber<? super R> o) {
                    o.setProducer(new Producer() {
                        public void request(long n) {
                            GroupBySubscriber.this.requestFromGroupedObservable(n, groupState);
                        }
                    });
                    final AtomicBoolean once = new AtomicBoolean();
                    groupState.getObservable().doOnUnsubscribe(new Action0() {
                        public void call() {
                            if (once.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }
                    }).unsafeSubscribe(new Subscriber<T>(o) {
                        public void onCompleted() {
                            o.onCompleted();
                            if (once.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }

                        public void onError(Throwable e) {
                            o.onError(e);
                            if (once.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }

                        public void onNext(T t) {
                            try {
                                o.onNext(GroupBySubscriber.this.elementSelector.call(t));
                            } catch (Throwable e) {
                                Exceptions.throwOrReport(e, this, t);
                            }
                        }
                    });
                }
            });
            int wip;
            do {
                wip = this.wipForUnsubscribe;
                if (wip <= 0) {
                    return null;
                }
            } while (!WIP_FOR_UNSUBSCRIBE_UPDATER.compareAndSet(this, wip, wip + 1));
            if (((GroupState) this.groups.putIfAbsent(key, groupState)) != null) {
                throw new IllegalStateException("Group already existed while creating a new one");
            }
            this.child.onNext(go);
            return groupState;
        }

        private void cleanupGroup(Object key) {
            GroupState<K, T> removed = (GroupState) this.groups.remove(key);
            if (removed != null) {
                if (!removed.buffer.isEmpty()) {
                    BUFFERED_COUNT.addAndGet(this.self, (long) (-removed.buffer.size()));
                }
                completeInner();
                requestMoreIfNecessary();
            }
        }

        private void emitItem(GroupState<K, T> groupState, Object item) {
            Queue<Object> q = groupState.buffer;
            AtomicLong keyRequested = groupState.requested;
            REQUESTED.decrementAndGet(this);
            if (keyRequested == null || keyRequested.get() <= 0 || !(q == null || q.isEmpty())) {
                q.add(item);
                BUFFERED_COUNT.incrementAndGet(this);
                if (groupState.count.getAndIncrement() == 0) {
                    pollQueue(groupState);
                }
            } else {
                nl.accept(groupState.getObserver(), item);
                if (keyRequested.get() != Long.MAX_VALUE) {
                    keyRequested.decrementAndGet();
                }
            }
            requestMoreIfNecessary();
        }

        private void pollQueue(GroupState<K, T> groupState) {
            do {
                drainIfPossible(groupState);
                if (groupState.count.decrementAndGet() > 1) {
                    groupState.count.set(1);
                }
            } while (groupState.count.get() > 0);
        }

        private void requestMoreIfNecessary() {
            if (REQUESTED.get(this) == 0 && this.terminated == 0) {
                long toRequest = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID - BUFFERED_COUNT.get(this);
                if (toRequest > 0 && REQUESTED.compareAndSet(this, 0, toRequest)) {
                    request(toRequest);
                }
            }
        }

        private void drainIfPossible(GroupState<K, T> groupState) {
            while (groupState.requested.get() > 0) {
                Object t = groupState.buffer.poll();
                if (t != null) {
                    nl.accept(groupState.getObserver(), t);
                    if (groupState.requested.get() != Long.MAX_VALUE) {
                        groupState.requested.decrementAndGet();
                    }
                    BUFFERED_COUNT.decrementAndGet(this);
                    requestMoreIfNecessary();
                } else {
                    return;
                }
            }
        }

        private void completeInner() {
            if (WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(this) == 0) {
                unsubscribe();
            } else if (this.groups.isEmpty() && this.terminated == 1 && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                this.child.onCompleted();
            }
        }
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector) {
        this(keySelector, IDENTITY);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> valueSelector) {
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super GroupedObservable<K, R>> child) {
        return new GroupBySubscriber(this.keySelector, this.valueSelector, child);
    }
}
