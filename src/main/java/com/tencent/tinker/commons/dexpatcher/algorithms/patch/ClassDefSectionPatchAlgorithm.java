package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.ClassDef;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class ClassDefSectionPatchAlgorithm extends DexSectionPatchAlgorithm<ClassDef> {
    private Section                 patchedClassDefSec;
    private TableOfContents.Section patchedClassDefTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isClassDefInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public ClassDefSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public ClassDefSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                         IndexMap oldToFullPatchedIndexMap, IndexMap
                                                 fullPatchedToSmallPatchedIndexMap,
                                         SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedClassDefTocSec = null;
        this.patchedClassDefSec = null;
        if (patchedDex != null) {
            this.patchedClassDefTocSec = patchedDex.getTableOfContents().classDefs;
            this.patchedClassDefSec = patchedDex.openSection(this.patchedClassDefTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().classDefs;
    }

    protected ClassDef nextItem(DexDataBuffer section) {
        return section.readClassDef();
    }

    protected int getItemSize(ClassDef item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedClassDefSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected ClassDef adjustItem(IndexMap indexMap, ClassDef item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(ClassDef patchedItem) {
        TableOfContents.Section section = this.patchedClassDefTocSec;
        section.size++;
        return this.patchedClassDefSec.writeClassDef(patchedItem);
    }
}
