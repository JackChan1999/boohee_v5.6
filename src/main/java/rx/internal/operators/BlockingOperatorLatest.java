package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorLatest {

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        Notification<? extends T> iNotif;
        final Semaphore notify = new Semaphore(0);
        final AtomicReference<Notification<? extends T>> value = new AtomicReference();

        LatestObserverIterator() {
        }

        public void onNext(Notification<? extends T> args) {
            if (this.value.getAndSet(args) == null) {
                this.notify.release();
            }
        }

        public void onError(Throwable e) {
        }

        public void onCompleted() {
        }

        public boolean hasNext() {
            if (this.iNotif == null || !this.iNotif.isOnError()) {
                if ((this.iNotif == null || !this.iNotif.isOnCompleted()) && this.iNotif == null) {
                    try {
                        this.notify.acquire();
                        this.iNotif = (Notification) this.value.getAndSet(null);
                        if (this.iNotif.isOnError()) {
                            throw Exceptions.propagate(this.iNotif.getThrowable());
                        }
                    } catch (InterruptedException ex) {
                        unsubscribe();
                        Thread.currentThread().interrupt();
                        this.iNotif = Notification.createOnError(ex);
                        throw Exceptions.propagate(ex);
                    }
                }
                return !this.iNotif.isOnCompleted();
            } else {
                throw Exceptions.propagate(this.iNotif.getThrowable());
            }
        }

        public T next() {
            if (hasNext() && this.iNotif.isOnNext()) {
                T v = this.iNotif.getValue();
                this.iNotif = null;
                return v;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }

    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(final Observable<? extends T> source) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                LatestObserverIterator<T> lio = new LatestObserverIterator();
                source.materialize().subscribe(lio);
                return lio;
            }
        };
    }
}
