package rx;

import rx.functions.Func1;

class Observable$6 implements Func1<T, Boolean> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Object val$element;

    Observable$6(Observable observable, Object obj) {
        this.this$0 = observable;
        this.val$element = obj;
    }

    public final Boolean call(T t1) {
        boolean equals = this.val$element == null ? t1 == null : this.val$element.equals(t1);
        return Boolean.valueOf(equals);
    }
}
