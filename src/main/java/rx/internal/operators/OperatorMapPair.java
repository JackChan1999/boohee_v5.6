package rx.internal.operators;

import rx.Observable;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorMapPair<T, U, R> implements Observable$Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> selector) {
        return new Func1<T, Observable<U>>() {
            public Observable<U> call(T t1) {
                return Observable.from((Iterable) selector.call(t1));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        this.collectionSelector = collectionSelector;
        this.resultSelector = resultSelector;
    }

    public Subscriber<? super T> call(final Subscriber<? super Observable<? extends R>> o) {
        return new Subscriber<T>(o) {
            public void onCompleted() {
                o.onCompleted();
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(final T outer) {
                try {
                    o.onNext(((Observable) OperatorMapPair.this.collectionSelector.call(outer)).map(new Func1<U, R>() {
                        public R call(U inner) {
                            return OperatorMapPair.this.resultSelector.call(outer, inner);
                        }
                    }));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, o, outer);
                }
            }
        };
    }
}
