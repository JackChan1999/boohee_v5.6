package rx.internal.operators;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicLong;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;

final class TakeLastQueueProducer<T> extends AtomicLong implements Producer {
    private final Deque<Object> deque;
    private volatile boolean emittingStarted = false;
    private final NotificationLite<T> notification;
    private final Subscriber<? super T> subscriber;

    public TakeLastQueueProducer(NotificationLite<T> n, Deque<Object> q, Subscriber<? super T> subscriber) {
        this.notification = n;
        this.deque = q;
        this.subscriber = subscriber;
    }

    void startEmitting() {
        if (!this.emittingStarted) {
            this.emittingStarted = true;
            emit(0);
        }
    }

    public void request(long n) {
        if (get() != Long.MAX_VALUE) {
            long _c;
            if (n == Long.MAX_VALUE) {
                _c = getAndSet(Long.MAX_VALUE);
            } else {
                _c = BackpressureUtils.getAndAddRequest(this, n);
            }
            if (this.emittingStarted) {
                emit(_c);
            }
        }
    }

    void emit(long previousRequested) {
        if (get() == Long.MAX_VALUE) {
            if (previousRequested == 0) {
                try {
                    for (Object value : this.deque) {
                        if (!this.subscriber.isUnsubscribed()) {
                            this.notification.accept(this.subscriber, value);
                        } else {
                            return;
                        }
                    }
                    this.deque.clear();
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this.subscriber);
                } finally {
                    this.deque.clear();
                }
            }
        } else if (previousRequested == 0) {
            while (true) {
                long newRequested;
                long numToEmit = get();
                int emitted = 0;
                while (true) {
                    numToEmit--;
                    if (numToEmit < 0) {
                        break;
                    }
                    Object o = this.deque.poll();
                    if (o == null) {
                        break;
                    } else if (!this.subscriber.isUnsubscribed() && !this.notification.accept(this.subscriber, o)) {
                        emitted++;
                    } else {
                        return;
                    }
                }
                long oldRequested;
                do {
                    oldRequested = get();
                    newRequested = oldRequested - ((long) emitted);
                    if (oldRequested == Long.MAX_VALUE) {
                        break;
                    }
                } while (!compareAndSet(oldRequested, newRequested));
                if (newRequested == 0) {
                    return;
                }
            }
        }
    }
}
