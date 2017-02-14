package org.eclipse.mat.hprof;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.hprof.IHprofParserHandler.HeapObject;
import org.eclipse.mat.parser.io.PositionInputStream;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.ObjectArrayImpl;
import org.eclipse.mat.parser.model.PrimitiveArrayImpl;
import org.eclipse.mat.snapshot.model.FieldDescriptor;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.MessageUtil;
import org.eclipse.mat.util.SimpleMonitor.Listener;

public class Pass2Parser extends AbstractParser {
    static final Set<String> ignorableClasses = new HashSet();
    private IHprofParserHandler handler;
    private Listener monitor;

    public Pass2Parser(IHprofParserHandler handler, Listener monitor) {
        this.handler = handler;
        this.monitor = monitor;
    }

    public void read(File file) throws SnapshotException, IOException {
        this.in = new PositionInputStream(new BufferedInputStream(new FileInputStream(file)));
        int dumpNrToRead = determineDumpNumber();
        int currentDumpNr = 0;
        try {
            this.version = AbstractParser.readVersion(this.in);
            this.idSize = this.in.readInt();
            if (this.idSize == 4 || this.idSize == 8) {
                this.in.skipBytes(8);
                long fileSize = file.length();
                long curPos = this.in.position();
                while (curPos < fileSize) {
                    if (this.monitor.isProbablyCanceled()) {
                        throw new OperationCanceledException();
                    }
                    this.monitor.totalWorkDone(curPos / 1000);
                    int record = this.in.readUnsignedByte();
                    this.in.skipBytes(4);
                    long length = readUnsignedInt();
                    if (length < 0) {
                        throw new SnapshotException(MessageUtil.format(Messages.Pass1Parser_Error_IllegalRecordLength, Long.valueOf(this.in.position())));
                    }
                    switch (record) {
                        case 12:
                        case 28:
                            if (dumpNrToRead == currentDumpNr) {
                                readDumpSegments(length);
                            } else {
                                this.in.skipBytes(length);
                            }
                            if (record == 12) {
                                currentDumpNr++;
                                break;
                            }
                            continue;
                        case 44:
                            currentDumpNr++;
                            break;
                    }
                    this.in.skipBytes(length);
                    curPos = this.in.position();
                }
                return;
            }
            throw new SnapshotException(Messages.Pass1Parser_Error_SupportedDumps);
        } finally {
            try {
                this.in.close();
            } catch (IOException e) {
            }
        }
    }

    private void readDumpSegments(long length) throws SnapshotException, IOException {
        long segmentStartPos = this.in.position();
        long segmentsEndPos = segmentStartPos + length;
        while (segmentStartPos < segmentsEndPos) {
            long workDone = segmentStartPos / 1000;
            if (this.monitor.getWorkDone() < workDone) {
                if (this.monitor.isProbablyCanceled()) {
                    throw new OperationCanceledException();
                }
                this.monitor.totalWorkDone(workDone);
            }
            switch (this.in.readUnsignedByte()) {
                case 1:
                    this.in.skipBytes(this.idSize * 2);
                    break;
                case 2:
                case 3:
                case 8:
                    this.in.skipBytes(this.idSize + 8);
                    break;
                case 4:
                case 6:
                    this.in.skipBytes(this.idSize + 4);
                    break;
                case 5:
                case 7:
                case 255:
                    this.in.skipBytes(this.idSize);
                    break;
                case 32:
                    skipClassDump();
                    break;
                case 33:
                    readInstanceDump(segmentStartPos);
                    break;
                case 34:
                    readObjectArrayDump(segmentStartPos);
                    break;
                case 35:
                    readPrimitiveArrayDump(segmentStartPos);
                    break;
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 144:
                    this.in.skipBytes(this.idSize);
                    break;
                case 142:
                    this.in.skipBytes(this.idSize + 8);
                    break;
                case 195:
                    readPrimitiveArrayNoDataDump(segmentStartPos);
                    break;
                case 254:
                    this.in.skipBytes(this.idSize + 4);
                    break;
                default:
                    throw new SnapshotException(MessageUtil.format(Messages.Pass1Parser_Error_InvalidHeapDumpFile, Integer.valueOf(segmentType), Long.valueOf(segmentStartPos)));
            }
            segmentStartPos = this.in.position();
        }
    }

    private void skipClassDump() throws IOException {
        this.in.skipBytes((this.idSize * 7) + 8);
        int constantPoolSize = this.in.readUnsignedShort();
        for (int ii = 0; ii < constantPoolSize; ii++) {
            this.in.skipBytes(2);
            skipValue();
        }
        int numStaticFields = this.in.readUnsignedShort();
        for (int i = 0; i < numStaticFields; i++) {
            this.in.skipBytes(this.idSize);
            skipValue();
        }
        this.in.skipBytes((this.idSize + 1) * this.in.readUnsignedShort());
    }

