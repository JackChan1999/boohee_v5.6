package org.eclipse.mat.hprof;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.mat.parser.io.PositionInputStream;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.util.MessageUtil;

abstract class AbstractParser {
    protected int idSize;
    protected PositionInputStream in;
    protected Version version;

    interface Constants {

        public interface DumpSegment {
            public static final int ANDROID_HEAP_DUMP_INFO = 254;
            public static final int ANDROID_PRIMITIVE_ARRAY_NODATA_DUMP = 195;
            public static final int ANDROID_ROOT_DEBUGGER = 139;
            public static final int ANDROID_ROOT_FINALIZING = 138;
            public static final int ANDROID_ROOT_INTERNED_STRING = 137;
            public static final int ANDROID_ROOT_JNI_MONITOR = 142;
            public static final int ANDROID_ROOT_REFERENCE_CLEANUP = 140;
            public static final int ANDROID_ROOT_VM_INTERNAL = 141;
            public static final int ANDROID_UNREACHABLE = 144;
            public static final int CLASS_DUMP = 32;
            public static final int INSTANCE_DUMP = 33;
            public static final int OBJECT_ARRAY_DUMP = 34;
            public static final int PRIMITIVE_ARRAY_DUMP = 35;
            public static final int ROOT_JAVA_FRAME = 3;
            public static final int ROOT_JNI_GLOBAL = 1;
            public static final int ROOT_JNI_LOCAL = 2;
            public static final int ROOT_MONITOR_USED = 7;
            public static final int ROOT_NATIVE_STACK = 4;
            public static final int ROOT_STICKY_CLASS = 5;
            public static final int ROOT_THREAD_BLOCK = 6;
            public static final int ROOT_THREAD_OBJECT = 8;
            public static final int ROOT_UNKNOWN = 255;
        }

        public interface Record {
            public static final int ALLOC_SITES = 6;
            public static final int CONTROL_SETTINGS = 14;
            public static final int CPU_SAMPLES = 13;
            public static final int END_THREAD = 11;
            public static final int HEAP_DUMP = 12;
            public static final int HEAP_DUMP_END = 44;
            public static final int HEAP_DUMP_SEGMENT = 28;
            public static final int HEAP_SUMMARY = 7;
            public static final int LOAD_CLASS = 2;
            public static final int STACK_FRAME = 4;
            public static final int STACK_TRACE = 5;
            public static final int START_THREAD = 10;
            public static final int STRING_IN_UTF8 = 1;
            public static final int UNLOAD_CLASS = 3;
        }
    }

    enum Version {
        JDK12BETA3("JAVA PROFILE 1.0"),
        JDK12BETA4("JAVA PROFILE 1.0.1"),
        JDK6("JAVA PROFILE 1.0.2"),
        ANDROID103("JAVA PROFILE 1.0.3");
        
        private String label;

        private Version(String label) {
            this.label = label;
        }

        public static Version byLabel(String label) {
            for (Version v : values()) {
                if (v.label.equals(label)) {
                    return v;
                }
            }
            return null;
        }

        public String getLabel() {
            return this.label;
        }
    }

    AbstractParser() {
    }

    static Version readVersion(InputStream in) throws IOException {
        StringBuilder version = new StringBuilder();
        int bytesRead = 0;
        while (bytesRead < 20) {
            byte b = (byte) in.read();
            bytesRead++;
            if (b != (byte) 0) {
                version.append((char) b);
            } else {
                Version answer = Version.byLabel(version.toString());
                if (answer == null) {
                    if (bytesRead <= 13) {
                        throw new IOException(Messages.AbstractParser_Error_NotHeapDump.pattern);
                    }
                    throw new IOException(MessageUtil.format(Messages.AbstractParser_Error_UnknownHPROFVersion, version.toString()));
                } else if (answer != Version.JDK12BETA3) {
                    return answer;
                } else {
                    throw new IOException(MessageUtil.format(Messages.AbstractParser_Error_UnsupportedHPROFVersion, answer.getLabel()));
                }
            }
        }
        throw new IOException(Messages.AbstractParser_Error_InvalidHPROFHeader.pattern);
    }

    protected long readUnsignedInt() throws IOException {
        return 4294967295L & ((long) this.in.readInt());
    }

    protected long readID() throws IOException {
        return this.idSize == 4 ? 4294967295L & ((long) this.in.readInt()) : this.in.readLong();
    }

    protected Object readValue(ISnapshot snapshot) throws IOException {
        return readValue(snapshot, this.in.readByte());
    }

    protected Object readValue(ISnapshot snapshot, int type) throws IOException {
        boolean z = true;
        switch (type) {
            case 2:
                long id = readID();
                if (id == 0) {
                    return null;
                }
                return new ObjectReference(snapshot, id);
            case 4:
                if (this.in.readByte() == (byte) 0) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 5:
                return Character.valueOf(this.in.readChar());
            case 6:
                return Float.valueOf(this.in.readFloat());
            case 7:
                return Double.valueOf(this.in.readDouble());
            case 8:
                return Byte.valueOf(this.in.readByte());
            case 9:
                return Short.valueOf(this.in.readShort());
            case 10:
                return Integer.valueOf(this.in.readInt());
            case 11:
                return Long.valueOf(this.in.readLong());
            default:
                throw new IOException(MessageUtil.format(Messages.AbstractParser_Error_IllegalType, Integer.valueOf(type)));
        }
    }

    protected void skipValue() throws IOException {
        skipValue(this.in.readByte());
    }

    protected void skipValue(int type) throws IOException {
        if (type == 2) {
            this.in.skipBytes(this.idSize);
        } else {
            this.in.skipBytes(IPrimitiveArray.ELEMENT_SIZE[type]);
        }
    }

    protected int determineDumpNumber() {
        String dumpNr = System.getProperty("MAT_HPROF_DUMP_NR");
        return dumpNr == null ? 0 : Integer.parseInt(dumpNr);
    }
}
