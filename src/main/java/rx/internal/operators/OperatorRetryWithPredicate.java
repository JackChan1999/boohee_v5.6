package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func2;
import rx.internal.producers.ProducerArbiter;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;

public final class OperatorRetryWithPredicate<T> implements Observable$Operator<T, Observable<T>> {
    final Func2<Integer, Throwable, Boolean> predicate;

    static final class SourceSubscriber<T> extends Subscriber<Observable<T>> {
        final AtomicInteger attempts = new AtomicInteger();
        final Subscriber<? super T> child;
        final Worker inner;
        final ProducerArbiter pa;
        final Func2<Integer, Throwable, Boolean> predicate;
        final SerialSubscription serialSubscription;

        public SourceSubscriber(Subscriber<? super T> child, Func2<Integer, Throwable, Boolean> predicate, Worker inner, SerialSubscription serialSubscription, ProducerArbiter pa) {
            this.child = child;
            this.predicate = predicate;
            this.inner = inner;
            this.serialSubscription = serialSubscription;
            this.pa = pa;
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(final Observable<T> o) {
            this.inner.schedule(new Action0() {
                public void call() {
                    final AnonymousClass1 _self = this;
                    SourceSubscriber.this.attempts.incrementAndGet();
                    Subscriber<T> subscriber = new Subscriber<T>() {
                        boolean done;

                        public void onCompleted() {
                            if (!this.done) {
                                this.done = true;
                                SourceSubscriber.this.child.onCompleted();
                            }
                        }

                        public void onError(Throwable e) {
                            if (!this.done) {
                                this.done = true;
                                if (!((Boolean) SourceSubscriber.this.predicate.call(Integer.valueOf(SourceSubscriber.this.attempts.get()), e)).booleanValue() || SourceSubscriber.this.inner.isUnsubscribed()) {
                                    SourceSubscriber.this.child.onError(e);
                                } else {
                                    SourceSubscriber.this.inner.schedule(_self);
                                }
                            }
                        }

                        public void onNext(T v) {
                            if (!this.done) {
                                SourceSubscriber.this.child.onNext(v);
                                SourceSubscriber.this.pa.produced(1);
                            }
                        }

                        public void setProducer(Producer p) {
                            SourceSubscriber.this.pa.setProducer(p);
                        }
                    };
                    SourceSubscriber.this.serialSubscription.set(subscriber);
                    o.unsafeSubscribe(subscriber);
                }
            });
        }
    }

    public OperatorRetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super Observable<T>> call(Subscriber<? super T> child) {
        Worker inner = Schedulers.trampoline().createWorker();
        child.add(inner);
        SerialSubscription serialSubscription = new SerialSubscription();
        child.add(serialSubscription);
        ProducerArbiter pa = new ProducerArbiter();
        child.setProducer(pa);
        return new SourceSubscriber(child, this.predicate, inner, serialSubscription, pa);
    }
}
