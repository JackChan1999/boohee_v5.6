package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public class AnnotationSetRefList extends Item<AnnotationSetRefList> {
    public int[] annotationSetRefItems;

    public AnnotationSetRefList(int off, int[] annotationSetRefItems) {
        super(off);
        this.annotationSetRefItems = annotationSetRefItems;
    }

    public int compareTo(AnnotationSetRefList other) {
        int size = this.annotationSetRefItems.length;
        int oSize = other.annotationSetRefItems.length;
        if (size != oSize) {
            return CompareUtils.uCompare(size, oSize);
        }
        for (int i = 0; i < size; i++) {
            if (this.annotationSetRefItems[i] != other.annotationSetRefItems[i]) {
                return CompareUtils.uCompare(this.annotationSetRefItems[i], other
                        .annotationSetRefItems[i]);
            }
        }
        return 0;
    }

    public int byteCountInDex() {
        return (this.annotationSetRefItems.length + 1) * 4;
    }
}
