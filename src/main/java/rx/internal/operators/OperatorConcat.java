package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

public final class OperatorConcat<T> implements Observable$Operator<T, Observable<? extends T>> {

    static class ConcatInnerSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private final AtomicInteger once = new AtomicInteger();
        private final ConcatSubscriber<T> parent;

        public ConcatInnerSubscriber(ConcatSubscriber<T> parent, Subscriber<T> child, ProducerArbiter arbiter) {
            this.parent = parent;
            this.child = child;
            this.arbiter = arbiter;
        }

        public void onNext(T t) {
            this.child.onNext(t);
            this.parent.decrementRequested();
            this.arbiter.produced(1);
        }

        public void onError(Throwable e) {
            if (this.once.compareAndSet(0, 1)) {
                this.parent.onError(e);
            }
        }

        public void onCompleted() {
            if (this.once.compareAndSet(0, 1)) {
                this.parent.completeInner();
            }
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }
    }

    static final class ConcatProducer<T> implements Producer {
        final ConcatSubscriber<T> cs;

        ConcatProducer(ConcatSubscriber<T> cs) {
            this.cs = cs;
        }

        public void request(long n) {
            this.cs.requestFromChild(n);
        }
    }

    static final class ConcatSubscriber<T> extends Subscriber<Observable<? extends T>> {
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private final SerialSubscription current;
        volatile ConcatInnerSubscriber<T> currentSubscriber;
        final NotificationLite<Observable<? extends T>> nl = NotificationLite.instance();
        final ConcurrentLinkedQueue<Object> queue;
        private final AtomicLong requested = new AtomicLong();
        final AtomicInteger wip = new AtomicInteger();

        public ConcatSubscriber(Subscriber<T> s, SerialSubscription current) {
            super(s);
            this.child = s;
            this.current = current;
            this.arbiter = new ProducerArbiter();
            this.queue = new ConcurrentLinkedQueue();
            add(Subscriptions.create(new Action0() {
                public void call() {
                    ConcatSubscriber.this.queue.clear();
                }
            }));
        }

        public void onStart() {
            request(2);
        }

        private void requestFromChild(long n) {
            if (n > 0) {
                long previous = BackpressureUtils.getAndAddRequest(this.requested, n);
                this.arbiter.request(n);
                if (previous == 0 && this.currentSubscriber == null && this.wip.get() > 0) {
                    subscribeNext();
                }
            }
        }

        private void decrementRequested() {
            this.requested.decrementAndGet();
        }

        public void onNext(Observable<? extends T> t) {
            this.queue.add(this.nl.next(t));
            if (this.wip.getAndIncrement() == 0) {
                subscribeNext();
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.queue.add(this.nl.completed());
            if (this.wip.getAndIncrement() == 0) {
                subscribeNext();
            }
        }

        void completeInner() {
            this.currentSubscriber = null;
            if (this.wip.decrementAndGet() > 0) {
                subscribeNext();
            }
            request(1);
        }

        void subscribeNext() {
            if (this.requested.get() > 0) {
                Object o = this.queue.poll();
                if (this.nl.isCompleted(o)) {
                    this.child.onCompleted();
                    return;
                } else if (o != null) {
                    Observable<? extends T> obs = (Observable) this.nl.getValue(o);
                    this.currentSubscriber = new ConcatInnerSubscriber(this, this.child, this.arbiter);
                    this.current.set(this.currentSubscriber);
                    obs.unsafeSubscribe(this.currentSubscriber);
                    return;
                } else {
                    return;
                }
            }
            if (this.nl.isCompleted(this.queue.peek())) {
                this.child.onCompleted();
            }
        }
    }

    private static final class Holder {
        static final OperatorConcat<Object> INSTANCE = new OperatorConcat();

        private Holder() {
        }
    }

    public static <T> OperatorConcat<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorConcat() {
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        SerialSubscription current = new SerialSubscription();
        child.add(current);
        ConcatSubscriber<T> cs = new ConcatSubscriber(s, current);
        child.setProducer(new ConcatProducer(cs));
        return cs;
    }
}
