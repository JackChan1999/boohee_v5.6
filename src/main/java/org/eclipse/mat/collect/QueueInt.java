package org.eclipse.mat.collect;

import org.eclipse.mat.hprof.Messages;

public class QueueInt {
    int capacity;
    int[] data;
    int headIdx;
    int size;
    int tailIdx;

    public QueueInt(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
    }

    public final int get() {
        if (this.size == 0) {
            throw new ArrayIndexOutOfBoundsException(Messages.QueueInt_ZeroSizeQueue.pattern);
        }
        int result = this.data[this.headIdx];
        this.headIdx++;
        this.size--;
        if (this.headIdx == this.capacity) {
            this.headIdx = 0;
        }
        return result;
    }

    public final int size() {
        return this.size;
    }

    public final void put(int x) {
        if (this.tailIdx == this.capacity) {
            this.tailIdx = 0;
        }
        if (this.size == this.capacity) {
            this.capacity <<= 1;
            int[] tmp = new int[this.capacity];
            int headToEnd = this.data.length - this.headIdx;
            System.arraycopy(this.data, this.headIdx, tmp, 0, headToEnd);
            if (this.tailIdx > 0) {
                System.arraycopy(this.data, 0, tmp, headToEnd, this.tailIdx);
            }
            this.headIdx = 0;
            this.tailIdx = this.data.length;
            this.data = tmp;
        }
        this.data[this.tailIdx] = x;
        this.size++;
        this.tailIdx++;
    }
}
