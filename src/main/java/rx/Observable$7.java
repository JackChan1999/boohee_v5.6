package rx;

import rx.internal.producers.SingleProducer;

class Observable$7 implements Observable$OnSubscribe<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Object val$defaultValue;

    Observable$7(Observable observable, Object obj) {
        this.this$0 = observable;
        this.val$defaultValue = obj;
    }

    public void call(Subscriber<? super T> subscriber) {
        subscriber.setProducer(new SingleProducer(subscriber, this.val$defaultValue));
    }
}
