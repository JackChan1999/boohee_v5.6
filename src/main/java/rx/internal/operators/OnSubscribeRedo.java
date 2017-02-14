package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Observable$Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.producers.ProducerArbiter;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.SerialSubscription;

public final class OnSubscribeRedo<T> implements Observable$OnSubscribe<T> {
    static final Func1<Observable<? extends Notification<?>>, Observable<?>> REDO_INFINITE = new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                public Notification<?> call(Notification<?> notification) {
                    return Notification.createOnNext(null);
                }
            });
        }
    };
    private final Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> controlHandlerFunction;
    private final Scheduler scheduler;
    private final Observable<T> source;
    private final boolean stopOnComplete;
    private final boolean stopOnError;

    public static final class RedoFinite implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        private final long count;

        public RedoFinite(long count) {
            this.count = count;
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                int num = 0;

                public Notification<?> call(Notification<?> terminalNotification) {
                    if (RedoFinite.this.count == 0) {
                        return terminalNotification;
                    }
                    this.num++;
                    if (((long) this.num) <= RedoFinite.this.count) {
                        return Notification.createOnNext(Integer.valueOf(this.num));
                    }
                    return terminalNotification;
                }
            }).dematerialize();
        }
    }

    public static final class RetryWithPredicate implements Func1<Observable<? extends Notification<?>>, Observable<? extends Notification<?>>> {
        private final Func2<Integer, Throwable, Boolean> predicate;

        public RetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate) {
            this.predicate = predicate;
        }

        public Observable<? extends Notification<?>> call(Observable<? extends Notification<?>> ts) {
            return ts.scan(Notification.createOnNext(Integer.valueOf(0)), new Func2<Notification<Integer>, Notification<?>, Notification<Integer>>() {
                public Notification<Integer> call(Notification<Integer> n, Notification<?> term) {
                    int value = ((Integer) n.getValue()).intValue();
                    if (((Boolean) RetryWithPredicate.this.predicate.call(Integer.valueOf(value), term.getThrowable())).booleanValue()) {
                        return Notification.createOnNext(Integer.valueOf(value + 1));
                    }
                    return term;
                }
            });
        }
    }

    public static <T> Observable<T> retry(Observable<T> source) {
        return retry((Observable) source, REDO_INFINITE);
    }

    public static <T> Observable<T> retry(Observable<T> source, long count) {
        if (count >= 0) {
            return count == 0 ? source : retry((Observable) source, new RedoFinite(count));
        } else {
            throw new IllegalArgumentException("count >= 0 expected");
        }
    }

    public static <T> Observable<T> retry(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, true, false, Schedulers.trampoline()));
    }

    public static <T> Observable<T> retry(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, true, false, scheduler));
    }

    public static <T> Observable<T> repeat(Observable<T> source) {
        return repeat((Observable) source, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source, Scheduler scheduler) {
        return repeat((Observable) source, REDO_INFINITE, scheduler);
    }

    public static <T> Observable<T> repeat(Observable<T> source, long count) {
        return repeat((Observable) source, count, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source, long count, Scheduler scheduler) {
        if (count == 0) {
            return Observable.empty();
        }
        if (count >= 0) {
            return repeat((Observable) source, new RedoFinite(count - 1), scheduler);
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> repeat(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, true, Schedulers.trampoline()));
    }

    public static <T> Observable<T> repeat(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, true, scheduler));
    }

    public static <T> Observable<T> redo(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return Observable.create(new OnSubscribeRedo(source, notificationHandler, false, false, scheduler));
    }

    private OnSubscribeRedo(Observable<T> source, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> f, boolean stopOnComplete, boolean stopOnError, Scheduler scheduler) {
        this.source = source;
        this.controlHandlerFunction = f;
        this.stopOnComplete = stopOnComplete;
        this.stopOnError = stopOnError;
        this.scheduler = scheduler;
    }

    public void call(Subscriber<? super T> child) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        final AtomicLong consumerCapacity = new AtomicLong();
        final Worker worker = this.scheduler.createWorker();
        child.add(worker);
        final SerialSubscription sourceSubscriptions = new SerialSubscription();
        child.add(sourceSubscriptions);
        final BehaviorSubject<Notification<?>> terminals = BehaviorSubject.create();
        terminals.subscribe(Subscribers.empty());
        final ProducerArbiter arbiter = new ProducerArbiter();
        final Subscriber<? super T> subscriber = child;
        Action0 subscribeToSource = new Action0() {
            public void call() {
                if (!subscriber.isUnsubscribed()) {
                    Subscriber<T> terminalDelegatingSubscriber = new Subscriber<T>() {
                        boolean done;

                        public void onCompleted() {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                terminals.onNext(Notification.createOnCompleted());
                            }
                        }

                        public void onError(Throwable e) {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                terminals.onNext(Notification.createOnError(e));
                            }
                        }

                        public void onNext(T v) {
                            if (!this.done) {
                                subscriber.onNext(v);
                                decrementConsumerCapacity();
                                arbiter.produced(1);
                            }
                        }

                        private void decrementConsumerCapacity() {
                            long cc;
                            do {
                                cc = consumerCapacity.get();
                                if (cc == Long.MAX_VALUE) {
                                    return;
                                }
                            } while (!consumerCapacity.compareAndSet(cc, cc - 1));
                        }

                        public void setProducer(Producer producer) {
                            arbiter.setProducer(producer);
                        }
                    };
                    sourceSubscriptions.set(terminalDelegatingSubscriber);
                    OnSubscribeRedo.this.source.unsafeSubscribe(terminalDelegatingSubscriber);
                }
            }
        };
        final Observable<?> restarts = (Observable) this.controlHandlerFunction.call(terminals.lift(new Observable$Operator<Notification<?>, Notification<?>>() {
            public Subscriber<? super Notification<?>> call(final Subscriber<? super Notification<?>> filteredTerminals) {
                return new Subscriber<Notification<?>>(filteredTerminals) {
                    public void onCompleted() {
                        filteredTerminals.onCompleted();
                    }

                    public void onError(Throwable e) {
                        filteredTerminals.onError(e);
                    }

                    public void onNext(Notification<?> t) {
                        if (t.isOnCompleted() && OnSubscribeRedo.this.stopOnComplete) {
                            filteredTerminals.onCompleted();
                        } else if (t.isOnError() && OnSubscribeRedo.this.stopOnError) {
                            filteredTerminals.onError(t.getThrowable());
                        } else {
                            filteredTerminals.onNext(t);
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(Long.MAX_VALUE);
                    }
                };
            }
        }));
        final Subscriber<? super T> subscriber2 = child;
        final AtomicLong atomicLong = consumerCapacity;
        final Action0 action0 = subscribeToSource;
        worker.schedule(new Action0() {
            public void call() {
                restarts.unsafeSubscribe(new Subscriber<Object>(subscriber2) {
                    public void onCompleted() {
                        subscriber2.onCompleted();
                    }

                    public void onError(Throwable e) {
                        subscriber2.onError(e);
                    }

                    public void onNext(Object t) {
                        if (!subscriber2.isUnsubscribed()) {
                            if (atomicLong.get() > 0) {
                                worker.schedule(action0);
                            } else {
                                atomicBoolean.compareAndSet(false, true);
                            }
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(Long.MAX_VALUE);
                    }
                });
            }
        });
        final AtomicLong atomicLong2 = consumerCapacity;
        final ProducerArbiter producerArbiter = arbiter;
        final AtomicBoolean atomicBoolean2 = atomicBoolean;
        final Worker worker2 = worker;
        final Action0 action02 = subscribeToSource;
        child.setProducer(new Producer() {
            public void request(long n) {
                if (n > 0) {
                    BackpressureUtils.getAndAddRequest(atomicLong2, n);
                    producerArbiter.request(n);
                    if (atomicBoolean2.compareAndSet(true, false)) {
                        worker2.schedule(action02);
                    }
                }
            }
        });
    }
}
