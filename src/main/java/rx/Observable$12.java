package rx;

import rx.functions.Action0;

class Observable$12 implements Observer<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action0 val$onTerminate;

    Observable$12(Observable observable, Action0 action0) {
        this.this$0 = observable;
        this.val$onTerminate = action0;
    }

    public final void onCompleted() {
        this.val$onTerminate.call();
    }

    public final void onError(Throwable e) {
        this.val$onTerminate.call();
    }

    public final void onNext(T t) {
    }
}
