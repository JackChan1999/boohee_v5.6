package rx;

import rx.functions.Func1;

class Observable$20 implements Func1<Observable<T>, Observable<R>> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Scheduler val$scheduler;
    final /* synthetic */ Func1 val$selector;

    Observable$20(Observable observable, Func1 func1, Scheduler scheduler) {
        this.this$0 = observable;
        this.val$selector = func1;
        this.val$scheduler = scheduler;
    }

    public Observable<R> call(Observable<T> t) {
        return ((Observable) this.val$selector.call(t)).observeOn(this.val$scheduler);
    }
}
