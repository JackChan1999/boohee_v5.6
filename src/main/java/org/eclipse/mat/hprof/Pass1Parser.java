package org.eclipse.mat.hprof;

import com.umeng.socialize.common.SocializeConstants;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.HashMapLongObject;
import org.eclipse.mat.parser.io.PositionInputStream;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.FieldDescriptor;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.IProgressListener.Severity;
import org.eclipse.mat.util.MessageUtil;
import org.eclipse.mat.util.SimpleMonitor.Listener;

public class Pass1Parser extends AbstractParser {
    private static final Pattern PATTERN_OBJ_ARRAY = Pattern.compile("^(\\[+)L(.*);$");
    private static final Pattern PATTERN_PRIMITIVE_ARRAY = Pattern.compile("^(\\[+)(.)$");
    private HashMapLongObject<String> class2name = new HashMapLongObject();
    private HashMapLongObject<Long> classSerNum2id = new HashMapLongObject();
    private IHprofParserHandler handler;
    private HashMapLongObject<StackFrame> id2frame = new HashMapLongObject();
    private Listener monitor;
    private HashMapLongObject<StackTrace> serNum2stackTrace = new HashMapLongObject();
    private HashMapLongObject<Long> thread2id = new HashMapLongObject();
    private HashMapLongObject<List<JavaLocal>> thread2locals = new HashMapLongObject();

    private static class JavaLocal {
        final int lineNumber;
        final long objectId;
        final int type;

