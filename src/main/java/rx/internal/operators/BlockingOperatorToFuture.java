package rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public final class BlockingOperatorToFuture {
    private BlockingOperatorToFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Future<T> toFuture(Observable<? extends T> that) {
        final CountDownLatch finished = new CountDownLatch(1);
        final AtomicReference<T> value = new AtomicReference();
        final AtomicReference<Throwable> error = new AtomicReference();
        final Subscription s = that.single().subscribe(new Subscriber<T>() {
            public void onCompleted() {
                finished.countDown();
            }

            public void onError(Throwable e) {
                error.compareAndSet(null, e);
                finished.countDown();
            }

            public void onNext(T v) {
                value.set(v);
            }
        });
        return new Future<T>() {
            private volatile boolean cancelled = false;

            public boolean cancel(boolean mayInterruptIfRunning) {
                if (finished.getCount() <= 0) {
                    return false;
                }
                this.cancelled = true;
                s.unsubscribe();
                finished.countDown();
                return true;
            }

            public boolean isCancelled() {
                return this.cancelled;
            }

            public boolean isDone() {
                return finished.getCount() == 0;
            }

            public T get() throws InterruptedException, ExecutionException {
                finished.await();
                return getValue();
            }

            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                if (finished.await(timeout, unit)) {
                    return getValue();
                }
                throw new TimeoutException("Timed out after " + unit.toMillis(timeout) + "ms waiting for underlying Observable.");
            }

            private T getValue() throws ExecutionException {
                Throwable throwable = (Throwable) error.get();
                if (throwable != null) {
                    throw new ExecutionException("Observable onError", throwable);
                } else if (!this.cancelled) {
                    return value.get();
                } else {
                    throw new CancellationException("Subscription unsubscribed");
                }
            }
        };
    }
}