    static {
        ignorableClasses.add(WeakReference.class.getName());
        ignorableClasses.add(SoftReference.class.getName());
        ignorableClasses.add(PhantomReference.class.getName());
        ignorableClasses.add("java.lang.ref.Finalizer");
        ignorableClasses.add("java.lang.ref.FinalizerReference");
    }

    private void readInstanceDump(long segmentStartPos) throws IOException {
        long id = readID();
        this.in.skipBytes(4);
        long classID = readID();
        long endPos = this.in.position() + ((long) this.in.readInt());
        List<IClass> hierarchy = this.handler.resolveClassHierarchy(classID);
        ClassImpl thisClazz = (ClassImpl) hierarchy.get(0);
        HeapObject heapObject = new HeapObject(this.handler.mapAddressToId(id), id, thisClazz, thisClazz.getHeapSizePerInstance());
        heapObject.references.add(thisClazz.getObjectAddress());
        boolean isWeakReferenceClass = false;
        for (IClass clazz : hierarchy) {
            if (ignorableClasses.contains(clazz.getName())) {
                isWeakReferenceClass = true;
                break;
            }
        }
        for (IClass clazz2 : hierarchy) {
            for (FieldDescriptor field : clazz2.getFieldDescriptors()) {
                int type = field.getType();
                if (type == 2) {
                    long refId = readID();
                    if (!(refId == 0 || (isWeakReferenceClass && field.getName().equals("referent")))) {
                        heapObject.references.add(refId);
                    }
                } else {
                    skipValue(type);
                }
            }
        }
        if (endPos != this.in.position()) {
            throw new IOException(MessageUtil.format(Messages.Pass2Parser_Error_InsufficientBytesRead, Long.valueOf(segmentStartPos)));
        }
        this.handler.addObject(heapObject, segmentStartPos);
    }

    private void readObjectArrayDump(long segmentStartPos) throws IOException {
        long id = readID();
        this.in.skipBytes(4);
        int size = this.in.readInt();
        ClassImpl arrayType = (ClassImpl) this.handler.lookupClass(readID());
        if (arrayType == null) {
            throw new RuntimeException(MessageUtil.format(Messages.Pass2Parser_Error_HandlerMustCreateFakeClassForAddress, Long.toHexString(arrayClassObjectID)));
        }
        HeapObject heapObject = new HeapObject(this.handler.mapAddressToId(id), id, arrayType, ObjectArrayImpl.doGetUsedHeapSize(arrayType, size));
        heapObject.references.add(arrayType.getObjectAddress());
        heapObject.isArray = true;
        for (int ii = 0; ii < size; ii++) {
            long refId = readID();
            if (refId != 0) {
                heapObject.references.add(refId);
            }
        }
        this.handler.addObject(heapObject, segmentStartPos);
    }

    private void readPrimitiveArrayDump(long segmentStartPost) throws SnapshotException, IOException {
        long id = readID();
        this.in.skipBytes(4);
        int size = this.in.readInt();
        byte elementType = this.in.readByte();
        if (elementType < (byte) 4 || elementType > (byte) 11) {
            throw new SnapshotException(Messages.Pass1Parser_Error_IllegalType);
        }
        ClassImpl clazz = (ClassImpl) this.handler.lookupClassByName(IPrimitiveArray.TYPE[elementType], true);
        if (clazz == null) {
            throw new RuntimeException(MessageUtil.format(Messages.Pass2Parser_Error_HandleMustCreateFakeClassForName, name));
        }
        HeapObject heapObject = new HeapObject(this.handler.mapAddressToId(id), id, clazz, PrimitiveArrayImpl.doGetUsedHeapSize(clazz, size, elementType));
        heapObject.references.add(clazz.getObjectAddress());
        heapObject.isArray = true;
        this.handler.addObject(heapObject, segmentStartPost);
        this.in.skipBytes(IPrimitiveArray.ELEMENT_SIZE[elementType] * size);
    }

    private void readPrimitiveArrayNoDataDump(long segmentStartPost) throws SnapshotException, IOException {
        long id = readID();
        this.in.skipBytes(4);
        int size = this.in.readInt();
        byte elementType = this.in.readByte();
        if (elementType < (byte) 4 || elementType > (byte) 11) {
            throw new SnapshotException(Messages.Pass1Parser_Error_IllegalType);
        }
        ClassImpl clazz = (ClassImpl) this.handler.lookupClassByName(IPrimitiveArray.TYPE[elementType], true);
        if (clazz == null) {
            throw new RuntimeException(MessageUtil.format(Messages.Pass2Parser_Error_HandleMustCreateFakeClassForName, name));
        }
        HeapObject heapObject = new HeapObject(this.handler.mapAddressToId(id), id, clazz, PrimitiveArrayImpl.doGetUsedHeapSize(clazz, size, elementType));
        heapObject.references.add(clazz.getObjectAddress());
        heapObject.isArray = true;
        this.handler.addObject(heapObject, segmentStartPost);
    }
}
