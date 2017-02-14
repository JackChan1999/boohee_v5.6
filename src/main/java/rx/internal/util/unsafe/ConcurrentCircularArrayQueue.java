package rx.internal.util.unsafe;

import java.util.Iterator;

public abstract class ConcurrentCircularArrayQueue<E> extends ConcurrentCircularArrayQueueL0Pad<E> {
    protected static final int BUFFER_PAD = 32;
    private static final long REF_ARRAY_BASE = ((long) (UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class) + (32 << (REF_ELEMENT_SHIFT - SPARSE_SHIFT))));
    private static final int REF_ELEMENT_SHIFT;
    protected static final int SPARSE_SHIFT = Integer.getInteger("sparse.shift", 0).intValue();
    protected final E[] buffer;
    protected final long mask;

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
    }

    public ConcurrentCircularArrayQueue(int capacity) {
        int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = (long) (actualCapacity - 1);
        this.buffer = new Object[((actualCapacity << SPARSE_SHIFT) + 64)];
    }

    protected final long calcElementOffset(long index) {
        return calcElementOffset(index, this.mask);
    }

    protected final long calcElementOffset(long index, long mask) {
        return REF_ARRAY_BASE + ((index & mask) << REF_ELEMENT_SHIFT);
    }

    protected final void spElement(long offset, E e) {
        spElement(this.buffer, offset, e);
    }

    protected final void spElement(E[] buffer, long offset, E e) {
        UnsafeAccess.UNSAFE.putObject(buffer, offset, e);
    }

    protected final void soElement(long offset, E e) {
        soElement(this.buffer, offset, e);
    }

    protected final void soElement(E[] buffer, long offset, E e) {
        UnsafeAccess.UNSAFE.putOrderedObject(buffer, offset, e);
    }

    protected final E lpElement(long offset) {
        return lpElement(this.buffer, offset);
    }

    protected final E lpElement(E[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getObject(buffer, offset);
    }

    protected final E lvElement(long offset) {
        return lvElement(this.buffer, offset);
    }

    protected final E lvElement(E[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getObjectVolatile(buffer, offset);
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }
}
