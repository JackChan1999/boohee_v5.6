package rx.internal.operators;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.util.UtilityFunctions;

public final class OperatorSequenceEqual {
    private static final Object LOCAL_ONCOMPLETED = new Object();

    private OperatorSequenceEqual() {
        throw new IllegalStateException("No instances!");
    }

    static <T> Observable<Object> materializeLite(Observable<T> source) {
        return Observable.concat(source.map(new Func1<T, Object>() {
            public Object call(T t1) {
                return t1;
            }
        }), Observable.just(LOCAL_ONCOMPLETED));
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, final Func2<? super T, ? super T, Boolean> equality) {
        return Observable.zip(materializeLite(first), materializeLite(second), new Func2<Object, Object, Boolean>() {
            public Boolean call(Object t1, Object t2) {
                boolean c1;
                if (t1 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                    c1 = true;
                } else {
                    c1 = false;
                }
                boolean c2;
                if (t2 == OperatorSequenceEqual.LOCAL_ONCOMPLETED) {
                    c2 = true;
                } else {
                    c2 = false;
                }
                if (c1 && c2) {
                    return Boolean.valueOf(true);
                }
                if (c1 || c2) {
                    return Boolean.valueOf(false);
                }
                return (Boolean) equality.call(t1, t2);
            }
        }).all(UtilityFunctions.identity());
    }
}
