package rx.internal.operators;

import rx.Observable;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {

    class AnonymousClass1 implements FirstTimeoutStub<T> {
        final /* synthetic */ Func0 val$firstTimeoutSelector;

        AnonymousClass1(Func0 func0) {
            this.val$firstTimeoutSelector = func0;
        }

        public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Worker inner) {
            if (this.val$firstTimeoutSelector == null) {
                return Subscriptions.unsubscribed();
            }
            try {
                return ((Observable) this.val$firstTimeoutSelector.call()).unsafeSubscribe(new Subscriber<U>() {
                    public void onCompleted() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }

                    public void onError(Throwable e) {
                        timeoutSubscriber.onError(e);
                    }

                    public void onNext(U u) {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                });
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, timeoutSubscriber);
                return Subscriptions.unsubscribed();
            }
        }
    }

    class AnonymousClass2 implements TimeoutStub<T> {
        final /* synthetic */ Func1 val$timeoutSelector;

        AnonymousClass2(Func1 func1) {
            this.val$timeoutSelector = func1;
        }

        public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T value, Worker inner) {
            try {
                return ((Observable) this.val$timeoutSelector.call(value)).unsafeSubscribe(new Subscriber<V>() {
                    public void onCompleted() {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }

                    public void onError(Throwable e) {
                        timeoutSubscriber.onError(e);
                    }

                    public void onNext(V v) {
                        timeoutSubscriber.onTimeout(seqId.longValue());
                    }
                });
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, timeoutSubscriber);
                return Subscriptions.unsubscribed();
            }
        }
    }

    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeoutWithSelector(Func0<? extends Observable<U>> firstTimeoutSelector, Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        super(new AnonymousClass1(firstTimeoutSelector), new AnonymousClass2(timeoutSelector), other, Schedulers.immediate());
    }
}
