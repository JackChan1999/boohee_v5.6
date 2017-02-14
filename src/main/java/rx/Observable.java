package rx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import rx.annotations.Beta;
import rx.annotations.Experimental;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;
import rx.functions.Functions;
import rx.internal.operators.CachedObservable;
import rx.internal.operators.OnSubscribeAmb;
import rx.internal.operators.OnSubscribeCombineLatest;
import rx.internal.operators.OnSubscribeDefer;
import rx.internal.operators.OnSubscribeDelaySubscription;
import rx.internal.operators.OnSubscribeDelaySubscriptionWithSelector;
import rx.internal.operators.OnSubscribeFromCallable;
import rx.internal.operators.OnSubscribeFromIterable;
import rx.internal.operators.OnSubscribeGroupJoin;
import rx.internal.operators.OnSubscribeJoin;
import rx.internal.operators.OnSubscribeRange;
import rx.internal.operators.OnSubscribeRedo;
import rx.internal.operators.OnSubscribeSingle;
import rx.internal.operators.OnSubscribeTimerOnce;
import rx.internal.operators.OnSubscribeTimerPeriodically;
import rx.internal.operators.OnSubscribeToObservableFuture;
import rx.internal.operators.OnSubscribeUsing;
import rx.internal.operators.OperatorAll;
import rx.internal.operators.OperatorAny;
import rx.internal.operators.OperatorAsObservable;
import rx.internal.operators.OperatorBufferWithSingleObservable;
import rx.internal.operators.OperatorBufferWithSize;
import rx.internal.operators.OperatorBufferWithStartEndObservable;
import rx.internal.operators.OperatorBufferWithTime;
import rx.internal.operators.OperatorCast;
import rx.internal.operators.OperatorConcat;
import rx.internal.operators.OperatorDebounceWithSelector;
import rx.internal.operators.OperatorDebounceWithTime;
import rx.internal.operators.OperatorDelay;
import rx.internal.operators.OperatorDelayWithSelector;
import rx.internal.operators.OperatorDematerialize;
import rx.internal.operators.OperatorDistinct;
import rx.internal.operators.OperatorDistinctUntilChanged;
import rx.internal.operators.OperatorDoOnEach;
import rx.internal.operators.OperatorDoOnRequest;
import rx.internal.operators.OperatorDoOnSubscribe;
import rx.internal.operators.OperatorDoOnUnsubscribe;
import rx.internal.operators.OperatorEagerConcatMap;
import rx.internal.operators.OperatorElementAt;
import rx.internal.operators.OperatorFilter;
import rx.internal.operators.OperatorFinally;
import rx.internal.operators.OperatorGroupBy;
import rx.internal.operators.OperatorIgnoreElements;
import rx.internal.operators.OperatorMap;
import rx.internal.operators.OperatorMapNotification;
import rx.internal.operators.OperatorMapPair;
import rx.internal.operators.OperatorMaterialize;
import rx.internal.operators.OperatorMerge;
import rx.internal.operators.OperatorObserveOn;
import rx.internal.operators.OperatorOnBackpressureBuffer;
import rx.internal.operators.OperatorOnBackpressureDrop;
import rx.internal.operators.OperatorOnBackpressureLatest;
import rx.internal.operators.OperatorOnErrorResumeNextViaFunction;
import rx.internal.operators.OperatorOnErrorResumeNextViaObservable;
import rx.internal.operators.OperatorOnErrorReturn;
import rx.internal.operators.OperatorOnExceptionResumeNextViaObservable;
import rx.internal.operators.OperatorPublish;
import rx.internal.operators.OperatorReplay;
import rx.internal.operators.OperatorRetryWithPredicate;
import rx.internal.operators.OperatorSampleWithObservable;
import rx.internal.operators.OperatorSampleWithTime;
import rx.internal.operators.OperatorScan;
import rx.internal.operators.OperatorSequenceEqual;
import rx.internal.operators.OperatorSerialize;
import rx.internal.operators.OperatorSingle;
import rx.internal.operators.OperatorSkip;
import rx.internal.operators.OperatorSkipLast;
import rx.internal.operators.OperatorSkipLastTimed;
import rx.internal.operators.OperatorSkipTimed;
import rx.internal.operators.OperatorSkipUntil;
import rx.internal.operators.OperatorSkipWhile;
import rx.internal.operators.OperatorSubscribeOn;
import rx.internal.operators.OperatorSwitch;
import rx.internal.operators.OperatorSwitchIfEmpty;
import rx.internal.operators.OperatorTake;
import rx.internal.operators.OperatorTakeLast;
import rx.internal.operators.OperatorTakeLastOne;
import rx.internal.operators.OperatorTakeLastTimed;
import rx.internal.operators.OperatorTakeTimed;
import rx.internal.operators.OperatorTakeUntil;
import rx.internal.operators.OperatorTakeUntilPredicate;
import rx.internal.operators.OperatorTakeWhile;
import rx.internal.operators.OperatorThrottleFirst;
import rx.internal.operators.OperatorTimeInterval;
import rx.internal.operators.OperatorTimeout;
import rx.internal.operators.OperatorTimeoutWithSelector;
import rx.internal.operators.OperatorTimestamp;
import rx.internal.operators.OperatorToMap;
import rx.internal.operators.OperatorToMultimap;
import rx.internal.operators.OperatorToObservableList;
import rx.internal.operators.OperatorToObservableSortedList;
import rx.internal.operators.OperatorUnsubscribeOn;
import rx.internal.operators.OperatorWindowWithObservable;
import rx.internal.operators.OperatorWindowWithObservableFactory;
import rx.internal.operators.OperatorWindowWithSize;
import rx.internal.operators.OperatorWindowWithStartEndObservable;
import rx.internal.operators.OperatorWindowWithTime;
import rx.internal.operators.OperatorWithLatestFrom;
import rx.internal.operators.OperatorZip;
import rx.internal.operators.OperatorZipIterable;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.UtilityFunctions;
import rx.observables.BlockingObservable;
import rx.observables.ConnectableObservable;
import rx.observables.GroupedObservable;
import rx.observers.SafeSubscriber;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

