package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.ClassData;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class ClassDataSectionPatchAlgorithm extends DexSectionPatchAlgorithm<ClassData> {
    private Section                 patchedClassDataSec;
    private TableOfContents.Section patchedClassDataTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isClassDataInSmallPatchedDex(oldDexSign,
                    patchedItemIndex);
        }
    }

    public ClassDataSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                          IndexMap oldToFullPatchedIndexMap, IndexMap
                                                  fullPatchedToSmallPatchedIndexMap,
                                          SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public ClassDataSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                          IndexMap oldToFullPatchedIndexMap, IndexMap
                                                  fullPatchedToSmallPatchedIndexMap,
                                          SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedClassDataTocSec = null;
        this.patchedClassDataSec = null;
        if (patchedDex != null) {
            this.patchedClassDataTocSec = patchedDex.getTableOfContents().classDatas;
            this.patchedClassDataSec = patchedDex.openSection(this.patchedClassDataTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().classDatas;
    }

    protected ClassData nextItem(DexDataBuffer section) {
        return section.readClassData();
    }

    protected int getItemSize(ClassData item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedClassDataSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected ClassData adjustItem(IndexMap indexMap, ClassData item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(ClassData patchedItem) {
        TableOfContents.Section section = this.patchedClassDataTocSec;
        section.size++;
        return this.patchedClassDataSec.writeClassData(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapClassDataOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markClassDataDeleted(deletedOffset);
    }
}
