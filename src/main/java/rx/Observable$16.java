package rx;

import rx.functions.Func0;
import rx.observables.ConnectableObservable;

class Observable$16 implements Func0<ConnectableObservable<T>> {
    final /* synthetic */ Observable this$0;

    Observable$16(Observable observable) {
        this.this$0 = observable;
    }

    public ConnectableObservable<T> call() {
        return this.this$0.replay();
    }
}
