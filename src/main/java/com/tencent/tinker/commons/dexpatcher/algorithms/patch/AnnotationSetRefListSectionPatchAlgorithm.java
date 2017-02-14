package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.AnnotationSetRefList;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class AnnotationSetRefListSectionPatchAlgorithm extends
        DexSectionPatchAlgorithm<AnnotationSetRefList> {
    private Section                 patchedAnnotationSetRefListSec;
    private TableOfContents.Section patchedAnnotationSetRefListTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isAnnotationSetRefListInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public AnnotationSetRefListSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex
            patchedDex, IndexMap oldToFullPatchedIndexMap, IndexMap
            fullPatchedToSmallPatchedIndexMap, SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public AnnotationSetRefListSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex
            patchedDex, IndexMap oldToFullPatchedIndexMap, IndexMap
            fullPatchedToSmallPatchedIndexMap, SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedAnnotationSetRefListTocSec = null;
        this.patchedAnnotationSetRefListSec = null;
        if (patchedDex != null) {
            this.patchedAnnotationSetRefListTocSec = patchedDex.getTableOfContents()
                    .annotationSetRefLists;
            this.patchedAnnotationSetRefListSec = patchedDex.openSection(this
                    .patchedAnnotationSetRefListTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().annotationSetRefLists;
    }

    protected AnnotationSetRefList nextItem(DexDataBuffer section) {
        return section.readAnnotationSetRefList();
    }

    protected int getItemSize(AnnotationSetRefList item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedAnnotationSetRefListSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected AnnotationSetRefList adjustItem(IndexMap indexMap, AnnotationSetRefList item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(AnnotationSetRefList patchedItem) {
        TableOfContents.Section section = this.patchedAnnotationSetRefListTocSec;
        section.size++;
        return this.patchedAnnotationSetRefListSec.writeAnnotationSetRefList(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapAnnotationSetRefListOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markAnnotationSetRefListDeleted(deletedOffset);
    }
}
