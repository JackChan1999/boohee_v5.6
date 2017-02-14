package org.eclipse.mat.collect;

import java.io.Serializable;

public final class BitField implements Serializable {
    private static final long serialVersionUID = 1;
    private int[] bits;

    public BitField(int size) {
        this.bits = new int[(((size - 1) >>> 5) + 1)];
    }

    public final void set(int index) {
        int[] iArr = this.bits;
        int i = index >>> 5;
        iArr[i] = iArr[i] | (1 << (index & 31));
    }

    public final void clear(int index) {
        int[] iArr = this.bits;
        int i = index >>> 5;
        iArr[i] = iArr[i] & ((1 << (index & 31)) ^ -1);
    }

    public final boolean get(int index) {
        return (this.bits[index >>> 5] & (1 << (index & 31))) != 0;
    }
}
