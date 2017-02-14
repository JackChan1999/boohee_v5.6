package rx;

import rx.internal.util.SubscriptionList;

public abstract class Subscriber<T> implements Observer<T>, Subscription {
    private static final Long NOT_SET = Long.valueOf(Long.MIN_VALUE);
    private Producer producer;
    private long requested;
    private final Subscriber<?> subscriber;
    private final SubscriptionList subscriptions;

    protected Subscriber() {
        this(null, false);
    }

    protected Subscriber(Subscriber<?> subscriber) {
        this(subscriber, true);
    }

    protected Subscriber(Subscriber<?> subscriber, boolean shareSubscriptions) {
        this.requested = NOT_SET.longValue();
        this.subscriber = subscriber;
        SubscriptionList subscriptionList = (!shareSubscriptions || subscriber == null) ? new SubscriptionList() : subscriber.subscriptions;
        this.subscriptions = subscriptionList;
    }

    public final void add(Subscription s) {
        this.subscriptions.add(s);
    }

    public final void unsubscribe() {
        this.subscriptions.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.subscriptions.isUnsubscribed();
    }

    public void onStart() {
    }

    protected final void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("number requested cannot be negative: " + n);
        }
        synchronized (this) {
            if (this.producer != null) {
                Producer producerToRequestFrom = this.producer;
                producerToRequestFrom.request(n);
                return;
            }
            addToRequested(n);
        }
    }

    private void addToRequested(long n) {
        if (this.requested == NOT_SET.longValue()) {
            this.requested = n;
            return;
        }
        long total = this.requested + n;
        if (total < 0) {
            this.requested = Long.MAX_VALUE;
        } else {
            this.requested = total;
        }
    }

    public void setProducer(Producer p) {
        boolean passToSubscriber = false;
        synchronized (this) {
            long toRequest = this.requested;
            this.producer = p;
            if (this.subscriber != null && toRequest == NOT_SET.longValue()) {
                passToSubscriber = true;
            }
        }
        if (passToSubscriber) {
            this.subscriber.setProducer(this.producer);
        } else if (toRequest == NOT_SET.longValue()) {
            this.producer.request(Long.MAX_VALUE);
        } else {
            this.producer.request(toRequest);
        }
    }
}
