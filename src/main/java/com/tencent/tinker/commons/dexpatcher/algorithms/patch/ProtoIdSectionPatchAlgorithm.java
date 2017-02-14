package com.tencent.tinker.commons.dexpatcher.algorithms.patch;

import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.Dex.Section;
import com.tencent.tinker.android.dex.ProtoId;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm
        .SmallPatchedDexItemChooser;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

public class ProtoIdSectionPatchAlgorithm extends DexSectionPatchAlgorithm<ProtoId> {
    private Section                 patchedProtoIdSec;
    private TableOfContents.Section patchedProtoIdTocSec;

    class AnonymousClass1 implements SmallPatchedDexItemChooser {
        final /* synthetic */ SmallPatchedDexItemFile val$extraInfoFile;

        AnonymousClass1(SmallPatchedDexItemFile smallPatchedDexItemFile) {
            this.val$extraInfoFile = smallPatchedDexItemFile;
        }

        public boolean isPatchedItemInSmallPatchedDex(String oldDexSign, int patchedItemIndex) {
            return this.val$extraInfoFile.isProtoIdInSmallPatchedDex(oldDexSign, patchedItemIndex);
        }
    }

    public ProtoIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                        IndexMap oldToFullPatchedIndexMap, IndexMap
                                                fullPatchedToSmallPatchedIndexMap,
                                        SmallPatchedDexItemFile extraInfoFile) {
        this(patchFile, oldDex, patchedDex, oldToFullPatchedIndexMap,
                fullPatchedToSmallPatchedIndexMap, new AnonymousClass1(extraInfoFile));
    }

    public ProtoIdSectionPatchAlgorithm(DexPatchFile patchFile, Dex oldDex, Dex patchedDex,
                                        IndexMap oldToFullPatchedIndexMap, IndexMap
                                                fullPatchedToSmallPatchedIndexMap,
                                        SmallPatchedDexItemChooser spdItemChooser) {
        super(patchFile, oldDex, oldToFullPatchedIndexMap, fullPatchedToSmallPatchedIndexMap,
                spdItemChooser);
        this.patchedProtoIdTocSec = null;
        this.patchedProtoIdSec = null;
        if (patchedDex != null) {
            this.patchedProtoIdTocSec = patchedDex.getTableOfContents().protoIds;
            this.patchedProtoIdSec = patchedDex.openSection(this.patchedProtoIdTocSec);
        }
    }

    protected TableOfContents.Section getTocSection(Dex dex) {
        return dex.getTableOfContents().protoIds;
    }

    protected ProtoId nextItem(DexDataBuffer section) {
        return section.readProtoId();
    }

    protected int getItemSize(ProtoId item) {
        return item.byteCountInDex();
    }

    protected int getFullPatchSectionBase() {
        if (this.patchFile != null) {
            return this.patchFile.getPatchedProtoIdSectionOffset();
        }
        return getTocSection(this.oldDex).off;
    }

    protected ProtoId adjustItem(IndexMap indexMap, ProtoId item) {
        return indexMap.adjust(item);
    }

    protected int writePatchedItem(ProtoId patchedItem) {
        TableOfContents.Section section = this.patchedProtoIdTocSec;
        section.size++;
        return this.patchedProtoIdSec.writeProtoId(patchedItem);
    }

    protected void updateIndexOrOffset(IndexMap indexMap, int oldIndex, int oldOffset, int
            newIndex, int newOffset) {
        if (oldIndex != newIndex) {
            indexMap.mapProtoIds(oldIndex, newIndex);
        }
    }

    protected void markDeletedIndexOrOffset(IndexMap indexMap, int deletedIndex, int
            deletedOffset) {
        indexMap.markProtoIdDeleted(deletedIndex);
    }
}
