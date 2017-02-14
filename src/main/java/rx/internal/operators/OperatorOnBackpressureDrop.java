package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;

public class OperatorOnBackpressureDrop<T> implements Observable$Operator<T, T> {
    private final Action1<? super T> onDrop;

    private static final class Holder {
        static final OperatorOnBackpressureDrop<Object> INSTANCE = new OperatorOnBackpressureDrop();

        private Holder() {
        }
    }

    public static <T> OperatorOnBackpressureDrop<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorOnBackpressureDrop() {
        this(null);
    }

    public OperatorOnBackpressureDrop(Action1<? super T> onDrop) {
        this.onDrop = onDrop;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final AtomicLong requested = new AtomicLong();
        child.setProducer(new Producer() {
            public void request(long n) {
                BackpressureUtils.getAndAddRequest(requested, n);
            }
        });
        return new Subscriber<T>(child) {
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                child.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T t) {
                if (requested.get() > 0) {
                    child.onNext(t);
                    requested.decrementAndGet();
                } else if (OperatorOnBackpressureDrop.this.onDrop != null) {
                    OperatorOnBackpressureDrop.this.onDrop.call(t);
                }
            }
        };
    }
}
