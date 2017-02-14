package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;

public final class OperatorWithLatestFrom<T, U, R> implements Observable$Operator<R, T> {
    static final Object EMPTY = new Object();
    final Observable<? extends U> other;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public OperatorWithLatestFrom(Observable<? extends U> other, Func2<? super T, ? super U, ? extends R> resultSelector) {
        this.other = other;
        this.resultSelector = resultSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super R> child) {
        final SerializedSubscriber<R> s = new SerializedSubscriber(child, false);
        child.add(s);
        final AtomicReference<Object> current = new AtomicReference(EMPTY);
        final SerializedSubscriber<R> serializedSubscriber = s;
        Subscriber<T> subscriber = new Subscriber<T>(s, true) {
            public void onNext(T t) {
                U o = current.get();
                if (o != OperatorWithLatestFrom.EMPTY) {
                    try {
                        serializedSubscriber.onNext(OperatorWithLatestFrom.this.resultSelector.call(t, o));
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this);
                    }
                }
            }

            public void onError(Throwable e) {
                serializedSubscriber.onError(e);
                serializedSubscriber.unsubscribe();
            }

            public void onCompleted() {
                serializedSubscriber.onCompleted();
                serializedSubscriber.unsubscribe();
            }
        };
        Subscriber<U> otherSubscriber = new Subscriber<U>() {
            public void onNext(U t) {
                current.set(t);
            }

            public void onError(Throwable e) {
                s.onError(e);
                s.unsubscribe();
            }

            public void onCompleted() {
                if (current.get() == OperatorWithLatestFrom.EMPTY) {
                    s.onCompleted();
                    s.unsubscribe();
                }
            }
        };
        s.add(subscriber);
        s.add(otherSubscriber);
        this.other.unsafeSubscribe(otherSubscriber);
        return subscriber;
    }
}
