package rx;

import rx.functions.Action0;

class Observable$8 implements Observer<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action0 val$onCompleted;

    Observable$8(Observable observable, Action0 action0) {
        this.this$0 = observable;
        this.val$onCompleted = action0;
    }

    public final void onCompleted() {
        this.val$onCompleted.call();
    }

    public final void onError(Throwable e) {
    }

    public final void onNext(T t) {
    }
}
