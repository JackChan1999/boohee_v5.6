package rx;

import rx.exceptions.OnErrorNotImplementedException;

class Observable$26 extends Subscriber<T> {
    final /* synthetic */ Observable this$0;

    Observable$26(Observable observable) {
        this.this$0 = observable;
    }

    public final void onCompleted() {
    }

    public final void onError(Throwable e) {
        throw new OnErrorNotImplementedException(e);
    }

    public final void onNext(T t) {
    }
}
