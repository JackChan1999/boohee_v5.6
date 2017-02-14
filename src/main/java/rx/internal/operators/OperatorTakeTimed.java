package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

public final class OperatorTakeTimed<T> implements Observable$Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    static final class TakeSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;

        public TakeSubscriber(Subscriber<? super T> child) {
            super(child);
            this.child = child;
        }

        public void onNext(T t) {
            this.child.onNext(t);
        }

        public void onError(Throwable e) {
            this.child.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.child.onCompleted();
            unsubscribe();
        }

        public void call() {
            onCompleted();
        }
    }

    public OperatorTakeTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        TakeSubscriber<T> ts = new TakeSubscriber(new SerializedSubscriber(child));
        worker.schedule(ts, this.time, this.unit);
        return ts;
    }
}
