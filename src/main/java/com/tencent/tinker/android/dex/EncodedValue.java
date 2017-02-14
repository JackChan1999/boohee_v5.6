package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.ByteInput;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class EncodedValue extends Item<EncodedValue> {
    public byte[] data;

    public EncodedValue(int off, byte[] data) {
        super(off);
        this.data = data;
    }

    public ByteInput asByteInput() {
        return new ByteInput() {
            private int position = 0;

            public byte readByte() {
                byte[] bArr = EncodedValue.this.data;
                int i = this.position;
                this.position = i + 1;
                return bArr[i];
            }
        };
    }

    public int compareTo(EncodedValue other) {
        return CompareUtils.uArrCompare(this.data, other.data);
    }

    public int byteCountInDex() {
        return this.data.length * 1;
    }
}
