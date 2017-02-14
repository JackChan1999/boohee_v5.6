package rx.internal.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.RefCountSubscription;

public final class OnSubscribeGroupJoin<T1, T2, D1, D2, R> implements Observable$OnSubscribe<R> {
    protected final Observable<T1> left;
    protected final Func1<? super T1, ? extends Observable<D1>> leftDuration;
    protected final Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector;
    protected final Observable<T2> right;
    protected final Func1<? super T2, ? extends Observable<D2>> rightDuration;

    final class ResultManager implements Subscription {
        final RefCountSubscription cancel;
        final CompositeSubscription group;
        final Object guard = new Object();
        boolean leftDone;
        int leftIds;
        final Map<Integer, Observer<T2>> leftMap = new HashMap();
        boolean rightDone;
        int rightIds;
        final Map<Integer, T2> rightMap = new HashMap();
        final Subscriber<? super R> subscriber;

        final class LeftDurationObserver extends Subscriber<D1> {
            final int id;
            boolean once = true;

            public LeftDurationObserver(int id) {
                this.id = id;
            }

            public void onCompleted() {
                if (this.once) {
                    Observer<T2> gr;
                    this.once = false;
                    synchronized (ResultManager.this.guard) {
                        gr = (Observer) ResultManager.this.leftMap.remove(Integer.valueOf(this.id));
                    }
                    if (gr != null) {
                        gr.onCompleted();
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D1 d1) {
                onCompleted();
            }
        }

        final class LeftObserver extends Subscriber<T1> {
            LeftObserver() {
            }

            public void onNext(T1 args) {
                try {
                    int id;
                    Subject<T2, T2> subj = PublishSubject.create();
                    Observer<T2> subjSerial = new SerializedObserver(subj);
                    synchronized (ResultManager.this.guard) {
                        ResultManager resultManager = ResultManager.this;
                        id = resultManager.leftIds;
                        resultManager.leftIds = id + 1;
                        ResultManager.this.leftMap.put(Integer.valueOf(id), subjSerial);
                    }
                    Observable<T2> window = Observable.create(new WindowObservableFunc(subj, ResultManager.this.cancel));
                    Observable<D1> duration = (Observable) OnSubscribeGroupJoin.this.leftDuration.call(args);
                    Subscriber<D1> d1 = new LeftDurationObserver(id);
                    ResultManager.this.group.add(d1);
                    duration.unsafeSubscribe(d1);
                    R result = OnSubscribeGroupJoin.this.resultSelector.call(args, window);
                    synchronized (ResultManager.this.guard) {
                        List<T2> rightMapValues = new ArrayList(ResultManager.this.rightMap.values());
                    }
                    ResultManager.this.subscriber.onNext(result);
                    for (T2 t2 : rightMapValues) {
                        subjSerial.onNext(t2);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this.guard) {
                    ResultManager.this.leftDone = true;
                    if (ResultManager.this.rightDone) {
                        List<Observer<T2>> list2 = new ArrayList(ResultManager.this.leftMap.values());
                        try {
                            ResultManager.this.leftMap.clear();
                            ResultManager.this.rightMap.clear();
                            list = list2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            list = list2;
                            throw th2;
                        }
                    }
                    try {
                        ResultManager.this.complete(list);
                    } catch (Throwable th3) {
                        th2 = th3;
                        throw th2;
                    }
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        final class RightDurationObserver extends Subscriber<D2> {
            final int id;
            boolean once = true;

            public RightDurationObserver(int id) {
                this.id = id;
            }

            public void onCompleted() {
                if (this.once) {
                    this.once = false;
                    synchronized (ResultManager.this.guard) {
                        ResultManager.this.rightMap.remove(Integer.valueOf(this.id));
                    }
                    ResultManager.this.group.remove(this);
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorMain(e);
            }

            public void onNext(D2 d2) {
                onCompleted();
            }
        }

        final class RightObserver extends Subscriber<T2> {
            RightObserver() {
            }

            public void onNext(T2 args) {
                try {
                    int id;
                    synchronized (ResultManager.this.guard) {
                        ResultManager resultManager = ResultManager.this;
                        id = resultManager.rightIds;
                        resultManager.rightIds = id + 1;
                        ResultManager.this.rightMap.put(Integer.valueOf(id), args);
                    }
                    Observable<D2> duration = (Observable) OnSubscribeGroupJoin.this.rightDuration.call(args);
                    Subscriber<D2> d2 = new RightDurationObserver(id);
                    ResultManager.this.group.add(d2);
                    duration.unsafeSubscribe(d2);
                    synchronized (ResultManager.this.guard) {
                        List<Observer<T2>> list = new ArrayList(ResultManager.this.leftMap.values());
                    }
                    for (Observer<T2> o : list) {
                        o.onNext(args);
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onCompleted() {
                List<Observer<T2>> list = null;
                synchronized (ResultManager.this.guard) {
                    ResultManager.this.rightDone = true;
                    if (ResultManager.this.leftDone) {
                        List<Observer<T2>> list2 = new ArrayList(ResultManager.this.leftMap.values());
                        try {
                            ResultManager.this.leftMap.clear();
                            ResultManager.this.rightMap.clear();
                            list = list2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            list = list2;
                            throw th2;
                        }
                    }
                    try {
                        ResultManager.this.complete(list);
                    } catch (Throwable th3) {
                        th2 = th3;
                        throw th2;
                    }
                }
            }

            public void onError(Throwable e) {
                ResultManager.this.errorAll(e);
            }
        }

        public ResultManager(Subscriber<? super R> subscriber) {
            this.subscriber = subscriber;
            this.group = new CompositeSubscription();
            this.cancel = new RefCountSubscription(this.group);
        }

        public void init() {
            Subscriber<T1> s1 = new LeftObserver();
            Subscriber<T2> s2 = new RightObserver();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeGroupJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeGroupJoin.this.right.unsafeSubscribe(s2);
        }

        public void unsubscribe() {
            this.cancel.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.cancel.isUnsubscribed();
        }

        void errorAll(Throwable e) {
            synchronized (this.guard) {
                List<Observer<T2>> list = new ArrayList(this.leftMap.values());
                this.leftMap.clear();
                this.rightMap.clear();
            }
            for (Observer<T2> o : list) {
                o.onError(e);
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        void errorMain(Throwable e) {
            synchronized (this.guard) {
                this.leftMap.clear();
                this.rightMap.clear();
            }
            this.subscriber.onError(e);
            this.cancel.unsubscribe();
        }

        void complete(List<Observer<T2>> list) {
            if (list != null) {
                for (Observer<T2> o : list) {
                    o.onCompleted();
                }
                this.subscriber.onCompleted();
                this.cancel.unsubscribe();
            }
        }
    }

    static final class WindowObservableFunc<T> implements Observable$OnSubscribe<T> {
        final RefCountSubscription refCount;
        final Observable<T> underlying;

        final class WindowSubscriber extends Subscriber<T> {
            private final Subscription ref;
            final Subscriber<? super T> subscriber;

            public WindowSubscriber(Subscriber<? super T> subscriber, Subscription ref) {
                super(subscriber);
                this.subscriber = subscriber;
                this.ref = ref;
            }

            public void onNext(T args) {
                this.subscriber.onNext(args);
            }

            public void onError(Throwable e) {
                this.subscriber.onError(e);
                this.ref.unsubscribe();
            }

            public void onCompleted() {
                this.subscriber.onCompleted();
                this.ref.unsubscribe();
            }
        }

        public WindowObservableFunc(Observable<T> underlying, RefCountSubscription refCount) {
            this.refCount = refCount;
            this.underlying = underlying;
        }

        public void call(Subscriber<? super T> t1) {
            Subscription ref = this.refCount.get();
            WindowSubscriber wo = new WindowSubscriber(t1, ref);
            wo.add(ref);
            this.underlying.unsafeSubscribe(wo);
        }
    }

    public OnSubscribeGroupJoin(Observable<T1> left, Observable<T2> right, Func1<? super T1, ? extends Observable<D1>> leftDuration, Func1<? super T2, ? extends Observable<D2>> rightDuration, Func2<? super T1, ? super Observable<T2>, ? extends R> resultSelector) {
        this.left = left;
        this.right = right;
        this.leftDuration = leftDuration;
        this.rightDuration = rightDuration;
        this.resultSelector = resultSelector;
    }

    public void call(Subscriber<? super R> child) {
        ResultManager ro = new ResultManager(new SerializedSubscriber(child));
        child.add(ro);
        ro.init();
    }
}
