package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;

public class StringData extends Item<StringData> {
    public String value;

    public StringData(int offset, String value) {
        super(offset);
        this.value = value;
    }

    public int compareTo(StringData other) {
        return this.value.compareTo(other.value);
    }

    public int byteCountInDex() {
        try {
            return (Leb128.unsignedLeb128Size(this.value.length()) + ((int) Mutf8.countBytes(this
                    .value, true))) + 1;
        } catch (Throwable e) {
            throw new DexException(e);
        }
    }
}
