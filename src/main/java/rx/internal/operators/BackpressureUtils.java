package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public final class BackpressureUtils {
    private BackpressureUtils() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> long getAndAddRequest(AtomicLongFieldUpdater<T> requested, T object, long n) {
        long current;
        do {
            current = requested.get(object);
        } while (!requested.compareAndSet(object, current, addCap(current, n)));
        return current;
    }

    public static long getAndAddRequest(AtomicLong requested, long n) {
        long current;
        do {
            current = requested.get();
        } while (!requested.compareAndSet(current, addCap(current, n)));
        return current;
    }

    public static long multiplyCap(long a, long b) {
        long u = a * b;
        if (((a | b) >>> 31) == 0 || b == 0 || u / b == a) {
            return u;
        }
        return Long.MAX_VALUE;
    }

    public static long addCap(long a, long b) {
        long u = a + b;
        if (u < 0) {
            return Long.MAX_VALUE;
        }
        return u;
    }
}
