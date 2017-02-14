package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.Subscribers;
import rx.subscriptions.Subscriptions;

public class OperatorDoOnUnsubscribe<T> implements Observable$Operator<T, T> {
    private final Action0 unsubscribe;

    public OperatorDoOnUnsubscribe(Action0 unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        child.add(Subscriptions.create(this.unsubscribe));
        return Subscribers.wrap(child);
    }
}
