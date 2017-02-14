package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorElementAt<T> implements Observable$Operator<T, T> {
    private final T defaultValue;
    private final boolean hasDefault;
    private final int index;

    static class InnerProducer extends AtomicBoolean implements Producer {
        private static final long serialVersionUID = 1;
        final Producer actual;

        public InnerProducer(Producer actual) {
            this.actual = actual;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (n > 0 && compareAndSet(false, true)) {
                this.actual.request(Long.MAX_VALUE);
            }
        }
    }

    public OperatorElementAt(int index) {
        this(index, null, false);
    }

    public OperatorElementAt(int index, T defaultValue) {
        this(index, defaultValue, true);
    }

    private OperatorElementAt(int index, T defaultValue, boolean hasDefault) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index + " is out of bounds");
        }
        this.index = index;
        this.defaultValue = defaultValue;
        this.hasDefault = hasDefault;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            private int currentIndex = 0;

            public void onNext(T value) {
                int i = this.currentIndex;
                this.currentIndex = i + 1;
                if (i == OperatorElementAt.this.index) {
                    child.onNext(value);
                    child.onCompleted();
                    unsubscribe();
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                if (this.currentIndex > OperatorElementAt.this.index) {
                    return;
                }
                if (OperatorElementAt.this.hasDefault) {
                    child.onNext(OperatorElementAt.this.defaultValue);
                    child.onCompleted();
                    return;
                }
                child.onError(new IndexOutOfBoundsException(OperatorElementAt.this.index + " is out of bounds"));
            }

            public void setProducer(Producer p) {
                child.setProducer(new InnerProducer(p));
            }
        };
        child.add(parent);
        return parent;
    }
}
