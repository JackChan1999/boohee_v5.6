package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class TypeList extends Item<TypeList> {
    public static final TypeList EMPTY = new TypeList(0, Dex.EMPTY_SHORT_ARRAY);
    public short[] types;

    public TypeList(int off, short[] types) {
        super(off);
        this.types = types;
    }

    public int compareTo(TypeList other) {
        return CompareUtils.uArrCompare(this.types, other.types);
    }

    public int byteCountInDex() {
        return (this.types.length * 2) + 4;
    }
}
