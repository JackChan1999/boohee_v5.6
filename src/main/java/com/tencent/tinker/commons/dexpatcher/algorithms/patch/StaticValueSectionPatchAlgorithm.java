package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.EncodedValue;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class StaticValueSectionPatchAlgorithm extends DexSectionPatchAlgorithm<EncodedValue> {
    private Section                 patchedEncodedValueSec;
    private TableOfContents.Section patchedEncodedValueTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isEncodedArrayInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public StaticValueSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                            IndexMap oldToFullPatchedIndexMap, IndexMap
                                                    fullPatchedToSmallPatchedIndexMap,
                                            SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public StaticValueSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                            IndexMap oldToFullPatchedIndexMap, IndexMap
                                                    fullPatchedToSmallPatchedIndexMap,
                                            SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedEncodedValueTocSec = null;
        this.patchedEncodedValueSec = null;
        if (patchedDex != null) {
            this.patchedEncodedValueTocSec = patchedDex.getTableOfContents().encodedArrays;
            this.patchedEncodedValueSec = patchedDex.openSection(this.patchedEncodedValueTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().encodedArrays;
    }

    protected EncodedValue nextItem(DexDataBuffer section) {
        return section.readEncodedArray();
    }

    protected int getItemSize(EncodedValue item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedEncodedArraySectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected EncodedValue adjustItem(IndexMap indexMap, EncodedValue item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(EncodedValue patchedItem) {
        TableOfContents.Section section = this.patchedEncodedValueTocSec;
        section.size++;
        return this.patchedEncodedValueSec.writeEncodedArray(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapStaticValuesOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markStaticValuesDeleted(deletedOffset);
    }
}
