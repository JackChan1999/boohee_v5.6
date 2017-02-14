package rx.observables;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.operators.BlockingOperatorLatest;
import rx.internal.operators.BlockingOperatorMostRecent;
import rx.internal.operators.BlockingOperatorNext;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.operators.BlockingOperatorToIterator;
import rx.internal.operators.NotificationLite;
import rx.internal.util.BlockingUtils;
import rx.internal.util.UtilityFunctions;
import rx.subscriptions.Subscriptions;

public final class BlockingObservable<T> {
    private static final Object ON_START = new Object();
    private static final Object SET_PRODUCER = new Object();
    private static final Object UNSUBSCRIBE = new Object();
    private final Observable<? extends T> o;

    private BlockingObservable(Observable<? extends T> o) {
        this.o = o;
    }

    public static <T> BlockingObservable<T> from(Observable<? extends T> o) {
        return new BlockingObservable(o);
    }

    public void forEach(final Action1<? super T> onNext) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> exceptionFromOnError = new AtomicReference();
        BlockingUtils.awaitForComplete(latch, this.o.subscribe(new Subscriber<T>() {
            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                exceptionFromOnError.set(e);
                latch.countDown();
            }

            public void onNext(T args) {
                onNext.call(args);
            }
        }));
        if (exceptionFromOnError.get() == null) {
            return;
        }
        if (exceptionFromOnError.get() instanceof RuntimeException) {
            throw ((RuntimeException) exceptionFromOnError.get());
        }
        throw new RuntimeException((Throwable) exceptionFromOnError.get());
    }

    public Iterator<T> getIterator() {
        return BlockingOperatorToIterator.toIterator(this.o);
    }

    public T first() {
        return blockForSingle(this.o.first());
    }

    public T first(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.first(predicate));
    }

    public T firstOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.filter(predicate).map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T last() {
        return blockForSingle(this.o.last());
    }

    public T last(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.last(predicate));
    }

    public T lastOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public T lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.filter(predicate).map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public Iterable<T> mostRecent(T initialValue) {
        return BlockingOperatorMostRecent.mostRecent(this.o, initialValue);
    }

    public Iterable<T> next() {
        return BlockingOperatorNext.next(this.o);
    }

    public Iterable<T> latest() {
        return BlockingOperatorLatest.latest(this.o);
    }

    public T single() {
        return blockForSingle(this.o.single());
    }

    public T single(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.single(predicate));
    }

    public T singleOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public T singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.filter(predicate).map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.o);
    }

    public Iterable<T> toIterable() {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return BlockingObservable.this.getIterator();
            }
        };
    }

    private T blockForSingle(Observable<? extends T> observable) {
        final AtomicReference<T> returnItem = new AtomicReference();
        final AtomicReference<Throwable> returnException = new AtomicReference();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, observable.subscribe(new Subscriber<T>() {
            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                returnException.set(e);
                latch.countDown();
            }

            public void onNext(T item) {
                returnItem.set(item);
            }
        }));
        if (returnException.get() == null) {
            return returnItem.get();
        }
        if (returnException.get() instanceof RuntimeException) {
            throw ((RuntimeException) returnException.get());
        }
        throw new RuntimeException((Throwable) returnException.get());
    }

    @Experimental
    public void subscribe() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] error = new Throwable[]{null};
        BlockingUtils.awaitForComplete(cdl, this.o.subscribe(new Subscriber<T>() {
            public void onNext(T t) {
            }

            public void onError(Throwable e) {
                error[0] = e;
                cdl.countDown();
            }

            public void onCompleted() {
                cdl.countDown();
            }
        }));
        Throwable e = error[0];
        if (e == null) {
            return;
        }
        if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        }
        throw new RuntimeException(e);
    }

    @Experimental
    public void subscribe(Observer<? super T> observer) {
        final NotificationLite<T> nl = NotificationLite.instance();
        final BlockingQueue<Object> queue = new LinkedBlockingQueue();
        Subscription s = this.o.subscribe(new Subscriber<T>() {
            public void onNext(T t) {
                queue.offer(nl.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(nl.error(e));
            }

            public void onCompleted() {
                queue.offer(nl.completed());
            }
        });
        while (true) {
            try {
                Object o = queue.poll();
                if (o == null) {
                    o = queue.take();
                }
                if (nl.accept(observer, o)) {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                observer.onError(e);
            } finally {
                s.unsubscribe();
            }
        }
    }

    @Experimental
    public void subscribe(Subscriber<? super T> subscriber) {
        final NotificationLite<T> nl = NotificationLite.instance();
        final BlockingQueue<Object> queue = new LinkedBlockingQueue();
        final Producer[] theProducer = new Producer[]{null};
        Subscriber<T> s = new Subscriber<T>() {
            public void onNext(T t) {
                queue.offer(nl.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(nl.error(e));
            }

            public void onCompleted() {
                queue.offer(nl.completed());
            }

            public void setProducer(Producer p) {
                theProducer[0] = p;
                queue.offer(BlockingObservable.SET_PRODUCER);
            }

            public void onStart() {
                queue.offer(BlockingObservable.ON_START);
            }
        };
        subscriber.add(s);
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                queue.offer(BlockingObservable.UNSUBSCRIBE);
            }
        }));
        this.o.subscribe(s);
        while (!subscriber.isUnsubscribed()) {
            try {
                Object o = queue.poll();
                if (o == null) {
                    o = queue.take();
                }
                if (subscriber.isUnsubscribed() || o == UNSUBSCRIBE) {
                    break;
                } else if (o == ON_START) {
                    subscriber.onStart();
                } else if (o == SET_PRODUCER) {
                    subscriber.setProducer(theProducer[0]);
                } else if (nl.accept(subscriber, o)) {
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                subscriber.onError(e);
                return;
            } finally {
                s.unsubscribe();
            }
        }
        s.unsubscribe();
    }

    @Experimental
    public void subscribe(Action1<? super T> onNext) {
        subscribe(onNext, new Action1<Throwable>() {
            public void call(Throwable t) {
                throw new OnErrorNotImplementedException(t);
            }
        }, Actions.empty());
    }

    @Experimental
    public void subscribe(Action1<? super T> onNext, Action1<? super Throwable> onError) {
        subscribe(onNext, onError, Actions.empty());
    }

    @Experimental
    public void subscribe(final Action1<? super T> onNext, final Action1<? super Throwable> onError, final Action0 onCompleted) {
        subscribe(new Observer<T>() {
            public void onNext(T t) {
                onNext.call(t);
            }

            public void onError(Throwable e) {
                onError.call(e);
            }

            public void onCompleted() {
                onCompleted.call();
            }
        });
    }
}
