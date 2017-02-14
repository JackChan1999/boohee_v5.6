package com.qiniu.android.dns.util;

public final class BitSet {
    private int set = 0;

    public BitSet set(int bitIndex) {
        this.set |= 1 << bitIndex;
        return this;
    }

    public boolean isSet(int index) {
        return (this.set & (1 << index)) != 0;
    }

    public boolean noneIsSet(int index) {
        return this.set == 0;
    }

    public boolean allIsSet(int index) {
        return this.set + 1 == (1 << index);
    }

    public int leadingZeros() {
        int n = 32;
        int y = this.set >> 16;
        if (y != 0) {
            n = 32 - 16;
            this.set = y;
        }
        y = this.set >> 8;
        if (y != 0) {
            n -= 8;
            this.set = y;
        }
        y = this.set >> 4;
        if (y != 0) {
            n -= 4;
            this.set = y;
        }
        y = this.set >> 2;
        if (y != 0) {
            n -= 2;
            this.set = y;
        }
        if ((this.set >> 1) != 0) {
            return n - 2;
        }
        return n - this.set;
    }

    public BitSet clear() {
        this.set = 0;
        return this;
    }

    public int value() {
        return this.set;
    }
}
