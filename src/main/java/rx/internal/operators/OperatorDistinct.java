package rx.internal.operators;

import java.util.HashSet;
import java.util.Set;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

public final class OperatorDistinct<T, U> implements Observable$Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    private static class Holder {
        static final OperatorDistinct<?, ?> INSTANCE = new OperatorDistinct(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinct<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinct(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            Set<U> keyMemory = new HashSet();

            public void onNext(T t) {
                if (this.keyMemory.add(OperatorDistinct.this.keySelector.call(t))) {
                    child.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                this.keyMemory = null;
                child.onError(e);
            }

            public void onCompleted() {
                this.keyMemory = null;
                child.onCompleted();
            }
        };
    }
}
