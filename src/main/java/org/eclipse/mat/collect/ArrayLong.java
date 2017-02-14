package org.eclipse.mat.collect;

import java.util.Arrays;

public final class ArrayLong {
    long[] elements;
    int size;

    public ArrayLong() {
        this(10);
    }

    public ArrayLong(int initialCapacity) {
        this.elements = new long[initialCapacity];
        this.size = 0;
    }

    public ArrayLong(long[] initialValues) {
        this(initialValues.length);
        System.arraycopy(initialValues, 0, this.elements, 0, initialValues.length);
        this.size = initialValues.length;
    }

    public ArrayLong(ArrayLong template) {
        this(template.size);
        System.arraycopy(template.elements, 0, this.elements, 0, template.size);
        this.size = template.size;
    }

    public void add(long element) {
        ensureCapacity(this.size + 1);
        long[] jArr = this.elements;
        int i = this.size;
        this.size = i + 1;
        jArr[i] = element;
    }

    public void addAll(long[] elements) {
        ensureCapacity(this.size + elements.length);
        System.arraycopy(elements, 0, this.elements, this.size, elements.length);
        this.size += elements.length;
    }

    public void addAll(ArrayLong template) {
        ensureCapacity(this.size + template.size);
        System.arraycopy(template.elements, 0, this.elements, this.size, template.size);
        this.size += template.size;
    }

    public long set(int index, long element) {
        if (index < 0 || index >= this.size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        long oldValue = this.elements[index];
        this.elements[index] = element;
        return oldValue;
    }

    public long get(int index) {
        if (index >= 0 && index < this.size) {
            return this.elements[index];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public int size() {
        return this.size;
    }

    public long[] toArray() {
        long[] result = new long[this.size];
        System.arraycopy(this.elements, 0, result, 0, this.size);
        return result;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public IteratorLong iterator() {
        return new IteratorLong() {
            int index = 0;

            public boolean hasNext() {
                return this.index < ArrayLong.this.size;
            }

            public long next() {
                long[] jArr = ArrayLong.this.elements;
                int i = this.index;
                this.index = i + 1;
                return jArr[i];
            }
        };
    }

    public void clear() {
        this.size = 0;
    }

    public long lastElement() {
        return this.elements[this.size - 1];
    }

    public long firstElement() {
        if (this.size != 0) {
            return this.elements[0];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void sort() {
        Arrays.sort(this.elements, 0, this.size);
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = this.elements.length;
        if (minCapacity > oldCapacity) {
            long[] oldData = this.elements;
            int newCapacity = ((oldCapacity * 3) / 2) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            this.elements = new long[newCapacity];
            System.arraycopy(oldData, 0, this.elements, 0, this.size);
        }
    }
}