        public JavaLocal(long objectId, int lineNumber, int type) {
            this.objectId = objectId;
            this.lineNumber = lineNumber;
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    private class StackFrame {
        final long classSerNum;
        final long frameId;
        final int lineNr;
        final String method;
        final String methodSignature;
        final String sourceFile;

        public StackFrame(long frameId, int lineNr, String method, String methodSignature, String sourceFile, long classSerNum) {
            this.frameId = frameId;
            this.lineNr = lineNr;
            this.method = method;
            this.methodSignature = methodSignature;
            this.sourceFile = sourceFile;
            this.classSerNum = classSerNum;
        }

        public String toString() {
            String className;
            Long classId = (Long) Pass1Parser.this.classSerNum2id.get(this.classSerNum);
            if (classId == null) {
                className = "<UNKNOWN CLASS>";
            } else {
                className = (String) Pass1Parser.this.class2name.get(classId.longValue());
            }
            String sourceLocation = "";
            if (this.lineNr > 0) {
                sourceLocation = SocializeConstants.OP_OPEN_PAREN + this.sourceFile + ":" + String.valueOf(this.lineNr) + SocializeConstants.OP_CLOSE_PAREN;
            } else if (this.lineNr == 0 || this.lineNr == -1) {
                sourceLocation = "(Unknown Source)";
            } else if (this.lineNr == -2) {
                sourceLocation = "(Compiled method)";
            } else if (this.lineNr == -3) {
                sourceLocation = "(Native Method)";
            }
            return "  at " + className + "." + this.method + this.methodSignature + " " + sourceLocation;
        }
    }

    private class StackTrace {
        final long[] frameIds;
        final long threadSerialNr;

        public StackTrace(long serialNr, long threadSerialNr, long[] frameIds) {
            this.threadSerialNr = threadSerialNr;
            this.frameIds = frameIds;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            for (long frameId : this.frameIds) {
                StackFrame frame = (StackFrame) Pass1Parser.this.id2frame.get(frameId);
                if (frame != null) {
                    b.append(frame).append("\r\n");
                }
            }
            return b.toString();
        }
    }

    public Pass1Parser(IHprofParserHandler handler, Listener monitor) {
        this.handler = handler;
        this.monitor = monitor;
    }

    public void read(File file) throws SnapshotException, IOException {
        this.in = new PositionInputStream(new BufferedInputStream(new FileInputStream(file)));
        int dumpNrToRead = determineDumpNumber();
        int currentDumpNr = 0;
        try {
            this.version = AbstractParser.readVersion(this.in);
            this.handler.addProperty(IHprofParserHandler.VERSION, this.version.toString());
            this.idSize = this.in.readInt();
            if (this.idSize == 4 || this.idSize == 8) {
                this.handler.addProperty(IHprofParserHandler.IDENTIFIER_SIZE, String.valueOf(this.idSize));
                this.handler.addProperty(IHprofParserHandler.CREATION_DATE, String.valueOf(this.in.readLong()));
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
                    if ((curPos + length) - 9 > fileSize) {
                        this.monitor.sendUserMessage(Severity.WARNING, MessageUtil.format(Messages.Pass1Parser_Error_invalidHPROFFile, Long.valueOf(length), Long.valueOf((fileSize - curPos) - 9)), null);
                    }
                    switch (record) {
                        case 1:
                            readString(length);
                            continue;
                        case 2:
                            readLoadClass();
                            continue;
                        case 4:
                            readStackFrame(length);
                            continue;
                        case 5:
                            readStackTrace(length);
                            continue;
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
                if (currentDumpNr <= dumpNrToRead) {
                    throw new SnapshotException(MessageUtil.format(Messages.Pass1Parser_Error_NoHeapDumpIndexFound, Integer.valueOf(currentDumpNr), file.getName(), Integer.valueOf(dumpNrToRead)));
                }
                if (currentDumpNr > 1) {
                    this.monitor.sendUserMessage(Severity.INFO, MessageUtil.format(Messages.Pass1Parser_Info_UsingDumpIndex, Integer.valueOf(currentDumpNr), file.getName(), Integer.valueOf(dumpNrToRead)), null);
                }
                if (this.serNum2stackTrace.size() > 0) {
                    dumpThreads();
                    return;
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

    private void readString(long length) throws IOException {
        long id = readID();
        byte[] chars = new byte[((int) (length - ((long) this.idSize)))];
        this.in.readFully(chars);
        this.handler.getConstantPool().put(id, new String(chars));
    }

    private void readLoadClass() throws IOException {
        long classSerNum = readUnsignedInt();
        long classID = readID();
        this.in.skipBytes(4);
        this.class2name.put(classID, getStringConstant(readID()).replace('/', '.'));
        this.classSerNum2id.put(classSerNum, Long.valueOf(classID));
    }

    private void readStackFrame(long length) throws IOException {
        long frameId = readID();
        long methodName = readID();
        long methodSig = readID();
        long srcFile = readID();
        this.id2frame.put(frameId, new StackFrame(frameId, this.in.readInt(), getStringConstant(methodName), getStringConstant(methodSig), getStringConstant(srcFile), readUnsignedInt()));
    }

    private void readStackTrace(long length) throws IOException {
        long stackTraceNr = readUnsignedInt();
        long threadNr = readUnsignedInt();
        long frameCount = readUnsignedInt();
        long[] frameIds = new long[((int) frameCount)];
        for (int i = 0; ((long) i) < frameCount; i++) {
            frameIds[i] = readID();
        }
        this.serNum2stackTrace.put(stackTraceNr, new StackTrace(stackTraceNr, threadNr, frameIds));
    }

    private void readDumpSegments(long length) throws IOException, SnapshotException {
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
                    readGC(128, this.idSize);
                    break;
                case 2:
                    readGCWithThreadContext(4, true);
                    break;
                case 3:
                    readGCWithThreadContext(64, true);
                    break;
                case 4:
                    readGCWithThreadContext(128, false);
                    break;
                case 5:
                    readGC(2, 0);
                    break;
                case 6:
                    readGC(16, 4);
                    break;
                case 7:
                    readGC(32, 0);
                    break;
                case 8:
                    readGCThreadObject(256);
                    break;
                case 32:
                    readClassDump(segmentStartPos);
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
                    readGC(1, 0);
                    break;
                case 138:
                    readGC(1, 0);
                    break;
                case 139:
                    readGC(1, 0);
                    break;
                case 140:
                    readGC(1, 0);
                    break;
                case 141:
                    readGC(1, 0);
                    break;
                case 142:
                    readGC(1, 8);
                    break;
                case 144:
                    readGC(1, 0);
                    break;
                case 195:
                    readPrimitiveArrayNoDataDump(segmentStartPos);
                    break;
                case 254:
                    this.in.skipBytes(this.idSize + 4);
                    break;
                case 255:
                    readGC(1, 0);
                    break;
                default:
                    throw new SnapshotException(MessageUtil.format(Messages.Pass1Parser_Error_InvalidHeapDumpFile, Integer.valueOf(segmentType), Long.valueOf(segmentStartPos)));
            }
            segmentStartPos = this.in.position();
        }
    }

    private void readGCThreadObject(int gcType) throws IOException {
        long id = readID();
        this.thread2id.put((long) this.in.readInt(), Long.valueOf(id));
        this.handler.addGCRoot(id, 0, gcType);
        this.in.skipBytes(4);
    }

    private void readGC(int gcType, int skip) throws IOException {
        this.handler.addGCRoot(readID(), 0, gcType);
        if (skip > 0) {
            this.in.skipBytes(skip);
        }
    }

    private void readGCWithThreadContext(int gcType, boolean hasLineInfo) throws IOException {
        long id = readID();
        int threadSerialNo = this.in.readInt();
        Long tid = (Long) this.thread2id.get((long) threadSerialNo);
        if (tid != null) {
            this.handler.addGCRoot(id, tid.longValue(), gcType);
        } else {
            this.handler.addGCRoot(id, 0, gcType);
        }
        if (hasLineInfo) {
            int lineNumber = this.in.readInt();
            List<JavaLocal> locals = (List) this.thread2locals.get((long) threadSerialNo);
            if (locals == null) {
                locals = new ArrayList();
                this.thread2locals.put((long) threadSerialNo, locals);
            }
            locals.add(new JavaLocal(id, lineNumber, gcType));
        }
    }

    private void readClassDump(long segmentStartPos) throws IOException {
        int ii;
        long address = readID();
        this.in.skipBytes(4);
        long superClassObjectId = readID();
        long classLoaderObjectId = readID();
        this.in.skipBytes((this.idSize * 4) + 4);
        int constantPoolSize = this.in.readUnsignedShort();
        for (ii = 0; ii < constantPoolSize; ii++) {
            this.in.skipBytes(2);
            skipValue();
        }
        int numStaticFields = this.in.readUnsignedShort();
        Field[] statics = new Field[numStaticFields];
        for (ii = 0; ii < numStaticFields; ii++) {
            String name = getStringConstant(readID());
            byte type = this.in.readByte();
            statics[ii] = new Field(name, type, readValue(null, type));
        }
        int numInstanceFields = this.in.readUnsignedShort();
        FieldDescriptor[] fields = new FieldDescriptor[numInstanceFields];
        for (ii = 0; ii < numInstanceFields; ii++) {
            fields[ii] = new FieldDescriptor(getStringConstant(readID()), this.in.readByte());
        }
        String className = (String) this.class2name.get(address);
        if (className == null) {
            className = "unknown-name@0x" + Long.toHexString(address);
        }
        if (className.charAt(0) == '[') {
            Matcher matcher = PATTERN_OBJ_ARRAY.matcher(className);
            if (matcher.matches()) {
                int l = matcher.group(1).length();
                className = matcher.group(2);
                for (ii = 0; ii < l; ii++) {
                    className = className + "[]";
                }
            }
            matcher = PATTERN_PRIMITIVE_ARRAY.matcher(className);
            if (matcher.matches()) {
                int count = matcher.group(1).length() - 1;
                className = "unknown[]";
                char signature = matcher.group(2).charAt(0);
                for (ii = 0; ii < IPrimitiveArray.SIGNATURES.length; ii++) {
                    if (IPrimitiveArray.SIGNATURES[ii] == ((byte) signature)) {
                        className = IPrimitiveArray.TYPE[ii];
                        break;
                    }
                }
                for (ii = 0; ii < count; ii++) {
                    className = className + "[]";
                }
            }
        }
        this.handler.addClass(new ClassImpl(address, className, superClassObjectId, classLoaderObjectId, statics, fields), segmentStartPos);
    }

    private void readInstanceDump(long segmentStartPos) throws IOException {
        this.handler.reportInstance(readID(), segmentStartPos);
        this.in.skipBytes(this.idSize + 4);
        this.in.skipBytes(this.in.readInt());
    }

    private void readObjectArrayDump(long segmentStartPos) throws IOException {
        this.handler.reportInstance(readID(), segmentStartPos);
        this.in.skipBytes(4);
        int size = this.in.readInt();
        long arrayClassObjectID = readID();
        if (this.handler.lookupClass(arrayClassObjectID) == null) {
            this.handler.reportRequiredObjectArray(arrayClassObjectID);
        }
        this.in.skipBytes(this.idSize * size);
    }

    private void readPrimitiveArrayDump(long segmentStartPos) throws SnapshotException, IOException {
        this.handler.reportInstance(readID(), segmentStartPos);
        this.in.skipBytes(4);
        int size = this.in.readInt();
        byte elementType = this.in.readByte();
        if (elementType < (byte) 4 || elementType > (byte) 11) {
            throw new SnapshotException(Messages.Pass1Parser_Error_IllegalType);
        }
        if (this.handler.lookupClassByName(IPrimitiveArray.TYPE[elementType], true) == null) {
            this.handler.reportRequiredPrimitiveArray(elementType);
        }
        this.in.skipBytes(IPrimitiveArray.ELEMENT_SIZE[elementType] * size);
    }

    private void readPrimitiveArrayNoDataDump(long segmentStartPos) throws SnapshotException, IOException {
        this.handler.reportInstance(readID(), segmentStartPos);
        this.in.skipBytes(4);
        int size = this.in.readInt();
        byte elementType = this.in.readByte();
        if (elementType < (byte) 4 || elementType > (byte) 11) {
            throw new SnapshotException(Messages.Pass1Parser_Error_IllegalType);
        }
        if (this.handler.lookupClassByName(IPrimitiveArray.TYPE[elementType], true) == null) {
            this.handler.reportRequiredPrimitiveArray(elementType);
        }
    }

    private String getStringConstant(long address) {
        if (address == 0) {
            return "";
        }
        String result = (String) this.handler.getConstantPool().get(address);
        return result == null ? Messages.Pass1Parser_Error_UnresolvedName + Long.toHexString(address) : result;
    }

    private void dumpThreads() {
        IOException e;
        Throwable th;
        if (this.serNum2stackTrace != null && this.serNum2stackTrace.size() > 1) {
            PrintWriter printWriter = null;
            try {
                PrintWriter out = new PrintWriter(new FileWriter(this.handler.getSnapshotInfo().getPrefix() + "threads"));
                try {
                    Iterator<StackTrace> it = this.serNum2stackTrace.values();
                    while (it.hasNext()) {
                        StackTrace stack = (StackTrace) it.next();
                        Long tid = (Long) this.thread2id.get(stack.threadSerialNr);
                        if (tid != null) {
                            String threadId;
                            if (tid == null) {
                                threadId = "<unknown>";
                            } else {
                                threadId = "0x" + Long.toHexString(tid.longValue());
                            }
                            out.println("Thread " + threadId);
                            out.println(stack);
                            out.println("  locals:");
                            List<JavaLocal> locals = (List) this.thread2locals.get(stack.threadSerialNr);
                            if (locals != null) {
                                for (JavaLocal javaLocal : locals) {
                                    out.println("    objecId=0x" + Long.toHexString(javaLocal.objectId) + ", line=" + javaLocal.lineNumber);
                                }
                            }
                            out.println();
                        }
                    }
                    out.flush();
                    this.monitor.sendUserMessage(Severity.INFO, MessageUtil.format(Messages.Pass1Parser_Info_WroteThreadsTo, outputName), null);
                    if (out != null) {
                        try {
                            out.close();
                            printWriter = out;
                            return;
                        } catch (Exception e2) {
                            printWriter = out;
                            return;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    printWriter = out;
                } catch (Throwable th2) {
                    th = th2;
                    printWriter = out;
                }
            } catch (IOException e4) {
                e = e4;
                try {
                    this.monitor.sendUserMessage(Severity.WARNING, MessageUtil.format(Messages.Pass1Parser_Error_WritingThreadsInformation, new Object[0]), e);
                    if (printWriter != null) {
                        try {
                            printWriter.close();
                        } catch (Exception e5) {
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (printWriter != null) {
                        try {
                            printWriter.close();
                        } catch (Exception e6) {
                        }
                    }
                    throw th;
                }
            }
        }
    }
}
