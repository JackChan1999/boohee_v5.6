package com.tencent.tinker.commons.dexpatcher;

import com.tencent.tinker.android.dex.Annotation;
import com.tencent.tinker.android.dex.AnnotationSet;
import com.tencent.tinker.android.dex.AnnotationSetRefList;
import com.tencent.tinker.android.dex.AnnotationsDirectory;
import com.tencent.tinker.android.dex.ClassData;
import com.tencent.tinker.android.dex.ClassDef;
import com.tencent.tinker.android.dex.Code;
import com.tencent.tinker.android.dex.DebugInfoItem;
import com.tencent.tinker.android.dex.Dex;
import com.tencent.tinker.android.dex.EncodedValue;
import com.tencent.tinker.android.dex.FieldId;
import com.tencent.tinker.android.dex.MethodId;
import com.tencent.tinker.android.dex.ProtoId;
import com.tencent.tinker.android.dex.StringData;
import com.tencent.tinker.android.dex.TableOfContents;
import com.tencent.tinker.android.dex.TypeList;
import com.tencent.tinker.android.dex.util.CompareUtils;
import com.tencent.tinker.android.dx.util.Hex;
import com.tencent.tinker.android.dx.util.IndexMap;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.AnnotationSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch
        .AnnotationSetRefListSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.AnnotationSetSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch
        .AnnotationsDirectorySectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.ClassDataSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.ClassDefSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.CodeSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DebugInfoItemSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.DexSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.FieldIdSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.MethodIdSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.ProtoIdSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.StaticValueSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.StringDataSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.TypeIdSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.algorithms.patch.TypeListSectionPatchAlgorithm;
