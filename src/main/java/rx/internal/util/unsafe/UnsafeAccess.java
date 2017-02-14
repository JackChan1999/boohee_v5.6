package rx.internal.util.unsafe;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public final class UnsafeAccess {
    public static final Unsafe UNSAFE;

    private UnsafeAccess() {
        throw new IllegalStateException("No instances!");
    }

    static {
        Unsafe u = null;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            u = (Unsafe) field.get(null);
        } catch (Throwable th) {
        }
        UNSAFE = u;
    }

    public static final boolean isUnsafeAvailable() {
        return UNSAFE != null;
    }

    public static int getAndIncrementInt(Object obj, long offset) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + 1));
        return current;
    }

    public static int getAndAddInt(Object obj, long offset, int n) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + n));
        return current;
    }

    public static int getAndSetInt(Object obj, long offset, int newValue) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, newValue));
        return current;
    }

    public static boolean compareAndSwapInt(Object obj, long offset, int expected, int newValue) {
        return UNSAFE.compareAndSwapInt(obj, offset, expected, newValue);
    }

    public static long addressOf(Class<?> clazz, String fieldName) {
        try {
            return UNSAFE.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException ex) {
            InternalError ie = new InternalError();
            ie.initCause(ex);
            throw ie;
        }
    }
}
