package rx;

class Observable$1 implements Observable$OnSubscribe<T> {
    final /* synthetic */ Observable this$0;

    Observable$1(Observable observable) {
        this.this$0 = observable;
    }

    public void call(Subscriber<? super T> subscriber) {
        subscriber.add(Observable.access$000(subscriber, this.this$0));
    }
}
