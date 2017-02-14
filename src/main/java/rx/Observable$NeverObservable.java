package rx;

class Observable$NeverObservable<T> extends Observable<T> {

    private static class Holder {
        static final Observable$NeverObservable<?> INSTANCE = new Observable$NeverObservable();

        private Holder() {
        }
    }

    static <T> Observable$NeverObservable<T> instance() {
        return Holder.INSTANCE;
    }

    Observable$NeverObservable() {
        super(new Observable$OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
            }
        });
    }
}
