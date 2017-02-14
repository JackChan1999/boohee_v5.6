package rx;

import rx.annotations.Beta;
import rx.internal.util.SubscriptionList;

@Beta
public abstract class SingleSubscriber<T> implements Subscription {
    private final SubscriptionList cs = new SubscriptionList();

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public final void add(Subscription s) {
        this.cs.add(s);
    }

    public final void unsubscribe() {
        this.cs.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.cs.isUnsubscribed();
    }
}
