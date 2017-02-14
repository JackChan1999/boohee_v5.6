package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.Subscribers;

public final class OnSubscribeUsing<T, Resource> implements Observable$OnSubscribe<T> {
    private final Action1<? super Resource> dispose;
    private final boolean disposeEagerly;
    private final Func1<? super Resource, ? extends Observable<? extends T>> observableFactory;
    private final Func0<Resource> resourceFactory;

    private static final class DisposeAction<Resource> extends AtomicBoolean implements Action0, Subscription {
        private static final long serialVersionUID = 4262875056400218316L;
        private Action1<? super Resource> dispose;
        private Resource resource;

        private DisposeAction(Action1<? super Resource> dispose, Resource resource) {
            this.dispose = dispose;
            this.resource = resource;
            lazySet(false);
        }

        public void call() {
            if (compareAndSet(false, true)) {
                try {
                    this.dispose.call(this.resource);
                } finally {
                    this.resource = null;
                    this.dispose = null;
                }
            }
        }

        public boolean isUnsubscribed() {
            return get();
        }

        public void unsubscribe() {
            call();
        }
    }

    public OnSubscribeUsing(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> dispose, boolean disposeEagerly) {
        this.resourceFactory = resourceFactory;
        this.observableFactory = observableFactory;
        this.dispose = dispose;
        this.disposeEagerly = disposeEagerly;
    }

    public void call(Subscriber<? super T> subscriber) {
        DisposeAction<Resource> disposeOnceOnly;
        try {
            Observable<? extends T> observable;
            Resource resource = this.resourceFactory.call();
            disposeOnceOnly = new DisposeAction(this.dispose, resource);
            subscriber.add(disposeOnceOnly);
            Observable<? extends T> source = (Observable) this.observableFactory.call(resource);
            if (this.disposeEagerly) {
                observable = source.doOnTerminate(disposeOnceOnly);
            } else {
                observable = source;
            }
            observable.unsafeSubscribe(Subscribers.wrap(subscriber));
        } catch (Throwable e) {
            Exceptions.throwOrReport(e, subscriber);
        }
    }

    private Throwable disposeEagerlyIfRequested(Action0 disposeOnceOnly) {
        Throwable th = null;
        if (this.disposeEagerly) {
            try {
                disposeOnceOnly.call();
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return th;
    }
}
