package rx.internal.operators;

import rx.Observable$Operator;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.TimeInterval;

public final class OperatorTimeInterval<T> implements Observable$Operator<TimeInterval<T>, T> {
    private final Scheduler scheduler;

    public OperatorTimeInterval(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(final Subscriber<? super TimeInterval<T>> subscriber) {
        return new Subscriber<T>(subscriber) {
            private long lastTimestamp = OperatorTimeInterval.this.scheduler.now();

            public void onNext(T args) {
                long nowTimestamp = OperatorTimeInterval.this.scheduler.now();
                subscriber.onNext(new TimeInterval(nowTimestamp - this.lastTimestamp, args));
                this.lastTimestamp = nowTimestamp;
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        };
    }
}
