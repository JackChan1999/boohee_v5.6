package rx;

import rx.functions.Action1;

class Observable$10 implements Observer<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action1 val$onError;

    Observable$10(Observable observable, Action1 action1) {
        this.this$0 = observable;
        this.val$onError = action1;
    }

    public final void onCompleted() {
    }

    public final void onError(Throwable e) {
        this.val$onError.call(e);
    }

    public final void onNext(T t) {
    }
}
