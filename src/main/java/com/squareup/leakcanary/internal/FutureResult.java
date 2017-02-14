package com.squareup.leakcanary.internal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class FutureResult<T> {
    private final CountDownLatch     latch        = new CountDownLatch(1);
    private final AtomicReference<T> resultHolder = new AtomicReference();

    public boolean wait(long timeout, TimeUnit unit) {
        try {
            return this.latch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Did not expect thread to be interrupted", e);
        }
    }

    public T get() {
        if (this.latch.getCount() <= 0) {
            return this.resultHolder.get();
        }
        throw new IllegalStateException("Call wait() and check its result");
    }

    public void set(T result) {
        this.resultHolder.set(result);
        this.latch.countDown();
    }
}
