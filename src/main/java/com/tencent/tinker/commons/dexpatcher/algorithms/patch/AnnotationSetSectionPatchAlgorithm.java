package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.AnnotationSet;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class AnnotationSetSectionPatchAlgorithm extends DexSectionPatchAlgorithm<AnnotationSet> {
    private Section                 patchedAnnotationSetSec;
    private TableOfContents.Section patchedAnnotationSetTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isAnnotationSetInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public AnnotationSetSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                              IndexMap oldToFullPatchedIndexMap, IndexMap
                                                      fullPatchedToSmallPatchedIndexMap,
                                              SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public AnnotationSetSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                              IndexMap oldToFullPatchedIndexMap, IndexMap
                                                      fullPatchedToSmallPatchedIndexMap,
                                              SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedAnnotationSetTocSec = null;
        this.patchedAnnotationSetSec = null;
        if (patchedDex != null) {
            this.patchedAnnotationSetTocSec = patchedDex.getTableOfContents().annotationSets;
            this.patchedAnnotationSetSec = patchedDex.openSection(this.patchedAnnotationSetTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().annotationSets;
    }

    protected AnnotationSet nextItem(DexDataBuffer section) {
        return section.readAnnotationSet();
    }

    protected int getItemSize(AnnotationSet item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedAnnotationSetSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected AnnotationSet adjustItem(IndexMap indexMap, AnnotationSet item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(AnnotationSet patchedItem) {
        TableOfContents.Section section = this.patchedAnnotationSetTocSec;
        section.size++;
        return this.patchedAnnotationSetSec.writeAnnotationSet(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapAnnotationSetOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markAnnotationSetDeleted(deletedOffset);
    }
}
