package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.MethodId;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class MethodIdSectionPatchAlgorithm extends DexSectionPatchAlgorithm<MethodId> {
    private Section                 patchedMethodIdSec;
    private TableOfContents.Section patchedMethodIdTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isMethodIdInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public MethodIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public MethodIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedMethodIdTocSec = null;
        this.patchedMethodIdSec = null;
        if (patchedDex != null) {
            this.patchedMethodIdTocSec = patchedDex.getTableOfContents().methodIds;
            this.patchedMethodIdSec = patchedDex.openSection(this.patchedMethodIdTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().methodIds;
    }

    protected MethodId nextItem(DexDataBuffer section) {
        return section.readMethodId();
    }

    protected int getItemSize(MethodId item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedMethodIdSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected MethodId adjustItem(IndexMap indexMap, MethodId item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(MethodId patchedItem) {
        TableOfContents.Section section = this.patchedMethodIdTocSec;
        section.size++;
        return this.patchedMethodIdSec.writeMethodId(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldIndex != newIndex) {
            indexMap.mapMethodIds(oldIndex, newIndex);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markMethodIdDeleted(deletedIndex);
    }
}
