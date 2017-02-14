package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorNext {

    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error;
        private boolean hasNext;
        private boolean isNextConsumed;
        private final Observable<? extends T> items;
        private T next;
        private final NextObserver<T> observer;
        private boolean started;

        private NextIterator(Observable<? extends T> items, NextObserver<T> observer) {
            this.hasNext = true;
            this.isNextConsumed = true;
            this.error = null;
            this.started = false;
            this.items = items;
            this.observer = observer;
        }

        public boolean hasNext() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            } else if (!this.hasNext) {
                return false;
            } else {
                if (this.isNextConsumed) {
                    return moveToNext();
                }
                return true;
            }
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting(1);
                    this.items.materialize().subscribe(this.observer);
                }
                Notification<? extends T> nextNotification = this.observer.takeNext();
                if (nextNotification.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = nextNotification.getValue();
                    return true;
                }
                this.hasNext = false;
                if (nextNotification.isOnCompleted()) {
                    return false;
                }
                if (nextNotification.isOnError()) {
                    this.error = nextNotification.getThrowable();
                    throw Exceptions.propagate(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (InterruptedException e) {
                this.observer.unsubscribe();
                Thread.currentThread().interrupt();
                this.error = e;
                throw Exceptions.propagate(this.error);
            }
        }

        public T next() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            } else if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            } else {
                throw new NoSuchElementException("No more elements");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    private static class NextObserver<T> extends Subscriber<Notification<? extends T>> {
        private final BlockingQueue<Notification<? extends T>> buf;
        final AtomicInteger waiting;

        private NextObserver() {
            this.buf = new ArrayBlockingQueue(1);
            this.waiting = new AtomicInteger();
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Notification<? extends T> args) {
            if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
                Notification<? extends T> toOffer = args;
                while (!this.buf.offer(toOffer)) {
                    Notification<? extends T> concurrentItem = (Notification) this.buf.poll();
                    if (!(concurrentItem == null || concurrentItem.isOnNext())) {
                        toOffer = concurrentItem;
                    }
                }
            }
        }

        public Notification<? extends T> takeNext() throws InterruptedException {
            setWaiting(1);
            return (Notification) this.buf.take();
        }

        void setWaiting(int value) {
            this.waiting.set(value);
        }
    }

    private BlockingOperatorNext() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> next(final Observable<? extends T> items) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return new NextIterator(items, new NextObserver());
            }
        };
    }
}
