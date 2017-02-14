package com.tencent.tinker.android.dex.io;

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
import com.tencent.tinker.android.dex.Code.Try;
import com.tencent.tinker.android.dex.DebugInfoItem;
import com.tencent.tinker.android.dex.DexException;
import com.tencent.tinker.android.dex.EncodedValue;
import com.tencent.tinker.android.dex.EncodedValueReader;
import com.tencent.tinker.android.dex.FieldId;
import com.tencent.tinker.android.dex.Leb128;
import com.tencent.tinker.android.dex.MethodId;
import com.tencent.tinker.android.dex.Mutf8;
import com.tencent.tinker.android.dex.ProtoId;
import com.tencent.tinker.android.dex.SizeOf;
import com.tencent.tinker.android.dex.StringData;
import com.tencent.tinker.android.dex.TypeList;
import com.tencent.tinker.android.dex.util.ByteInput;
import com.tencent.tinker.android.dex.util.ByteOutput;

import java.io.ByteArrayOutputStream;
import java.io.UTFDataFormatException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DexDataBuffer implements ByteInput, ByteOutput {
    public static final  int     DEFAULT_BUFFER_SIZE = 512;
    private static final short[] EMPTY_SHORT_ARRAY   = new short[0];
    private ByteBuffer data;
    private int        dataBound;
    private boolean    isResizeAllowed;

    public DexDataBuffer() {
        this.data = ByteBuffer.allocate(512);
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.dataBound = this.data.position();
        this.data.limit(this.data.capacity());
        this.isResizeAllowed = true;
    }

    public DexDataBuffer(ByteBuffer data) {
        this.data = data;
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.dataBound = data.limit();
        this.isResizeAllowed = false;
    }

    public DexDataBuffer(ByteBuffer data, boolean isResizeAllowed) {
        this.data = data;
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.dataBound = data.limit();
        this.isResizeAllowed = isResizeAllowed;
    }

    public int position() {
        return this.data.position();
    }

    public void position(int pos) {
        this.data.position(pos);
    }

    public int available() {
        return this.dataBound - this.data.position();
    }

    private void ensureBufferSize(int bytes) {
        if (this.data.position() + bytes > this.data.limit() && this.isResizeAllowed) {
            byte[] array = this.data.array();
            byte[] newArray = new byte[((array.length + bytes) + (array.length >> 1))];
            System.arraycopy(array, 0, newArray, 0, this.data.position());
            int lastPos = this.data.position();
            this.data = ByteBuffer.wrap(newArray);
            this.data.order(ByteOrder.LITTLE_ENDIAN);
            this.data.position(lastPos);
            this.data.limit(this.data.capacity());
        }
    }

    public byte[] array() {
        byte[] result = new byte[this.dataBound];
        System.arraycopy(this.data.array(), 0, result, 0, this.dataBound);
        return result;
    }

    public byte readByte() {
        return this.data.get();
    }

    public int readUnsignedByte() {
        return readByte() & 255;
    }

    public short readShort() {
        return this.data.getShort();
    }

    public int readUnsignedShort() {
        return readShort() & 65535;
    }

    public int readInt() {
        return this.data.getInt();
    }

    public byte[] readByteArray(int length) {
        byte[] result = new byte[length];
        this.data.get(result);
        return result;
    }

    public short[] readShortArray(int length) {
        if (length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] result = new short[length];
        for (int i = 0; i < length; i++) {
            result[i] = readShort();
        }
        return result;
    }

    public int readUleb128() {
        return Leb128.readUnsignedLeb128(this);
    }

    public int readUleb128p1() {
        return Leb128.readUnsignedLeb128(this) - 1;
    }

    public int readSleb128() {
        return Leb128.readSignedLeb128(this);
    }

    public StringData readStringData() {
        int off = this.data.position();
        try {
            int expectedLength = readUleb128();
            String result = Mutf8.decode(this, new char[expectedLength]);
            if (result.length() == expectedLength) {
                return new StringData(off, result);
            }
            throw new DexException("Declared length " + expectedLength + " doesn't match decoded " +
                    "length of " + result.length());
        } catch (Throwable e) {
            throw new DexException(e);
        }
    }

    public TypeList readTypeList() {
        return new TypeList(this.data.position(), readShortArray(readInt()));
    }

    public FieldId readFieldId() {
        return new FieldId(this.data.position(), readUnsignedShort(), readUnsignedShort(),
                readInt());
    }

    public MethodId readMethodId() {
        return new MethodId(this.data.position(), readUnsignedShort(), readUnsignedShort(),
                readInt());
    }

    public ProtoId readProtoId() {
        return new ProtoId(this.data.position(), readInt(), readInt(), readInt());
    }

    public ClassDef readClassDef() {
        return new ClassDef(position(), readInt(), readInt(), readInt(), readInt(), readInt(),
                readInt(), readInt(), readInt());
    }

    public Code readCode() {
        CatchHandler[] catchHandlers;
        Try[] tries;
        int off = this.data.position();
        int registersSize = readUnsignedShort();
        int insSize = readUnsignedShort();
        int outsSize = readUnsignedShort();
        int triesSize = readUnsignedShort();
        int debugInfoOffset = readInt();
        short[] instructions = readShortArray(readInt());
        if (triesSize > 0) {
            if (instructions.length % 2 == 1) {
                readShort();
            }
            int posBeforeTries = this.data.position();
            skip(triesSize * 8);
            catchHandlers = readCatchHandlers();
            int posAfterCatchHandlers = this.data.position();
            this.data.position(posBeforeTries);
            tries = readTries(triesSize, catchHandlers);
            this.data.position(posAfterCatchHandlers);
        } else {
            tries = new Try[0];
            catchHandlers = new CatchHandler[0];
        }
        return new Code(off, registersSize, insSize, outsSize, debugInfoOffset, instructions,
                tries, catchHandlers);
    }

    private CatchHandler[] readCatchHandlers() {
        int baseOffset = this.data.position();
        int catchHandlersSize = readUleb128();
        CatchHandler[] result = new CatchHandler[catchHandlersSize];
        for (int i = 0; i < catchHandlersSize; i++) {
            result[i] = readCatchHandler(this.data.position() - baseOffset);
        }
        return result;
    }

    private Try[] readTries(int triesSize, CatchHandler[] catchHandlers) {
        Try[] result = new Try[triesSize];
        for (int i = 0; i < triesSize; i++) {
            result[i] = new Try(readInt(), readUnsignedShort(), findCatchHandlerIndex
                    (catchHandlers, readUnsignedShort()));
        }
        return result;
    }

    private int findCatchHandlerIndex(CatchHandler[] catchHandlers, int offset) {
        for (int i = 0; i < catchHandlers.length; i++) {
            if (catchHandlers[i].offset == offset) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    private CatchHandler readCatchHandler(int offset) {
        int size = readSleb128();
        int handlersCount = Math.abs(size);
        int[] typeIndexes = new int[handlersCount];
        int[] addresses = new int[handlersCount];
        for (int i = 0; i < handlersCount; i++) {
            typeIndexes[i] = readUleb128();
            addresses[i] = readUleb128();
        }
        return new CatchHandler(typeIndexes, addresses, size <= 0 ? readUleb128() : -1, offset);
    }

    public DebugInfoItem readDebugInfoItem() {
        Throwable th;
        int off = this.data.position();
        int lineStart = readUleb128();
        int parametersSize = readUleb128();
        int[] parameterNames = new int[parametersSize];
        for (int i = 0; i < parametersSize; i++) {
            parameterNames[i] = readUleb128p1();
        }
        ByteArrayOutputStream baos = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream(64);
            final ByteArrayOutputStream baosRef = baos2;
            ByteOutput outAdapter = new ByteOutput() {
                public void writeByte(int i) {
                    baosRef.write(i);
                }
            };
            while (true) {
                int opcode = readByte();
                baos2.write(opcode);
                switch (opcode) {
                    case 0:
                        DebugInfoItem debugInfoItem = new DebugInfoItem(off, lineStart,
                                parameterNames, baos2.toByteArray());
                        if (baos2 != null) {
                            try {
                                baos2.close();
                            } catch (Exception e) {
                            }
                        }
                        return debugInfoItem;
                    case 1:
                        try {
                            Leb128.writeUnsignedLeb128(outAdapter, readUleb128());
                            break;
                        } catch (Throwable th2) {
                            th = th2;
                            baos = baos2;
                            break;
                        }
                    case 2:
                        Leb128.writeSignedLeb128(outAdapter, readSleb128());
                        break;
                    case 3:
                    case 4:
                        Leb128.writeUnsignedLeb128(outAdapter, readUleb128());
                        Leb128.writeUnsignedLeb128p1(outAdapter, readUleb128p1());
                        Leb128.writeUnsignedLeb128p1(outAdapter, readUleb128p1());
                        if (opcode != 4) {
                            break;
                        }
                        Leb128.writeUnsignedLeb128p1(outAdapter, readUleb128p1());
                        break;
                    case 5:
                    case 6:
                        Leb128.writeUnsignedLeb128(outAdapter, readUleb128());
                        break;
                    case 9:
                        Leb128.writeUnsignedLeb128p1(outAdapter, readUleb128p1());
                        break;
                    default:
                        break;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e2) {
                }
            }
            throw th;
        }
    }

    public ClassData readClassData() {
        return new ClassData(this.data.position(), readFields(readUleb128()), readFields
                (readUleb128()), readMethods(readUleb128()), readMethods(readUleb128()));
    }

    private Field[] readFields(int count) {
        Field[] result = new Field[count];
        int fieldIndex = 0;
        for (int i = 0; i < count; i++) {
            fieldIndex += readUleb128();
            result[i] = new Field(fieldIndex, readUleb128());
        }
        return result;
    }

    private Method[] readMethods(int count) {
        Method[] result = new Method[count];
        int methodIndex = 0;
        for (int i = 0; i < count; i++) {
            methodIndex += readUleb128();
            result[i] = new Method(methodIndex, readUleb128(), readUleb128());
        }
        return result;
    }

    private byte[] getBytesFrom(int start) {
        byte[] result = new byte[(this.data.position() - start)];
        this.data.position(start);
        this.data.get(result);
        return result;
    }

    public Annotation readAnnotation() {
        int off = this.data.position();
        byte visibility = readByte();
        int start = this.data.position();
        new EncodedValueReader((ByteInput) this, 29).skipValue();
        return new Annotation(off, visibility, new EncodedValue(start, getBytesFrom(start)));
    }

    public AnnotationSet readAnnotationSet() {
        int off = this.data.position();
        int size = readInt();
        int[] annotationOffsets = new int[size];
        for (int i = 0; i < size; i++) {
            annotationOffsets[i] = readInt();
        }
        return new AnnotationSet(off, annotationOffsets);
    }

    public AnnotationSetRefList readAnnotationSetRefList() {
        int off = this.data.position();
        int size = readInt();
        int[] annotationSetRefItems = new int[size];
        for (int i = 0; i < size; i++) {
            annotationSetRefItems[i] = readInt();
        }
        return new AnnotationSetRefList(off, annotationSetRefItems);
    }

    public AnnotationsDirectory readAnnotationsDirectory() {
        int i;
        int off = this.data.position();
        int classAnnotationsOffset = readInt();
        int fieldsSize = readInt();
        int methodsSize = readInt();
        int parameterListSize = readInt();
        int[][] fieldAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{fieldsSize, 2});
        for (i = 0; i < fieldsSize; i++) {
            fieldAnnotations[i][0] = readInt();
            fieldAnnotations[i][1] = readInt();
        }
        int[][] methodAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{methodsSize, 2});
        for (i = 0; i < methodsSize; i++) {
            methodAnnotations[i][0] = readInt();
            methodAnnotations[i][1] = readInt();
        }
        int[][] parameterAnnotations = (int[][]) Array.newInstance(Integer.TYPE, new
                int[]{parameterListSize, 2});
        for (i = 0; i < parameterListSize; i++) {
            parameterAnnotations[i][0] = readInt();
            parameterAnnotations[i][1] = readInt();
        }
        return new AnnotationsDirectory(off, classAnnotationsOffset, fieldAnnotations,
                methodAnnotations, parameterAnnotations);
    }

    public EncodedValue readEncodedArray() {
        int start = this.data.position();
        new EncodedValueReader((ByteInput) this, 28).skipValue();
        return new EncodedValue(start, getBytesFrom(start));
    }

    public void skip(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        this.data.position(this.data.position() + count);
    }

    public void skipWithAutoExpand(int count) {
        ensureBufferSize(count * 1);
        skip(count);
    }

    public void alignToFourBytes() {
        this.data.position((this.data.position() + 3) & -4);
    }

    public void alignToFourBytesWithZeroFill() {
        ensureBufferSize((SizeOf.roundToTimesOfFour(this.data.position()) - this.data.position())
                * 1);
        while ((this.data.position() & 3) != 0) {
            this.data.put((byte) 0);
        }
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void writeByte(int b) {
        ensureBufferSize(1);
        this.data.put((byte) b);
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void writeShort(short i) {
        ensureBufferSize(2);
        this.data.putShort(i);
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void writeUnsignedShort(int i) {
        short s = (short) i;
        if (i != (65535 & s)) {
            throw new IllegalArgumentException("Expected an unsigned short: " + i);
        }
        writeShort(s);
    }

    public void writeInt(int i) {
        ensureBufferSize(4);
        this.data.putInt(i);
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void write(byte[] bytes) {
        ensureBufferSize(bytes.length * 1);
        this.data.put(bytes);
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void write(short[] shorts) {
        ensureBufferSize(shorts.length * 2);
        for (short s : shorts) {
            writeShort(s);
        }
        if (this.data.position() > this.dataBound) {
            this.dataBound = this.data.position();
        }
    }

    public void writeUleb128(int i) {
        Leb128.writeUnsignedLeb128(this, i);
    }

    public void writeUleb128p1(int i) {
        writeUleb128(i + 1);
    }

    public void writeSleb128(int i) {
        Leb128.writeSignedLeb128(this, i);
    }

    public int writeStringData(StringData stringData) {
        int off = this.data.position();
        try {
            writeUleb128(stringData.value.length());
            write(Mutf8.encode(stringData.value));
            writeByte(0);
            return off;
        } catch (UTFDataFormatException e) {
            throw new AssertionError(e);
        }
    }

    public int writeTypeList(TypeList typeList) {
        int off = this.data.position();
        short[] types = typeList.types;
        writeInt(types.length);
        for (short type : types) {
            writeShort(type);
        }
        return off;
    }

    public int writeFieldId(FieldId fieldId) {
        int off = this.data.position();
        writeUnsignedShort(fieldId.declaringClassIndex);
        writeUnsignedShort(fieldId.typeIndex);
        writeInt(fieldId.nameIndex);
        return off;
    }

    public int writeMethodId(MethodId methodId) {
        int off = this.data.position();
        writeUnsignedShort(methodId.declaringClassIndex);
        writeUnsignedShort(methodId.protoIndex);
        writeInt(methodId.nameIndex);
        return off;
    }

    public int writeProtoId(ProtoId protoId) {
        int off = this.data.position();
        writeInt(protoId.shortyIndex);
        writeInt(protoId.returnTypeIndex);
        writeInt(protoId.parametersOffset);
        return off;
    }

    public int writeClassDef(ClassDef classDef) {
        int off = this.data.position();
        writeInt(classDef.typeIndex);
        writeInt(classDef.accessFlags);
        writeInt(classDef.supertypeIndex);
        writeInt(classDef.interfacesOffset);
        writeInt(classDef.sourceFileIndex);
        writeInt(classDef.annotationsOffset);
        writeInt(classDef.classDataOffset);
        writeInt(classDef.staticValuesOffset);
        return off;
    }

    public int writeCode(Code code) {
        int off = this.data.position();
        writeUnsignedShort(code.registersSize);
        writeUnsignedShort(code.insSize);
        writeUnsignedShort(code.outsSize);
        writeUnsignedShort(code.tries.length);
        writeInt(code.debugInfoOffset);
        writeInt(code.instructions.length);
        write(code.instructions);
        if (code.tries.length > 0) {
            if ((code.instructions.length & 1) == 1) {
                writeShort((short) 0);
            }
            int posBeforeTries = this.data.position();
            skipWithAutoExpand(code.tries.length * 8);
            int[] offsets = writeCatchHandlers(code.catchHandlers);
            int posAfterCatchHandlers = this.data.position();
            this.data.position(posBeforeTries);
            writeTries(code.tries, offsets);
            this.data.position(posAfterCatchHandlers);
        }
        return off;
    }

    private int[] writeCatchHandlers(CatchHandler[] catchHandlers) {
        int baseOffset = this.data.position();
        writeUleb128(catchHandlers.length);
        int[] offsets = new int[catchHandlers.length];
        for (int i = 0; i < catchHandlers.length; i++) {
            offsets[i] = this.data.position() - baseOffset;
            writeCatchHandler(catchHandlers[i]);
        }
        return offsets;
    }

    private void writeCatchHandler(CatchHandler catchHandler) {
        int catchAllAddress = catchHandler.catchAllAddress;
        int[] typeIndexes = catchHandler.typeIndexes;
        int[] addresses = catchHandler.addresses;
        if (catchAllAddress != -1) {
            writeSleb128(-typeIndexes.length);
        } else {
            writeSleb128(typeIndexes.length);
        }
        for (int i = 0; i < typeIndexes.length; i++) {
            writeUleb128(typeIndexes[i]);
            writeUleb128(addresses[i]);
        }
        if (catchAllAddress != -1) {
            writeUleb128(catchAllAddress);
        }
    }

    private void writeTries(Try[] tries, int[] catchHandlerOffsets) {
        for (Try tryItem : tries) {
            writeInt(tryItem.startAddress);
            writeUnsignedShort(tryItem.instructionCount);
            writeUnsignedShort(catchHandlerOffsets[tryItem.catchHandlerIndex]);
        }
    }

    public int writeDebugInfoItem(DebugInfoItem debugInfoItem) {
        int off = this.data.position();
        writeUleb128(debugInfoItem.lineStart);
        writeUleb128(parametersSize);
        for (int parameterName : debugInfoItem.parameterNames) {
            writeUleb128p1(parameterName);
        }
        write(debugInfoItem.infoSTM);
        return off;
    }

    public int writeClassData(ClassData classData) {
        int off = this.data.position();
        writeUleb128(classData.staticFields.length);
        writeUleb128(classData.instanceFields.length);
        writeUleb128(classData.directMethods.length);
        writeUleb128(classData.virtualMethods.length);
        writeFields(classData.staticFields);
        writeFields(classData.instanceFields);
        writeMethods(classData.directMethods);
        writeMethods(classData.virtualMethods);
        return off;
    }

    private void writeFields(Field[] fields) {
        int lastOutFieldIndex = 0;
        for (Field field : fields) {
            writeUleb128(field.fieldIndex - lastOutFieldIndex);
            lastOutFieldIndex = field.fieldIndex;
            writeUleb128(field.accessFlags);
        }
    }

    private void writeMethods(Method[] methods) {
        int lastOutMethodIndex = 0;
        for (Method method : methods) {
            writeUleb128(method.methodIndex - lastOutMethodIndex);
            lastOutMethodIndex = method.methodIndex;
            writeUleb128(method.accessFlags);
            writeUleb128(method.codeOffset);
        }
    }

    public int writeAnnotation(Annotation annotation) {
        int off = this.data.position();
        writeByte(annotation.visibility);
        writeEncodedArray(annotation.encodedAnnotation);
        return off;
    }

    public int writeAnnotationSet(AnnotationSet annotationSet) {
        int off = this.data.position();
        writeInt(annotationSet.annotationOffsets.length);
        for (int annotationOffset : annotationSet.annotationOffsets) {
            writeInt(annotationOffset);
        }
        return off;
    }

    public int writeAnnotationSetRefList(AnnotationSetRefList annotationSetRefList) {
        int off = this.data.position();
        writeInt(annotationSetRefList.annotationSetRefItems.length);
        for (int annotationSetRefItem : annotationSetRefList.annotationSetRefItems) {
            writeInt(annotationSetRefItem);
        }
        return off;
    }

    public int writeAnnotationsDirectory(AnnotationsDirectory annotationsDirectory) {
        int off = this.data.position();
        writeInt(annotationsDirectory.classAnnotationsOffset);
        writeInt(annotationsDirectory.fieldAnnotations.length);
        writeInt(annotationsDirectory.methodAnnotations.length);
        writeInt(annotationsDirectory.parameterAnnotations.length);
        for (int[] fieldAnnotation : annotationsDirectory.fieldAnnotations) {
            writeInt(fieldAnnotation[0]);
            writeInt(fieldAnnotation[1]);
        }
        for (int[] methodAnnotation : annotationsDirectory.methodAnnotations) {
            writeInt(methodAnnotation[0]);
            writeInt(methodAnnotation[1]);
        }
        for (int[] parameterAnnotation : annotationsDirectory.parameterAnnotations) {
            writeInt(parameterAnnotation[0]);
            writeInt(parameterAnnotation[1]);
        }
        return off;
    }

    public int writeEncodedArray(EncodedValue encodedValue) {
        int off = this.data.position();
        write(encodedValue.data);
        return off;
    }
}
