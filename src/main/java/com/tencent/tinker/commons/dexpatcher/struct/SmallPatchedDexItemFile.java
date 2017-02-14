package com.tencent.tinker.commons.dexpatcher.struct;

import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dex.util.CompareUtils;
import com.tencent.tinker.android.dex.util.FileUtils;
import com.tencent.tinker.android.dx.util.Hex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SmallPatchedDexItemFile {
    public static final short  CURRENT_VERSION = (short) 1;
    public static final byte[] MAGIC           = new byte[]{(byte) 68, (byte) 68, (byte) 69,
            (byte) 88, (byte) 84, (byte) 82, (byte) 65};
    private int firstChunkOffset;
    private final Map<String, BitSet>     oldDexSignToAnnotationIndicesInSmallPatch           =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToAnnotationSetIndicesInSmallPatch        =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToAnnotationSetRefListIndicesInSmallPatch =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToAnnotationsDirectoryIndicesInSmallPatch =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToClassDataIndicesInSmallPatch            =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToClassDefIndicesInSmallPatch             =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToCodeIndicesInSmallPatch                 =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToDebugInfoIndicesInSmallPatch            =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToEncodedArrayIndicesInSmallPatch         =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToFieldIdIndicesInSmallPatch              =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToMethodIdIndicesInSmallPatch             =
            new HashMap();
    private final Map<String, DexOffsets> oldDexSignToOffsetInfoMap                           =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToProtoIdIndicesInSmallPatch              =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToStringIndicesInSmallPatch               =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToTypeIdIndicesInSmallPatch               =
            new HashMap();
    private final Map<String, BitSet>     oldDexSignToTypeListIndicesInSmallPatch             =
            new HashMap();
    private final List<String>            oldDexSigns                                         =
            new ArrayList();
    private int version;

    private static final class DexOffsets {
        int annotationSetRefListsOffset;
        int annotationSetsOffset;
        int annotationsDirectoriesOffset;
        int annotationsOffset;
        int classDataItemsOffset;
        int classDefsOffset;
        int codeItemsOffset;
        int debugInfoItemsOffset;
        int dexSize;
        int encodedArraysOffset;
        int fieldIdsOffset;
        int mapListOffset;
        int methodIdsOffset;
        int protoIdsOffset;
        int stringDataItemsOffset;
        int stringIdsOffset;
        int typeIdsOffset;
        int typeListsOffset;

        private DexOffsets() {
            this.stringIdsOffset = -1;
            this.typeIdsOffset = -1;
            this.protoIdsOffset = -1;
            this.fieldIdsOffset = -1;
            this.methodIdsOffset = -1;
            this.classDefsOffset = -1;
            this.mapListOffset = -1;
            this.typeListsOffset = -1;
            this.annotationsOffset = -1;
            this.annotationSetsOffset = -1;
            this.annotationSetRefListsOffset = -1;
            this.annotationsDirectoriesOffset = -1;
            this.classDataItemsOffset = -1;
            this.codeItemsOffset = -1;
            this.stringDataItemsOffset = -1;
            this.debugInfoItemsOffset = -1;
            this.encodedArraysOffset = -1;
            this.dexSize = -1;
        }
    }

    public SmallPatchedDexItemFile(File input) throws IOException {
        init(new DexDataBuffer(ByteBuffer.wrap(FileUtils.readFile(input))));
    }

    public SmallPatchedDexItemFile(InputStream is) throws IOException {
        init(new DexDataBuffer(ByteBuffer.wrap(FileUtils.readStream(is))));
    }

    private void init(DexDataBuffer buffer) throws IOException {
        byte[] magic = buffer.readByteArray(MAGIC.length);
        if (CompareUtils.uArrCompare(magic, MAGIC) != 0) {
            throw new IllegalStateException("bad dexdiff extra file magic: " + Arrays.toString
                    (magic));
        }
        this.version = buffer.readShort();
        if (this.version != 1) {
            throw new IllegalStateException("bad dexdiff extra file version: " + this.version +
                    ", expected: " + 1);
        }
        int i;
        this.firstChunkOffset = buffer.readInt();
        buffer.position(this.firstChunkOffset);
        int oldDexSignCount = buffer.readUleb128();
        for (i = 0; i < oldDexSignCount; i++) {
            this.oldDexSigns.add(Hex.toHexString(buffer.readByteArray(20)));
        }
        for (i = 0; i < oldDexSignCount; i++) {
            String oldDexSign = (String) this.oldDexSigns.get(i);
            DexOffsets dexOffsets = new DexOffsets();
            dexOffsets.stringIdsOffset = buffer.readInt();
            dexOffsets.typeIdsOffset = buffer.readInt();
            dexOffsets.protoIdsOffset = buffer.readInt();
            dexOffsets.fieldIdsOffset = buffer.readInt();
            dexOffsets.methodIdsOffset = buffer.readInt();
            dexOffsets.classDefsOffset = buffer.readInt();
            dexOffsets.stringDataItemsOffset = buffer.readInt();
            dexOffsets.typeListsOffset = buffer.readInt();
            dexOffsets.annotationsOffset = buffer.readInt();
            dexOffsets.annotationSetsOffset = buffer.readInt();
            dexOffsets.annotationSetRefListsOffset = buffer.readInt();
            dexOffsets.annotationsDirectoriesOffset = buffer.readInt();
            dexOffsets.debugInfoItemsOffset = buffer.readInt();
            dexOffsets.codeItemsOffset = buffer.readInt();
            dexOffsets.classDataItemsOffset = buffer.readInt();
            dexOffsets.encodedArraysOffset = buffer.readInt();
            dexOffsets.mapListOffset = buffer.readInt();
            dexOffsets.dexSize = buffer.readInt();
            this.oldDexSignToOffsetInfoMap.put(oldDexSign, dexOffsets);
        }
        readDataChunk(buffer, this.oldDexSignToStringIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToTypeIdIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToTypeListIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToProtoIdIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToFieldIdIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToMethodIdIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToAnnotationIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToAnnotationSetIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToAnnotationSetRefListIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToAnnotationsDirectoryIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToEncodedArrayIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToDebugInfoIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToCodeIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToClassDataIndicesInSmallPatch);
        readDataChunk(buffer, this.oldDexSignToClassDefIndicesInSmallPatch);
    }

    private void readDataChunk(DexDataBuffer buffer, Map<String, BitSet>
            oldDexSignToIndicesInSmallPatchMap) {
        int oldDexSignCount = this.oldDexSigns.size();
        for (int i = 0; i < oldDexSignCount; i++) {
            int itemCount = buffer.readUleb128();
            int prevIndex = 0;
            for (int j = 0; j < itemCount; j++) {
                prevIndex += buffer.readSleb128();
                String oldDexSign = (String) this.oldDexSigns.get(i);
                BitSet indices = (BitSet) oldDexSignToIndicesInSmallPatchMap.get(oldDexSign);
                if (indices == null) {
                    indices = new BitSet();
                    oldDexSignToIndicesInSmallPatchMap.put(oldDexSign, indices);
                }
                indices.set(prevIndex);
            }
        }
    }

    public boolean isAffectedOldDex(String oldDexSign) {
        return this.oldDexSigns.contains(oldDexSign);
    }

    public boolean isSmallPatchedDexEmpty(String oldDexSign) {
        BitSet indices = (BitSet) this.oldDexSignToClassDefIndicesInSmallPatch.get(oldDexSign);
        return indices == null || indices.isEmpty();
    }

    public int getPatchedStringIdOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.stringIdsOffset : -1;
    }

    public int getPatchedTypeIdOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.typeIdsOffset : -1;
    }

    public int getPatchedProtoIdOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.protoIdsOffset : -1;
    }

    public int getPatchedFieldIdOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.fieldIdsOffset : -1;
    }

    public int getPatchedMethodIdOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.methodIdsOffset : -1;
    }

    public int getPatchedClassDefOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.classDefsOffset : -1;
    }

    public int getPatchedMapListOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.mapListOffset : -1;
    }

    public int getPatchedTypeListOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.typeListsOffset : -1;
    }

    public int getPatchedAnnotationSetRefListOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.annotationSetRefListsOffset : -1;
    }

    public int getPatchedAnnotationSetOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.annotationSetsOffset : -1;
    }

    public int getPatchedClassDataOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.classDataItemsOffset : -1;
    }

    public int getPatchedCodeOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.codeItemsOffset : -1;
    }

    public int getPatchedStringDataOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.stringDataItemsOffset : -1;
    }

    public int getPatchedDebugInfoOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.debugInfoItemsOffset : -1;
    }

    public int getPatchedAnnotationOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.annotationsOffset : -1;
    }

    public int getPatchedEncodedArrayOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.encodedArraysOffset : -1;
    }

    public int getPatchedAnnotationsDirectoryOffsetByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.annotationsDirectoriesOffset : -1;
    }

    public int getPatchedDexSizeByOldDexSign(String oldDexSign) {
        DexOffsets dexOffsets = (DexOffsets) this.oldDexSignToOffsetInfoMap.get(oldDexSign);
        return dexOffsets != null ? dexOffsets.dexSize : -1;
    }

    public boolean isStringInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToStringIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isTypeIdInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToTypeIdIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isTypeListInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToTypeListIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isProtoIdInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToProtoIdIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isFieldIdInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToFieldIdIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isMethodIdInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToMethodIdIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isAnnotationInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToAnnotationIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isAnnotationSetInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToAnnotationSetIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isAnnotationSetRefListInSmallPatchedDex(String oldDexSign, int
            indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToAnnotationSetRefListIndicesInSmallPatch.get
                (oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isAnnotationsDirectoryInSmallPatchedDex(String oldDexSign, int
            indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToAnnotationsDirectoryIndicesInSmallPatch.get
                (oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isEncodedArrayInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToEncodedArrayIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isDebugInfoInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToDebugInfoIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isCodeInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToCodeIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isClassDataInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToClassDataIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }

    public boolean isClassDefInSmallPatchedDex(String oldDexSign, int indexInPatchedDex) {
        BitSet indices = (BitSet) this.oldDexSignToClassDefIndicesInSmallPatch.get(oldDexSign);
        if (indices == null) {
            return false;
        }
        return indices.get(indexInPatchedDex);
    }
}
