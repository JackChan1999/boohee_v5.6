package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;

public final class OperatorSkipTimed<T> implements Observable$Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OperatorSkipTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        final AtomicBoolean gate = new AtomicBoolean();
        worker.schedule(new Action0() {
            public void call() {
                gate.set(true);
            }
        }, this.time, this.unit);
        return new Subscriber<T>(child) {
            public void onNext(T t) {
                if (gate.get()) {
                    child.onNext(t);
                }
            }

            public void onError(Throwable e) {
                try {
                    child.onError(e);
                } finally {
                    unsubscribe();
                }
            }

            public void onCompleted() {
                try {
                    child.onCompleted();
                } finally {
                    unsubscribe();
                }
            }
        };
    }
}
