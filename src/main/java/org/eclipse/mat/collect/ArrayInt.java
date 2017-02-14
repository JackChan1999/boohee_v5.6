package org.eclipse.mat.collect;

import java.util.Arrays;

public final class ArrayInt {
    int[] elements;
    int size;

    public ArrayInt() {
        this(10);
    }

    public ArrayInt(int initialCapacity) {
        this.elements = new int[initialCapacity];
        this.size = 0;
    }

    public ArrayInt(int[] initialValues) {
        this(initialValues.length);
        System.arraycopy(initialValues, 0, this.elements, 0, initialValues.length);
        this.size = initialValues.length;
    }

    public ArrayInt(ArrayInt template) {
        this(template.size);
        System.arraycopy(template.elements, 0, this.elements, 0, template.size);
        this.size = template.size;
    }

    public void add(int element) {
        ensureCapacity(this.size + 1);
        int[] iArr = this.elements;
        int i = this.size;
        this.size = i + 1;
        iArr[i] = element;
    }

    public void addAll(int[] elements) {
        ensureCapacity(this.size + elements.length);
        System.arraycopy(elements, 0, this.elements, this.size, elements.length);
        this.size += elements.length;
    }

    public void addAll(ArrayInt template) {
        ensureCapacity(this.size + template.size);
        System.arraycopy(template.elements, 0, this.elements, this.size, template.size);
        this.size += template.size;
    }

    public int set(int index, int element) {
        if (index < 0 || index >= this.size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        int oldValue = this.elements[index];
        this.elements[index] = element;
        return oldValue;
    }

    public int get(int index) {
        if (index >= 0 && index < this.size) {
            return this.elements[index];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public int size() {
        return this.size;
    }

    public int[] toArray() {
        int[] result = new int[this.size];
        System.arraycopy(this.elements, 0, result, 0, this.size);
        return result;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public IteratorInt iterator() {
        return new IteratorInt() {
            int index = 0;

            public boolean hasNext() {
                return this.index < ArrayInt.this.size;
            }

            public int next() {
                int[] iArr = ArrayInt.this.elements;
                int i = this.index;
                this.index = i + 1;
                return iArr[i];
            }
        };
    }

    public void clear() {
        this.size = 0;
    }

    public long lastElement() {
        return (long) this.elements[this.size - 1];
    }

    public long firstElement() {
        if (this.size != 0) {
            return (long) this.elements[0];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void sort() {
        Arrays.sort(this.elements, 0, this.size);
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = this.elements.length;
        if (minCapacity > oldCapacity) {
            int[] oldData = this.elements;
            int newCapacity = ((oldCapacity * 3) / 2) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            this.elements = new int[newCapacity];
            System.arraycopy(oldData, 0, this.elements, 0, this.size);
        }
    }
}
