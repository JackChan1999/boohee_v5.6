package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Timestamped;

public class OperatorSkipLastTimed<T> implements Observable$Operator<T, T> {
    private final Scheduler scheduler;
    private final long timeInMillis;

    public OperatorSkipLastTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.timeInMillis = unit.toMillis(time);
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            private Deque<Timestamped<T>> buffer = new ArrayDeque();

            private void emitItemsOutOfWindow(long now) {
                long limit = now - OperatorSkipLastTimed.this.timeInMillis;
                while (!this.buffer.isEmpty()) {
                    Timestamped<T> v = (Timestamped) this.buffer.getFirst();
                    if (v.getTimestampMillis() < limit) {
                        this.buffer.removeFirst();
                        subscriber.onNext(v.getValue());
                    } else {
                        return;
                    }
                }
            }

            public void onNext(T value) {
                long now = OperatorSkipLastTimed.this.scheduler.now();
                emitItemsOutOfWindow(now);
                this.buffer.offerLast(new Timestamped(now, value));
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onCompleted() {
                emitItemsOutOfWindow(OperatorSkipLastTimed.this.scheduler.now());
                subscriber.onCompleted();
            }
        };
    }
}
