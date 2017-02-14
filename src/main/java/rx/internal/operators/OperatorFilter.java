package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public final class OperatorFilter<T> implements Observable$Operator<T, T> {
    private final Func1<? super T, Boolean> predicate;

    public OperatorFilter(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            public void onCompleted() {
                child.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T t) {
                try {
                    if (((Boolean) OperatorFilter.this.predicate.call(t)).booleanValue()) {
                        child.onNext(t);
                    } else {
                        request(1);
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, child, t);
                }
            }
        };
    }
}
