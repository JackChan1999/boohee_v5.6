package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable$OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;

public final class OnSubscribeTimerOnce implements Observable$OnSubscribe<Long> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OnSubscribeTimerOnce(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public void call(final Subscriber<? super Long> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        worker.schedule(new Action0() {
            public void call() {
                try {
                    child.onNext(Long.valueOf(0));
                    child.onCompleted();
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, child);
                }
            }
        }, this.time, this.unit);
    }
}
