package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorSkipWhile<T> implements Observable$Operator<T, T> {
    private final Func2<? super T, Integer, Boolean> predicate;

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            int index;
            boolean skipping = true;

            public void onNext(T t) {
                if (this.skipping) {
                    Func2 access$000 = OperatorSkipWhile.this.predicate;
                    int i = this.index;
                    this.index = i + 1;
                    if (((Boolean) access$000.call(t, Integer.valueOf(i))).booleanValue()) {
                        request(1);
                        return;
                    }
                    this.skipping = false;
                    child.onNext(t);
                    return;
                }
                child.onNext(t);
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                child.onCompleted();
            }
        };
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(final Func1<? super T, Boolean> predicate) {
        return new Func2<T, Integer, Boolean>() {
            public Boolean call(T t1, Integer t2) {
                return (Boolean) predicate.call(t1);
            }
        };
    }
}