public class Observable<T> {
    private static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();
    final OnSubscribe<T> onSubscribe;

    protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    public static final <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable(hook.onCreate(f));
    }

    @Experimental
    public <R> R extend(Func1<? super OnSubscribe<T>, ? extends R> conversion) {
        return conversion.call(new 1(this));
    }

    public final <R> Observable<R> lift(Operator<? extends R, ? super T> operator) {
        return new Observable(new 2(this, operator));
    }

    public <R> Observable<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Observable) transformer.call(this);
    }

    @Beta
    public Single<T> toSingle() {
        return new Single(OnSubscribeSingle.create(this));
    }

    public static final <T> Observable<T> amb(Iterable<? extends Observable<? extends T>> sources) {
        return create(OnSubscribeAmb.amb(sources));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2) {
        return create(OnSubscribeAmb.amb(o1, o2));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return create(OnSubscribeAmb.amb(o1, o2, o3));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    public static final <T> Observable<T> amb(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return create(OnSubscribeAmb.amb(o1, o2, o3, o4, o5, o6, o7, o8, o9));
    }

    public static final <T1, T2, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8}), Functions.fromFunc(combineFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatest(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8, o9}), Functions.fromFunc(combineFunction));
    }

    public static final <T, R> Observable<R> combineLatest(List<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction) {
        return create(new OnSubscribeCombineLatest(sources, combineFunction));
    }

    public static final <T> Observable<T> concat(Observable<? extends Observable<? extends T>> observables) {
        return observables.lift(OperatorConcat.instance());
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2) {
        return concat(just(t1, t2));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return concat(just(t1, t2, t3));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return concat(just(t1, t2, t3, t4));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return concat(just(t1, t2, t3, t4, t5));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return concat(just(t1, t2, t3, t4, t5, t6));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static final <T> Observable<T> concat(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public static final <T> Observable<T> defer(Func0<Observable<T>> observableFactory) {
        return create(new OnSubscribeDefer(observableFactory));
    }

    public static final <T> Observable<T> empty() {
        return EmptyHolder.INSTANCE;
    }

    public static final <T> Observable<T> error(Throwable exception) {
        return new ThrowObservable(exception);
    }

    public static final <T> Observable<T> from(Future<? extends T> future) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future));
    }

    public static final <T> Observable<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future, timeout, unit));
    }

    public static final <T> Observable<T> from(Future<? extends T> future, Scheduler scheduler) {
        return create(OnSubscribeToObservableFuture.toObservableFuture(future)).subscribeOn(scheduler);
    }

    public static final <T> Observable<T> from(Iterable<? extends T> iterable) {
        return create(new OnSubscribeFromIterable(iterable));
    }

    public static final <T> Observable<T> from(T[] array) {
        return from(Arrays.asList(array));
    }

    @Experimental
    public static <T> Observable<T> fromCallable(Callable<? extends T> func) {
        return create(new OnSubscribeFromCallable(func));
    }

    public static final Observable<Long> interval(long interval, TimeUnit unit) {
        return interval(interval, interval, unit, Schedulers.computation());
    }

    public static final Observable<Long> interval(long interval, TimeUnit unit, Scheduler scheduler) {
        return interval(interval, interval, unit, scheduler);
    }

    public static final Observable<Long> interval(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    public static final Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerPeriodically(initialDelay, period, unit, scheduler));
    }

    public static final <T> Observable<T> just(T value) {
        return ScalarSynchronousObservable.create(value);
    }

    public static final <T> Observable<T> just(T t1, T t2) {
        return from(Arrays.asList(new Object[]{t1, t2}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3) {
        return from(Arrays.asList(new Object[]{t1, t2, t3}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5, t6}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5, t6, t7}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9}));
    }

    public static final <T> Observable<T> just(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        return from(Arrays.asList(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10}));
    }

    public static final <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences) {
        return merge(from((Iterable) sequences));
    }

    public static final <T> Observable<T> merge(Iterable<? extends Observable<? extends T>> sequences, int maxConcurrent) {
        return merge(from((Iterable) sequences), maxConcurrent);
    }

    public static final <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source) {
        if (source.getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) source).scalarFlatMap(UtilityFunctions.identity());
        }
        return source.lift(OperatorMerge.instance(false));
    }

    public static final <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source, int maxConcurrent) {
        if (source.getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) source).scalarFlatMap(UtilityFunctions.identity());
        }
        return source.lift(OperatorMerge.instance(false, maxConcurrent));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4, t5})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4, t5, t6})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4, t5, t6, t7})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4, t5, t6, t7, t8})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return merge(from(Arrays.asList(new Observable[]{t1, t2, t3, t4, t5, t6, t7, t8, t9})));
    }

    public static final <T> Observable<T> merge(Observable<? extends T>[] sequences) {
        return merge(from((Object[]) sequences));
    }

    public static final <T> Observable<T> merge(Observable<? extends T>[] sequences, int maxConcurrent) {
        return merge(from((Object[]) sequences), maxConcurrent);
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends Observable<? extends T>> source) {
        return source.lift(OperatorMerge.instance(true));
    }

    @Experimental
    public static final <T> Observable<T> mergeDelayError(Observable<? extends Observable<? extends T>> source, int maxConcurrent) {
        return source.lift(OperatorMerge.instance(true, maxConcurrent));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2) {
        return mergeDelayError(just(t1, t2));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3) {
        return mergeDelayError(just(t1, t2, t3));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4) {
        return mergeDelayError(just(t1, t2, t3, t4));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5) {
        return mergeDelayError(just(t1, t2, t3, t4, t5));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    public static final <T> Observable<T> mergeDelayError(Observable<? extends T> t1, Observable<? extends T> t2, Observable<? extends T> t3, Observable<? extends T> t4, Observable<? extends T> t5, Observable<? extends T> t6, Observable<? extends T> t7, Observable<? extends T> t8, Observable<? extends T> t9) {
        return mergeDelayError(just(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    public final Observable<Observable<T>> nest() {
        return just(this);
    }

    public static final <T> Observable<T> never() {
        return NeverObservable.instance();
    }

    public static final Observable<Integer> range(int start, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count can not be negative");
        } else if (count == 0) {
            return empty();
        } else {
            if (start > (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED - count) + 1) {
                throw new IllegalArgumentException("start + count can not exceed Integer.MAX_VALUE");
            } else if (count == 1) {
                return just(Integer.valueOf(start));
            } else {
                return create(new OnSubscribeRange(start, (count - 1) + start));
            }
        }
    }

    public static final Observable<Integer> range(int start, int count, Scheduler scheduler) {
        return range(start, count).subscribeOn(scheduler);
    }

    public static final <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second) {
        return sequenceEqual(first, second, new 3());
    }

    public static final <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return OperatorSequenceEqual.sequenceEqual(first, second, equality);
    }

    public static final <T> Observable<T> switchOnNext(Observable<? extends Observable<? extends T>> sequenceOfSequences) {
        return sequenceOfSequences.lift(OperatorSwitch.instance());
    }

    @Deprecated
    public static final Observable<Long> timer(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    @Deprecated
    public static final Observable<Long> timer(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return interval(initialDelay, period, unit, scheduler);
    }

    public static final Observable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    public static final Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeTimerOnce(delay, unit, scheduler));
    }

    public static final <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction) {
        return using(resourceFactory, observableFactory, disposeAction, false);
    }

    @Experimental
    public static final <T, Resource> Observable<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> disposeAction, boolean disposeEagerly) {
        return create(new OnSubscribeUsing(resourceFactory, observableFactory, disposeAction, disposeEagerly));
    }

    public static final <R> Observable<R> zip(Iterable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        List<Observable<?>> os = new ArrayList();
        for (Observable<?> o : ws) {
            os.add(o);
        }
        return just(os.toArray(new Observable[os.size()])).lift(new OperatorZip(zipFunction));
    }

    public static final <R> Observable<R> zip(Observable<? extends Observable<?>> ws, FuncN<? extends R> zipFunction) {
        return ws.toList().map(new 4()).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8}).lift(new OperatorZip(zipFunction));
    }

    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(Observable<? extends T1> o1, Observable<? extends T2> o2, Observable<? extends T3> o3, Observable<? extends T4> o4, Observable<? extends T5> o5, Observable<? extends T6> o6, Observable<? extends T7> o7, Observable<? extends T8> o8, Observable<? extends T9> o9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return just(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8, o9}).lift(new OperatorZip(zipFunction));
    }

    public final Observable<Boolean> all(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAll(predicate));
    }

    public final Observable<T> ambWith(Observable<? extends T> t1) {
        return amb(this, t1);
    }

    public final Observable<T> asObservable() {
        return lift(OperatorAsObservable.instance());
    }

    public final <TClosing> Observable<List<T>> buffer(Func0<? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithSingleObservable(bufferClosingSelector, 16));
    }

    public final Observable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    public final Observable<List<T>> buffer(int count, int skip) {
        return lift(new OperatorBufferWithSize(count, skip));
    }

    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit) {
        return buffer(timespan, timeshift, unit, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime(timespan, timeshift, unit, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, (int) ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, Schedulers.computation());
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return lift(new OperatorBufferWithTime(timespan, timespan, unit, count, Schedulers.computation()));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorBufferWithTime(timespan, timespan, unit, count, scheduler));
    }

    public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timespan, unit, scheduler);
    }

    public final <TOpening, TClosing> Observable<List<T>> buffer(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        return lift(new OperatorBufferWithStartEndObservable(bufferOpenings, bufferClosingSelector));
    }

    public final <B> Observable<List<T>> buffer(Observable<B> boundary) {
        return buffer((Observable) boundary, 16);
    }

    public final <B> Observable<List<T>> buffer(Observable<B> boundary, int initialCapacity) {
        return lift(new OperatorBufferWithSingleObservable(boundary, initialCapacity));
    }

    public final Observable<T> cache() {
        return CachedObservable.from(this);
    }

    public final Observable<T> cache(int capacityHint) {
        return CachedObservable.from(this, capacityHint);
    }

    public final <R> Observable<R> cast(Class<R> klass) {
        return lift(new OperatorCast(klass));
    }

    public final <R> Observable<R> collect(Func0<R> stateFactory, Action2<R, ? super T> collector) {
        return lift(new OperatorScan(stateFactory, new 5(this, collector))).last();
    }

    public final <R> Observable<R> concatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return concat(map(func));
    }

    public final Observable<T> concatWith(Observable<? extends T> t1) {
        return concat(this, t1);
    }

    public final Observable<Boolean> contains(Object element) {
        return exists(new 6(this, element));
    }

    public final Observable<Integer> count() {
        return reduce(Integer.valueOf(0), CountHolder.INSTANCE);
    }

    public final Observable<Long> countLong() {
        return reduce(Long.valueOf(0), CountLongHolder.INSTANCE);
    }

    public final <U> Observable<T> debounce(Func1<? super T, ? extends Observable<U>> debounceSelector) {
        return lift(new OperatorDebounceWithSelector(debounceSelector));
    }

    public final Observable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    public final Observable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDebounceWithTime(timeout, unit, scheduler));
    }

    public final Observable<T> defaultIfEmpty(T defaultValue) {
        return switchIfEmpty(create(new 7(this, defaultValue)));
    }

    public final Observable<T> switchIfEmpty(Observable<? extends T> alternate) {
        return lift(new OperatorSwitchIfEmpty(alternate));
    }

    public final <U, V> Observable<T> delay(Func0<? extends Observable<U>> subscriptionDelay, Func1<? super T, ? extends Observable<V>> itemDelay) {
        return delaySubscription(subscriptionDelay).lift(new OperatorDelayWithSelector(this, itemDelay));
    }

    public final <U> Observable<T> delay(Func1<? super T, ? extends Observable<U>> itemDelay) {
        return lift(new OperatorDelayWithSelector(this, itemDelay));
    }

    public final Observable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorDelay(delay, unit, scheduler));
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return create(new OnSubscribeDelaySubscription(this, delay, unit, scheduler));
    }

    public final <U> Observable<T> delaySubscription(Func0<? extends Observable<U>> subscriptionDelay) {
        return create(new OnSubscribeDelaySubscriptionWithSelector(this, subscriptionDelay));
    }

    public final <T2> Observable<T2> dematerialize() {
        return lift(OperatorDematerialize.instance());
    }

    public final Observable<T> distinct() {
        return lift(OperatorDistinct.instance());
    }

    public final <U> Observable<T> distinct(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinct(keySelector));
    }

    public final Observable<T> distinctUntilChanged() {
        return lift(OperatorDistinctUntilChanged.instance());
    }

    public final <U> Observable<T> distinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        return lift(new OperatorDistinctUntilChanged(keySelector));
    }

    public final Observable<T> doOnCompleted(Action0 onCompleted) {
        return lift(new OperatorDoOnEach(new 8(this, onCompleted)));
    }

    public final Observable<T> doOnEach(Action1<Notification<? super T>> onNotification) {
        return lift(new OperatorDoOnEach(new 9(this, onNotification)));
    }

    public final Observable<T> doOnEach(Observer<? super T> observer) {
        return lift(new OperatorDoOnEach(observer));
    }

    public final Observable<T> doOnError(Action1<Throwable> onError) {
        return lift(new OperatorDoOnEach(new 10(this, onError)));
    }

    public final Observable<T> doOnNext(Action1<? super T> onNext) {
        return lift(new OperatorDoOnEach(new 11(this, onNext)));
    }

    @Beta
    public final Observable<T> doOnRequest(Action1<Long> onRequest) {
        return lift(new OperatorDoOnRequest(onRequest));
    }

    public final Observable<T> doOnSubscribe(Action0 subscribe) {
        return lift(new OperatorDoOnSubscribe(subscribe));
    }

    public final Observable<T> doOnTerminate(Action0 onTerminate) {
        return lift(new OperatorDoOnEach(new 12(this, onTerminate)));
    }

    public final Observable<T> doOnUnsubscribe(Action0 unsubscribe) {
        return lift(new OperatorDoOnUnsubscribe(unsubscribe));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends T> o1, Observable<? extends T> o2, Observable<? extends T> o3, Observable<? extends T> o4, Observable<? extends T> o5, Observable<? extends T> o6, Observable<? extends T> o7, Observable<? extends T> o8, Observable<? extends T> o9) {
        return concatEager(Arrays.asList(new Observable[]{o1, o2, o3, o4, o5, o6, o7, o8, o9}));
    }

    @Experimental
    public static <T> Observable<T> concatEager(Iterable<? extends Observable<? extends T>> sources) {
        return from((Iterable) sources).concatMapEager(UtilityFunctions.identity());
    }

    @Experimental
    public static <T> Observable<T> concatEager(Iterable<? extends Observable<? extends T>> sources, int capacityHint) {
        return from((Iterable) sources).concatMapEager(UtilityFunctions.identity(), capacityHint);
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends Observable<? extends T>> sources) {
        return sources.concatMapEager(UtilityFunctions.identity());
    }

    @Experimental
    public static <T> Observable<T> concatEager(Observable<? extends Observable<? extends T>> sources, int capacityHint) {
        return sources.concatMapEager(UtilityFunctions.identity(), capacityHint);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper) {
        return concatMapEager(mapper, RxRingBuffer.SIZE);
    }

    @Experimental
    public final <R> Observable<R> concatMapEager(Func1<? super T, ? extends Observable<? extends R>> mapper, int capacityHint) {
        if (capacityHint >= 1) {
            return lift(new OperatorEagerConcatMap(mapper, capacityHint));
        }
        throw new IllegalArgumentException("capacityHint > 0 required but it was " + capacityHint);
    }

    public final Observable<T> elementAt(int index) {
        return lift(new OperatorElementAt(index));
    }

    public final Observable<T> elementAtOrDefault(int index, T defaultValue) {
        return lift(new OperatorElementAt(index, defaultValue));
    }

    public final Observable<Boolean> exists(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorAny(predicate, false));
    }

    public final Observable<T> filter(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorFilter(predicate));
    }

    public final Observable<T> finallyDo(Action0 action) {
        return lift(new OperatorFinally(action));
    }

    public final Observable<T> first() {
        return take(1).single();
    }

    public final Observable<T> first(Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).single();
    }

    public final Observable<T> firstOrDefault(T defaultValue) {
        return take(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return takeFirst(predicate).singleOrDefault(defaultValue);
    }

    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        if (getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return merge(map(func));
    }

    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> func, int maxConcurrent) {
        if (getClass() == ScalarSynchronousObservable.class) {
            return ((ScalarSynchronousObservable) this).scalarFlatMap(func);
        }
        return merge(map(func), maxConcurrent);
    }

    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> onNext, Func1<? super Throwable, ? extends Observable<? extends R>> onError, Func0<? extends Observable<? extends R>> onCompleted) {
        return merge(mapNotification(onNext, onError, onCompleted));
    }

    @Beta
    public final <R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends R>> onNext, Func1<? super Throwable, ? extends Observable<? extends R>> onError, Func0<? extends Observable<? extends R>> onCompleted, int maxConcurrent) {
        return merge(mapNotification(onNext, onError, onCompleted), maxConcurrent);
    }

    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)));
    }

    @Beta
    public final <U, R> Observable<R> flatMap(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector, int maxConcurrent) {
        return merge(lift(new OperatorMapPair(collectionSelector, resultSelector)), maxConcurrent);
    }

    public final <R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends R>> collectionSelector) {
        return merge(map(OperatorMapPair.convertSelector(collectionSelector)));
    }

    public final <U, R> Observable<R> flatMapIterable(Func1<? super T, ? extends Iterable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return flatMap(OperatorMapPair.convertSelector(collectionSelector), (Func2) resultSelector);
    }

    public final void forEach(Action1<? super T> onNext) {
        subscribe((Action1) onNext);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError) {
        subscribe((Action1) onNext, (Action1) onError);
    }

    public final void forEach(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        subscribe(onNext, onError, onComplete);
    }

    public final <K, R> Observable<GroupedObservable<K, R>> groupBy(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> elementSelector) {
        return lift(new OperatorGroupBy(keySelector, elementSelector));
    }

    public final <K> Observable<GroupedObservable<K, T>> groupBy(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorGroupBy(keySelector));
    }

    public final <T2, D1, D2, R> Observable<R> groupJoin(Observable<T2> right, Func1<? super T, ? extends Observable<D1>> leftDuration, Func1<? super T2, ? extends Observable<D2>> rightDuration, Func2<? super T, ? super Observable<T2>, ? extends R> resultSelector) {
        return create(new OnSubscribeGroupJoin(this, right, leftDuration, rightDuration, resultSelector));
    }

    public final Observable<T> ignoreElements() {
        return lift(OperatorIgnoreElements.instance());
    }

    public final Observable<Boolean> isEmpty() {
        return lift(HolderAnyForEmpty.INSTANCE);
    }

    public final <TRight, TLeftDuration, TRightDuration, R> Observable<R> join(Observable<TRight> right, Func1<T, Observable<TLeftDuration>> leftDurationSelector, Func1<TRight, Observable<TRightDuration>> rightDurationSelector, Func2<T, TRight, R> resultSelector) {
        return create(new OnSubscribeJoin(this, right, leftDurationSelector, rightDurationSelector, resultSelector));
    }

    public final Observable<T> last() {
        return takeLast(1).single();
    }

    public final Observable<T> last(Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).single();
    }

    public final Observable<T> lastOrDefault(T defaultValue) {
        return takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).takeLast(1).singleOrDefault(defaultValue);
    }

    public final Observable<T> limit(int count) {
        return take(count);
    }

    public final <R> Observable<R> map(Func1<? super T, ? extends R> func) {
        return lift(new OperatorMap(func));
    }

    private final <R> Observable<R> mapNotification(Func1<? super T, ? extends R> onNext, Func1<? super Throwable, ? extends R> onError, Func0<? extends R> onCompleted) {
        return lift(new OperatorMapNotification(onNext, onError, onCompleted));
    }

    public final Observable<Notification<T>> materialize() {
        return lift(OperatorMaterialize.instance());
    }

    public final Observable<T> mergeWith(Observable<? extends T> t1) {
        return merge(this, (Observable) t1);
    }

    public final Observable<T> observeOn(Scheduler scheduler) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler);
        }
        return lift(new OperatorObserveOn(scheduler));
    }

    public final <R> Observable<R> ofType(Class<R> klass) {
        return filter(new 13(this, klass)).cast(klass);
    }

    public final Observable<T> onBackpressureBuffer() {
        return lift(OperatorOnBackpressureBuffer.instance());
    }

    public final Observable<T> onBackpressureBuffer(long capacity) {
        return lift(new OperatorOnBackpressureBuffer(capacity));
    }

    public final Observable<T> onBackpressureBuffer(long capacity, Action0 onOverflow) {
        return lift(new OperatorOnBackpressureBuffer(capacity, onOverflow));
    }

    public final Observable<T> onBackpressureDrop(Action1<? super T> onDrop) {
        return lift(new OperatorOnBackpressureDrop(onDrop));
    }

    public final Observable<T> onBackpressureDrop() {
        return lift(OperatorOnBackpressureDrop.instance());
    }

    public final Observable<T> onBackpressureLatest() {
        return lift(OperatorOnBackpressureLatest.instance());
    }

    public final Observable<T> onErrorResumeNext(Func1<Throwable, ? extends Observable<? extends T>> resumeFunction) {
        return lift(new OperatorOnErrorResumeNextViaFunction(resumeFunction));
    }

    public final Observable<T> onErrorResumeNext(Observable<? extends T> resumeSequence) {
        return lift(new OperatorOnErrorResumeNextViaObservable(resumeSequence));
    }

    public final Observable<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return lift(new OperatorOnErrorReturn(resumeFunction));
    }

    public final Observable<T> onExceptionResumeNext(Observable<? extends T> resumeSequence) {
        return lift(new OperatorOnExceptionResumeNextViaObservable(resumeSequence));
    }

    public final ConnectableObservable<T> publish() {
        return OperatorPublish.create(this);
    }

    public final <R> Observable<R> publish(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorPublish.create(this, selector);
    }

    public final Observable<T> reduce(Func2<T, T, T> accumulator) {
        return scan(accumulator).last();
    }

    public final <R> Observable<R> reduce(R initialValue, Func2<R, ? super T, R> accumulator) {
        return scan(initialValue, accumulator).takeLast(1);
    }

    public final Observable<T> repeat() {
        return OnSubscribeRedo.repeat(this);
    }

    public final Observable<T> repeat(Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, scheduler);
    }

    public final Observable<T> repeat(long count) {
        return OnSubscribeRedo.repeat(this, count);
    }

    public final Observable<T> repeat(long count, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, count, scheduler);
    }

    public final Observable<T> repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.repeat(this, new 14(this, notificationHandler), scheduler);
    }

    public final Observable<T> repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.repeat(this, new 15(this, notificationHandler));
    }

    public final ConnectableObservable<T> replay() {
        return OperatorReplay.create(this);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return OperatorReplay.multicastSelector(new 16(this), selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize) {
        return OperatorReplay.multicastSelector(new 17(this, bufferSize), selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        if (bufferSize >= 0) {
            return OperatorReplay.multicastSelector(new 18(this, bufferSize, time, unit, scheduler), selector);
        }
        throw new IllegalArgumentException("bufferSize < 0");
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, int bufferSize, Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new 19(this, bufferSize), new 20(this, selector, scheduler));
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit) {
        return replay((Func1) selector, time, unit, Schedulers.computation());
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new 21(this, time, unit, scheduler), selector);
    }

    public final <R> Observable<R> replay(Func1<? super Observable<T>, ? extends Observable<R>> selector, Scheduler scheduler) {
        return OperatorReplay.multicastSelector(new 22(this), new 23(this, selector, scheduler));
    }

    public final ConnectableObservable<T> replay(int bufferSize) {
        return OperatorReplay.create(this, bufferSize);
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        if (bufferSize >= 0) {
            return OperatorReplay.create(this, time, unit, scheduler, bufferSize);
        }
        throw new IllegalArgumentException("bufferSize < 0");
    }

    public final ConnectableObservable<T> replay(int bufferSize, Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(bufferSize), scheduler);
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    public final ConnectableObservable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        return OperatorReplay.create(this, time, unit, scheduler);
    }

    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        return OperatorReplay.observeOn(replay(), scheduler);
    }

    public final Observable<T> retry() {
        return OnSubscribeRedo.retry(this);
    }

    public final Observable<T> retry(long count) {
        return OnSubscribeRedo.retry(this, count);
    }

    public final Observable<T> retry(Func2<Integer, Throwable, Boolean> predicate) {
        return nest().lift(new OperatorRetryWithPredicate(predicate));
    }

    public final Observable<T> retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.retry(this, new 24(this, notificationHandler));
    }

    public final Observable<T> retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler, Scheduler scheduler) {
        return OnSubscribeRedo.retry(this, new 25(this, notificationHandler), scheduler);
    }

    public final Observable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSampleWithTime(period, unit, scheduler));
    }

    public final <U> Observable<T> sample(Observable<U> sampler) {
        return lift(new OperatorSampleWithObservable(sampler));
    }

    public final Observable<T> scan(Func2<T, T, T> accumulator) {
        return lift(new OperatorScan(accumulator));
    }

    public final <R> Observable<R> scan(R initialValue, Func2<R, ? super T, R> accumulator) {
        return lift(new OperatorScan(initialValue, accumulator));
    }

    public final Observable<T> serialize() {
        return lift(OperatorSerialize.instance());
    }

    public final Observable<T> share() {
        return publish().refCount();
    }

    public final Observable<T> single() {
        return lift(OperatorSingle.instance());
    }

    public final Observable<T> single(Func1<? super T, Boolean> predicate) {
        return filter(predicate).single();
    }

    public final Observable<T> singleOrDefault(T defaultValue) {
        return lift(new OperatorSingle(defaultValue));
    }

    public final Observable<T> singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return filter(predicate).singleOrDefault(defaultValue);
    }

    public final Observable<T> skip(int count) {
        return lift(new OperatorSkip(count));
    }

    public final Observable<T> skip(long time, TimeUnit unit) {
        return skip(time, unit, Schedulers.computation());
    }

    public final Observable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipTimed(time, unit, scheduler));
    }

    public final Observable<T> skipLast(int count) {
        return lift(new OperatorSkipLast(count));
    }

    public final Observable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorSkipLastTimed(time, unit, scheduler));
    }

    public final <U> Observable<T> skipUntil(Observable<U> other) {
        return lift(new OperatorSkipUntil(other));
    }

    public final Observable<T> skipWhile(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorSkipWhile(OperatorSkipWhile.toPredicate2(predicate)));
    }

    public final Observable<T> startWith(Observable<T> values) {
        return concat(values, this);
    }

    public final Observable<T> startWith(Iterable<T> values) {
        return concat(from((Iterable) values), this);
    }

    public final Observable<T> startWith(T t1) {
        return concat(just(t1), this);
    }

    public final Observable<T> startWith(T t1, T t2) {
        return concat(just(t1, t2), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3) {
        return concat(just(t1, t2, t3), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4) {
        return concat(just(t1, t2, t3, t4), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5) {
        return concat(just(t1, t2, t3, t4, t5), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6) {
        return concat(just(t1, t2, t3, t4, t5, t6), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8), this);
    }

    public final Observable<T> startWith(T t1, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        return concat(just(t1, t2, t3, t4, t5, t6, t7, t8, t9), this);
    }

    public final Subscription subscribe() {
        return subscribe(new 26(this));
    }

    public final Subscription subscribe(Action1<? super T> onNext) {
        if (onNext != null) {
            return subscribe(new 27(this, onNext));
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public final Subscription subscribe(Action1<? super T> onNext, Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError != null) {
            return subscribe(new 28(this, onError, onNext));
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final Subscription subscribe(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (onComplete != null) {
            return subscribe(new 29(this, onComplete, onError, onNext));
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }

    public final Subscription subscribe(Observer<? super T> observer) {
        if (observer instanceof Subscriber) {
            return subscribe((Subscriber) observer);
        }
        return subscribe(new 30(this, observer));
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            subscriber.onStart();
            hook.onSubscribeStart(this, this.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2));
        }
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        return subscribe((Subscriber) subscriber, this);
    }

    private static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        } else if (observable.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        } else {
            subscriber.onStart();
            if (!(subscriber instanceof SafeSubscriber)) {
                subscriber = new SafeSubscriber(subscriber);
            }
            try {
                hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
                return hook.onSubscribeReturn(subscriber);
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                hook.onSubscribeError(new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2));
            }
        }
    }

    public final Observable<T> subscribeOn(Scheduler scheduler) {
        if (this instanceof ScalarSynchronousObservable) {
            return ((ScalarSynchronousObservable) this).scalarScheduleOn(scheduler);
        }
        return nest().lift(new OperatorSubscribeOn(scheduler));
    }

    public final <R> Observable<R> switchMap(Func1<? super T, ? extends Observable<? extends R>> func) {
        return switchOnNext(map(func));
    }

    public final Observable<T> take(int count) {
        return lift(new OperatorTake(count));
    }

    public final Observable<T> take(long time, TimeUnit unit) {
        return take(time, unit, Schedulers.computation());
    }

    public final Observable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeTimed(time, unit, scheduler));
    }

    public final Observable<T> takeFirst(Func1<? super T, Boolean> predicate) {
        return filter(predicate).take(1);
    }

    public final Observable<T> takeLast(int count) {
        if (count == 0) {
            return ignoreElements();
        }
        if (count == 1) {
            return lift(OperatorTakeLastOne.instance());
        }
        return lift(new OperatorTakeLast(count));
    }

    public final Observable<T> takeLast(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed(count, time, unit, scheduler));
    }

    public final Observable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation());
    }

    public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorTakeLastTimed(time, unit, scheduler));
    }

    public final Observable<List<T>> takeLastBuffer(int count) {
        return takeLast(count).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit) {
        return takeLast(count, time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(int count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit) {
        return takeLast(time, unit).toList();
    }

    public final Observable<List<T>> takeLastBuffer(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler).toList();
    }

    public final <E> Observable<T> takeUntil(Observable<? extends E> other) {
        return lift(new OperatorTakeUntil(other));
    }

    public final Observable<T> takeWhile(Func1<? super T, Boolean> predicate) {
        return lift(new OperatorTakeWhile(predicate));
    }

    public final Observable<T> takeUntil(Func1<? super T, Boolean> stopPredicate) {
        return lift(new OperatorTakeUntilPredicate(stopPredicate));
    }

    public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    public final Observable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        return lift(new OperatorThrottleFirst(skipDuration, unit, scheduler));
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    public final Observable<TimeInterval<T>> timeInterval() {
        return timeInterval(Schedulers.immediate());
    }

    public final Observable<TimeInterval<T>> timeInterval(Scheduler scheduler) {
        return lift(new OperatorTimeInterval(scheduler));
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout((Func0) firstTimeoutSelector, (Func1) timeoutSelector, null);
    }

    public final <U, V> Observable<T> timeout(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        if (timeoutSelector != null) {
            return lift(new OperatorTimeoutWithSelector(firstTimeoutSelector, timeoutSelector, other));
        }
        throw new NullPointerException("timeoutSelector is null");
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector) {
        return timeout(null, (Func1) timeoutSelector, null);
    }

    public final <V> Observable<T> timeout(Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        return timeout(null, (Func1) timeoutSelector, (Observable) other);
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        return lift(new OperatorTimeout(timeout, timeUnit, other, scheduler));
    }

    public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Observable<Timestamped<T>> timestamp() {
        return timestamp(Schedulers.immediate());
    }

    public final Observable<Timestamped<T>> timestamp(Scheduler scheduler) {
        return lift(new OperatorTimestamp(scheduler));
    }

    public final BlockingObservable<T> toBlocking() {
        return BlockingObservable.from(this);
    }

    public final Observable<List<T>> toList() {
        return lift(OperatorToObservableList.instance());
    }

    public final <K> Observable<Map<K, T>> toMap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMap(keySelector, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMap(keySelector, valueSelector));
    }

    public final <K, V> Observable<Map<K, V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, V>> mapFactory) {
        return lift(new OperatorToMap(keySelector, valueSelector, mapFactory));
    }

    public final <K> Observable<Map<K, Collection<T>>> toMultimap(Func1<? super T, ? extends K> keySelector) {
        return lift(new OperatorToMultimap(keySelector, UtilityFunctions.identity()));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return lift(new OperatorToMultimap(keySelector, valueSelector));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory) {
        return lift(new OperatorToMultimap(keySelector, valueSelector, mapFactory));
    }

    public final <K, V> Observable<Map<K, Collection<V>>> toMultimap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, Collection<V>>> mapFactory, Func1<? super K, ? extends Collection<V>> collectionFactory) {
        return lift(new OperatorToMultimap(keySelector, valueSelector, mapFactory, collectionFactory));
    }

    public final Observable<List<T>> toSortedList() {
        return lift(new OperatorToObservableSortedList(10));
    }

    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> sortFunction) {
        return lift(new OperatorToObservableSortedList(sortFunction, 10));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(int initialCapacity) {
        return lift(new OperatorToObservableSortedList(initialCapacity));
    }

    @Experimental
    public final Observable<List<T>> toSortedList(Func2<? super T, ? super T, Integer> sortFunction, int initialCapacity) {
        return lift(new OperatorToObservableSortedList(sortFunction, initialCapacity));
    }

    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        return lift(new OperatorUnsubscribeOn(scheduler));
    }

    @Experimental
    public final <U, R> Observable<R> withLatestFrom(Observable<? extends U> other, Func2<? super T, ? super U, ? extends R> resultSelector) {
        return lift(new OperatorWithLatestFrom(other, resultSelector));
    }

    public final <TClosing> Observable<Observable<T>> window(Func0<? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithObservableFactory(closingSelector));
    }

    public final Observable<Observable<T>> window(int count) {
        return window(count, count);
    }

    public final Observable<Observable<T>> window(int count, int skip) {
        return lift(new OperatorWindowWithSize(count, skip));
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit) {
        return window(timespan, timeshift, unit, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeshift, unit, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, scheduler);
    }

    public final Observable<Observable<T>> window(long timespan, long timeshift, TimeUnit unit, int count, Scheduler scheduler) {
        return lift(new OperatorWindowWithTime(timespan, timeshift, unit, count, scheduler));
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, timespan, unit, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count) {
        return window(timespan, unit, count, Schedulers.computation());
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, int count, Scheduler scheduler) {
        return window(timespan, timespan, unit, count, scheduler);
    }

    public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, (int) ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, scheduler);
    }

    public final <TOpening, TClosing> Observable<Observable<T>> window(Observable<? extends TOpening> windowOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> closingSelector) {
        return lift(new OperatorWindowWithStartEndObservable(windowOpenings, closingSelector));
    }

    public final <U> Observable<Observable<T>> window(Observable<U> boundary) {
        return lift(new OperatorWindowWithObservable(boundary));
    }

    public final <T2, R> Observable<R> zipWith(Iterable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return lift(new OperatorZipIterable(other, zipFunction));
    }

    public final <T2, R> Observable<R> zipWith(Observable<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }
}
