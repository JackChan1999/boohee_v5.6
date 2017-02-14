package rx;

import rx.functions.Action1;

class Observable$11 implements Observer<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action1 val$onNext;

    Observable$11(Observable observable, Action1 action1) {
        this.this$0 = observable;
        this.val$onNext = action1;
    }

    public final void onCompleted() {
    }

    public final void onError(Throwable e) {
    }

    public final void onNext(T args) {
        this.val$onNext.call(args);
    }
}
