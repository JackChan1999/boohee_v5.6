package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class ClassDef extends Item<ClassDef> {
    public static final int NO_INDEX  = -1;
    public static final int NO_OFFSET = 0;
    public int accessFlags;
    public int annotationsOffset;
    public int classDataOffset;
    public int interfacesOffset;
    public int sourceFileIndex;
    public int staticValuesOffset;
    public int supertypeIndex;
    public int typeIndex;

    public ClassDef(int off, int typeIndex, int accessFlags, int supertypeIndex, int
            interfacesOffset, int sourceFileIndex, int annotationsOffset, int classDataOffset,
                    int staticValuesOffset) {
        super(off);
        this.typeIndex = typeIndex;
        this.accessFlags = accessFlags;
        this.supertypeIndex = supertypeIndex;
        this.interfacesOffset = interfacesOffset;
        this.sourceFileIndex = sourceFileIndex;
        this.annotationsOffset = annotationsOffset;
        this.classDataOffset = classDataOffset;
        this.staticValuesOffset = staticValuesOffset;
    }

    public int compareTo(ClassDef other) {
        int res = CompareUtils.uCompare(this.typeIndex, other.typeIndex);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.accessFlags, other.accessFlags);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.uCompare(this.supertypeIndex, other.supertypeIndex);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.interfacesOffset, other.interfacesOffset);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.uCompare(this.sourceFileIndex, other.sourceFileIndex);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.annotationsOffset, other.annotationsOffset);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.sCompare(this.classDataOffset, other.classDataOffset);
        if (res != 0) {
            return res;
        }
        return CompareUtils.sCompare(this.staticValuesOffset, other.staticValuesOffset);
    }

    public int byteCountInDex() {
        return 32;
    }
}
