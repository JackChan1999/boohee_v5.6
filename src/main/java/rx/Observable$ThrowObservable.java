package rx;

class Observable$ThrowObservable<T> extends Observable<T> {

    class AnonymousClass1 implements Observable$OnSubscribe<T> {
        final /* synthetic */ Throwable val$exception;

        AnonymousClass1(Throwable th) {
            this.val$exception = th;
        }

        public void call(Subscriber<? super T> observer) {
            observer.onError(this.val$exception);
        }
    }

    public Observable$ThrowObservable(Throwable exception) {
        super(new AnonymousClass1(exception));
    }
}
