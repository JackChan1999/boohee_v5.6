package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.TypeList;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class TypeListSectionPatchAlgorithm extends DexSectionPatchAlgorithm<TypeList> {
    private Section                 patchedTypeListSec;
    private TableOfContents.Section patchedTypeListTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isTypeListInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public TypeListSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public TypeListSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedTypeListTocSec = null;
        this.patchedTypeListSec = null;
        if (patchedDex != null) {
            this.patchedTypeListTocSec = patchedDex.getTableOfContents().typeLists;
            this.patchedTypeListSec = patchedDex.openSection(this.patchedTypeListTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().typeLists;
    }

    protected TypeList nextItem(DexDataBuffer section) {
        return section.readTypeList();
    }

    protected int getItemSize(TypeList item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedTypeListSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected TypeList adjustItem(IndexMap indexMap, TypeList item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(TypeList patchedItem) {
        TableOfContents.Section section = this.patchedTypeListTocSec;
        section.size++;
        return this.patchedTypeListSec.writeTypeList(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapTypeListOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markTypeListDeleted(deletedOffset);
    }
}
