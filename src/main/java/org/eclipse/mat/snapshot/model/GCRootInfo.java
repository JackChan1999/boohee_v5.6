package org.eclipse.mat.snapshot.model;

import java.io.Serializable;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.util.MessageUtil;

public abstract class GCRootInfo implements Serializable {
    private static final String[] TYPE_STRING = new String[]{MessageUtil.format(Messages.GCRootInfo_Unkown, new Object[0]), MessageUtil.format(Messages.GCRootInfo_SystemClass, new Object[0]), MessageUtil.format(Messages.GCRootInfo_JNILocal, new Object[0]), MessageUtil.format(Messages.GCRootInfo_JNIGlobal, new Object[0]), MessageUtil.format(Messages.GCRootInfo_ThreadBlock, new Object[0]), MessageUtil.format(Messages.GCRootInfo_BusyMonitor, new Object[0]), MessageUtil.format(Messages.GCRootInfo_JavaLocal, new Object[0]), MessageUtil.format(Messages.GCRootInfo_NativeStack, new Object[0]), MessageUtil.format(Messages.GCRootInfo_Thread, new Object[0]), MessageUtil.format(Messages.GCRootInfo_Finalizable, new Object[0]), MessageUtil.format(Messages.GCRootInfo_Unfinalized, new Object[0]), MessageUtil.format(Messages.GCRootInfo_Unreachable, new Object[0])};
    private static final long serialVersionUID = 2;
    private long contextAddress;
    protected int contextId;
    private long objectAddress;
    protected int objectId;
    private int type;

    public interface Type {
        public static final int BUSY_MONITOR = 32;
        public static final int FINALIZABLE = 512;
        public static final int JAVA_LOCAL = 64;
        public static final int NATIVE_LOCAL = 4;
        public static final int NATIVE_STACK = 128;
        public static final int NATIVE_STATIC = 8;
        public static final int SYSTEM_CLASS = 2;
        public static final int THREAD_BLOCK = 16;
        public static final int THREAD_OBJ = 256;
        public static final int UNFINALIZED = 1024;
        public static final int UNKNOWN = 1;
        public static final int UNREACHABLE = 2048;
    }

    public GCRootInfo(long objectAddress, long contextAddress, int type) {
        this.objectAddress = objectAddress;
        this.contextAddress = contextAddress;
        this.type = type;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public long getObjectAddress() {
        return this.objectAddress;
    }

    public long getContextAddress() {
        return this.contextAddress;
    }

    public int getContextId() {
        return this.contextId;
    }

    public int getType() {
        return this.type;
    }

    public static String getTypeAsString(int type) {
        for (int i = 0; i < TYPE_STRING.length; i++) {
            if (((1 << i) & type) != 0) {
                return TYPE_STRING[i];
            }
        }
        return null;
    }

    public static String getTypeSetAsString(GCRootInfo[] roots) {
        int typeSet = 0;
        for (GCRootInfo info : roots) {
            typeSet |= info.getType();
        }
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < TYPE_STRING.length; i++) {
            if (((1 << i) & typeSet) != 0) {
                if (!first) {
                    buf.append(", ");
                } else if ((1 << i) == typeSet) {
                    return TYPE_STRING[i];
                } else {
                    first = false;
                }
                buf.append(TYPE_STRING[i]);
            }
        }
        return buf.toString();
    }
}
