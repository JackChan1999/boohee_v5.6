package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;

public final class OperatorAsObservable<T> implements Observable$Operator<T, T> {

    private static final class Holder {
        static final OperatorAsObservable<Object> INSTANCE = new OperatorAsObservable();

        private Holder() {
        }
    }

    public static <T> OperatorAsObservable<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorAsObservable() {
    }

    public Subscriber<? super T> call(Subscriber<? super T> s) {
        return s;
    }
}
