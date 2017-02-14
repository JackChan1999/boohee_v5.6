package rx.internal.operators;

import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public class OperatorSubscribeOn<T> implements Observable$Operator<T, Observable<T>> {
    private final Scheduler scheduler;

    public OperatorSubscribeOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super Observable<T>> call(final Subscriber<? super T> subscriber) {
        final Worker inner = this.scheduler.createWorker();
        subscriber.add(inner);
        return new Subscriber<Observable<T>>(subscriber) {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(final Observable<T> o) {
                inner.schedule(new Action0() {
                    public void call() {
                        final Thread t = Thread.currentThread();
                        o.unsafeSubscribe(new Subscriber<T>(subscriber) {
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }

                            public void setProducer(final Producer producer) {
                                subscriber.setProducer(new Producer() {
                                    public void request(final long n) {
                                        if (Thread.currentThread() == t) {
                                            producer.request(n);
                                        } else {
                                            inner.schedule(new Action0() {
                                                public void call() {
                                                    producer.request(n);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
    }
}
