package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public class AnnotationSet extends Item<AnnotationSet> {
    public int[] annotationOffsets;

    public AnnotationSet(int off, int[] annotationOffsets) {
        super(off);
        this.annotationOffsets = annotationOffsets;
    }

    public int compareTo(AnnotationSet other) {
        int size = this.annotationOffsets.length;
        int oSize = other.annotationOffsets.length;
        if (size != oSize) {
            return CompareUtils.uCompare(size, oSize);
        }
        for (int i = 0; i < size; i++) {
            if (this.annotationOffsets[i] != other.annotationOffsets[i]) {
                return CompareUtils.uCompare(this.annotationOffsets[i], other.annotationOffsets[i]);
            }
        }
        return 0;
    }

    public int byteCountInDex() {
        return (this.annotationOffsets.length + 1) * 4;
    }
}
