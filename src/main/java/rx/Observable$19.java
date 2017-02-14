package rx;

import rx.functions.Func0;
import rx.observables.ConnectableObservable;

class Observable$19 implements Func0<ConnectableObservable<T>> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ int val$bufferSize;

    Observable$19(Observable observable, int i) {
        this.this$0 = observable;
        this.val$bufferSize = i;
    }

    public ConnectableObservable<T> call() {
        return this.this$0.replay(this.val$bufferSize);
    }
}
