package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSampleWithObservable<T, U> implements Observable$Operator<T, T> {
    static final Object EMPTY_TOKEN = new Object();
    final Observable<U> sampler;

    public OperatorSampleWithObservable(Observable<U> sampler) {
        this.sampler = sampler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber(child);
        final AtomicReference<Object> value = new AtomicReference(EMPTY_TOKEN);
        Subscriber<U> samplerSub = new Subscriber<U>(child) {
            public void onNext(U u) {
                T localValue = value.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
                if (localValue != OperatorSampleWithObservable.EMPTY_TOKEN) {
                    s.onNext(localValue);
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
        Subscriber<T> result = new Subscriber<T>(child) {
            public void onNext(T t) {
                value.set(t);
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
        this.sampler.unsafeSubscribe(samplerSub);
        return result;
    }
}
