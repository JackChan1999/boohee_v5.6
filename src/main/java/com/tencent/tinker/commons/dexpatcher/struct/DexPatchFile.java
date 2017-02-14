package com.tencent.tinker.commons.dexpatcher.struct;

import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dex.util.CompareUtils;
import com.tencent.tinker.android.dex.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class DexPatchFile {
    public static final short  CURRENT_VERSION = (short) 2;
    public static final byte[] MAGIC           = new byte[]{(byte) 68, (byte) 88, (byte) 68,
            (byte) 73, (byte) 70, (byte) 70};
    private final DexDataBuffer buffer;
    private       int           firstChunkOffset;
    private       byte[]        oldDexSignature;
    private       int           patchedAnnotationSectionOffset;
    private       int           patchedAnnotationSetRefListSectionOffset;
    private       int           patchedAnnotationSetSectionOffset;
    private       int           patchedAnnotationsDirectorySectionOffset;
    private       int           patchedClassDataSectionOffset;
    private       int           patchedClassDefSectionOffset;
    private       int           patchedCodeSectionOffset;
    private       int           patchedDebugInfoSectionOffset;
    private       int           patchedDexSize;
    private       int           patchedEncodedArraySectionOffset;
    private       int           patchedFieldIdSectionOffset;
    private       int           patchedMapListSectionOffset;
    private       int           patchedMethodIdSectionOffset;
    private       int           patchedProtoIdSectionOffset;
    private       int           patchedStringDataSectionOffset;
    private       int           patchedStringIdSectionOffset;
    private       int           patchedTypeIdSectionOffset;
    private       int           patchedTypeListSectionOffset;
    private       short         version;

    public DexPatchFile(File file) throws IOException {
        this.buffer = new DexDataBuffer(ByteBuffer.wrap(FileUtils.readFile(file)));
        init();
    }

    public DexPatchFile(InputStream is) throws IOException {
        this.buffer = new DexDataBuffer(ByteBuffer.wrap(FileUtils.readStream(is)));
        init();
    }

    private void init() {
        byte[] magic = this.buffer.readByteArray(MAGIC.length);
        if (CompareUtils.uArrCompare(magic, MAGIC) != 0) {
            throw new IllegalStateException("bad dex patch file magic: " + Arrays.toString(magic));
        }
        this.version = this.buffer.readShort();
        if (CompareUtils.uCompare(this.version, (short) 2) != 0) {
            throw new IllegalStateException("bad dex patch file version: " + this.version + ", " +
                    "expected: " + 2);
        }
        this.patchedDexSize = this.buffer.readInt();
        this.firstChunkOffset = this.buffer.readInt();
        this.patchedStringIdSectionOffset = this.buffer.readInt();
        this.patchedTypeIdSectionOffset = this.buffer.readInt();
        this.patchedProtoIdSectionOffset = this.buffer.readInt();
        this.patchedFieldIdSectionOffset = this.buffer.readInt();
        this.patchedMethodIdSectionOffset = this.buffer.readInt();
        this.patchedClassDefSectionOffset = this.buffer.readInt();
        this.patchedMapListSectionOffset = this.buffer.readInt();
        this.patchedTypeListSectionOffset = this.buffer.readInt();
        this.patchedAnnotationSetRefListSectionOffset = this.buffer.readInt();
        this.patchedAnnotationSetSectionOffset = this.buffer.readInt();
        this.patchedClassDataSectionOffset = this.buffer.readInt();
        this.patchedCodeSectionOffset = this.buffer.readInt();
        this.patchedStringDataSectionOffset = this.buffer.readInt();
        this.patchedDebugInfoSectionOffset = this.buffer.readInt();
        this.patchedAnnotationSectionOffset = this.buffer.readInt();
        this.patchedEncodedArraySectionOffset = this.buffer.readInt();
        this.patchedAnnotationsDirectorySectionOffset = this.buffer.readInt();
        this.oldDexSignature = this.buffer.readByteArray(20);
        this.buffer.position(this.firstChunkOffset);
    }

    public short getVersion() {
        return this.version;
    }

    public byte[] getOldDexSignature() {
        return this.oldDexSignature;
    }

    public int getPatchedDexSize() {
        return this.patchedDexSize;
    }

    public int getPatchedStringIdSectionOffset() {
        return this.patchedStringIdSectionOffset;
    }

    public int getPatchedTypeIdSectionOffset() {
        return this.patchedTypeIdSectionOffset;
    }

    public int getPatchedProtoIdSectionOffset() {
        return this.patchedProtoIdSectionOffset;
    }

    public int getPatchedFieldIdSectionOffset() {
        return this.patchedFieldIdSectionOffset;
    }

    public int getPatchedMethodIdSectionOffset() {
        return this.patchedMethodIdSectionOffset;
    }

    public int getPatchedClassDefSectionOffset() {
        return this.patchedClassDefSectionOffset;
    }

    public int getPatchedMapListSectionOffset() {
        return this.patchedMapListSectionOffset;
    }

    public int getPatchedTypeListSectionOffset() {
        return this.patchedTypeListSectionOffset;
    }

    public int getPatchedAnnotationSetRefListSectionOffset() {
        return this.patchedAnnotationSetRefListSectionOffset;
    }

    public int getPatchedAnnotationSetSectionOffset() {
        return this.patchedAnnotationSetSectionOffset;
    }

    public int getPatchedClassDataSectionOffset() {
        return this.patchedClassDataSectionOffset;
    }

    public int getPatchedCodeSectionOffset() {
        return this.patchedCodeSectionOffset;
    }

    public int getPatchedStringDataSectionOffset() {
        return this.patchedStringDataSectionOffset;
    }

    public int getPatchedDebugInfoSectionOffset() {
        return this.patchedDebugInfoSectionOffset;
    }

    public int getPatchedAnnotationSectionOffset() {
        return this.patchedAnnotationSectionOffset;
    }

    public int getPatchedEncodedArraySectionOffset() {
        return this.patchedEncodedArraySectionOffset;
    }

    public int getPatchedAnnotationsDirectorySectionOffset() {
        return this.patchedAnnotationsDirectorySectionOffset;
    }

    public DexDataBuffer getBuffer() {
        return this.buffer;
    }
}
