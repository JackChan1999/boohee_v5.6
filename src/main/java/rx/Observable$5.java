package rx;

import rx.functions.Action2;
import rx.functions.Func2;

class Observable$5 implements Func2<R, T, R> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action2 val$collector;

    Observable$5(Observable observable, Action2 action2) {
        this.this$0 = observable;
        this.val$collector = action2;
    }

    public final R call(R state, T value) {
        this.val$collector.call(state, value);
        return state;
    }
}
