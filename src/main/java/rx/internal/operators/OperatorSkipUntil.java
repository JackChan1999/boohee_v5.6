package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSkipUntil<T, U> implements Observable$Operator<T, T> {
    final Observable<U> other;

    public OperatorSkipUntil(Observable<U> other) {
        this.other = other;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber(child);
        final AtomicBoolean gate = new AtomicBoolean();
        Subscriber<U> u = new Subscriber<U>() {
            public void onNext(U u) {
                gate.set(true);
                unsubscribe();
            }

            public void onError(Throwable e) {
                s.onError(e);
                s.unsubscribe();
            }

            public void onCompleted() {
                unsubscribe();
            }
        };
        child.add(u);
        this.other.unsafeSubscribe(u);
        return new Subscriber<T>(child) {
            public void onNext(T t) {
                if (gate.get()) {
                    s.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
            }

            public void onCompleted() {
                s.onCompleted();
                unsubscribe();
            }
        };
    }
}
