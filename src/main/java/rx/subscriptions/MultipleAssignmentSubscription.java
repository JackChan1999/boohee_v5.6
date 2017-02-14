package rx.subscriptions;

import java.util.concurrent.atomic.AtomicReference;
import rx.Subscription;

public final class MultipleAssignmentSubscription implements Subscription {
    final AtomicReference<State> state = new AtomicReference(new State(false, Subscriptions.empty()));

    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean u, Subscription s) {
            this.isUnsubscribed = u;
            this.subscription = s;
        }

        State unsubscribe() {
            return new State(true, this.subscription);
        }

        State set(Subscription s) {
            return new State(this.isUnsubscribed, s);
        }
    }

    public boolean isUnsubscribed() {
        return ((State) this.state.get()).isUnsubscribed;
    }

    public void unsubscribe() {
        State oldState;
        AtomicReference<State> localState = this.state;
        do {
            oldState = (State) localState.get();
            if (oldState.isUnsubscribed) {
                return;
            }
        } while (!localState.compareAndSet(oldState, oldState.unsubscribe()));
        oldState.subscription.unsubscribe();
    }

    public void set(Subscription s) {
        if (s == null) {
            throw new IllegalArgumentException("Subscription can not be null");
        }
        AtomicReference<State> localState = this.state;
        State oldState;
        do {
            oldState = (State) localState.get();
            if (oldState.isUnsubscribed) {
                s.unsubscribe();
                return;
            }
        } while (!localState.compareAndSet(oldState, oldState.set(s)));
    }

    public Subscription get() {
        return ((State) this.state.get()).subscription;
    }
}
