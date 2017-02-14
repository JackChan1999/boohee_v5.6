package org.eclipse.mat.parser.internal.util;

public class IntStack {
    private int[] data;
    private int size;

    public IntStack() {
        this(16);
    }

    public IntStack(int capacity) {
        this.data = new int[capacity];
    }

    public final int pop() {
        int[] iArr = this.data;
        int i = this.size - 1;
        this.size = i;
        return iArr[i];
    }

    public final void push(int i) {
        if (this.size == this.data.length) {
            int[] newArr = new int[(this.data.length << 1)];
            System.arraycopy(this.data, 0, newArr, 0, this.data.length);
            this.data = newArr;
        }
        int[] iArr = this.data;
        int i2 = this.size;
        this.size = i2 + 1;
        iArr[i2] = i;
    }

    public final int peek() {
        return this.data[this.size - 1];
    }

    public final int size() {
        return this.size;
    }

    public final int capacity() {
        return this.data.length;
    }
}
