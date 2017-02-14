package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Subscriber;

public final class OperatorTakeLastTimed<T> implements Observable$Operator<T, T> {
    private final long ageMillis;
    private final int count;
    private final Scheduler scheduler;

    public OperatorTakeLastTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int count, long time, TimeUnit unit, Scheduler scheduler) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = count;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final Deque<Object> buffer = new ArrayDeque();
        final Deque<Long> timestampBuffer = new ArrayDeque();
        final NotificationLite<T> notification = NotificationLite.instance();
        final TakeLastQueueProducer<T> producer = new TakeLastQueueProducer(notification, buffer, subscriber);
        subscriber.setProducer(producer);
        final Subscriber<? super T> subscriber2 = subscriber;
        return new Subscriber<T>(subscriber) {
            protected void runEvictionPolicy(long now) {
                while (OperatorTakeLastTimed.this.count >= 0 && buffer.size() > OperatorTakeLastTimed.this.count) {
                    timestampBuffer.pollFirst();
                    buffer.pollFirst();
                }
                while (!buffer.isEmpty() && ((Long) timestampBuffer.peekFirst()).longValue() < now - OperatorTakeLastTimed.this.ageMillis) {
                    timestampBuffer.pollFirst();
                    buffer.pollFirst();
                }
            }

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T args) {
                long t = OperatorTakeLastTimed.this.scheduler.now();
                timestampBuffer.add(Long.valueOf(t));
                buffer.add(notification.next(args));
                runEvictionPolicy(t);
            }

            public void onError(Throwable e) {
                timestampBuffer.clear();
                buffer.clear();
                subscriber2.onError(e);
            }

            public void onCompleted() {
                runEvictionPolicy(OperatorTakeLastTimed.this.scheduler.now());
                timestampBuffer.clear();
                buffer.offer(notification.completed());
                producer.startEmitting();
            }
        };
    }
}
