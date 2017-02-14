package rx.internal.operators;

import java.util.NoSuchElementException;
import rx.Observable;
import rx.Single.OnSubscribe;
import rx.SingleSubscriber;
import rx.Subscriber;

public class OnSubscribeSingle<T> implements OnSubscribe<T> {
    private final Observable<T> observable;

    public OnSubscribeSingle(Observable<T> observable) {
        this.observable = observable;
    }

    public void call(final SingleSubscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            private T emission = null;
            private boolean emittedTooMany = false;
            private boolean itemEmitted = false;

            public void onStart() {
                request(2);
            }

            public void onCompleted() {
                if (!this.emittedTooMany) {
                    if (this.itemEmitted) {
                        child.onSuccess(this.emission);
                    } else {
                        child.onError(new NoSuchElementException("Observable emitted no items"));
                    }
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
                unsubscribe();
            }

            public void onNext(T t) {
                if (this.itemEmitted) {
                    this.emittedTooMany = true;
                    child.onError(new IllegalArgumentException("Observable emitted too many elements"));
                    unsubscribe();
                    return;
                }
                this.itemEmitted = true;
                this.emission = t;
            }
        };
        child.add(parent);
        this.observable.unsafeSubscribe(parent);
    }

    public static <T> OnSubscribeSingle<T> create(Observable<T> observable) {
        return new OnSubscribeSingle(observable);
    }
}
