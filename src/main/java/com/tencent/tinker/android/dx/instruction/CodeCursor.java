package com.tencent.tinker.android.dx.instruction;

import com.tencent.tinker.android.utils.SparseIntArray;

public abstract class CodeCursor {
    private final SparseIntArray baseAddressMap = new SparseIntArray();
    private       int            cursor         = 0;

    public final int cursor() {
        return this.cursor;
    }

    public final int baseAddressForCursor() {
        int index = this.baseAddressMap.indexOfKey(this.cursor);
        if (index < 0) {
            return this.cursor;
        }
        return this.baseAddressMap.valueAt(index);
    }

    public final void setBaseAddress(int targetAddress, int baseAddress) {
        this.baseAddressMap.put(targetAddress, baseAddress);
    }

    public void reset() {
        this.baseAddressMap.clear();
        this.cursor = 0;
    }

    protected final void advance(int amount) {
        this.cursor += amount;
    }
}
