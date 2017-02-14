package org.eclipse.mat.collect;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import java.io.Serializable;
import java.util.NoSuchElementException;

public final class SetInt implements Serializable {
    private static final long serialVersionUID = 1;
    private int capacity;
    private int[] keys;
    private int limit;
    private int size;
    private int step;
    private boolean[] used;

    public SetInt() {
        this(10);
    }

    public SetInt(int initialCapacity) {
        init(initialCapacity);
    }

    public boolean add(int key) {
        if (this.size == this.limit) {
            resize(this.capacity << 1);
        }
        int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return false;
            }
            hash = (this.step + hash) % this.capacity;
        }
        this.used[hash] = true;
        this.keys[hash] = key;
        this.size++;
        return true;
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
                    hash = (this.step + hash) % this.capacity;
                }
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return false;
    }

    public boolean contains(int key) {
        int hash = (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED & key) % this.capacity;
        while (this.used[hash]) {
            if (this.keys[hash] == key) {
                return true;
            }
            hash = (this.step + hash) % this.capacity;
        }
        return false;
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

    public IteratorInt iterator() {
        return new IteratorInt() {
            int i = -1;
            int n = 0;

            public boolean hasNext() {
                return this.n < SetInt.this.size;
            }

            public int next() throws NoSuchElementException {
                do {
                    int i = this.i + 1;
                    this.i = i;
                    if (i >= SetInt.this.used.length) {
                        throw new NoSuchElementException();
                    }
                } while (!SetInt.this.used[this.i]);
                this.n++;
                return SetInt.this.keys[this.i];
            }
        };
    }

    public int[] toArray() {
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

    private void init(int initialCapacity) {
        this.capacity = PrimeFinder.findNextPrime(initialCapacity);
        this.step = Math.max(1, PrimeFinder.findPrevPrime(initialCapacity / 3));
        this.limit = (int) (((double) this.capacity) * 0.75d);
        clear();
        this.keys = new int[this.capacity];
    }

    private void resize(int newCapacity) {
        int oldSize = this.size;
        boolean[] oldUsed = this.used;
        int[] oldKeys = this.keys;
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
            }
        }
        this.size = oldSize;
    }
}
