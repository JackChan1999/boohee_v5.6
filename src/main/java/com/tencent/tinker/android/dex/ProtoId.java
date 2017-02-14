package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class ProtoId extends Item<ProtoId> {
    public int parametersOffset;
    public int returnTypeIndex;
    public int shortyIndex;

    public ProtoId(int off, int shortyIndex, int returnTypeIndex, int parametersOffset) {
        super(off);
        this.shortyIndex = shortyIndex;
        this.returnTypeIndex = returnTypeIndex;
        this.parametersOffset = parametersOffset;
    }

    public int compareTo(ProtoId other) {
        int res = CompareUtils.uCompare(this.shortyIndex, other.shortyIndex);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.uCompare(this.returnTypeIndex, other.returnTypeIndex);
        if (res != 0) {
            return res;
        }
        return CompareUtils.sCompare(this.parametersOffset, other.parametersOffset);
    }

    public int byteCountInDex() {
        return 12;
    }
}
