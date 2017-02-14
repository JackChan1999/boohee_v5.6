package rx;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import rx.annotations.Beta;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.internal.operators.OnSubscribeToObservableFuture;
import rx.internal.operators.OperatorDelay;
import rx.internal.operators.OperatorDoOnEach;
import rx.internal.operators.OperatorMap;
import rx.internal.operators.OperatorObserveOn;
import rx.internal.operators.OperatorOnErrorReturn;
import rx.internal.operators.OperatorSubscribeOn;
import rx.internal.operators.OperatorTimeout;
import rx.internal.operators.OperatorZip;
import rx.internal.producers.SingleDelayedProducer;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.singles.BlockingSingle;

@Beta
public class Single<T> {
    private static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    final Observable$OnSubscribe<T> onSubscribe;

    public interface OnSubscribe<T> extends Action1<SingleSubscriber<? super T>> {
    }

    public interface Transformer<T, R> extends Func1<Single<T>, Single<R>> {
    }

    protected Single(final OnSubscribe<T> f) {
        this.onSubscribe = new Observable$OnSubscribe<T>() {
            public void call(final Subscriber<? super T> child) {
                final SingleDelayedProducer<T> producer = new SingleDelayedProducer(child);
                child.setProducer(producer);
                SingleSubscriber<T> ss = new SingleSubscriber<T>() {
                    public void onSuccess(T value) {
                        producer.setValue(value);
                    }

                    public void onError(Throwable error) {
                        child.onError(error);
                    }
                };
                child.add(ss);
                f.call(ss);
            }
        };
    }

