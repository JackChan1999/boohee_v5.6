package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class OperatorCast<T, R> implements Observable$Operator<R, T> {
    private final Class<R> castClass;

    public OperatorCast(Class<R> castClass) {
        this.castClass = castClass;
    }

    public Subscriber<? super T> call(final Subscriber<? super R> o) {
        return new Subscriber<T>(o) {
            public void onCompleted() {
                o.onCompleted();
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(T t) {
                try {
                    o.onNext(OperatorCast.this.castClass.cast(t));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this, t);
                }
            }
        };
    }
}
