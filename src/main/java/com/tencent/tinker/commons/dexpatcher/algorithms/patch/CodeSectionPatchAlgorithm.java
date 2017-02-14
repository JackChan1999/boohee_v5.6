package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Code;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class CodeSectionPatchAlgorithm extends DexSectionPatchAlgorithm<Code> {
    private Section                 patchedCodeSec;
    private TableOfContents.Section patchedCodeTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isCodeInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public CodeSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex, IndexMap
            oldToFullPatchedIndexMap, IndexMap fullPatchedToSmallPatchedIndexMap,
                                     SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public CodeSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex, IndexMap
            oldToFullPatchedIndexMap, IndexMap fullPatchedToSmallPatchedIndexMap,
                                     SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedCodeTocSec = null;
        this.patchedCodeSec = null;
        if (patchedDex != null) {
            this.patchedCodeTocSec = patchedDex.getTableOfContents().codes;
            this.patchedCodeSec = patchedDex.openSection(this.patchedCodeTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().codes;
    }

    protected Code nextItem(DexDataBuffer section) {
        return section.readCode();
    }

    protected int getItemSize(Code item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedCodeSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected Code adjustItem(IndexMap indexMap, Code item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(Code patchedItem) {
        TableOfContents.Section section = this.patchedCodeTocSec;
        section.size++;
        return this.patchedCodeSec.writeCode(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldOffset != newOffset) {
            indexMap.mapCodeOffset(oldOffset, newOffset);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markCodeDeleted(deletedOffset);
    }
}
