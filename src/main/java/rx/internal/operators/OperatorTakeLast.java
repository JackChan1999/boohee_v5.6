package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import rx.Observable$Operator;
import rx.Subscriber;

public final class OperatorTakeLast<T> implements Observable$Operator<T, T> {
    private final int count;

    public OperatorTakeLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count cannot be negative");
        }
        this.count = count;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final Deque<Object> deque = new ArrayDeque();
        final NotificationLite<T> notification = NotificationLite.instance();
        final TakeLastQueueProducer<T> producer = new TakeLastQueueProducer(notification, deque, subscriber);
        subscriber.setProducer(producer);
        final Subscriber<? super T> subscriber2 = subscriber;
        return new Subscriber<T>(subscriber) {
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                deque.offer(notification.completed());
                producer.startEmitting();
            }

            public void onError(Throwable e) {
                deque.clear();
                subscriber2.onError(e);
            }

            public void onNext(T value) {
                if (OperatorTakeLast.this.count != 0) {
                    if (deque.size() == OperatorTakeLast.this.count) {
                        deque.removeFirst();
                    }
                    deque.offerLast(notification.next(value));
                }
            }
        };
    }
}
