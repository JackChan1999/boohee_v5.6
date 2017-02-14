package org.eclipse.mat.collect;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class HashMapLongObject<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private int capacity;
    private long[] keys;
    private int limit;
    private int size;
    private int step;
    private boolean[] used;
    private E[] values;

    public interface Entry<E> {
        long getKey();

        E getValue();
    }

    public HashMapLongObject() {
        this(10);
    }

    public HashMapLongObject(int initialCapacity) {
        init(initialCapacity);
    }

    public E put(long key, E value) {
        if (this.size == this.limit) {
            resize(this.capacity << 1);
        }
        int hash = hash(key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                E oldValue = this.values[hash];
                this.values[hash] = value;
                return oldValue;
            }
            hash = (this.step + hash) % this.capacity;
        }
        this.used[hash] = true;
        this.keys[hash] = key;
        this.values[hash] = value;
        this.size++;
        return null;
    }

    private int hash(long key) {
        return (int) (2147483647L & key);
    }

    public E remove(long key) {
        int hash = hash(key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                E e = this.values[hash];
                this.used[hash] = false;
                this.size--;
                hash = (this.step + hash) % this.capacity;
                while (this.used[hash]) {
                    key = this.keys[hash];
                    this.used[hash] = false;
                    int newHash = hash(key) % this.capacity;
                    while (this.used[newHash]) {
                        newHash = (this.step + newHash) % this.capacity;
                    }
                    this.used[newHash] = true;
                    this.keys[newHash] = key;
                    this.values[newHash] = this.values[hash];
                    hash = (this.step + hash) % this.capacity;
                }
                return e;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return null;
    }

    public boolean containsKey(long key) {
        int hash = hash(key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return false;
    }

    public E get(long key) {
        int hash = hash(key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return this.values[hash];
            }
            hash = (this.step + hash) % this.capacity;
        }
        return null;
    }

    public long[] getAllKeys() {
        long[] array = new long[this.size];
        int j = 0;
        for (int i = 0; i < this.used.length; i++) {
            if (this.used[i]) {
                int j2 = j + 1;
                array[j] = this.keys[i];
                j = j2;
            }
        }
        return array;
    }

    public Object[] getAllValues() {
        Object[] array = new Object[this.size];
        int index = 0;
        for (int ii = 0; ii < this.used.length; ii++) {
            if (this.used[ii]) {
                int index2 = index + 1;
                array[index] = this.values[ii];
                index = index2;
            }
        }
        return array;
    }

    public <T> T[] getAllValues(T[] a) {
        if (a.length < this.size) {
            a = (Object[]) ((Object[]) Array.newInstance(a.getClass().getComponentType(), this.size));
        }
        int index = 0;
        for (int ii = 0; ii < this.used.length; ii++) {
            if (this.used[ii]) {
                int index2 = index + 1;
                a[index] = this.values[ii];
                index = index2;
            }
        }
        if (a.length > this.size) {
            a[this.size] = null;
        }
        return a;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        this.size = 0;
        this.used = new boolean[this.capacity];
    }

    public IteratorLong keys() {
        return new IteratorLong() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapLongObject.this.size;
            }

            public long next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapLongObject.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapLongObject.this.used[this.i]);
                this.n++;
                return HashMapLongObject.this.keys[this.i];
            }
        };
    }

    public Iterator<E> values() {
        return new Iterator<E>() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapLongObject.this.size;
            }

            public E next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapLongObject.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapLongObject.this.used[this.i]);
                this.n++;
                return HashMapLongObject.this.values[this.i];
            }

            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Iterator<Entry<E>> entries() {
        return new Iterator<Entry<E>>() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapLongObject.this.size;
            }

            public Entry<E> next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapLongObject.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapLongObject.this.used[this.i]);
                this.n++;
                return new Entry<E>() {
                    public long getKey() {
                        return HashMapLongObject.this.keys[AnonymousClass3.this.i];
                    }

                    public E getValue() {
                        return HashMapLongObject.this.values[AnonymousClass3.this.i];
                    }
                };
            }

            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void init(int initialCapacity) {
        this.capacity = PrimeFinder.findNextPrime(initialCapacity);
        this.step = Math.max(1, PrimeFinder.findPrevPrime(initialCapacity / 3));
        this.limit = (int) (((double) this.capacity) * 0.75d);
        clear();
        this.keys = new long[this.capacity];
        this.values = new Object[this.capacity];
    }

    private void resize(int newCapacity) {
        int oldSize = this.size;
        boolean[] oldUsed = this.used;
        long[] oldKeys = this.keys;
        E[] oldValues = this.values;
        init(newCapacity);
        for (int i = 0; i < oldUsed.length; i++) {
            if (oldUsed[i]) {
                long key = oldKeys[i];
                int hash = hash(key) % this.capacity;
                while (this.used[hash]) {
                    hash = (this.step + hash) % this.capacity;
                }
                this.used[hash] = true;
                this.keys[hash] = key;
                this.values[hash] = oldValues[i];
            }
        }
        this.size = oldSize;
    }
}
