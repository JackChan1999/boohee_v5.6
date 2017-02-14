package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import rx.Observable$Operator;
import rx.Subscriber;

public class OperatorSkipLast<T> implements Observable$Operator<T, T> {
    private final int count;

    public OperatorSkipLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.count = count;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            private final Deque<Object> deque = new ArrayDeque();
            private final NotificationLite<T> on = NotificationLite.instance();

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(T value) {
                if (OperatorSkipLast.this.count == 0) {
                    subscriber.onNext(value);
                    return;
                }
                if (this.deque.size() == OperatorSkipLast.this.count) {
                    subscriber.onNext(this.on.getValue(this.deque.removeFirst()));
                } else {
                    request(1);
                }
                this.deque.offerLast(this.on.next(value));
            }
        };
    }
}
