package rx.internal.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.SerialSubscription;

public final class OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R> implements Observable$OnSubscribe<R> {
    final Observable<TLeft> left;
    final Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector;
    final Func2<TLeft, TRight, R> resultSelector;
    final Observable<TRight> right;
    final Func1<TRight, Observable<TRightDuration>> rightDurationSelector;

    final class ResultSink {
        final CompositeSubscription group;
        final Object guard = new Object();
        boolean leftDone;
        int leftId;
        final Map<Integer, TLeft> leftMap;
        boolean rightDone;
        int rightId;
        final Map<Integer, TRight> rightMap;
        final Subscriber<? super R> subscriber;

        final class LeftSubscriber extends Subscriber<TLeft> {

            final class LeftDurationSubscriber extends Subscriber<TLeftDuration> {
                final int id;
                boolean once = true;

                public LeftDurationSubscriber(int id) {
                    this.id = id;
                }

                public void onNext(TLeftDuration tLeftDuration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    LeftSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        LeftSubscriber.this.expire(this.id, this);
                    }
                }
            }

            LeftSubscriber() {
            }

            protected void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.leftMap.remove(Integer.valueOf(id)) != null && ResultSink.this.leftMap.isEmpty() && ResultSink.this.leftDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            public void onNext(TLeft args) {
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    int id = resultSink.leftId;
                    resultSink.leftId = id + 1;
                    ResultSink.this.leftMap.put(Integer.valueOf(id), args);
                    int highRightId = ResultSink.this.rightId;
                }
                try {
                    Observable<TLeftDuration> duration = (Observable) OnSubscribeJoin.this.leftDurationSelector.call(args);
                    Subscriber<TLeftDuration> d1 = new LeftDurationSubscriber(id);
                    ResultSink.this.group.add(d1);
                    duration.unsafeSubscribe(d1);
                    List<TRight> rightValues = new ArrayList();
                    synchronized (ResultSink.this.guard) {
                        for (Entry<Integer, TRight> entry : ResultSink.this.rightMap.entrySet()) {
                            if (((Integer) entry.getKey()).intValue() < highRightId) {
                                rightValues.add(entry.getValue());
                            }
                        }
                    }
                    for (TRight r : rightValues) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(args, r));
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.leftDone = true;
                    if (ResultSink.this.rightDone || ResultSink.this.leftMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }
        }

        final class RightSubscriber extends Subscriber<TRight> {

            final class RightDurationSubscriber extends Subscriber<TRightDuration> {
                final int id;
                boolean once = true;

                public RightDurationSubscriber(int id) {
                    this.id = id;
                }

                public void onNext(TRightDuration tRightDuration) {
                    onCompleted();
                }

                public void onError(Throwable e) {
                    RightSubscriber.this.onError(e);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        RightSubscriber.this.expire(this.id, this);
                    }
                }
            }

            RightSubscriber() {
            }

            void expire(int id, Subscription resource) {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    if (ResultSink.this.rightMap.remove(Integer.valueOf(id)) != null && ResultSink.this.rightMap.isEmpty() && ResultSink.this.rightDone) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(resource);
            }

            public void onNext(TRight args) {
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    int id = resultSink.rightId;
                    resultSink.rightId = id + 1;
                    ResultSink.this.rightMap.put(Integer.valueOf(id), args);
                    int highLeftId = ResultSink.this.leftId;
                }
                ResultSink.this.group.add(new SerialSubscription());
                try {
                    Observable<TRightDuration> duration = (Observable) OnSubscribeJoin.this.rightDurationSelector.call(args);
                    Subscriber<TRightDuration> d2 = new RightDurationSubscriber(id);
                    ResultSink.this.group.add(d2);
                    duration.unsafeSubscribe(d2);
                    List<TLeft> leftValues = new ArrayList();
                    synchronized (ResultSink.this.guard) {
                        for (Entry<Integer, TLeft> entry : ResultSink.this.leftMap.entrySet()) {
                            if (((Integer) entry.getKey()).intValue() < highLeftId) {
                                leftValues.add(entry.getValue());
                            }
                        }
                    }
                    for (TLeft lv : leftValues) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(lv, args));
                    }
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }

            public void onError(Throwable e) {
                ResultSink.this.subscriber.onError(e);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean complete = false;
                synchronized (ResultSink.this.guard) {
                    ResultSink.this.rightDone = true;
                    if (ResultSink.this.leftDone || ResultSink.this.rightMap.isEmpty()) {
                        complete = true;
                    }
                }
                if (complete) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }
        }

        public ResultSink(Subscriber<? super R> subscriber) {
            this.subscriber = subscriber;
            this.group = new CompositeSubscription();
            this.leftMap = new HashMap();
            this.rightMap = new HashMap();
        }

        public void run() {
            this.subscriber.add(this.group);
            Subscriber<TLeft> s1 = new LeftSubscriber();
            Subscriber<TRight> s2 = new RightSubscriber();
            this.group.add(s1);
            this.group.add(s2);
            OnSubscribeJoin.this.left.unsafeSubscribe(s1);
            OnSubscribeJoin.this.right.unsafeSubscribe(s2);
        }
    }

    public OnSubscribeJoin(Observable<TLeft> left, Observable<TRight> right, Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector, Func1<TRight, Observable<TRightDuration>> rightDurationSelector, Func2<TLeft, TRight, R> resultSelector) {
        this.left = left;
        this.right = right;
        this.leftDurationSelector = leftDurationSelector;
        this.rightDurationSelector = rightDurationSelector;
        this.resultSelector = resultSelector;
    }

    public void call(Subscriber<? super R> t1) {
        new ResultSink(new SerializedSubscriber(t1)).run();
    }
}
