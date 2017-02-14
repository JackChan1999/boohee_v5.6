package rx.subscriptions;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import rx.Subscription;

public final class RefCountSubscription implements Subscription {
    static final State EMPTY_STATE = new State(false, 0);
    private final Subscription actual;
    final AtomicReference<State> state = new AtomicReference(EMPTY_STATE);

    private static final class InnerSubscription extends AtomicInteger implements Subscription {
        final RefCountSubscription parent;

        public InnerSubscription(RefCountSubscription parent) {
            this.parent = parent;
        }

        public void unsubscribe() {
            if (compareAndSet(0, 1)) {
                this.parent.unsubscribeAChild();
            }
        }

        public boolean isUnsubscribed() {
            return get() != 0;
        }
    }

    private static final class State {
        final int children;
        final boolean isUnsubscribed;

        State(boolean u, int c) {
            this.isUnsubscribed = u;
            this.children = c;
        }

        State addChild() {
            return new State(this.isUnsubscribed, this.children + 1);
        }

        State removeChild() {
            return new State(this.isUnsubscribed, this.children - 1);
        }

        State unsubscribe() {
            return new State(true, this.children);
        }
    }

    public RefCountSubscription(Subscription s) {
        if (s == null) {
            throw new IllegalArgumentException("s");
        }
        this.actual = s;
    }

    public Subscription get() {
        AtomicReference<State> localState = this.state;
        State oldState;
        do {
            oldState = (State) localState.get();
            if (oldState.isUnsubscribed) {
                return Subscriptions.unsubscribed();
            }
        } while (!localState.compareAndSet(oldState, oldState.addChild()));
        return new InnerSubscription(this);
    }

    public boolean isUnsubscribed() {
        return ((State) this.state.get()).isUnsubscribed;
    }

    public void unsubscribe() {
        State newState;
        AtomicReference<State> localState = this.state;
        State oldState;
        do {
            oldState = (State) localState.get();
            if (!oldState.isUnsubscribed) {
                newState = oldState.unsubscribe();
            } else {
                return;
            }
        } while (!localState.compareAndSet(oldState, newState));
        unsubscribeActualIfApplicable(newState);
    }

    private void unsubscribeActualIfApplicable(State state) {
        if (state.isUnsubscribed && state.children == 0) {
            this.actual.unsubscribe();
        }
    }

    void unsubscribeAChild() {
        State newState;
        AtomicReference<State> localState = this.state;
        State oldState;
        do {
            oldState = (State) localState.get();
            newState = oldState.removeChild();
        } while (!localState.compareAndSet(oldState, newState));
        unsubscribeActualIfApplicable(newState);
    }
}
