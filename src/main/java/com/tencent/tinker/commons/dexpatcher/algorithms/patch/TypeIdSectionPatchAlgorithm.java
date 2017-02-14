package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class TypeIdSectionPatchAlgorithm extends DexSectionPatchAlgorithm<Integer> {
    private Section                 patchedTypeIdSec;
    private TableOfContents.Section patchedTypeIdTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isTypeIdInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public TypeIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                       IndexMap oldToFullPatchedIndexMap, IndexMap
                                               fullPatchedToSmallPatchedIndexMap,
                                       SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public TypeIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                       IndexMap oldToFullPatchedIndexMap, IndexMap
                                               fullPatchedToSmallPatchedIndexMap,
                                       SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedTypeIdTocSec = null;
        this.patchedTypeIdSec = null;
        if (patchedDex != null) {
            this.patchedTypeIdTocSec = patchedDex.getTableOfContents().typeIds;
            this.patchedTypeIdSec = patchedDex.openSection(this.patchedTypeIdTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().typeIds;
    }

    protected Integer nextItem(DexDataBuffer section) {
        return Integer.valueOf(section.readInt());
    }

    protected int getItemSize(Integer item) {
        return 4;
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedTypeIdSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected Integer adjustItem(IndexMap indexMap, Integer item) {
        return Integer.valueOf(indexMap.adjustStringIndex(item.intValue()));
    }

    protected int writePatchedItem(Integer patchedItem) {
        int off = this.patchedTypeIdSec.position();
        this.patchedTypeIdSec.writeInt(patchedItem.intValue());
        TableOfContents.Section section = this.patchedTypeIdTocSec;
        section.size++;
        return off;
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldIndex != newIndex) {
            indexMap.mapTypeIds(oldIndex, newIndex);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markTypeIdDeleted(deletedIndex);
    }
}
