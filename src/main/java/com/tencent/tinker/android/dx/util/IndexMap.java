package com.tencent.tinker.android.dx.util;

import com.tencent.tinker.android.dex.Annotation;
import com.tencent.tinker.android.dex.AnnotationSet;
import com.tencent.tinker.android.dex.AnnotationSetRefList;
import com.tencent.tinker.android.dex.AnnotationsDirectory;
import com.tencent.tinker.android.dex.ClassData;
import com.tencent.tinker.android.dex.ClassData.Field;
import com.tencent.tinker.android.dex.ClassData.Method;
import com.tencent.tinker.android.dex.ClassDef;
import com.tencent.tinker.android.dex.Code;
import com.tencent.tinker.android.dex.Code.CatchHandler;
import com.tencent.tinker.android.dex.DebugInfoItem;
import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dex.EncodedValue;
import com.tencent.tinker.android.dex.EncodedValueCodec;
import com.tencent.tinker.android.dex.EncodedValueReader;
import com.tencent.tinker.android.dex.FieldId;
import com.tencent.tinker.android.dex.Leb128;
import com.tencent.tinker.android.dex.MethodId;
import com.tencent.tinker.android.dex.ProtoId;
import com.tencent.tinker.android.dex.TypeList;
import com.tencent.tinker.android.dex.util.ByteInput;
import com.tencent.tinker.android.dex.util.ByteOutput;
import com.tencent.tinker.android.utils.SparseIntArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.BitSet;

public class IndexMap {
    private final SparseIntArray annotationOffsetsMap               = new SparseIntArray();
    private final SparseIntArray annotationSetOffsetsMap            = new SparseIntArray();
    private final SparseIntArray annotationSetRefListOffsetsMap     = new SparseIntArray();
    private final SparseIntArray annotationsDirectoryOffsetsMap     = new SparseIntArray();
    private final SparseIntArray classDataOffsetsMap                = new SparseIntArray();
    private final SparseIntArray codeOffsetsMap                     = new SparseIntArray();
    private final SparseIntArray debugInfoItemOffsetsMap            = new SparseIntArray();
    private final BitSet         deletedAnnotationOffsets           = new BitSet();
    private final BitSet         deletedAnnotationSetOffsets        = new BitSet();
    private final BitSet         deletedAnnotationSetRefListOffsets = new BitSet();
    private final BitSet         deletedAnnotationsDirectoryOffsets = new BitSet();
    private final BitSet         deletedClassDataOffsets            = new BitSet();
    private final BitSet         deletedCodeOffsets                 = new BitSet();
    private final BitSet         deletedDebugInfoItemOffsets        = new BitSet();
    private final BitSet         deletedFieldIds                    = new BitSet();
    private final BitSet         deletedMethodIds                   = new BitSet();
    private final BitSet         deletedProtoIds                    = new BitSet();
    private final BitSet         deletedStaticValuesOffsets         = new BitSet();
    private final BitSet         deletedStringIds                   = new BitSet();
    private final BitSet         deletedTypeIds                     = new BitSet();
    private final BitSet         deletedTypeListOffsets             = new BitSet();
    private final SparseIntArray fieldIdsMap                        = new SparseIntArray();
    private final SparseIntArray methodIdsMap                       = new SparseIntArray();
    private final SparseIntArray protoIdsMap                        = new SparseIntArray();
    private final SparseIntArray staticValuesOffsetsMap             = new SparseIntArray();
    private final SparseIntArray stringIdsMap                       = new SparseIntArray();
    private final SparseIntArray typeIdsMap                         = new SparseIntArray();
    private final SparseIntArray typeListOffsetsMap                 = new SparseIntArray();

    private final class EncodedValueTransformer {
        private final ByteOutput out;

        EncodedValueTransformer(ByteOutput out) {
            this.out = out;
        }

