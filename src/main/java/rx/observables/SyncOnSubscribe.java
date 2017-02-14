package rx.observables;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.internal.operators.BackpressureUtils;
import rx.plugins.RxJavaPlugins;

@Experimental
public abstract class SyncOnSubscribe<S, T> implements Observable$OnSubscribe<T> {

    private static class SubscriptionProducer<S, T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        private static final long serialVersionUID = -3736864024352728072L;
        private final Subscriber<? super T> actualSubscriber;
        private boolean hasTerminated;
        private boolean onNextCalled;
        private final SyncOnSubscribe<S, T> parent;
        private S state;

        private SubscriptionProducer(Subscriber<? super T> subscriber, SyncOnSubscribe<S, T> parent, S state) {
            this.actualSubscriber = subscriber;
            this.parent = parent;
            this.state = state;
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            long requestCount;
            do {
                requestCount = get();
                if (compareAndSet(0, -1)) {
                    doUnsubscribe();
                    return;
                }
            } while (!compareAndSet(requestCount, -2));
        }

        private boolean tryUnsubscribe() {
            if (!this.hasTerminated && get() >= -1) {
                return false;
            }
            set(-1);
            doUnsubscribe();
            return true;
        }

        private void doUnsubscribe() {
            this.parent.onUnsubscribe(this.state);
        }

        public void request(long n) {
            if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                if (n == Long.MAX_VALUE) {
                    fastpath();
                } else {
                    slowPath(n);
                }
            }
        }

        private void fastpath() {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            do {
                try {
                    this.onNextCalled = false;
                    nextIteration(p);
                } catch (Throwable ex) {
                    handleThrownError(a, ex);
                    return;
                }
            } while (!tryUnsubscribe());
        }

        private void handleThrownError(Subscriber<? super T> a, Throwable ex) {
            if (this.hasTerminated) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(ex);
                return;
            }
            this.hasTerminated = true;
            a.onError(ex);
            unsubscribe();
        }

        private void slowPath(long n) {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            long numRequested = n;
            do {
                long numRemaining = numRequested;
                do {
                    try {
                        this.onNextCalled = false;
                        nextIteration(p);
                        if (!tryUnsubscribe()) {
                            if (this.onNextCalled) {
                                numRemaining--;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        handleThrownError(a, ex);
                        return;
                    }
                } while (numRemaining != 0);
                numRequested = addAndGet(-numRequested);
            } while (numRequested > 0);
            tryUnsubscribe();
        }

        private void nextIteration(SyncOnSubscribe<S, T> parent) {
            this.state = parent.next(this.state, this);
        }

        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onError(e);
            }
        }

        public void onNext(T value) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            this.actualSubscriber.onNext(value);
        }
    }

    private static final class SyncOnSubscribeImpl<S, T> extends SyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func2<? super S, ? super Observer<? super T>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            super.call((Subscriber) x0);
        }

        private SyncOnSubscribeImpl(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
            this.generator = generator;
            this.next = next;
            this.onUnsubscribe = onUnsubscribe;
        }

        public SyncOnSubscribeImpl(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
            this(generator, next, null);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> next, Action1<? super S> onUnsubscribe) {
            this(null, next, onUnsubscribe);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> nextFunc) {
            this(null, nextFunc, null);
        }

        protected S generateState() {
            return this.generator == null ? null : this.generator.call();
        }

        protected S next(S state, Observer<? super T> observer) {
            return this.next.call(state, observer);
        }

        protected void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    protected abstract S generateState();

    protected abstract S next(S s, Observer<? super T> observer);

    public final void call(Subscriber<? super T> subscriber) {
        SubscriptionProducer<S, T> p = new SubscriptionProducer(subscriber, this, generateState());
        subscriber.add(p);
        subscriber.setProducer(p);
    }

    protected void onUnsubscribe(S s) {
    }

    @Experimental
    public static <S, T> Observable$OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl((Func0) generator, new Func2<S, Observer<? super T>, S>() {
            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        });
    }

    @Experimental
    public static <S, T> Observable$OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, new Func2<S, Observer<? super T>, S>() {
            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        }, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable$OnSubscribe<T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable$OnSubscribe<T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
        return new SyncOnSubscribeImpl((Func0) generator, (Func2) next);
    }

    @Experimental
    public static <T> Observable$OnSubscribe<T> createStateless(final Action1<? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return state;
            }
        });
    }

    @Experimental
    public static <T> Observable$OnSubscribe<T> createStateless(final Action1<? super Observer<? super T>> next, final Action0 onUnsubscribe) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return null;
            }
        }, new Action1<Void>() {
            public void call(Void t) {
                onUnsubscribe.call();
            }
        });
    }
}
