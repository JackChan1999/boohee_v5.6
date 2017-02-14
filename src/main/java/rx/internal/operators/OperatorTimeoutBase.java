package rx.internal.operators;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

class OperatorTimeoutBase<T> implements Observable$Operator<T, T> {
    private final FirstTimeoutStub<T> firstTimeoutStub;
    private final Observable<? extends T> other;
    private final Scheduler scheduler;
    private final TimeoutStub<T> timeoutStub;

    interface FirstTimeoutStub<T> extends Func3<TimeoutSubscriber<T>, Long, Worker, Subscription> {
    }

    interface TimeoutStub<T> extends Func4<TimeoutSubscriber<T>, Long, T, Worker, Subscription> {
    }

    static final class TimeoutSubscriber<T> extends Subscriber<T> {
        final AtomicLong actual;
        private final Object gate;
        private final Worker inner;
        private final Observable<? extends T> other;
        private final SerialSubscription serial;
        private final SerializedSubscriber<T> serializedSubscriber;
        final AtomicInteger terminated;
        private final TimeoutStub<T> timeoutStub;

        private TimeoutSubscriber(SerializedSubscriber<T> serializedSubscriber, TimeoutStub<T> timeoutStub, SerialSubscription serial, Observable<? extends T> other, Worker inner) {
            super(serializedSubscriber);
            this.gate = new Object();
            this.terminated = new AtomicInteger();
            this.actual = new AtomicLong();
            this.serializedSubscriber = serializedSubscriber;
            this.timeoutStub = timeoutStub;
            this.serial = serial;
            this.other = other;
            this.inner = inner;
        }

        public void onNext(T value) {
            boolean onNextWins = false;
            synchronized (this.gate) {
                if (this.terminated.get() == 0) {
                    this.actual.incrementAndGet();
                    onNextWins = true;
                }
            }
            if (onNextWins) {
                this.serializedSubscriber.onNext(value);
                this.serial.set((Subscription) this.timeoutStub.call(this, Long.valueOf(this.actual.get()), value, this.inner));
            }
        }

        public void onError(Throwable error) {
            boolean onErrorWins = false;
            synchronized (this.gate) {
                if (this.terminated.getAndSet(1) == 0) {
                    onErrorWins = true;
                }
            }
            if (onErrorWins) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onError(error);
            }
        }

        public void onCompleted() {
            boolean onCompletedWins = false;
            synchronized (this.gate) {
                if (this.terminated.getAndSet(1) == 0) {
                    onCompletedWins = true;
                }
            }
            if (onCompletedWins) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onCompleted();
            }
        }

        public void onTimeout(long seqId) {
            long expected = seqId;
            boolean timeoutWins = false;
            synchronized (this.gate) {
                if (expected == this.actual.get() && this.terminated.getAndSet(1) == 0) {
                    timeoutWins = true;
                }
            }
            if (!timeoutWins) {
                return;
            }
            if (this.other == null) {
                this.serializedSubscriber.onError(new TimeoutException());
                return;
            }
            this.other.unsafeSubscribe(this.serializedSubscriber);
            this.serial.set(this.serializedSubscriber);
        }
    }

    OperatorTimeoutBase(FirstTimeoutStub<T> firstTimeoutStub, TimeoutStub<T> timeoutStub, Observable<? extends T> other, Scheduler scheduler) {
        this.firstTimeoutStub = firstTimeoutStub;
        this.timeoutStub = timeoutStub;
        this.other = other;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Worker inner = this.scheduler.createWorker();
        subscriber.add(inner);
        SerialSubscription serial = new SerialSubscription();
        subscriber.add(serial);
        TimeoutSubscriber<T> timeoutSubscriber = new TimeoutSubscriber(new SerializedSubscriber(subscriber), this.timeoutStub, serial, this.other, inner);
        serial.set((Subscription) this.firstTimeoutStub.call(timeoutSubscriber, Long.valueOf(0), inner));
        return timeoutSubscriber;
    }
}
