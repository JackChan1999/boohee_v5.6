package rx;

final class Observable$EmptyHolder {
    static final Observable<Object> INSTANCE = Observable.create(new Observable$OnSubscribe<Object>() {
        public void call(Subscriber<? super Object> subscriber) {
            subscriber.onCompleted();
        }
    });

    private Observable$EmptyHolder() {
    }
}
