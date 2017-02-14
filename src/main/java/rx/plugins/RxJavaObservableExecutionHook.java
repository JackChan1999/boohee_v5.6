package rx.plugins;

import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Observable$Operator;
import rx.Subscription;

public abstract class RxJavaObservableExecutionHook {
    public <T> Observable$OnSubscribe<T> onCreate(Observable$OnSubscribe<T> f) {
        return f;
    }

    public <T> Observable$OnSubscribe<T> onSubscribeStart(Observable<? extends T> observable, Observable$OnSubscribe<T> onSubscribe) {
        return onSubscribe;
    }

    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        return subscription;
    }

    public <T> Throwable onSubscribeError(Throwable e) {
        return e;
    }

    public <T, R> Observable$Operator<? extends R, ? super T> onLift(Observable$Operator<? extends R, ? super T> lift) {
        return lift;
    }
}
