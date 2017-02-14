package rx;

import rx.functions.Func1;

class Observable$13 implements Func1<T, Boolean> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Class val$klass;

    Observable$13(Observable observable, Class cls) {
        this.this$0 = observable;
        this.val$klass = cls;
    }

    public final Boolean call(T t) {
        return Boolean.valueOf(this.val$klass.isInstance(t));
    }
}
