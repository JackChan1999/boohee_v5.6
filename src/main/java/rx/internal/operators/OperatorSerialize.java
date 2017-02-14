package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSerialize<T> implements Observable$Operator<T, T> {

    private static final class Holder {
        static final OperatorSerialize<Object> INSTANCE = new OperatorSerialize();

        private Holder() {
        }
    }

    public static <T> OperatorSerialize<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSerialize() {
    }

    public Subscriber<? super T> call(final Subscriber<? super T> s) {
        return new SerializedSubscriber(new Subscriber<T>(s) {
            public void onCompleted() {
                s.onCompleted();
            }

            public void onError(Throwable e) {
                s.onError(e);
            }

            public void onNext(T t) {
                s.onNext(t);
            }
        });
    }
}
