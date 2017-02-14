package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

public final class OperatorDistinctUntilChanged<T, U> implements Observable$Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    private static class Holder {
        static final OperatorDistinctUntilChanged<?, ?> INSTANCE = new OperatorDistinctUntilChanged(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinctUntilChanged<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            boolean hasPrevious;
            U previousKey;

            public void onNext(T t) {
                U currentKey = this.previousKey;
                U key = OperatorDistinctUntilChanged.this.keySelector.call(t);
                this.previousKey = key;
                if (!this.hasPrevious) {
                    this.hasPrevious = true;
                    child.onNext(t);
                } else if (currentKey == key || (key != null && key.equals(currentKey))) {
                    request(1);
                } else {
                    child.onNext(t);
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                child.onCompleted();
            }
        };
    }
}
