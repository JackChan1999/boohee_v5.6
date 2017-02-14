package rx.observers;

import java.util.Arrays;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorFailedException;
import rx.exceptions.UnsubscribeFailedException;
import rx.internal.util.RxJavaPluginUtils;

public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done = false;

    public SafeSubscriber(Subscriber<? super T> actual) {
        super(actual);
        this.actual = actual;
    }

    public void onCompleted() {
        UnsubscribeFailedException unsubscribeFailedException;
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
                try {
                    unsubscribe();
                } catch (Throwable e) {
                    RxJavaPluginUtils.handleException(e);
                    unsubscribeFailedException = new UnsubscribeFailedException(e.getMessage(), e);
                }
            } catch (Throwable th) {
                try {
                    unsubscribe();
                } catch (Throwable e2) {
                    RxJavaPluginUtils.handleException(e2);
                    unsubscribeFailedException = new UnsubscribeFailedException(e2.getMessage(), e2);
                }
            }
        }
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.done) {
            this.done = true;
            _onError(e);
        }
    }

    public void onNext(T args) {
        try {
            if (!this.done) {
                this.actual.onNext(args);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            onError(e);
        }
    }

    protected void _onError(Throwable e) {
        OnErrorFailedException onErrorFailedException;
        RxJavaPluginUtils.handleException(e);
        try {
            this.actual.onError(e);
            try {
                unsubscribe();
            } catch (RuntimeException unsubscribeException) {
                RxJavaPluginUtils.handleException(unsubscribeException);
                throw new OnErrorFailedException(unsubscribeException);
            }
        } catch (Throwable unsubscribeException2) {
            RxJavaPluginUtils.handleException(unsubscribeException2);
            onErrorFailedException = new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError and during unsubscription.", new CompositeException(Arrays.asList(new Throwable[]{e, e2, unsubscribeException2})));
        }
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
