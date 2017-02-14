package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription s;

    public SynchronizedSubscription(Subscription s) {
        this.s = s;
    }

    public synchronized void unsubscribe() {
        this.s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.s.isUnsubscribed();
    }
}