    private Single(Observable$OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    public static final <T> Single<T> create(OnSubscribe<T> f) {
        return new Single((OnSubscribe) f);
    }

    private final <R> Single<R> lift(final Observable$Operator<? extends R, ? super T> lift) {
        return new Single(new Observable$OnSubscribe<R>() {
            public void call(Subscriber<? super R> o) {
                Subscriber<? super T> st;
                try {
                    st = (Subscriber) Single.hook.onLift(lift).call(o);
                    st.onStart();
                    Single.this.onSubscribe.call(st);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    o.onError(e);
                }
            }
        });
    }

    public <R> Single<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Single) transformer.call(this);
    }

    private static <T> Observable<T> asObservable(Single<T> t) {
        return Observable.create(t.onSubscribe);
    }

    private final Single<Observable<T>> nest() {
        return just(asObservable(this));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.concat(asObservable(t1), asObservable(t2));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static final <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static final <T> Single<T> error(final Throwable exception) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> te) {
                te.onError(exception);
            }
        });
    }

    public static final <T> Single<T> from(Future<? extends T> future) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static final <T> Single<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    public static final <T> Single<T> from(Future<? extends T> future, Scheduler scheduler) {
        return new Single(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    @Experimental
    public static <T> Single<T> fromCallable(final Callable<? extends T> func) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                try {
                    singleSubscriber.onSuccess(func.call());
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    singleSubscriber.onError(t);
                }
            }
        });
    }

    public static final <T> Single<T> just(final T value) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> te) {
                te.onSuccess(value);
            }
        });
    }

    public static final <T> Single<T> merge(final Single<? extends Single<? extends T>> source) {
        return create(new OnSubscribe<T>() {
            public void call(final SingleSubscriber<? super T> child) {
                source.subscribe(new SingleSubscriber<Single<? extends T>>() {
                    public void onSuccess(Single<? extends T> innerSingle) {
                        innerSingle.subscribe(child);
                    }

                    public void onError(Throwable error) {
                        child.onError(error);
                    }
                });
            }
        });
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.merge(asObservable(t1), asObservable(t2));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static final <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static final <T1, T2, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2)}).lift(new OperatorZip((Func2) zipFunction));
    }

    public static final <T1, T2, T3, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3)}).lift(new OperatorZip((Func3) zipFunction));
    }

    public static final <T1, T2, T3, T4, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4)}).lift(new OperatorZip((Func4) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5)}).lift(new OperatorZip((Func5) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6)}).lift(new OperatorZip((Func6) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7)}).lift(new OperatorZip((Func7) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Single<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7), asObservable(o8)}).lift(new OperatorZip((Func8) zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(Single<? extends T1> o1, Single<? extends T2> o2, Single<? extends T3> o3, Single<? extends T4> o4, Single<? extends T5> o5, Single<? extends T6> o6, Single<? extends T7> o7, Single<? extends T8> o8, Single<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable[]{asObservable(o1), asObservable(o2), asObservable(o3), asObservable(o4), asObservable(o5), asObservable(o6), asObservable(o7), asObservable(o8), asObservable(o9)}).lift(new OperatorZip((Func9) zipFunction));
    }

    public final Observable<T> concatWith(Single<? extends T> t1) {
        return concat(this, t1);
    }

    public final <R> Single<R> flatMap(Func1<? super T, ? extends Single<? extends R>> func) {
        return merge(map(func));
    }

    public final <R> Observable<R> flatMapObservable(Func1<? super T, ? extends Observable<? extends R>> func) {
        return Observable.merge(asObservable(map(func)));
    }

    public final <R> Single<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap(func));
    }

    public final Observable<T> mergeWith(Single<? extends T> t1) {
        return merge(this, t1);
    }

    public final Single<T> observeOn(Scheduler scheduler) {
        return lift(new OperatorObserveOn(scheduler));
    }

    public final Single<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return lift(new OperatorOnErrorReturn(resumeFunction));
    }

    public final Subscription subscribe() {
        return subscribe(new Subscriber<T>() {
            public final void onCompleted() {
            }

            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            public final void onNext(T t) {
            }
        });
    }

    public final Subscription subscribe(final Action1<? super T> onSuccess) {
        if (onSuccess != null) {
            return subscribe(new Subscriber<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    throw new OnErrorNotImplementedException(e);
                }

                public final void onNext(T args) {
                    onSuccess.call(args);
                }
            });
        }
        throw new IllegalArgumentException("onSuccess can not be null");
    }

    public final Subscription subscribe(final Action1<? super T> onSuccess, final Action1<Throwable> onError) {
        if (onSuccess == null) {
            throw new IllegalArgumentException("onSuccess can not be null");
        } else if (onError != null) {
            return subscribe(new Subscriber<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onSuccess.call(args);
                }
            });
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final void unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            this.onSubscribe.call(subscriber);
            hook.onSubscribeReturn(subscriber);
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2));
        }
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        } else if (this.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        } else {
            subscriber.onStart();
            if (!(subscriber instanceof SafeSubscriber)) {
                subscriber = new SafeSubscriber(subscriber);
            }
            try {
                this.onSubscribe.call(subscriber);
                return hook.onSubscribeReturn(subscriber);
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2));
            }
        }
    }

    public final Subscription subscribe(final SingleSubscriber<? super T> te) {
        Subscriber s = new Subscriber<T>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                te.onError(e);
            }

            public void onNext(T t) {
                te.onSuccess(t);
            }
        };
        te.add(s);
        subscribe(s);
        return s;
    }

    public final Single<T> subscribeOn(Scheduler scheduler) {
        return nest().lift(new OperatorSubscribeOn(scheduler));
    }

    public final Observable<T> toObservable() {
        return asObservable(this);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other, Scheduler scheduler) {
        if (other == null) {
            other = error(new TimeoutException());
        }
        return lift(new OperatorTimeout(timeout, timeUnit, asObservable(other), scheduler));
    }

    @Experimental
    public final BlockingSingle<T> toBlocking() {
        return BlockingSingle.from(this);
    }

    public final <T2, R> Single<R> zipWith(Single<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }

    @Experimental
    public final Single<T> doOnError(final Action1<Throwable> onError) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
                onError.call(e);
            }

            public void onNext(T t) {
            }
        }));
    }

    @Experimental
    public final Single<T> doOnSuccess(final Action1<? super T> onSuccess) {
        return lift(new OperatorDoOnEach(new Observer<T>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
            }

            public void onNext(T t) {
                onSuccess.call(t);
            }
        }));
    }

    @Experimental
    public final Single<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDelay(delay, unit, scheduler));
    }

    @Experimental
    public final Single<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    @Experimental
    public static <T> Single<T> defer(final Callable<Single<T>> singleFactory) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                try {
                    ((Single) singleFactory.call()).subscribe((SingleSubscriber) singleSubscriber);
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    singleSubscriber.onError(t);
                }
            }
        });
    }
}
