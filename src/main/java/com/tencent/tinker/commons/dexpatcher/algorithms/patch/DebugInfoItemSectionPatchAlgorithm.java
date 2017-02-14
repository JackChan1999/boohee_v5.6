package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.DebugInfoItem;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class DebugInfoItemSectionPatchAlgorithm extends DexSectionPatchAlgorithm<DebugInfoItem> {
    private Section                 patchedDebugInfoItemSec;
    private TableOfContents.Section patchedDebugInfoItemTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isDebugInfoInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public DebugInfoItemSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                              IndexMap oldToFullPatchedIndexMap, IndexMap
                                                      fullPatchedToSmallPatchedIndexMap,
                                              SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public DebugInfoItemSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                              IndexMap oldToFullPatchedIndexMap, IndexMap
                                                      fullPatchedToSmallPatchedIndexMap,
                                              SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedDebugInfoItemTocSec = null;
        this.patchedDebugInfoItemSec = null;
        if (patchedDex != null) {
            this.patchedDebugInfoItemTocSec = patchedDex.getTableOfContents().debugInfos;
            this.patchedDebugInfoItemSec = patchedDex.openSection(this.patchedDebugInfoItemTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().debugInfos;
    }

    protected DebugInfoItem nextItem(DexDataBuffer section) {
        return section.readDebugInfoItem();
    }

    protected int getItemSize(DebugInfoItem item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedDebugInfoSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected DebugInfoItem adjustItem(IndexMap indexMap, DebugInfoItem item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(DebugInfoItem patchedItem) {
        TableOfContents.Section section = this.patchedDebugInfoItemTocSec;
        section.size++;
        return this.patchedDebugInfoItemSec.writeDebugInfoItem(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapDebugInfoItemOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markDebugInfoItemDeleted(deletedOffset);
    }
}
