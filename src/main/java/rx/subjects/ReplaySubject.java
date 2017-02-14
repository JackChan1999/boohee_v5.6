package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Scheduler;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.NotificationLite;
import rx.internal.util.UtilityFunctions;
import rx.schedulers.Timestamped;

public final class ReplaySubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    final SubjectSubscriptionManager<T> ssm;
    final ReplayState<T, ?> state;

    static final class AddTimestamped implements Func1<Object, Object> {
        final Scheduler scheduler;

        public AddTimestamped(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        public Object call(Object t1) {
            return new Timestamped(this.scheduler.now(), t1);
        }
    }

    interface ReplayState<T, I> {
        void complete();

        void error(Throwable th);

        boolean isEmpty();

        T latest();

        void next(T t);

        boolean replayObserver(SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndex(I i, SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndexTest(I i, SubjectObserver<? super T> subjectObserver, long j);

        int size();

        boolean terminated();

        T[] toArray(T[] tArr);
    }

    static final class BoundedState<T> implements ReplayState<T, Node<Object>> {
        final Func1<Object, Object> enterTransform;
        final EvictionPolicy evictionPolicy;
        final Func1<Object, Object> leaveTransform;
        final NodeList<Object> list = new NodeList();
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile Node<Object> tail = this.list.tail;
        volatile boolean terminated;

        public BoundedState(EvictionPolicy evictionPolicy, Func1<Object, Object> enterTransform, Func1<Object, Object> leaveTransform) {
            this.evictionPolicy = evictionPolicy;
            this.enterTransform = enterTransform;
            this.leaveTransform = leaveTransform;
        }

        public void next(T value) {
            if (!this.terminated) {
                this.list.addLast(this.enterTransform.call(this.nl.next(value)));
                this.evictionPolicy.evict(this.list);
                this.tail = this.list.tail;
            }
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.completed()));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.error(e)));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void accept(Observer<? super T> o, Node<Object> node) {
            this.nl.accept(o, this.leaveTransform.call(node.value));
        }

        public void acceptTest(Observer<? super T> o, Node<Object> node, long now) {
            Object v = node.value;
            if (!this.evictionPolicy.test(v, now)) {
                this.nl.accept(o, this.leaveTransform.call(v));
            }
        }

        public Node<Object> head() {
            return this.list.head;
        }

        public Node<Object> tail() {
            return this.tail;
        }

        public boolean replayObserver(SubjectObserver<? super T> observer) {
            synchronized (observer) {
                observer.first = false;
                if (observer.emitting) {
                    return false;
                }
                observer.index(replayObserverFromIndex((Node) observer.index(), (SubjectObserver) observer));
                return true;
            }
        }

        public Node<Object> replayObserverFromIndex(Node<Object> l, SubjectObserver<? super T> observer) {
            while (l != tail()) {
                accept(observer, l.next);
                l = l.next;
            }
            return l;
        }

        public Node<Object> replayObserverFromIndexTest(Node<Object> l, SubjectObserver<? super T> observer, long now) {
            while (l != tail()) {
                acceptTest(observer, l.next, now);
                l = l.next;
            }
            return l;
        }

        public boolean terminated() {
            return this.terminated;
        }

        public int size() {
            int size = 0;
            Node<Object> l = head();
            for (Node<Object> next = l.next; next != null; next = next.next) {
                size++;
                l = next;
            }
            if (l.value == null) {
                return size;
            }
            Object value = this.leaveTransform.call(l.value);
            if (value == null) {
                return size;
            }
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                return size - 1;
            }
            return size;
        }

        public boolean isEmpty() {
            Node<Object> next = head().next;
            if (next == null) {
                return true;
            }
            Object value = this.leaveTransform.call(next.value);
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                return true;
            }
            return false;
        }

        public T[] toArray(T[] a) {
            List<T> list = new ArrayList();
            for (Node<Object> next = head().next; next != null; next = next.next) {
                Object o = this.leaveTransform.call(next.value);
                if (next.next == null && (this.nl.isError(o) || this.nl.isCompleted(o))) {
                    break;
                }
                list.add(o);
                Node<Object> l = next;
            }
            return list.toArray(a);
        }

        public T latest() {
            Node<Object> h = head().next;
            if (h == null) {
                return null;
            }
            Node<Object> p = null;
            while (h != tail()) {
                p = h;
                h = h.next;
            }
            Object value = this.leaveTransform.call(h.value);
            if (!this.nl.isError(value) && !this.nl.isCompleted(value)) {
                return this.nl.getValue(value);
            }
            if (p == null) {
                return null;
            }
            return this.nl.getValue(this.leaveTransform.call(p.value));
        }
    }

    static final class DefaultOnAdd<T> implements Action1<SubjectObserver<T>> {
        final BoundedState<T> state;

        public DefaultOnAdd(BoundedState<T> state) {
            this.state = state;
        }

        public void call(SubjectObserver<T> t1) {
            t1.index(this.state.replayObserverFromIndex(this.state.head(), (SubjectObserver) t1));
        }
    }

    interface EvictionPolicy {
        void evict(NodeList<Object> nodeList);

        void evictFinal(NodeList<Object> nodeList);

        boolean test(Object obj, long j);
    }

    static final class EmptyEvictionPolicy implements EvictionPolicy {
        EmptyEvictionPolicy() {
        }

        public boolean test(Object value, long now) {
            return true;
        }

        public void evict(NodeList<Object> nodeList) {
        }

        public void evictFinal(NodeList<Object> nodeList) {
        }
    }

    static final class NodeList<T> {
        final Node<T> head = new Node(null);
        int size;
        Node<T> tail = this.head;

        static final class Node<T> {
            volatile Node<T> next;
            final T value;

            Node(T value) {
                this.value = value;
            }
        }

        NodeList() {
        }

        public void addLast(T value) {
            Node<T> t = this.tail;
            Node<T> t2 = new Node(value);
            t.next = t2;
            this.tail = t2;
            this.size++;
        }

        public T removeFirst() {
            if (this.head.next == null) {
                throw new IllegalStateException("Empty!");
            }
            Node<T> t = this.head.next;
            this.head.next = t.next;
            if (this.head.next == null) {
                this.tail = this.head;
            }
            this.size--;
            return t.value;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public int size() {
            return this.size;
        }

        public void clear() {
            this.tail = this.head;
            this.size = 0;
        }
    }

    static final class PairEvictionPolicy implements EvictionPolicy {
        final EvictionPolicy first;
        final EvictionPolicy second;

        public PairEvictionPolicy(EvictionPolicy first, EvictionPolicy second) {
            this.first = first;
            this.second = second;
        }

        public void evict(NodeList<Object> t1) {
            this.first.evict(t1);
            this.second.evict(t1);
        }

        public void evictFinal(NodeList<Object> t1) {
            this.first.evictFinal(t1);
            this.second.evictFinal(t1);
        }

        public boolean test(Object value, long now) {
            return this.first.test(value, now) || this.second.test(value, now);
        }
    }

    static final class RemoveTimestamped implements Func1<Object, Object> {
        RemoveTimestamped() {
        }

        public Object call(Object t1) {
            return ((Timestamped) t1).getValue();
        }
    }

    static final class SizeEvictionPolicy implements EvictionPolicy {
        final int maxSize;

        public SizeEvictionPolicy(int maxSize) {
            this.maxSize = maxSize;
        }

        public void evict(NodeList<Object> t1) {
            while (t1.size() > this.maxSize) {
                t1.removeFirst();
            }
        }

        public boolean test(Object value, long now) {
            return false;
        }

        public void evictFinal(NodeList<Object> t1) {
            while (t1.size() > this.maxSize + 1) {
                t1.removeFirst();
            }
        }
    }

    static final class TimeEvictionPolicy implements EvictionPolicy {
        final long maxAgeMillis;
        final Scheduler scheduler;

        public TimeEvictionPolicy(long maxAgeMillis, Scheduler scheduler) {
            this.maxAgeMillis = maxAgeMillis;
            this.scheduler = scheduler;
        }

        public void evict(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (!t1.isEmpty() && test(t1.head.next.value, now)) {
                t1.removeFirst();
            }
        }

        public void evictFinal(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (t1.size > 1 && test(t1.head.next.value, now)) {
                t1.removeFirst();
            }
        }

        public boolean test(Object value, long now) {
            return ((Timestamped) value).getTimestampMillis() <= now - this.maxAgeMillis;
        }
    }

    static final class TimedOnAdd<T> implements Action1<SubjectObserver<T>> {
        final Scheduler scheduler;
        final BoundedState<T> state;

        public TimedOnAdd(BoundedState<T> state, Scheduler scheduler) {
            this.state = state;
            this.scheduler = scheduler;
        }

        public void call(SubjectObserver<T> t1) {
            Node<Object> l;
            if (this.state.terminated) {
                l = this.state.replayObserverFromIndex(this.state.head(), (SubjectObserver) t1);
            } else {
                l = this.state.replayObserverFromIndexTest(this.state.head(), (SubjectObserver) t1, this.scheduler.now());
            }
            t1.index(l);
        }
    }

    static final class UnboundedReplayState<T> extends AtomicInteger implements ReplayState<T, Integer> {
        private final ArrayList<Object> list;
        private final NotificationLite<T> nl = NotificationLite.instance();
        private volatile boolean terminated;

        public UnboundedReplayState(int initialCapacity) {
            this.list = new ArrayList(initialCapacity);
        }

        public void next(T n) {
            if (!this.terminated) {
                this.list.add(this.nl.next(n));
                getAndIncrement();
            }
        }

        public void accept(Observer<? super T> o, int idx) {
            this.nl.accept(o, this.list.get(idx));
        }

        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.completed());
                getAndIncrement();
            }
        }

        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.error(e));
                getAndIncrement();
            }
        }

        public boolean terminated() {
            return this.terminated;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean replayObserver(rx.subjects.SubjectSubscriptionManager.SubjectObserver<? super T> r6) {
            /*
            r5 = this;
            r2 = 0;
            monitor-enter(r6);
            r3 = 0;
            r6.first = r3;	 Catch:{ all -> 0x0025 }
            r3 = r6.emitting;	 Catch:{ all -> 0x0025 }
            if (r3 == 0) goto L_0x000b;
        L_0x0009:
            monitor-exit(r6);	 Catch:{ all -> 0x0025 }
        L_0x000a:
            return r2;
        L_0x000b:
            monitor-exit(r6);	 Catch:{ all -> 0x0025 }
            r1 = r6.index();
            r1 = (java.lang.Integer) r1;
            if (r1 == 0) goto L_0x0028;
        L_0x0014:
            r2 = r5.replayObserverFromIndex(r1, r6);
            r0 = r2.intValue();
            r2 = java.lang.Integer.valueOf(r0);
            r6.index(r2);
            r2 = 1;
            goto L_0x000a;
        L_0x0025:
            r2 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0025 }
            throw r2;
        L_0x0028:
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "failed to find lastEmittedLink for: ";
            r3 = r3.append(r4);
            r3 = r3.append(r6);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.UnboundedReplayState.replayObserver(rx.subjects.SubjectSubscriptionManager$SubjectObserver):boolean");
        }

        public Integer replayObserverFromIndex(Integer idx, SubjectObserver<? super T> observer) {
            int i = idx.intValue();
            while (i < get()) {
                accept(observer, i);
                i++;
            }
            return Integer.valueOf(i);
        }

        public Integer replayObserverFromIndexTest(Integer idx, SubjectObserver<? super T> observer, long now) {
            return replayObserverFromIndex(idx, (SubjectObserver) observer);
        }

        public int size() {
            int idx = get();
            if (idx <= 0) {
                return idx;
            }
            Object o = this.list.get(idx - 1);
            if (this.nl.isCompleted(o) || this.nl.isError(o)) {
                return idx - 1;
            }
            return idx;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public T[] toArray(T[] a) {
            int s = size();
            if (s > 0) {
                if (s > a.length) {
                    a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), s));
                }
                for (int i = 0; i < s; i++) {
                    a[i] = this.list.get(i);
                }
                if (a.length > s) {
                    a[s] = null;
                }
            } else if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }

        public T latest() {
            int idx = get();
            if (idx <= 0) {
                return null;
            }
            Object o = this.list.get(idx - 1);
            if (!this.nl.isCompleted(o) && !this.nl.isError(o)) {
                return this.nl.getValue(o);
            }
            if (idx > 1) {
                return this.nl.getValue(this.list.get(idx - 2));
            }
            return null;
        }
    }

    public static <T> ReplaySubject<T> create() {
        return create(16);
    }

    public static <T> ReplaySubject<T> create(int capacity) {
        final UnboundedReplayState<T> state = new UnboundedReplayState(capacity);
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager();
        ssm.onStart = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                o.index(Integer.valueOf(state.replayObserverFromIndex(Integer.valueOf(0), (SubjectObserver) o).intValue()));
            }
        };
        ssm.onAdded = new Action1<SubjectObserver<T>>() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r8) {
                /*
                r7 = this;
                monitor-enter(r8);
                r5 = r8.first;	 Catch:{ all -> 0x0047 }
                if (r5 == 0) goto L_0x0009;
            L_0x0005:
                r5 = r8.emitting;	 Catch:{ all -> 0x0047 }
                if (r5 == 0) goto L_0x000b;
            L_0x0009:
                monitor-exit(r8);	 Catch:{ all -> 0x0047 }
            L_0x000a:
                return;
            L_0x000b:
                r5 = 0;
                r8.first = r5;	 Catch:{ all -> 0x0047 }
                r5 = 1;
                r8.emitting = r5;	 Catch:{ all -> 0x0047 }
                monitor-exit(r8);	 Catch:{ all -> 0x0047 }
                r4 = 0;
                r2 = r1;	 Catch:{ all -> 0x004f }
            L_0x0015:
                r5 = r8.index();	 Catch:{ all -> 0x004f }
                r5 = (java.lang.Integer) r5;	 Catch:{ all -> 0x004f }
                r0 = r5.intValue();	 Catch:{ all -> 0x004f }
                r3 = r2.get();	 Catch:{ all -> 0x004f }
                if (r0 == r3) goto L_0x0030;
            L_0x0025:
                r5 = java.lang.Integer.valueOf(r0);	 Catch:{ all -> 0x004f }
                r1 = r2.replayObserverFromIndex(r5, r8);	 Catch:{ all -> 0x004f }
                r8.index(r1);	 Catch:{ all -> 0x004f }
            L_0x0030:
                monitor-enter(r8);	 Catch:{ all -> 0x004f }
                r5 = r2.get();	 Catch:{ all -> 0x004c }
                if (r3 != r5) goto L_0x004a;
            L_0x0037:
                r5 = 0;
                r8.emitting = r5;	 Catch:{ all -> 0x004c }
                r4 = 1;
                monitor-exit(r8);	 Catch:{ all -> 0x004c }
                if (r4 != 0) goto L_0x000a;
            L_0x003e:
                monitor-enter(r8);
                r5 = 0;
                r8.emitting = r5;	 Catch:{ all -> 0x0044 }
                monitor-exit(r8);	 Catch:{ all -> 0x0044 }
                goto L_0x000a;
            L_0x0044:
                r5 = move-exception;
                monitor-exit(r8);	 Catch:{ all -> 0x0044 }
                throw r5;
            L_0x0047:
                r5 = move-exception;
                monitor-exit(r8);	 Catch:{ all -> 0x0047 }
                throw r5;
            L_0x004a:
                monitor-exit(r8);	 Catch:{ all -> 0x004c }
                goto L_0x0015;
            L_0x004c:
                r5 = move-exception;
                monitor-exit(r8);	 Catch:{ all -> 0x004c }
                throw r5;	 Catch:{ all -> 0x004f }
            L_0x004f:
                r5 = move-exception;
                if (r4 != 0) goto L_0x0057;
            L_0x0052:
                monitor-enter(r8);
                r6 = 0;
                r8.emitting = r6;	 Catch:{ all -> 0x0058 }
                monitor-exit(r8);	 Catch:{ all -> 0x0058 }
            L_0x0057:
                throw r5;
            L_0x0058:
                r5 = move-exception;
                monitor-exit(r8);	 Catch:{ all -> 0x0058 }
                throw r5;
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.2.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        ssm.onTerminated = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                Integer idx = (Integer) o.index();
                if (idx == null) {
                    idx = Integer.valueOf(0);
                }
                state.replayObserverFromIndex(idx, (SubjectObserver) o);
            }
        };
        return new ReplaySubject(ssm, ssm, state);
    }

    static <T> ReplaySubject<T> createUnbounded() {
        BoundedState<T> state = new BoundedState(new EmptyEvictionPolicy(), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithSize(int size) {
        BoundedState<T> state = new BoundedState(new SizeEvictionPolicy(size), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState(new TimeEvictionPolicy(unit.toMillis(time), scheduler), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    public static <T> ReplaySubject<T> createWithTimeAndSize(long time, TimeUnit unit, int size, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState(new PairEvictionPolicy(new SizeEvictionPolicy(size), new TimeEvictionPolicy(unit.toMillis(time), scheduler)), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    static final <T> ReplaySubject<T> createWithState(final BoundedState<T> state, Action1<SubjectObserver<T>> onStart) {
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager();
        ssm.onStart = onStart;
        ssm.onAdded = new Action1<SubjectObserver<T>>() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
                /*
                r6 = this;
                monitor-enter(r7);
                r4 = r7.first;	 Catch:{ all -> 0x0043 }
                if (r4 == 0) goto L_0x0009;
            L_0x0005:
                r4 = r7.emitting;	 Catch:{ all -> 0x0043 }
                if (r4 == 0) goto L_0x000b;
            L_0x0009:
                monitor-exit(r7);	 Catch:{ all -> 0x0043 }
            L_0x000a:
                return;
            L_0x000b:
                r4 = 0;
                r7.first = r4;	 Catch:{ all -> 0x0043 }
                r4 = 1;
                r7.emitting = r4;	 Catch:{ all -> 0x0043 }
                monitor-exit(r7);	 Catch:{ all -> 0x0043 }
                r3 = 0;
            L_0x0013:
                r0 = r7.index();	 Catch:{ all -> 0x004b }
                r0 = (rx.subjects.ReplaySubject.NodeList.Node) r0;	 Catch:{ all -> 0x004b }
                r4 = r2;	 Catch:{ all -> 0x004b }
                r2 = r4.tail();	 Catch:{ all -> 0x004b }
                if (r0 == r2) goto L_0x002a;
            L_0x0021:
                r4 = r2;	 Catch:{ all -> 0x004b }
                r1 = r4.replayObserverFromIndex(r0, r7);	 Catch:{ all -> 0x004b }
                r7.index(r1);	 Catch:{ all -> 0x004b }
            L_0x002a:
                monitor-enter(r7);	 Catch:{ all -> 0x004b }
                r4 = r2;	 Catch:{ all -> 0x0048 }
                r4 = r4.tail();	 Catch:{ all -> 0x0048 }
                if (r2 != r4) goto L_0x0046;
            L_0x0033:
                r4 = 0;
                r7.emitting = r4;	 Catch:{ all -> 0x0048 }
                r3 = 1;
                monitor-exit(r7);	 Catch:{ all -> 0x0048 }
                if (r3 != 0) goto L_0x000a;
            L_0x003a:
                monitor-enter(r7);
                r4 = 0;
                r7.emitting = r4;	 Catch:{ all -> 0x0040 }
                monitor-exit(r7);	 Catch:{ all -> 0x0040 }
                goto L_0x000a;
            L_0x0040:
                r4 = move-exception;
                monitor-exit(r7);	 Catch:{ all -> 0x0040 }
                throw r4;
            L_0x0043:
                r4 = move-exception;
                monitor-exit(r7);	 Catch:{ all -> 0x0043 }
                throw r4;
            L_0x0046:
                monitor-exit(r7);	 Catch:{ all -> 0x0048 }
                goto L_0x0013;
            L_0x0048:
                r4 = move-exception;
                monitor-exit(r7);	 Catch:{ all -> 0x0048 }
                throw r4;	 Catch:{ all -> 0x004b }
            L_0x004b:
                r4 = move-exception;
                if (r3 != 0) goto L_0x0053;
            L_0x004e:
                monitor-enter(r7);
                r5 = 0;
                r7.emitting = r5;	 Catch:{ all -> 0x0054 }
                monitor-exit(r7);	 Catch:{ all -> 0x0054 }
            L_0x0053:
                throw r4;
            L_0x0054:
                r4 = move-exception;
                monitor-exit(r7);	 Catch:{ all -> 0x0054 }
                throw r4;
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.4.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        ssm.onTerminated = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> t1) {
                Node l = (Node) t1.index();
                if (l == null) {
                    l = state.head();
                }
                state.replayObserverFromIndex(l, (SubjectObserver) t1);
            }
        };
        return new ReplaySubject(ssm, ssm, state);
    }

    ReplaySubject(Observable$OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> ssm, ReplayState<T, ?> state) {
        super(onSubscribe);
        this.ssm = ssm;
        this.state = state;
    }

    public void onNext(T t) {
        if (this.ssm.active) {
            this.state.next(t);
            for (SubjectObserver<? super T> o : this.ssm.observers()) {
                if (caughtUp(o)) {
                    o.onNext(t);
                }
            }
        }
    }

    public void onError(Throwable e) {
        if (this.ssm.active) {
            this.state.error(e);
            List<Throwable> errors = null;
            for (SubjectObserver<? super T> o : this.ssm.terminate(NotificationLite.instance().error(e))) {
                try {
                    if (caughtUp(o)) {
                        o.onError(e);
                    }
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onCompleted() {
        if (this.ssm.active) {
            this.state.complete();
            for (SubjectObserver<? super T> o : this.ssm.terminate(NotificationLite.instance().completed())) {
                if (caughtUp(o)) {
                    o.onCompleted();
                }
            }
        }
    }

    int subscriberCount() {
        return ((State) this.ssm.get()).observers.length;
    }

    public boolean hasObservers() {
        return this.ssm.observers().length > 0;
    }

    private boolean caughtUp(SubjectObserver<? super T> o) {
        if (o.caughtUp) {
            return true;
        }
        if (this.state.replayObserver(o)) {
            o.caughtUp = true;
            o.index(null);
        }
        return false;
    }

    @Beta
    public boolean hasThrowable() {
        return this.ssm.nl.isError(this.ssm.getLatest());
    }

    @Beta
    public boolean hasCompleted() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.getLatest();
        return (o == null || nl.isError(o)) ? false : true;
    }

    @Beta
    public Throwable getThrowable() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.getLatest();
        if (nl.isError(o)) {
            return nl.getError(o);
        }
        return null;
    }

    @Beta
    public int size() {
        return this.state.size();
    }

    @Beta
    public boolean hasAnyValue() {
        return !this.state.isEmpty();
    }

    @Beta
    public boolean hasValue() {
        return hasAnyValue();
    }

    @Beta
    public T[] getValues(T[] a) {
        return this.state.toArray(a);
    }

    @Beta
    public Object[] getValues() {
        T[] r = getValues(EMPTY_ARRAY);
        if (r == EMPTY_ARRAY) {
            return new Object[0];
        }
        return r;
    }

    @Beta
    public T getValue() {
        return this.state.latest();
    }
}