        public void transform(EncodedValueReader reader) {
            int i = 0;
            switch (reader.peek()) {
                case 0:
                    EncodedValueCodec.writeSignedIntegralValue(this.out, 0, (long) reader
                            .readByte());
                    return;
                case 2:
                    EncodedValueCodec.writeSignedIntegralValue(this.out, 2, (long) reader
                            .readShort());
                    return;
                case 3:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 3, (long) reader
                            .readChar());
                    return;
                case 4:
                    EncodedValueCodec.writeSignedIntegralValue(this.out, 4, (long) reader.readInt
                            ());
                    return;
                case 6:
                    EncodedValueCodec.writeSignedIntegralValue(this.out, 6, reader.readLong());
                    return;
                case 16:
                    EncodedValueCodec.writeRightZeroExtendedValue(this.out, 16, ((long) Float
                            .floatToIntBits(reader.readFloat())) << 32);
                    return;
                case 17:
                    EncodedValueCodec.writeRightZeroExtendedValue(this.out, 17, Double
                            .doubleToLongBits(reader.readDouble()));
                    return;
                case 23:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 23, (long) IndexMap
                            .this.adjustStringIndex(reader.readString()));
                    return;
                case 24:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 24, (long) IndexMap
                            .this.adjustTypeIdIndex(reader.readType()));
                    return;
                case 25:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 25, (long) IndexMap
                            .this.adjustFieldIdIndex(reader.readField()));
                    return;
                case 26:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 26, (long) IndexMap
                            .this.adjustMethodIdIndex(reader.readMethod()));
                    return;
                case 27:
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, 27, (long) IndexMap
                            .this.adjustFieldIdIndex(reader.readEnum()));
                    return;
                case 28:
                    writeTypeAndArg(28, 0);
                    transformArray(reader);
                    return;
                case 29:
                    writeTypeAndArg(29, 0);
                    transformAnnotation(reader);
                    return;
                case 30:
                    reader.readNull();
                    writeTypeAndArg(30, 0);
                    return;
                case 31:
                    if (reader.readBoolean()) {
                        i = 1;
                    }
                    writeTypeAndArg(31, i);
                    return;
                default:
                    throw new DexException("Unexpected type: " + Integer.toHexString(reader.peek
                            ()));
            }
        }

        private void transformAnnotation(EncodedValueReader reader) {
            int fieldCount = reader.readAnnotation();
            Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustTypeIdIndex(reader
                    .getAnnotationType()));
            Leb128.writeUnsignedLeb128(this.out, fieldCount);
            for (int i = 0; i < fieldCount; i++) {
                Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustStringIndex(reader
                        .readAnnotationName()));
                transform(reader);
            }
        }

        private void transformArray(EncodedValueReader reader) {
            int size = reader.readArray();
            Leb128.writeUnsignedLeb128(this.out, size);
            for (int i = 0; i < size; i++) {
                transform(reader);
            }
        }

        private void writeTypeAndArg(int type, int arg) {
            this.out.writeByte((arg << 5) | type);
        }
    }

    public void mapStringIds(int oldIndex, int newIndex) {
        this.stringIdsMap.put(oldIndex, newIndex);
    }

    public void markStringIdDeleted(int index) {
        if (index >= 0) {
            this.deletedStringIds.set(index);
        }
    }

    public void mapTypeIds(int oldIndex, int newIndex) {
        this.typeIdsMap.put(oldIndex, newIndex);
    }

    public void markTypeIdDeleted(int index) {
        if (index >= 0) {
            this.deletedTypeIds.set(index);
        }
    }

    public void mapProtoIds(int oldIndex, int newIndex) {
        this.protoIdsMap.put(oldIndex, newIndex);
    }

    public void markProtoIdDeleted(int index) {
        if (index >= 0) {
            this.deletedProtoIds.set(index);
        }
    }

    public void mapFieldIds(int oldIndex, int newIndex) {
        this.fieldIdsMap.put(oldIndex, newIndex);
    }

    public void markFieldIdDeleted(int index) {
        if (index >= 0) {
            this.deletedFieldIds.set(index);
        }
    }

    public void mapMethodIds(int oldIndex, int newIndex) {
        this.methodIdsMap.put(oldIndex, newIndex);
    }

    public void markMethodIdDeleted(int index) {
        if (index >= 0) {
            this.deletedMethodIds.set(index);
        }
    }

    public void mapTypeListOffset(int oldOffset, int newOffset) {
        this.typeListOffsetsMap.put(oldOffset, newOffset);
    }

    public void markTypeListDeleted(int offset) {
        if (offset >= 0) {
            this.deletedTypeListOffsets.set(offset);
        }
    }

    public void mapAnnotationOffset(int oldOffset, int newOffset) {
        this.annotationOffsetsMap.put(oldOffset, newOffset);
    }

    public void markAnnotationDeleted(int offset) {
        if (offset >= 0) {
            this.deletedAnnotationOffsets.set(offset);
        }
    }

    public void mapAnnotationSetOffset(int oldOffset, int newOffset) {
        this.annotationSetOffsetsMap.put(oldOffset, newOffset);
    }

    public void markAnnotationSetDeleted(int offset) {
        if (offset >= 0) {
            this.deletedAnnotationSetOffsets.set(offset);
        }
    }

    public void mapAnnotationSetRefListOffset(int oldOffset, int newOffset) {
        this.annotationSetRefListOffsetsMap.put(oldOffset, newOffset);
    }

    public void markAnnotationSetRefListDeleted(int offset) {
        if (offset >= 0) {
            this.deletedAnnotationSetRefListOffsets.set(offset);
        }
    }

    public void mapAnnotationsDirectoryOffset(int oldOffset, int newOffset) {
        this.annotationsDirectoryOffsetsMap.put(oldOffset, newOffset);
    }

    public void markAnnotationsDirectoryDeleted(int offset) {
        if (offset >= 0) {
            this.deletedAnnotationsDirectoryOffsets.set(offset);
        }
    }

    public void mapStaticValuesOffset(int oldOffset, int newOffset) {
        this.staticValuesOffsetsMap.put(oldOffset, newOffset);
    }

    public void markStaticValuesDeleted(int offset) {
        if (offset >= 0) {
            this.deletedStaticValuesOffsets.set(offset);
        }
    }

    public void mapClassDataOffset(int oldOffset, int newOffset) {
        this.classDataOffsetsMap.put(oldOffset, newOffset);
    }

    public void markClassDataDeleted(int offset) {
        if (offset >= 0) {
            this.deletedClassDataOffsets.set(offset);
        }
    }

    public void mapDebugInfoItemOffset(int oldOffset, int newOffset) {
        this.debugInfoItemOffsetsMap.put(oldOffset, newOffset);
    }

    public void markDebugInfoItemDeleted(int offset) {
        if (offset >= 0) {
            this.deletedDebugInfoItemOffsets.set(offset);
        }
    }

    public void mapCodeOffset(int oldOffset, int newOffset) {
        this.codeOffsetsMap.put(oldOffset, newOffset);
    }

    public void markCodeDeleted(int offset) {
        if (offset >= 0) {
            this.deletedCodeOffsets.set(offset);
        }
    }

    public int adjustStringIndex(int stringIndex) {
        int index = this.stringIdsMap.indexOfKey(stringIndex);
        if (index >= 0) {
            return this.stringIdsMap.valueAt(index);
        }
        if (stringIndex < 0 || !this.deletedStringIds.get(stringIndex)) {
            return stringIndex;
        }
        return -1;
    }

    public int adjustTypeIdIndex(int typeIdIndex) {
        int index = this.typeIdsMap.indexOfKey(typeIdIndex);
        if (index >= 0) {
            return this.typeIdsMap.valueAt(index);
        }
        if (typeIdIndex < 0 || !this.deletedTypeIds.get(typeIdIndex)) {
            return typeIdIndex;
        }
        return -1;
    }

    public int adjustProtoIdIndex(int protoIndex) {
        int index = this.protoIdsMap.indexOfKey(protoIndex);
        if (index >= 0) {
            return this.protoIdsMap.valueAt(index);
        }
        if (protoIndex < 0 || !this.deletedProtoIds.get(protoIndex)) {
            return protoIndex;
        }
        return -1;
    }

    public int adjustFieldIdIndex(int fieldIndex) {
        int index = this.fieldIdsMap.indexOfKey(fieldIndex);
        if (index >= 0) {
            return this.fieldIdsMap.valueAt(index);
        }
        if (fieldIndex < 0 || !this.deletedFieldIds.get(fieldIndex)) {
            return fieldIndex;
        }
        return -1;
    }

    public int adjustMethodIdIndex(int methodIndex) {
        int index = this.methodIdsMap.indexOfKey(methodIndex);
        if (index >= 0) {
            return this.methodIdsMap.valueAt(index);
        }
        if (methodIndex < 0 || !this.deletedMethodIds.get(methodIndex)) {
            return methodIndex;
        }
        return -1;
    }

    public int adjustTypeListOffset(int typeListOffset) {
        int index = this.typeListOffsetsMap.indexOfKey(typeListOffset);
        if (index >= 0) {
            return this.typeListOffsetsMap.valueAt(index);
        }
        if (typeListOffset < 0 || !this.deletedTypeListOffsets.get(typeListOffset)) {
            return typeListOffset;
        }
        return -1;
    }

    public int adjustAnnotationOffset(int annotationOffset) {
        int index = this.annotationOffsetsMap.indexOfKey(annotationOffset);
        if (index >= 0) {
            return this.annotationOffsetsMap.valueAt(index);
        }
        if (annotationOffset < 0 || !this.deletedAnnotationOffsets.get(annotationOffset)) {
            return annotationOffset;
        }
        return -1;
    }

    public int adjustAnnotationSetOffset(int annotationSetOffset) {
        int index = this.annotationSetOffsetsMap.indexOfKey(annotationSetOffset);
        if (index >= 0) {
            return this.annotationSetOffsetsMap.valueAt(index);
        }
        if (annotationSetOffset < 0 || !this.deletedAnnotationSetOffsets.get(annotationSetOffset)) {
            return annotationSetOffset;
        }
        return -1;
    }

    public int adjustAnnotationSetRefListOffset(int annotationSetRefListOffset) {
        int index = this.annotationSetRefListOffsetsMap.indexOfKey(annotationSetRefListOffset);
        if (index >= 0) {
            return this.annotationSetRefListOffsetsMap.valueAt(index);
        }
        if (annotationSetRefListOffset < 0 || !this.deletedAnnotationSetRefListOffsets.get
                (annotationSetRefListOffset)) {
            return annotationSetRefListOffset;
        }
        return -1;
    }

    public int adjustAnnotationsDirectoryOffset(int annotationsDirectoryOffset) {
        int index = this.annotationsDirectoryOffsetsMap.indexOfKey(annotationsDirectoryOffset);
        if (index >= 0) {
            return this.annotationsDirectoryOffsetsMap.valueAt(index);
        }
        if (annotationsDirectoryOffset < 0 || !this.deletedAnnotationsDirectoryOffsets.get
                (annotationsDirectoryOffset)) {
            return annotationsDirectoryOffset;
        }
        return -1;
    }

    public int adjustStaticValuesOffset(int staticValuesOffset) {
        int index = this.staticValuesOffsetsMap.indexOfKey(staticValuesOffset);
        if (index >= 0) {
            return this.staticValuesOffsetsMap.valueAt(index);
        }
        if (staticValuesOffset < 0 || !this.deletedStaticValuesOffsets.get(staticValuesOffset)) {
            return staticValuesOffset;
        }
        return -1;
    }

    public int adjustClassDataOffset(int classDataOffset) {
        int index = this.classDataOffsetsMap.indexOfKey(classDataOffset);
        if (index >= 0) {
            return this.classDataOffsetsMap.valueAt(index);
        }
        if (classDataOffset < 0 || !this.deletedClassDataOffsets.get(classDataOffset)) {
            return classDataOffset;
        }
        return -1;
    }

    public int adjustDebugInfoItemOffset(int debugInfoItemOffset) {
        int index = this.debugInfoItemOffsetsMap.indexOfKey(debugInfoItemOffset);
        if (index >= 0) {
            return this.debugInfoItemOffsetsMap.valueAt(index);
        }
        if (debugInfoItemOffset < 0 || !this.deletedDebugInfoItemOffsets.get(debugInfoItemOffset)) {
            return debugInfoItemOffset;
        }
        return -1;
    }

    public int adjustCodeOffset(int codeOffset) {
        int index = this.codeOffsetsMap.indexOfKey(codeOffset);
        if (index >= 0) {
            return this.codeOffsetsMap.valueAt(index);
        }
        if (codeOffset < 0 || !this.deletedCodeOffsets.get(codeOffset)) {
            return codeOffset;
        }
        return -1;
    }

    public TypeList adjust(TypeList typeList) {
        if (typeList == TypeList.EMPTY) {
            return typeList;
        }
        short[] types = new short[typeList.types.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = (short) adjustTypeIdIndex(typeList.types[i]);
        }
        return new TypeList(typeList.off, types);
    }

    public MethodId adjust(MethodId methodId) {
        return new MethodId(methodId.off, adjustTypeIdIndex(methodId.declaringClassIndex),
                adjustProtoIdIndex(methodId.protoIndex), adjustStringIndex(methodId.nameIndex));
    }

    public FieldId adjust(FieldId fieldId) {
        return new FieldId(fieldId.off, adjustTypeIdIndex(fieldId.declaringClassIndex),
                adjustTypeIdIndex(fieldId.typeIndex), adjustStringIndex(fieldId.nameIndex));
    }

    public ProtoId adjust(ProtoId protoId) {
        return new ProtoId(protoId.off, adjustStringIndex(protoId.shortyIndex), adjustTypeIdIndex
                (protoId.returnTypeIndex), adjustTypeListOffset(protoId.parametersOffset));
    }

    public ClassDef adjust(ClassDef classDef) {
        return new ClassDef(classDef.off, adjustTypeIdIndex(classDef.typeIndex), classDef
                .accessFlags, adjustTypeIdIndex(classDef.supertypeIndex), adjustTypeListOffset
                (classDef.interfacesOffset), adjustStringIndex(classDef.sourceFileIndex),
                adjustAnnotationsDirectoryOffset(classDef.annotationsOffset),
                adjustClassDataOffset(classDef.classDataOffset), adjustStaticValuesOffset
                (classDef.staticValuesOffset));
    }

    public ClassData adjust(ClassData classData) {
        return new ClassData(classData.off, adjustFields(classData.staticFields), adjustFields
                (classData.instanceFields), adjustMethods(classData.directMethods), adjustMethods
                (classData.virtualMethods));
    }

    public Code adjust(Code code) {
        return new Code(code.off, code.registersSize, code.insSize, code.outsSize,
                adjustDebugInfoItemOffset(code.debugInfoOffset), adjustInstructions(code
                .instructions), code.tries, adjustCatchHandlers(code.catchHandlers));
    }

    private short[] adjustInstructions(short[] instructions) {
        return (instructions == null || instructions.length == 0) ? instructions : new
                InstructionTransformer(this).transform(instructions);
    }

    private CatchHandler[] adjustCatchHandlers(CatchHandler[] catchHandlers) {
        if (catchHandlers == null || catchHandlers.length == 0) {
            return catchHandlers;
        }
        CatchHandler[] adjustedCatchHandlers = new CatchHandler[catchHandlers.length];
        for (int i = 0; i < catchHandlers.length; i++) {
            CatchHandler catchHandler = catchHandlers[i];
            int typeIndexesCount = catchHandler.typeIndexes.length;
            int[] adjustedTypeIndexes = new int[typeIndexesCount];
            for (int j = 0; j < typeIndexesCount; j++) {
                adjustedTypeIndexes[j] = adjustTypeIdIndex(catchHandler.typeIndexes[j]);
            }
            adjustedCatchHandlers[i] = new CatchHandler(adjustedTypeIndexes, catchHandler
                    .addresses, catchHandler.catchAllAddress, catchHandler.offset);
        }
        return adjustedCatchHandlers;
    }

    private Field[] adjustFields(Field[] fields) {
        Field[] adjustedFields = new Field[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            adjustedFields[i] = new Field(adjustFieldIdIndex(field.fieldIndex), field.accessFlags);
        }
        return adjustedFields;
    }

    private Method[] adjustMethods(Method[] methods) {
        Method[] adjustedMethods = new Method[methods.length];
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            adjustedMethods[i] = new Method(adjustMethodIdIndex(method.methodIndex), method
                    .accessFlags, adjustCodeOffset(method.codeOffset));
        }
        return adjustedMethods;
    }

    public DebugInfoItem adjust(DebugInfoItem debugInfoItem) {
        return new DebugInfoItem(debugInfoItem.off, debugInfoItem.lineStart, adjustParameterNames
                (debugInfoItem.parameterNames), adjustDebugInfoItemSTM(debugInfoItem.infoSTM));
    }

    private int[] adjustParameterNames(int[] parameterNames) {
        int size = parameterNames.length;
        int[] adjustedParameterNames = new int[size];
        for (int i = 0; i < size; i++) {
            adjustedParameterNames[i] = adjustStringIndex(parameterNames[i]);
        }
        return adjustedParameterNames;
    }

    private byte[] adjustDebugInfoItemSTM(byte[] infoSTM) {
        ByteArrayInputStream bais = new ByteArrayInputStream(infoSTM);
        final ByteArrayInputStream baisRef = bais;
        ByteInput inAdapter = new ByteInput() {
            public byte readByte() {
                return (byte) (baisRef.read() & 255);
            }
        };
        ByteArrayOutputStream baos = new ByteArrayOutputStream(infoSTM.length + 512);
        final ByteArrayOutputStream baosRef = baos;
        ByteOutput outAdapter = new ByteOutput() {
            public void writeByte(int i) {
                baosRef.write(i);
            }
        };
        while (true) {
            int opcode = bais.read() & 255;
            baos.write(opcode);
            switch (opcode) {
                case 0:
                    return baos.toByteArray();
                case 1:
                    Leb128.writeUnsignedLeb128(outAdapter, Leb128.readUnsignedLeb128(inAdapter));
                    break;
                case 2:
                    Leb128.writeSignedLeb128(outAdapter, Leb128.readSignedLeb128(inAdapter));
                    break;
                case 3:
                case 4:
                    Leb128.writeUnsignedLeb128(outAdapter, Leb128.readUnsignedLeb128(inAdapter));
                    Leb128.writeUnsignedLeb128p1(outAdapter, adjustStringIndex(Leb128
                            .readUnsignedLeb128p1(inAdapter)));
                    Leb128.writeUnsignedLeb128p1(outAdapter, adjustTypeIdIndex(Leb128
                            .readUnsignedLeb128p1(inAdapter)));
                    if (opcode != 4) {
                        break;
                    }
                    Leb128.writeUnsignedLeb128p1(outAdapter, adjustStringIndex(Leb128
                            .readUnsignedLeb128p1(inAdapter)));
                    break;
                case 5:
                case 6:
                    Leb128.writeUnsignedLeb128(outAdapter, Leb128.readUnsignedLeb128(inAdapter));
                    break;
                case 9:
                    Leb128.writeUnsignedLeb128p1(outAdapter, adjustStringIndex(Leb128
                            .readUnsignedLeb128p1(inAdapter)));
                    break;
                default:
                    break;
            }
        }
    }

    public EncodedValue adjust(EncodedValue encodedArray) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(encodedArray.data.length);
        new EncodedValueTransformer(new ByteOutput() {
            public void writeByte(int i) {
                baos.write(i);
            }
        }).transformArray(new EncodedValueReader(encodedArray, 28));
        return new EncodedValue(encodedArray.off, baos.toByteArray());
    }

    public Annotation adjust(Annotation annotation) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(annotation.encodedAnnotation
                .data.length);
        new EncodedValueTransformer(new ByteOutput() {
            public void writeByte(int i) {
                baos.write(i);
            }
        }).transformAnnotation(annotation.getReader());
        return new Annotation(annotation.off, annotation.visibility, new EncodedValue(annotation
                .encodedAnnotation.off, baos.toByteArray()));
    }

    public AnnotationSet adjust(AnnotationSet annotationSet) {
        int size = annotationSet.annotationOffsets.length;
        int[] adjustedAnnotationOffsets = new int[size];
        for (int i = 0; i < size; i++) {
            adjustedAnnotationOffsets[i] = adjustAnnotationOffset(annotationSet
                    .annotationOffsets[i]);
        }
        return new AnnotationSet(annotationSet.off, adjustedAnnotationOffsets);
    }

    public AnnotationSetRefList adjust(AnnotationSetRefList annotationSetRefList) {
        int size = annotationSetRefList.annotationSetRefItems.length;
        int[] adjustedAnnotationSetRefItems = new int[size];
        for (int i = 0; i < size; i++) {
            adjustedAnnotationSetRefItems[i] = adjustAnnotationSetOffset(annotationSetRefList
                    .annotationSetRefItems[i]);
        }
        return new AnnotationSetRefList(annotationSetRefList.off, adjustedAnnotationSetRefItems);
    }

    public AnnotationsDirectory adjust(AnnotationsDirectory annotationsDirectory) {
        int i;
        int adjustedClassAnnotationsOffset = adjustAnnotationSetOffset(annotationsDirectory
                .classAnnotationsOffset);
        int[][] adjustedFieldAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{annotationsDirectory.fieldAnnotations.length, 2});
        for (i = 0; i < adjustedFieldAnnotations.length; i++) {
            adjustedFieldAnnotations[i][0] = adjustFieldIdIndex(annotationsDirectory
                    .fieldAnnotations[i][0]);
            adjustedFieldAnnotations[i][1] = adjustAnnotationSetOffset(annotationsDirectory
                    .fieldAnnotations[i][1]);
        }
        int[][] adjustedMethodAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{annotationsDirectory.methodAnnotations.length, 2});
        for (i = 0; i < adjustedMethodAnnotations.length; i++) {
            adjustedMethodAnnotations[i][0] = adjustMethodIdIndex(annotationsDirectory
                    .methodAnnotations[i][0]);
            adjustedMethodAnnotations[i][1] = adjustAnnotationSetOffset(annotationsDirectory
                    .methodAnnotations[i][1]);
        }
        int[][] adjustedParameterAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{annotationsDirectory.parameterAnnotations.length, 2});
        for (i = 0; i < adjustedParameterAnnotations.length; i++) {
            adjustedParameterAnnotations[i][0] = adjustMethodIdIndex(annotationsDirectory.parameterAnnotations[i][0]);
            adjustedParameterAnnotations[i][1] = adjustAnnotationSetRefListOffset(annotationsDirectory.parameterAnnotations[i][1]);
        }
        return new AnnotationsDirectory(annotationsDirectory.off, adjustedClassAnnotationsOffset, adjustedFieldAnnotations, adjustedMethodAnnotations, adjustedParameterAnnotations);
    }
}
