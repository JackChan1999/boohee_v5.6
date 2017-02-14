package rx;

class Observable$30 extends Subscriber<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Observer val$observer;

    Observable$30(Observable observable, Observer observer) {
        this.this$0 = observable;
        this.val$observer = observer;
    }

    public void onCompleted() {
        this.val$observer.onCompleted();
    }

    public void onError(Throwable e) {
        this.val$observer.onError(e);
    }

    public void onNext(T t) {
        this.val$observer.onNext(t);
    }
}
