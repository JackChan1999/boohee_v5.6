package rx.observers;

import rx.Observer;
import rx.Subscriber;

public class SerializedSubscriber<T> extends Subscriber<T> {
    private final Observer<T> s;

    public SerializedSubscriber(Subscriber<? super T> s) {
        this(s, true);
    }

    public SerializedSubscriber(Subscriber<? super T> s, boolean shareSubscriptions) {
        super(s, shareSubscriptions);
        this.s = new SerializedObserver(s);
    }

    public void onCompleted() {
        this.s.onCompleted();
    }

    public void onError(Throwable e) {
        this.s.onError(e);
    }

    public void onNext(T t) {
        this.s.onNext(t);
    }
}
