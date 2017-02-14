package org.eclipse.mat.hprof;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.parser.io.BufferedRandomAccessInputStream;
import org.eclipse.mat.parser.io.PositionInputStream;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.ClassLoaderImpl;
import org.eclipse.mat.parser.model.InstanceImpl;
import org.eclipse.mat.parser.model.ObjectArrayImpl;
import org.eclipse.mat.parser.model.PrimitiveArrayImpl;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.FieldDescriptor;
import org.eclipse.mat.snapshot.model.IArray;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.util.MessageUtil;

public class HprofRandomAccessParser extends AbstractParser {
    public static final int LAZY_LOADING_LIMIT = 256;

    public HprofRandomAccessParser(File file, Version version, int identifierSize) throws IOException {
        this.in = new PositionInputStream(new BufferedRandomAccessInputStream(new RandomAccessFile(file, "r"), 512));
        this.version = version;
        this.idSize = identifierSize;
    }

    public synchronized void close() throws IOException {
        this.in.close();
    }

    public synchronized IObject read(int objectId, long position, ISnapshot dump) throws IOException, SnapshotException {
        IObject readInstanceDump;
        this.in.seek(position);
        switch (this.in.readUnsignedByte()) {
            case 33:
                readInstanceDump = readInstanceDump(objectId, dump);
                break;
            case 34:
                readInstanceDump = readObjectArrayDump(objectId, dump);
                break;
            case 35:
                readInstanceDump = readPrimitiveArrayDump(objectId, dump);
                break;
            default:
                throw new IOException(MessageUtil.format(Messages.HprofRandomAccessParser_Error_IllegalDumpSegment, Integer.valueOf(segmentType)));
        }
        return readInstanceDump;
    }

    public List<IClass> resolveClassHierarchy(ISnapshot snapshot, IClass clazz) throws SnapshotException {
        List<IClass> answer = new ArrayList();
        answer.add(clazz);
        while (clazz.hasSuperClass()) {
            clazz = (IClass) snapshot.getObject(clazz.getSuperClassId());
            if (clazz == null) {
                return null;
            }
            answer.add(clazz);
        }
        return answer;
    }

    private IObject readInstanceDump(int objectId, ISnapshot dump) throws IOException, SnapshotException {
        long address = readID();
        if (this.in.skipBytes(this.idSize + 8) != this.idSize + 8) {
            throw new IOException();
        }
        List<IClass> hierarchy = resolveClassHierarchy(dump, dump.getClassOf(objectId));
        if (hierarchy == null) {
            throw new IOException(Messages.HprofRandomAccessParser_Error_DumpIncomplete.pattern);
        }
        List<Field> instanceFields = new ArrayList();
        for (IClass clazz : hierarchy) {
            List<FieldDescriptor> fields = clazz.getFieldDescriptors();
            for (int ii = 0; ii < fields.size(); ii++) {
                FieldDescriptor field = (FieldDescriptor) fields.get(ii);
                instanceFields.add(new Field(field.getName(), field.getType(), readValue(dump, field.getType())));
            }
        }
        ClassImpl classImpl = (ClassImpl) hierarchy.get(0);
        if (dump.isClassLoader(objectId)) {
            return new ClassLoaderImpl(objectId, address, classImpl, instanceFields);
        }
        return new InstanceImpl(objectId, address, classImpl, instanceFields);
    }

    private IArray readObjectArrayDump(int objectId, ISnapshot dump) throws IOException, SnapshotException {
        long id = readID();
        this.in.skipBytes(4);
        int size = this.in.readInt();
        IClass arrayType = (IClass) dump.getObject(dump.mapAddressToId(readID()));
        if (arrayType == null) {
            throw new RuntimeException(Messages.HprofRandomAccessParser_Error_MissingFakeClass.pattern);
        }
        Object content;
        if (this.idSize * size < 256) {
            Object data = new long[size];
            for (int ii = 0; ii < data.length; ii++) {
                data[ii] = readID();
            }
            content = data;
        } else {
            content = new Offline(false, this.in.position(), 0, size);
        }
        ObjectArrayImpl array = new ObjectArrayImpl(objectId, id, (ClassImpl) arrayType, size);
        array.setInfo(content);
        return array;
    }

    private IArray readPrimitiveArrayDump(int objectId, ISnapshot dump) throws IOException, SnapshotException {
        long id = readID();
        this.in.skipBytes(4);
        int arraySize = this.in.readInt();
        long elementType = (long) this.in.readByte();
        if (elementType < 4 || elementType > 11) {
            throw new IOException(Messages.Pass1Parser_Error_IllegalType.pattern);
        }
        Object content;
        int elementSize = IPrimitiveArray.ELEMENT_SIZE[(int) elementType];
        int len = elementSize * arraySize;
        if (len < 256) {
            Object data = new byte[len];
            this.in.readFully(data);
            content = elementType == 8 ? data : new Raw(data);
        } else {
            content = new Offline(true, this.in.position(), elementSize, arraySize);
        }
        Collection<IClass> classes = dump.getClassesByName(IPrimitiveArray.TYPE[(int) elementType], false);
        if (classes == null || classes.isEmpty()) {
            throw new IOException(MessageUtil.format(Messages.HprofRandomAccessParser_Error_MissingClass, name));
        } else if (classes.size() > 1) {
            throw new IOException(MessageUtil.format(Messages.HprofRandomAccessParser_Error_DuplicateClass, name));
        } else {
            PrimitiveArrayImpl array = new PrimitiveArrayImpl(objectId, id, (ClassImpl) ((IClass) classes.iterator().next()), arraySize, (int) elementType);
            array.setInfo(content);
            return array;
        }
    }

    public synchronized long[] readObjectArray(Offline descriptor, int offset, int length) throws IOException {
        long[] data;
        this.in.seek(descriptor.getPosition() + ((long) (offset * this.idSize)));
        data = new long[length];
        for (int ii = 0; ii < data.length; ii++) {
            data[ii] = readID();
        }
        return data;
    }

    public synchronized byte[] readPrimitiveArray(Offline descriptor, int offset, int length) throws IOException {
        byte[] data;
        int elementSize = descriptor.getElementSize();
        this.in.seek(descriptor.getPosition() + ((long) (offset * elementSize)));
        data = new byte[(length * elementSize)];
        this.in.readFully(data);
        return data;
    }
}
