package rx.singles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import rx.Single;
import rx.SingleSubscriber;
import rx.annotations.Experimental;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.util.BlockingUtils;

@Experimental
public class BlockingSingle<T> {
    private final Single<? extends T> single;

    private BlockingSingle(Single<? extends T> single) {
        this.single = single;
    }

    @Experimental
    public static <T> BlockingSingle<T> from(Single<? extends T> single) {
        return new BlockingSingle(single);
    }

    @Experimental
    public T value() {
        final AtomicReference<T> returnItem = new AtomicReference();
        final AtomicReference<Throwable> returnException = new AtomicReference();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, this.single.subscribe(new SingleSubscriber<T>() {
            public void onSuccess(T value) {
                returnItem.set(value);
                latch.countDown();
            }

            public void onError(Throwable error) {
                returnException.set(error);
                latch.countDown();
            }
        }));
        Throwable throwable = (Throwable) returnException.get();
        if (throwable == null) {
            return returnItem.get();
        }
        if (throwable instanceof RuntimeException) {
            throw ((RuntimeException) throwable);
        }
        throw new RuntimeException(throwable);
    }

    @Experimental
    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.single.toObservable());
    }
}
