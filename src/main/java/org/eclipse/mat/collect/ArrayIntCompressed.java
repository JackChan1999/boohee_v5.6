package org.eclipse.mat.collect;

public class ArrayIntCompressed {
    private static final int BIT_LENGTH = 32;
    private byte[] data;
    private byte trailingClearBits;
    private byte varyingBits;

    public ArrayIntCompressed(byte[] bytes) {
        this.data = bytes;
        this.varyingBits = this.data[0];
        this.trailingClearBits = this.data[1];
    }

    public ArrayIntCompressed(int size, int leadingClearBits, int trailingClearBits) {
        init(size, (32 - leadingClearBits) - trailingClearBits, trailingClearBits);
    }

    public ArrayIntCompressed(int[] ints) {
        this(ints, 0, ints.length);
    }

    public ArrayIntCompressed(int[] ints, int offset, int length) {
        int i;
        int mask = 0;
        for (i = 0; i < length; i++) {
            mask |= ints[offset + i];
        }
        int leadingClearBits = 0;
        int trailingClearBits = 0;
        while (((1 << ((32 - leadingClearBits) - 1)) & mask) == 0 && leadingClearBits < 32) {
            leadingClearBits++;
        }
        while (((1 << trailingClearBits) & mask) == 0 && trailingClearBits < 32 - leadingClearBits) {
            trailingClearBits++;
        }
        init(length, (32 - leadingClearBits) - trailingClearBits, trailingClearBits);
        for (i = 0; i < length; i++) {
            set(i, ints[offset + i]);
        }
    }

    private void init(int size, int varyingBits, int trailingClearBits) {
        this.data = new byte[((((int) (((((long) size) * ((long) varyingBits)) - 1) / 8)) + 2) + 1)];
        byte b = (byte) varyingBits;
        this.data[0] = b;
        this.varyingBits = b;
        b = (byte) trailingClearBits;
        this.data[1] = b;
        this.trailingClearBits = b;
    }

    public void set(int index, int value) {
        byte[] bArr;
        value >>>= this.trailingClearBits;
        long pos = ((long) index) * ((long) this.varyingBits);
        int idx = ((int) (pos >>> 3)) + 2;
        int off = (((int) pos) & 7) + this.varyingBits;
        if (off > 8) {
            off -= 8;
            bArr = this.data;
            int idx2 = idx + 1;
            bArr[idx] = (byte) (bArr[idx] | ((byte) (value >>> off)));
            while (off > 8) {
                off -= 8;
                idx = idx2 + 1;
                this.data[idx2] = (byte) (value >>> off);
                idx2 = idx;
            }
            idx = idx2;
        }
        bArr = this.data;
        bArr[idx] = (byte) (bArr[idx] | ((byte) (value << (8 - off))));
    }

    public int get(int index) {
        int value;
        long pos = ((long) index) * ((long) this.varyingBits);
        int idx = ((int) (pos >>> 3)) + 2;
        int off = ((int) pos) & 7;
        if (this.varyingBits + off > 8) {
            int idx2 = idx + 1;
            value = ((this.data[idx] << off) & 255) >>> off;
            off += this.varyingBits - 8;
            while (off > 8) {
                value = (value << 8) | (this.data[idx2] & 255);
                off -= 8;
                idx2++;
            }
            value = (value << off) | ((this.data[idx2] & 255) >>> (8 - off));
            idx = idx2;
        } else {
            value = ((this.data[idx] << off) & 255) >>> (8 - this.varyingBits);
        }
        return value << this.trailingClearBits;
    }

    public byte[] toByteArray() {
        return this.data;
    }
}
