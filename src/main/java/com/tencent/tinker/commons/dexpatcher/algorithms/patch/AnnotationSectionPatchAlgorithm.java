package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Annotation;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class AnnotationSectionPatchAlgorithm extends DexSectionPatchAlgorithm<Annotation> {
    private Section                 patchedAnnotationSec;
    private TableOfContents.Section patchedAnnotationTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isAnnotationInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public AnnotationSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                           IndexMap oldToFullPatchedIndexMap, IndexMap
                                                   fullPatchedToSmallPatchedIndexMap,
                                           SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public AnnotationSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                           IndexMap oldToFullPatchedIndexMap, IndexMap
                                                   fullPatchedToSmallPatchedIndexMap,
                                           SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedAnnotationTocSec = null;
        this.patchedAnnotationSec = null;
        if (patchedDex != null) {
            this.patchedAnnotationTocSec = patchedDex.getTableOfContents().annotations;
            this.patchedAnnotationSec = patchedDex.openSection(this.patchedAnnotationTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().annotations;
    }

    protected Annotation nextItem(DexDataBuffer section) {
        return section.readAnnotation();
    }

    protected int getItemSize(Annotation item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedAnnotationSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected Annotation adjustItem(IndexMap indexMap, Annotation item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(Annotation patchedItem) {
        TableOfContents.Section section = this.patchedAnnotationTocSec;
        section.size++;
        return this.patchedAnnotationSec.writeAnnotation(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapAnnotationOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markAnnotationDeleted(deletedOffset);
    }
}
