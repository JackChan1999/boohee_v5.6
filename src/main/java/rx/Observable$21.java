package rx;

import java.util.concurrent.TimeUnit;
import rx.functions.Func0;
import rx.observables.ConnectableObservable;

class Observable$21 implements Func0<ConnectableObservable<T>> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Scheduler val$scheduler;
    final /* synthetic */ long val$time;
    final /* synthetic */ TimeUnit val$unit;

    Observable$21(Observable observable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.this$0 = observable;
        this.val$time = j;
        this.val$unit = timeUnit;
        this.val$scheduler = scheduler;
    }

    public ConnectableObservable<T> call() {
        return this.this$0.replay(this.val$time, this.val$unit, this.val$scheduler);
    }
}
