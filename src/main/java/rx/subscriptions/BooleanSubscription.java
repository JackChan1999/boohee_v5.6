package rx.subscriptions;

import java.util.concurrent.atomic.AtomicReference;
import rx.Subscription;
import rx.functions.Action0;

public final class BooleanSubscription implements Subscription {
    static final Action0 EMPTY_ACTION = new Action0() {
        public void call() {
        }
    };
    final AtomicReference<Action0> actionRef;

    public BooleanSubscription() {
        this.actionRef = new AtomicReference();
    }

    private BooleanSubscription(Action0 action) {
        this.actionRef = new AtomicReference(action);
    }

    public static BooleanSubscription create() {
        return new BooleanSubscription();
    }

    public static BooleanSubscription create(Action0 onUnsubscribe) {
        return new BooleanSubscription(onUnsubscribe);
    }

    public boolean isUnsubscribed() {
        return this.actionRef.get() == EMPTY_ACTION;
    }

    public final void unsubscribe() {
        if (((Action0) this.actionRef.get()) != EMPTY_ACTION) {
            Action0 action = (Action0) this.actionRef.getAndSet(EMPTY_ACTION);
            if (action != null && action != EMPTY_ACTION) {
                action.call();
            }
        }
    }
}
