package rx;

import rx.exceptions.Exceptions;

class Observable$2 implements Observable$OnSubscribe<R> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Observable$Operator val$operator;

    Observable$2(Observable observable, Observable$Operator observable$Operator) {
        this.this$0 = observable;
        this.val$operator = observable$Operator;
    }

    public void call(Subscriber<? super R> o) {
        Subscriber<? super T> st;
        try {
            st = (Subscriber) Observable.access$100().onLift(this.val$operator).call(o);
            st.onStart();
            this.this$0.onSubscribe.call(st);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            o.onError(e);
        }
    }
}
