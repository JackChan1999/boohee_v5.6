package com.tencent.tinker.android.dex;

import com.boohee.utils.Coder;
import com.tencent.tinker.android.dex.ClassData.Method;
import com.tencent.tinker.android.dex.io.DexDataBuffer;
import com.tencent.tinker.android.dex.util.FileUtils;
import com.tencent.tinker.android.dx.util.Hex;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Dex {
    private static final int     CHECKSUM_OFFSET   = 8;
    static final         short[] EMPTY_SHORT_ARRAY = new short[0];
    private static final int     SIGNATURE_OFFSET  = 12;
    private final ClassDefTable                   classDefs;
    private       ByteBuffer                      data;
    private final FieldIdTable                    fieldIds;
    private final MethodIdTable                   methodIds;
    private       int                             nextSectionStart;
    private final ProtoIdTable                    protoIds;
    private       byte[]                          signature;
    private final StringTable                     strings;
    private final TableOfContents                 tableOfContents;
    private final TypeIndexToDescriptorIndexTable typeIds;
    private final TypeIndexToDescriptorTable      typeNames;

    private final class ClassDefIterable implements Iterable<ClassDef> {
        private ClassDefIterable() {
        }

        public Iterator<ClassDef> iterator() {
            return !Dex.this.tableOfContents.classDefs.exists() ? Collections.emptySet().iterator
                    () : new ClassDefIterator();
        }
    }

    private final class ClassDefIterator implements Iterator<ClassDef> {
        private       int     count;
        private final Section in;

        private ClassDefIterator() {
            this.in = Dex.this.openSection(Dex.this.tableOfContents.classDefs);
            this.count = 0;
        }

        public boolean hasNext() {
            return this.count < Dex.this.tableOfContents.classDefs.size;
        }

        public ClassDef next() {
            if (hasNext()) {
                this.count++;
                return this.in.readClassDef();
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final class ClassDefTable extends AbstractList<ClassDef> implements RandomAccess {
        private ClassDefTable() {
        }

        public ClassDef get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.classDefs.size);
            return Dex.this.openSection(Dex.this.tableOfContents.classDefs.off + (index * 32))
                    .readClassDef();
        }

        public int size() {
            return Dex.this.tableOfContents.classDefs.size;
        }
    }

    private final class FieldIdTable extends AbstractList<FieldId> implements RandomAccess {
        private FieldIdTable() {
        }

        public FieldId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.fieldIds.size);
            return Dex.this.openSection(Dex.this.tableOfContents.fieldIds.off + (index * 8))
                    .readFieldId();
        }

        public int size() {
            return Dex.this.tableOfContents.fieldIds.size;
        }
    }

    private final class MethodIdTable extends AbstractList<MethodId> implements RandomAccess {
        private MethodIdTable() {
        }

        public MethodId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.methodIds.size);
            return Dex.this.openSection(Dex.this.tableOfContents.methodIds.off + (index * 8))
                    .readMethodId();
        }

        public int size() {
            return Dex.this.tableOfContents.methodIds.size;
        }
    }

    private final class ProtoIdTable extends AbstractList<ProtoId> implements RandomAccess {
        private ProtoIdTable() {
        }

        public ProtoId get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.protoIds.size);
            return Dex.this.openSection(Dex.this.tableOfContents.protoIds.off + (index * 12))
                    .readProtoId();
        }

        public int size() {
            return Dex.this.tableOfContents.protoIds.size;
        }
    }

    public final class Section extends DexDataBuffer {
        private final String name;

        private Section(String name, ByteBuffer data) {
            super(data);
            this.name = name;
        }

        public StringData readStringData() {
            ensureFourBytesAligned(Dex.this.tableOfContents.stringDatas, false);
            return super.readStringData();
        }

        public TypeList readTypeList() {
            ensureFourBytesAligned(Dex.this.tableOfContents.typeLists, false);
            return super.readTypeList();
        }

        public FieldId readFieldId() {
            ensureFourBytesAligned(Dex.this.tableOfContents.fieldIds, false);
            return super.readFieldId();
        }

        public MethodId readMethodId() {
            ensureFourBytesAligned(Dex.this.tableOfContents.methodIds, false);
            return super.readMethodId();
        }

        public ProtoId readProtoId() {
            ensureFourBytesAligned(Dex.this.tableOfContents.protoIds, false);
            return super.readProtoId();
        }

        public ClassDef readClassDef() {
            ensureFourBytesAligned(Dex.this.tableOfContents.classDefs, false);
            return super.readClassDef();
        }

        public Code readCode() {
            ensureFourBytesAligned(Dex.this.tableOfContents.codes, false);
            return super.readCode();
        }

        public DebugInfoItem readDebugInfoItem() {
            ensureFourBytesAligned(Dex.this.tableOfContents.debugInfos, false);
            return super.readDebugInfoItem();
        }

        public ClassData readClassData() {
            ensureFourBytesAligned(Dex.this.tableOfContents.classDatas, false);
            return super.readClassData();
        }

        public Annotation readAnnotation() {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotations, false);
            return super.readAnnotation();
        }

        public AnnotationSet readAnnotationSet() {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationSets, false);
            return super.readAnnotationSet();
        }

        public AnnotationSetRefList readAnnotationSetRefList() {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationSetRefLists, false);
            return super.readAnnotationSetRefList();
        }

        public AnnotationsDirectory readAnnotationsDirectory() {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationsDirectories, false);
            return super.readAnnotationsDirectory();
        }

        public EncodedValue readEncodedArray() {
            ensureFourBytesAligned(Dex.this.tableOfContents.encodedArrays, false);
            return super.readEncodedArray();
        }

        private void ensureFourBytesAligned(com.tencent.tinker.android.dex.TableOfContents
                                                    .Section tocSec, boolean isFillWithZero) {
            if (!tocSec.isElementFourByteAligned) {
                return;
            }
            if (isFillWithZero) {
                alignToFourBytesWithZeroFill();
            } else {
                alignToFourBytes();
            }
        }

        public int writeStringData(StringData stringData) {
            ensureFourBytesAligned(Dex.this.tableOfContents.stringDatas, true);
            return super.writeStringData(stringData);
        }

        public int writeTypeList(TypeList typeList) {
            ensureFourBytesAligned(Dex.this.tableOfContents.typeLists, true);
            return super.writeTypeList(typeList);
        }

        public int writeFieldId(FieldId fieldId) {
            ensureFourBytesAligned(Dex.this.tableOfContents.fieldIds, true);
            return super.writeFieldId(fieldId);
        }

        public int writeMethodId(MethodId methodId) {
            ensureFourBytesAligned(Dex.this.tableOfContents.methodIds, true);
            return super.writeMethodId(methodId);
        }

        public int writeProtoId(ProtoId protoId) {
            ensureFourBytesAligned(Dex.this.tableOfContents.protoIds, true);
            return super.writeProtoId(protoId);
        }

        public int writeClassDef(ClassDef classDef) {
            ensureFourBytesAligned(Dex.this.tableOfContents.classDefs, true);
            return super.writeClassDef(classDef);
        }

        public int writeCode(Code code) {
            ensureFourBytesAligned(Dex.this.tableOfContents.codes, true);
            return super.writeCode(code);
        }

        public int writeDebugInfoItem(DebugInfoItem debugInfoItem) {
            ensureFourBytesAligned(Dex.this.tableOfContents.debugInfos, true);
            return super.writeDebugInfoItem(debugInfoItem);
        }

        public int writeClassData(ClassData classData) {
            ensureFourBytesAligned(Dex.this.tableOfContents.classDatas, true);
            return super.writeClassData(classData);
        }

        public int writeAnnotation(Annotation annotation) {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotations, true);
            return super.writeAnnotation(annotation);
        }

        public int writeAnnotationSet(AnnotationSet annotationSet) {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationSets, true);
            return super.writeAnnotationSet(annotationSet);
        }

        public int writeAnnotationSetRefList(AnnotationSetRefList annotationSetRefList) {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationSetRefLists, true);
            return super.writeAnnotationSetRefList(annotationSetRefList);
        }

        public int writeAnnotationsDirectory(AnnotationsDirectory annotationsDirectory) {
            ensureFourBytesAligned(Dex.this.tableOfContents.annotationsDirectories, true);
            return super.writeAnnotationsDirectory(annotationsDirectory);
        }

        public int writeEncodedArray(EncodedValue encodedValue) {
            ensureFourBytesAligned(Dex.this.tableOfContents.encodedArrays, true);
            return super.writeEncodedArray(encodedValue);
        }
    }

    private final class StringTable extends AbstractList<String> implements RandomAccess {
        private StringTable() {
        }

        public String get(int index) {
            Dex.checkBounds(index, Dex.this.tableOfContents.stringIds.size);
            return Dex.this.openSection(Dex.this.openSection(Dex.this.tableOfContents.stringIds
                    .off + (index * 4)).readInt()).readStringData().value;
        }

        public int size() {
            return Dex.this.tableOfContents.stringIds.size;
        }
    }

    private final class TypeIndexToDescriptorIndexTable extends AbstractList<Integer> implements
            RandomAccess {
        private TypeIndexToDescriptorIndexTable() {
        }

        public Integer get(int index) {
            return Integer.valueOf(Dex.this.descriptorIndexFromTypeIndex(index));
        }

        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }

    private final class TypeIndexToDescriptorTable extends AbstractList<String> implements
            RandomAccess {
        private TypeIndexToDescriptorTable() {
        }

        public String get(int index) {
            return Dex.this.strings.get(Dex.this.descriptorIndexFromTypeIndex(index));
        }

        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }

    public Dex(byte[] data) throws IOException {
        this(ByteBuffer.wrap(data));
    }

    private Dex(ByteBuffer data) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.classDefs = new ClassDefTable();
        this.nextSectionStart = 0;
        this.signature = null;
        this.data = data;
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }

    public Dex(int byteCount) {
        this.tableOfContents = new TableOfContents();
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.classDefs = new ClassDefTable();
        this.nextSectionStart = 0;
        this.signature = null;
        this.data = ByteBuffer.wrap(new byte[byteCount]);
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.fileSize = byteCount;
    }

    public Dex(InputStream in) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.classDefs = new ClassDefTable();
        this.nextSectionStart = 0;
        this.signature = null;
        loadFrom(in);
    }

    public Dex(InputStream in, int initSize) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.classDefs = new ClassDefTable();
        this.nextSectionStart = 0;
        this.signature = null;
        loadFrom(in, initSize);
    }

    public Dex(File file) throws IOException {
        Throwable th;
        Throwable e;
        this.tableOfContents = new TableOfContents();
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.classDefs = new ClassDefTable();
        this.nextSectionStart = 0;
        this.signature = null;
        if (file == null) {
            throw new IllegalArgumentException("file is null.");
        } else if (FileUtils.hasArchiveSuffix(file.getName())) {
            ZipFile zipFile = null;
            try {
                ZipFile zipFile2 = new ZipFile(file);
                InputStream inputStream;
                try {
                    ZipEntry entry = zipFile2.getEntry("classes.dex");
                    if (entry != null) {
                        inputStream = null;
                        inputStream = zipFile2.getInputStream(entry);
                        loadFrom(inputStream, (int) entry.getSize());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (zipFile2 != null) {
                            try {
                                zipFile2.close();
                                return;
                            } catch (Exception e2) {
                                return;
                            }
                        }
                        return;
                    }
                    throw new DexException("Expected classes.dex in " + file);
                } catch (Throwable th2) {
                    th = th2;
                    zipFile = zipFile2;
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (Exception e3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (zipFile != null) {
                    zipFile.close();
                }
                throw th;
            }
        } else if (file.getName().endsWith(ShareConstants.DEX_SUFFIX)) {
            InputStream in = null;
            try {
                InputStream in2 = new BufferedInputStream(new FileInputStream(file));
                try {
                    loadFrom(in2, (int) file.length());
                    if (in2 != null) {
                        try {
                            in2.close();
                        } catch (Exception e4) {
                        }
                    }
                } catch (Exception e5) {
                    e = e5;
                    in = in2;
                    try {
                        throw new DexException(e);
                    } catch (Throwable th4) {
                        th = th4;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e6) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    in = in2;
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e = e7;
                throw new DexException(e);
            }
        } else {
            throw new DexException("unknown output extension: " + file);
        }
    }

    private static void checkBounds(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index:" + index + ", length=" + length);
        }
    }

    private void loadFrom(InputStream in) throws IOException {
        loadFrom(in, 0);
    }

    private void loadFrom(InputStream in, int initSize) throws IOException {
        this.data = ByteBuffer.wrap(FileUtils.readStream(in, initSize));
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.data.array());
        out.flush();
    }

    public void writeTo(File dexOut) throws IOException {
        Throwable e;
        Throwable th;
        OutputStream out = null;
        try {
            OutputStream out2 = new BufferedOutputStream(new FileOutputStream(dexOut));
            try {
                writeTo(out2);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    throw new DexException(e);
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            throw new DexException(e);
        }
    }

    public TableOfContents getTableOfContents() {
        return this.tableOfContents;
    }

    public Section openSection(int position) {
        if (position < 0 || position >= this.data.capacity()) {
            throw new IllegalArgumentException("position=" + position + " length=" + this.data
                    .capacity());
        }
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(position);
        sectionData.limit(this.data.capacity());
        return new Section("temp-section", sectionData);
    }

    public Section openSection(com.tencent.tinker.android.dex.TableOfContents.Section tocSec) {
        int position = tocSec.off;
        if (position < 0 || position >= this.data.capacity()) {
            throw new IllegalArgumentException("position=" + position + " length=" + this.data
                    .capacity());
        }
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(position);
        sectionData.limit(tocSec.byteCount + position);
        return new Section("section", sectionData);
    }

    public Section appendSection(int maxByteCount, String name) {
        int limit = this.nextSectionStart + maxByteCount;
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(this.nextSectionStart);
        sectionData.limit(limit);
        Section result = new Section(name, sectionData);
        this.nextSectionStart = limit;
        return result;
    }

    public int getLength() {
        return this.data.capacity();
    }

    public int getNextSectionStart() {
        return this.nextSectionStart;
    }

    public byte[] getBytes() {
        ByteBuffer data = this.data.duplicate();
        byte[] result = new byte[data.capacity()];
        data.position(0);
        data.get(result);
        return result;
    }

    public List<String> strings() {
        return this.strings;
    }

    public List<Integer> typeIds() {
        return this.typeIds;
    }

    public List<String> typeNames() {
        return this.typeNames;
    }

    public List<ProtoId> protoIds() {
        return this.protoIds;
    }

    public List<FieldId> fieldIds() {
        return this.fieldIds;
    }

    public List<MethodId> methodIds() {
        return this.methodIds;
    }

    public List<ClassDef> classDefs() {
        return this.classDefs;
    }

    public Iterable<ClassDef> classDefIterable() {
        return new ClassDefIterable();
    }

    public ClassData readClassData(ClassDef classDef) {
        int offset = classDef.classDataOffset;
        if (offset != 0) {
            return openSection(offset).readClassData();
        }
        throw new IllegalArgumentException("offset == 0");
    }

    public Code readCode(Method method) {
        int offset = method.codeOffset;
        if (offset != 0) {
            return openSection(offset).readCode();
        }
        throw new IllegalArgumentException("offset == 0");
    }

    public byte[] computeSignature(boolean forceRecompute) {
        if (this.signature != null && !forceRecompute) {
            return this.signature;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(Coder.KEY_SHA);
            byte[] buffer = new byte[8192];
            ByteBuffer data = this.data.duplicate();
            data.limit(data.capacity());
            data.position(32);
            while (data.hasRemaining()) {
                int count = Math.min(buffer.length, data.remaining());
                data.get(buffer, 0, count);
                digest.update(buffer, 0, count);
            }
            byte[] digest2 = digest.digest();
            this.signature = digest2;
            return digest2;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder strBuilder = new StringBuilder(bytes.length << 1);
        for (byte b : bytes) {
            strBuilder.append(Hex.u1(b));
        }
        return strBuilder.toString();
    }

    public int computeChecksum() throws IOException {
        Adler32 adler32 = new Adler32();
        byte[] buffer = new byte[8192];
        ByteBuffer data = this.data.duplicate();
        data.limit(data.capacity());
        data.position(12);
        while (data.hasRemaining()) {
            int count = Math.min(buffer.length, data.remaining());
            data.get(buffer, 0, count);
            adler32.update(buffer, 0, count);
        }
        return (int) adler32.getValue();
    }

    public void writeHashes() throws IOException {
        openSection(12).write(computeSignature(true));
        openSection(8).writeInt(computeChecksum());
    }

    public int nameIndexFromFieldIndex(int fieldIndex) {
        checkBounds(fieldIndex, this.tableOfContents.fieldIds.size);
        return this.data.getInt(((this.tableOfContents.fieldIds.off + (fieldIndex * 8)) + 2) + 2);
    }

    public int findStringIndex(String s) {
        return Collections.binarySearch(this.strings, s);
    }

    public int findTypeIndex(String descriptor) {
        return Collections.binarySearch(this.typeNames, descriptor);
    }

    public int findFieldIndex(FieldId fieldId) {
        return Collections.binarySearch(this.fieldIds, fieldId);
    }

    public int findMethodIndex(MethodId methodId) {
        return Collections.binarySearch(this.methodIds, methodId);
    }

    public int findClassDefIndexFromTypeIndex(int typeIndex) {
        checkBounds(typeIndex, this.tableOfContents.typeIds.size);
        if (!this.tableOfContents.classDefs.exists()) {
            return -1;
        }
        for (int i = 0; i < this.tableOfContents.classDefs.size; i++) {
            if (typeIndexFromClassDefIndex(i) == typeIndex) {
                return i;
            }
        }
        return -1;
    }

    public int typeIndexFromFieldIndex(int fieldIndex) {
        checkBounds(fieldIndex, this.tableOfContents.fieldIds.size);
        return this.data.getShort((this.tableOfContents.fieldIds.off + (fieldIndex * 8)) + 2) &
                65535;
    }

    public int declaringClassIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        return this.data.getShort(this.tableOfContents.methodIds.off + (methodIndex * 8)) & 65535;
    }

    public int nameIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        return this.data.getInt(((this.tableOfContents.methodIds.off + (methodIndex * 8)) + 2) + 2);
    }

    public short[] parameterTypeIndicesFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        int protoIndex = this.data.getShort((this.tableOfContents.methodIds.off + (methodIndex *
                8)) + 2) & 65535;
        checkBounds(protoIndex, this.tableOfContents.protoIds.size);
        int parametersOffset = this.data.getInt(((this.tableOfContents.protoIds.off + (protoIndex
                * 12)) + 4) + 4);
        if (parametersOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = parametersOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected parameter type list size: " + size);
        }
        position += 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position);
            position += 2;
        }
        return types;
    }

    public short[] parameterTypeIndicesFromMethodId(MethodId methodId) {
        int protoIndex = methodId.protoIndex & 65535;
        checkBounds(protoIndex, this.tableOfContents.protoIds.size);
        int parametersOffset = this.data.getInt(((this.tableOfContents.protoIds.off + (protoIndex
                * 12)) + 4) + 4);
        if (parametersOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = parametersOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected parameter type list size: " + size);
        }
        position += 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position);
            position += 2;
        }
        return types;
    }

    public int returnTypeIndexFromMethodIndex(int methodIndex) {
        checkBounds(methodIndex, this.tableOfContents.methodIds.size);
        int protoIndex = this.data.getShort((this.tableOfContents.methodIds.off + (methodIndex *
                8)) + 2) & 65535;
        checkBounds(protoIndex, this.tableOfContents.protoIds.size);
        return this.data.getInt((this.tableOfContents.protoIds.off + (protoIndex * 12)) + 4);
    }

    public int descriptorIndexFromTypeIndex(int typeIndex) {
        checkBounds(typeIndex, this.tableOfContents.typeIds.size);
        return this.data.getInt(this.tableOfContents.typeIds.off + (typeIndex * 4));
    }

    public int typeIndexFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        return this.data.getInt(this.tableOfContents.classDefs.off + (classDefIndex * 32));
    }

    public int annotationDirectoryOffsetFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        return this.data.getInt((((((this.tableOfContents.classDefs.off + (classDefIndex * 32)) +
                4) + 4) + 4) + 4) + 4);
    }

    public short[] interfaceTypeIndicesFromClassDefIndex(int classDefIndex) {
        checkBounds(classDefIndex, this.tableOfContents.classDefs.size);
        int interfacesOffset = this.data.getInt((((this.tableOfContents.classDefs.off +
                (classDefIndex * 32)) + 4) + 4) + 4);
        if (interfacesOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = interfacesOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected interfaces list size: " + size);
        }
        position += 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position);
            position += 2;
        }
        return types;
    }

    public short[] interfaceTypeIndicesFromClassDef(ClassDef classDef) {
        int interfacesOffset = this.data.getInt(((classDef.off + 4) + 4) + 4);
        if (interfacesOffset == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        int position = interfacesOffset;
        int size = this.data.getInt(position);
        if (size <= 0) {
            throw new AssertionError("Unexpected interfaces list size: " + size);
        }
        position += 4;
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = this.data.getShort(position);
            position += 2;
        }
        return types;
    }
}
