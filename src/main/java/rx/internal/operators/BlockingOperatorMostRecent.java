package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class BlockingOperatorMostRecent {

    private static final class MostRecentObserver<T> extends Subscriber<T> {
        final NotificationLite<T> nl;
        volatile Object value;

        private MostRecentObserver(T value) {
            this.nl = NotificationLite.instance();
            this.value = this.nl.next(value);
        }

        public void onCompleted() {
            this.value = this.nl.completed();
        }

        public void onError(Throwable e) {
            this.value = this.nl.error(e);
        }

        public void onNext(T args) {
            this.value = this.nl.next(args);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                private Object buf = null;

                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !MostRecentObserver.this.nl.isCompleted(this.buf);
                }

                public T next() {
                    try {
                        if (this.buf == null) {
                            Object obj = MostRecentObserver.this.value;
                        }
                        if (MostRecentObserver.this.nl.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (MostRecentObserver.this.nl.isError(this.buf)) {
                            throw Exceptions.propagate(MostRecentObserver.this.nl.getError(this.buf));
                        } else {
                            T value = MostRecentObserver.this.nl.getValue(this.buf);
                            this.buf = null;
                            return value;
                        }
                    } finally {
                        this.buf = null;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }

    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> source, final T initialValue) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                MostRecentObserver<T> mostRecentObserver = new MostRecentObserver(initialValue);
                source.subscribe(mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }
}
