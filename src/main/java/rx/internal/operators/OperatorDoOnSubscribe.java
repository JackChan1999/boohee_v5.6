package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.Subscribers;

public class OperatorDoOnSubscribe<T> implements Observable$Operator<T, T> {
    private final Action0 subscribe;

    public OperatorDoOnSubscribe(Action0 subscribe) {
        this.subscribe = subscribe;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        this.subscribe.call();
        return Subscribers.wrap(child);
    }
}
