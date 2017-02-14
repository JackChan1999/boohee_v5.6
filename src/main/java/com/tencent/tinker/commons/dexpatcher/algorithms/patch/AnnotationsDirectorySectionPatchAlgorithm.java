package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.AnnotationsDirectory;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class AnnotationsDirectorySectionPatchAlgorithm extends
        DexSectionPatchAlgorithm<AnnotationsDirectory> {
    private Section                 patchedAnnotationsDirectorySec;
    private TableOfContents.Section patchedAnnotationsDirectoryTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isAnnotationsDirectoryInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public AnnotationsDirectorySectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex
            patchedDex, IndexMap oldToFullPatchedIndexMap, IndexMap
            fullPatchedToSmallPatchedIndexMap, SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public AnnotationsDirectorySectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex
            patchedDex, IndexMap oldToFullPatchedIndexMap, IndexMap
            fullPatchedToSmallPatchedIndexMap, SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedAnnotationsDirectoryTocSec = null;
        this.patchedAnnotationsDirectorySec = null;
        if (patchedDex != null) {
            this.patchedAnnotationsDirectoryTocSec = patchedDex.getTableOfContents()
                    .annotationsDirectories;
            this.patchedAnnotationsDirectorySec = patchedDex.openSection(this
                    .patchedAnnotationsDirectoryTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().annotationsDirectories;
    }

    protected AnnotationsDirectory nextItem(DexDataBuffer section) {
        return section.readAnnotationsDirectory();
    }

    protected int getItemSize(AnnotationsDirectory item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedAnnotationsDirectorySectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected AnnotationsDirectory adjustItem(IndexMap indexMap, AnnotationsDirectory item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(AnnotationsDirectory patchedItem) {
        TableOfContents.Section section = this.patchedAnnotationsDirectoryTocSec;
        section.size++;
        return this.patchedAnnotationsDirectorySec.writeAnnotationsDirectory(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapAnnotationsDirectoryOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markAnnotationsDirectoryDeleted(deletedOffset);
    }
}
