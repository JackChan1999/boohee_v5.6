package rx.observers;

import rx.Observer;
import rx.Subscriber;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;

public final class Subscribers {
    private Subscribers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Subscriber<T> empty() {
        return from(Observers.empty());
    }

    public static <T> Subscriber<T> from(final Observer<? super T> o) {
        return new Subscriber<T>() {
            public void onCompleted() {
                o.onCompleted();
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(T t) {
                o.onNext(t);
            }
        };
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext) {
        if (onNext != null) {
            return new Subscriber<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    throw new OnErrorNotImplementedException(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        }
        throw new IllegalArgumentException("onNext can not be null");
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError != null) {
            return new Subscriber<T>() {
                public final void onCompleted() {
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        } else if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        } else if (onComplete != null) {
            return new Subscriber<T>() {
                public final void onCompleted() {
                    onComplete.call();
                }

                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onNext(T args) {
                    onNext.call(args);
                }
            };
        } else {
            throw new IllegalArgumentException("onComplete can not be null");
        }
    }

    public static <T> Subscriber<T> wrap(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(T t) {
                subscriber.onNext(t);
            }
        };
    }
}
