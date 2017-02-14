package org.eclipse.mat.collect;

import java.io.Serializable;
import java.util.ArrayList;

public final class ArrayIntBig implements Serializable {
    private static final long serialVersionUID = 1;
    private int length = 0;
    private int[] page;
    private ArrayList<int[]> pages = new ArrayList();

    public final void add(int element) {
        int i = this.length;
        this.length = i + 1;
        int index = i & 1023;
        if (index == 0) {
            ArrayList arrayList = this.pages;
            Object obj = new int[1024];
            this.page = obj;
            arrayList.add(obj);
        }
        this.page[index] = element;
    }

    public final void addAll(int[] elements) {
        int bite;
        int free = this.length & 1023;
        if (free == 0) {
            bite = 0;
        } else {
            bite = Math.min(elements.length, 1024 - free);
        }
        if (bite > 0) {
            System.arraycopy(elements, 0, this.pages.get(this.length >> 10), this.length & 1023, bite);
            this.length += bite;
        }
        int copied = bite;
        while (copied < elements.length) {
            ArrayList arrayList = this.pages;
            Object obj = new int[1024];
            this.page = obj;
            arrayList.add(obj);
            bite = Math.min(elements.length - copied, 1024);
            System.arraycopy(elements, copied, this.page, 0, bite);
            copied += bite;
            this.length += bite;
        }
    }

    public final int get(int index) throws IndexOutOfBoundsException {
        if (index < this.length) {
            return ((int[]) this.pages.get(index >> 10))[index & 1023];
        }
        throw new IndexOutOfBoundsException();
    }

    public final int length() {
        return this.length;
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    public final long consumption() {
        return ((long) this.pages.size()) << 12;
    }

    public final int[] toArray() {
        int[] elements = new int[this.length];
        int copied = 0;
        while (copied < this.length) {
            int bite = Math.min(this.length - copied, 1024);
            System.arraycopy(this.pages.get(copied >> 10), 0, elements, copied, bite);
            copied += bite;
        }
        return elements;
    }
}
