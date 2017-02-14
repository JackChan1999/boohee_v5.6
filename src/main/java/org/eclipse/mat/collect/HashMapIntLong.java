package org.eclipse.mat.collect;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class HashMapIntLong implements Serializable {
    private static NoSuchElementException noSuchElementException = new NoSuchElementException("This is static exception, there is no stack trace available. It is thrown by get() method.");
    private static final long serialVersionUID = 1;
    private int capacity;
    private int[] keys;
    private int limit;
    private int size;
    private int step;
    private boolean[] used;
    private long[] values;

    public interface Entry {
        int getKey();

        long getValue();
    }

    public HashMapIntLong() {
        this(10);
    }

    public HashMapIntLong(int initialCapacity) {
        init(initialCapacity);
    }

    public boolean put(int key, long value) {
        if (this.size == this.limit) {
            resize(this.capacity << 1);
        }
        int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                this.values[hash] = value;
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        this.used[hash] = true;
        this.keys[hash] = key;
        this.values[hash] = value;
        this.size++;
        return false;
    }

    public boolean remove(int key) {
        int hash = (key & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                this.used[hash] = false;
                this.size--;
                hash = (this.step + hash) % this.capacity;
                while (this.used[hash]) {
                    key = this.keys[hash];
                    this.used[hash] = false;
                    int newHash = (key & ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) % this.capacity;
                    while (this.used[newHash]) {
                        newHash = (this.step + newHash) % this.capacity;
                    }
                    this.used[newHash] = true;
                    this.keys[newHash] = key;
                    this.values[newHash] = this.values[hash];
                    hash = (this.step + hash) % this.capacity;
                }
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return false;
    }

    public boolean containsKey(int key) {
        int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return false;
    }

    public long get(int key) {
        int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return this.values[hash];
            }
            hash = (this.step + hash) % this.capacity;
        }
        throw noSuchElementException;
    }

    public int[] getAllKeys() {
        int[] array = new int[this.size];
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

    public IteratorInt keys() {
        return new IteratorInt() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapIntLong.this.size;
            }

            public int next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapIntLong.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapIntLong.this.used[this.i]);
                this.n++;
                return HashMapIntLong.this.keys[this.i];
            }
        };
    }

    public IteratorLong values() {
        return new IteratorLong() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapIntLong.this.size;
            }

            public long next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapIntLong.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapIntLong.this.used[this.i]);
                this.n++;
                return HashMapIntLong.this.values[this.i];
            }
        };
    }

    public Iterator<Entry> entries() {
        return new Iterator<Entry>() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < HashMapIntLong.this.size;
            }

            public Entry next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= HashMapIntLong.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!HashMapIntLong.this.used[this.i]);
                this.n++;
                return new Entry() {
                    public int getKey() {
                        return HashMapIntLong.this.keys[AnonymousClass3.this.i];
                    }

                    public long getValue() {
                        return HashMapIntLong.this.values[AnonymousClass3.this.i];
                    }
                };
            }

            public void remove() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
    }

    public long[] getAllValues() {
        long[] a = new long[this.size];
        int index = 0;
        for (int ii = 0; ii < this.values.length; ii++) {
            if (this.used[ii]) {
                int index2 = index + 1;
                a[index] = this.values[ii];
                index = index2;
            }
        }
        return a;
    }

    private void init(int initialCapacity) {
        this.capacity = PrimeFinder.findNextPrime(initialCapacity);
        this.step = Math.max(1, PrimeFinder.findPrevPrime(initialCapacity / 3));
        this.limit = (int) (((double) this.capacity) * 0.75d);
        clear();
        this.keys = new int[this.capacity];
        this.values = new long[this.capacity];
    }

    private void resize(int newCapacity) {
        int oldSize = this.size;
        boolean[] oldUsed = this.used;
        int[] oldKeys = this.keys;
        long[] oldValues = this.values;
        init(newCapacity);
        for (int i = 0; i < oldUsed.length; i++) {
            if (oldUsed[i]) {
                int key = oldKeys[i];
                int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
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
