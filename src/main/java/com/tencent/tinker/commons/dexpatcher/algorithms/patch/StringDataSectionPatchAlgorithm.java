package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.StringData;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class StringDataSectionPatchAlgorithm extends DexSectionPatchAlgorithm<StringData> {
    private Section                 patchedStringDataSec;
    private TableOfContents.Section patchedStringDataTocSec;
    private Section                 patchedStringIdSec;
    private TableOfContents.Section patchedStringIdTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isStringInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public StringDataSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                           IndexMap oldToFullPatchedIndexMap, IndexMap
                                                   fullPatchedToSmallPatchedIndexMap,
                                           SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public StringDataSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                           IndexMap oldToFullPatchedIndexMap, IndexMap
                                                   fullPatchedToSmallPatchedIndexMap,
                                           SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedStringDataTocSec = null;
        this.patchedStringIdTocSec = null;
        this.patchedStringDataSec = null;
        this.patchedStringIdSec = null;
        if (patchedDex != null) {
            this.patchedStringDataTocSec = patchedDex.getTableOfContents().stringDatas;
            this.patchedStringIdTocSec = patchedDex.getTableOfContents().stringIds;
            this.patchedStringDataSec = patchedDex.openSection(this.patchedStringDataTocSec);
            this.patchedStringIdSec = patchedDex.openSection(this.patchedStringIdTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().stringDatas;
    }

    protected StringData nextItem(DexDataBuffer section) {
        return section.readStringData();
    }

    protected int getItemSize(StringData item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedStringDataSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected int writePatchedItem(StringData patchedItem) {
        int off = this.patchedStringDataSec.writeStringData(patchedItem);
        this.patchedStringIdSec.writeInt(off);
        TableOfContents.Section section = this.patchedStringDataTocSec;
        section.size++;
        section = this.patchedStringIdTocSec;
        section.size++;
        return off;
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldIndex != newIndex) {
            indexMap.mapStringIds(oldIndex, newIndex);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markStringIdDeleted(deletedIndex);
    }
}