import com.tencent.tinker.commons.dexpatcher.struct.DexPatchFile;
import com.tencent.tinker.commons.dexpatcher.struct.SmallPatchedDexItemFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class DexPatchApplier {
    private       DexSectionPatchAlgorithm<Annotation>           annotationSectionPatchAlg;
    private       DexSectionPatchAlgorithm<AnnotationSetRefList>
            annotationSetRefListSectionPatchAlg;
    private       DexSectionPatchAlgorithm<AnnotationSet>        annotationSetSectionPatchAlg;
    private       DexSectionPatchAlgorithm<AnnotationsDirectory>
            annotationsDirectorySectionPatchAlg;
    private       DexSectionPatchAlgorithm<ClassData>            classDataSectionPatchAlg;
    private       DexSectionPatchAlgorithm<ClassDef>             classDefSectionPatchAlg;
    private       DexSectionPatchAlgorithm<Code>                 codeSectionPatchAlg;
    private       DexSectionPatchAlgorithm<DebugInfoItem>        debugInfoSectionPatchAlg;
    private       DexSectionPatchAlgorithm<EncodedValue>         encodedArraySectionPatchAlg;
    private final SmallPatchedDexItemFile                        extraInfoFile;
    private       DexSectionPatchAlgorithm<FieldId>              fieldIdSectionPatchAlg;
    private       DexSectionPatchAlgorithm<MethodId>             methodIdSectionPatchAlg;
    private final Dex                                            oldDex;
    private final String                                         oldDexSignStr;
    private final IndexMap                                       oldToFullPatchedIndexMap;
    private final DexPatchFile                                   patchFile;
    private final Dex                                            patchedDex;
    private final IndexMap                                       patchedToSmallPatchedIndexMap;
    private       DexSectionPatchAlgorithm<ProtoId>              protoIdSectionPatchAlg;
    private       DexSectionPatchAlgorithm<StringData>           stringDataSectionPatchAlg;
    private       DexSectionPatchAlgorithm<Integer>              typeIdSectionPatchAlg;
    private       DexSectionPatchAlgorithm<TypeList>             typeListSectionPatchAlg;

    public DexPatchApplier(File oldDexIn, File patchFileIn) throws IOException {
        this(new Dex(oldDexIn), patchFileIn != null ? new DexPatchFile(patchFileIn) : null, null);
    }

    public DexPatchApplier(InputStream oldDexIn, InputStream patchFileIn) throws IOException {
        this(new Dex(oldDexIn), patchFileIn != null ? new DexPatchFile(patchFileIn) : null, null);
    }

    public DexPatchApplier(InputStream oldDexIn, int initDexSize, InputStream patchFileIn) throws
            IOException {
        this(new Dex(oldDexIn, initDexSize), patchFileIn != null ? new DexPatchFile(patchFileIn)
                : null, null);
    }

    public DexPatchApplier(File oldDexIn, File patchFileIn, SmallPatchedDexItemFile
            extraInfoFile) throws IOException {
        this(new Dex(oldDexIn), patchFileIn != null ? new DexPatchFile(patchFileIn) : null,
                extraInfoFile);
    }

    public DexPatchApplier(InputStream oldDexIn, InputStream patchFileIn, SmallPatchedDexItemFile
            extraInfoFile) throws IOException {
        this(new Dex(oldDexIn), patchFileIn != null ? new DexPatchFile(patchFileIn) : null,
                extraInfoFile);
    }

    public DexPatchApplier(InputStream oldDexIn, int initDexSize, InputStream patchFileIn,
                           SmallPatchedDexItemFile extraInfoFile) throws IOException {
        this(new Dex(oldDexIn, initDexSize), patchFileIn != null ? new DexPatchFile(patchFileIn)
                : null, extraInfoFile);
    }

    public DexPatchApplier(Dex oldDexIn, DexPatchFile patchFileIn, SmallPatchedDexItemFile
            extraAddedDexElementsFile) {
        this.oldDex = oldDexIn;
        this.oldDexSignStr = Hex.toHexString(oldDexIn.computeSignature(false));
        this.patchFile = patchFileIn;
        if (extraAddedDexElementsFile == null) {
            this.patchedDex = new Dex(patchFileIn.getPatchedDexSize());
        } else {
            this.patchedDex = new Dex(extraAddedDexElementsFile.getPatchedDexSizeByOldDexSign
                    (this.oldDexSignStr));
        }
        this.oldToFullPatchedIndexMap = new IndexMap();
        this.patchedToSmallPatchedIndexMap = extraAddedDexElementsFile != null ? new IndexMap() :
                null;
        this.extraInfoFile = extraAddedDexElementsFile;
        if (patchFileIn != null) {
            return;
        }
        if (extraAddedDexElementsFile == null || !extraAddedDexElementsFile.isAffectedOldDex(this
                .oldDexSignStr)) {
            throw new UnsupportedOperationException("patchFileIn is null, and " +
                    "extraAddedDexElementFile(smallPatchInfo) is null or does not mention " +
                    "oldDexIn.");
        }
    }

    public void executeAndSaveTo(OutputStream out) throws IOException {
        byte[] oldDexSign = this.oldDex.computeSignature(false);
        if (oldDexSign == null) {
            throw new IOException("failed to compute old dex's signature.");
        }
        if (this.patchFile != null) {
            if (CompareUtils.uArrCompare(oldDexSign, this.patchFile.getOldDexSignature()) != 0) {
                throw new IOException(String.format("old dex signature mismatch! expected: %s, " +
                        "actual: %s", new Object[]{Arrays.toString(oldDexSign), Arrays.toString
                        (this.patchFile.getOldDexSignature())}));
            }
        }
        String oldDexSignStr = Hex.toHexString(oldDexSign);
        TableOfContents patchedToc = this.patchedDex.getTableOfContents();
        patchedToc.header.off = 0;
        patchedToc.header.size = 1;
        patchedToc.mapList.size = 1;
        if (this.extraInfoFile == null || !this.extraInfoFile.isAffectedOldDex(this
                .oldDexSignStr)) {
            patchedToc.stringIds.off = this.patchFile.getPatchedStringIdSectionOffset();
            patchedToc.typeIds.off = this.patchFile.getPatchedTypeIdSectionOffset();
            patchedToc.typeLists.off = this.patchFile.getPatchedTypeListSectionOffset();
            patchedToc.protoIds.off = this.patchFile.getPatchedProtoIdSectionOffset();
            patchedToc.fieldIds.off = this.patchFile.getPatchedFieldIdSectionOffset();
            patchedToc.methodIds.off = this.patchFile.getPatchedMethodIdSectionOffset();
            patchedToc.classDefs.off = this.patchFile.getPatchedClassDefSectionOffset();
            patchedToc.mapList.off = this.patchFile.getPatchedMapListSectionOffset();
            patchedToc.stringDatas.off = this.patchFile.getPatchedStringDataSectionOffset();
            patchedToc.annotations.off = this.patchFile.getPatchedAnnotationSectionOffset();
            patchedToc.annotationSets.off = this.patchFile.getPatchedAnnotationSetSectionOffset();
            patchedToc.annotationSetRefLists.off = this.patchFile
                    .getPatchedAnnotationSetRefListSectionOffset();
            patchedToc.annotationsDirectories.off = this.patchFile
                    .getPatchedAnnotationsDirectorySectionOffset();
            patchedToc.encodedArrays.off = this.patchFile.getPatchedEncodedArraySectionOffset();
            patchedToc.debugInfos.off = this.patchFile.getPatchedDebugInfoSectionOffset();
            patchedToc.codes.off = this.patchFile.getPatchedCodeSectionOffset();
            patchedToc.classDatas.off = this.patchFile.getPatchedClassDataSectionOffset();
            patchedToc.fileSize = this.patchFile.getPatchedDexSize();
        } else {
            patchedToc.stringIds.off = this.extraInfoFile.getPatchedStringIdOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.typeIds.off = this.extraInfoFile.getPatchedTypeIdOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.typeLists.off = this.extraInfoFile.getPatchedTypeListOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.protoIds.off = this.extraInfoFile.getPatchedProtoIdOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.fieldIds.off = this.extraInfoFile.getPatchedFieldIdOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.methodIds.off = this.extraInfoFile.getPatchedMethodIdOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.classDefs.off = this.extraInfoFile.getPatchedClassDefOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.mapList.off = this.extraInfoFile.getPatchedMapListOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.stringDatas.off = this.extraInfoFile
                    .getPatchedStringDataOffsetByOldDexSign(oldDexSignStr);
            patchedToc.annotations.off = this.extraInfoFile
                    .getPatchedAnnotationOffsetByOldDexSign(oldDexSignStr);
            patchedToc.annotationSets.off = this.extraInfoFile
                    .getPatchedAnnotationSetOffsetByOldDexSign(oldDexSignStr);
            patchedToc.annotationSetRefLists.off = this.extraInfoFile
                    .getPatchedAnnotationSetRefListOffsetByOldDexSign(oldDexSignStr);
            patchedToc.annotationsDirectories.off = this.extraInfoFile
                    .getPatchedAnnotationsDirectoryOffsetByOldDexSign(oldDexSignStr);
            patchedToc.encodedArrays.off = this.extraInfoFile
                    .getPatchedEncodedArrayOffsetByOldDexSign(oldDexSignStr);
            patchedToc.debugInfos.off = this.extraInfoFile.getPatchedDebugInfoOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.codes.off = this.extraInfoFile.getPatchedCodeOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.classDatas.off = this.extraInfoFile.getPatchedClassDataOffsetByOldDexSign
                    (oldDexSignStr);
            patchedToc.fileSize = this.extraInfoFile.getPatchedDexSizeByOldDexSign(oldDexSignStr);
        }
        Arrays.sort(patchedToc.sections);
        patchedToc.computeSizesFromOffsets();
        this.stringDataSectionPatchAlg = new StringDataSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.typeIdSectionPatchAlg = new TypeIdSectionPatchAlgorithm(this.patchFile, this.oldDex,
                this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.protoIdSectionPatchAlg = new ProtoIdSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.fieldIdSectionPatchAlg = new FieldIdSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.methodIdSectionPatchAlg = new MethodIdSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.classDefSectionPatchAlg = new ClassDefSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.typeListSectionPatchAlg = new TypeListSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.annotationSetRefListSectionPatchAlg = new AnnotationSetRefListSectionPatchAlgorithm
                (this.patchFile, this.oldDex, this.patchedDex, this.oldToFullPatchedIndexMap,
                        this.patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.annotationSetSectionPatchAlg = new AnnotationSetSectionPatchAlgorithm(this
                .patchFile, this.oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.classDataSectionPatchAlg = new ClassDataSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.codeSectionPatchAlg = new CodeSectionPatchAlgorithm(this.patchFile, this.oldDex,
                this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.debugInfoSectionPatchAlg = new DebugInfoItemSectionPatchAlgorithm(this.patchFile,
                this.oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.annotationSectionPatchAlg = new AnnotationSectionPatchAlgorithm(this.patchFile, this
                .oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.encodedArraySectionPatchAlg = new StaticValueSectionPatchAlgorithm(this.patchFile,
                this.oldDex, this.patchedDex, this.oldToFullPatchedIndexMap, this
                .patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.annotationsDirectorySectionPatchAlg = new AnnotationsDirectorySectionPatchAlgorithm
                (this.patchFile, this.oldDex, this.patchedDex, this.oldToFullPatchedIndexMap,
                        this.patchedToSmallPatchedIndexMap, this.extraInfoFile);
        this.stringDataSectionPatchAlg.execute();
        this.typeIdSectionPatchAlg.execute();
        this.typeListSectionPatchAlg.execute();
        this.protoIdSectionPatchAlg.execute();
        this.fieldIdSectionPatchAlg.execute();
        this.methodIdSectionPatchAlg.execute();
        Runtime.getRuntime().gc();
        this.annotationSectionPatchAlg.execute();
        this.annotationSetSectionPatchAlg.execute();
        this.annotationSetRefListSectionPatchAlg.execute();
        this.annotationsDirectorySectionPatchAlg.execute();
        Runtime.getRuntime().gc();
        this.debugInfoSectionPatchAlg.execute();
        this.codeSectionPatchAlg.execute();
        Runtime.getRuntime().gc();
        this.classDataSectionPatchAlg.execute();
        this.encodedArraySectionPatchAlg.execute();
        this.classDefSectionPatchAlg.execute();
        Runtime.getRuntime().gc();
        patchedToc.writeHeader(this.patchedDex.openSection(patchedToc.header.off));
        patchedToc.writeMap(this.patchedDex.openSection(patchedToc.mapList.off));
        this.patchedDex.writeHashes();
        this.patchedDex.writeTo(out);
    }

    public void executeAndSaveTo(File file) throws IOException {
        Throwable th;
        OutputStream os = null;
        try {
            OutputStream os2 = new BufferedOutputStream(new FileOutputStream(file));
            try {
                executeAndSaveTo(os2);
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                os = os2;
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (os != null) {
                os.close();
            }
            throw th;
        }
    }
}
