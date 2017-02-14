package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorFinally<T> implements Observable$Operator<T, T> {
    final Action0 action;

    public OperatorFinally(Action0 action) {
        if (action == null) {
            throw new NullPointerException("Action can not be null");
        }
        this.action = action;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            public void onNext(T t) {
                child.onNext(t);
            }

            public void onError(Throwable e) {
                try {
                    child.onError(e);
                } finally {
                    OperatorFinally.this.action.call();
                }
            }

            public void onCompleted() {
                try {
                    child.onCompleted();
                } finally {
                    OperatorFinally.this.action.call();
                }
            }
        };
    }
}
