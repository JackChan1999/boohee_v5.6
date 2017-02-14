package org.eclipse.mat.parser.model;

import java.io.Serializable;
import java.util.Comparator;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.internal.SnapshotImpl;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.GCRootInfo;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.snapshot.registry.ClassSpecificNameResolverRegistry;
import org.eclipse.mat.util.MessageUtil;

public abstract class AbstractObjectImpl implements IObject, Serializable {
    private static final long serialVersionUID = 2451875423035843852L;
    private long address;
    protected ClassImpl classInstance;
    private int objectId;
    protected transient SnapshotImpl source;

    public abstract ArrayLong getReferences();

    public abstract int getUsedHeapSize();

    protected abstract Field internalGetField(String str);

    public AbstractObjectImpl(int objectId, long address, ClassImpl classInstance) {
        this.objectId = objectId;
        this.address = address;
        this.classInstance = classInstance;
    }

    public long getObjectAddress() {
        return this.address;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectAddress(long address) {
        this.address = address;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public ClassImpl getClazz() {
        return this.classInstance;
    }

    public long getClassAddress() {
        return this.classInstance.getObjectAddress();
    }

    public int getClassId() {
        return this.classInstance.getObjectId();
    }

    public void setClassInstance(ClassImpl classInstance) {
        this.classInstance = classInstance;
    }

    public void setSnapshot(ISnapshot dump) {
        this.source = (SnapshotImpl) dump;
    }

    public ISnapshot getSnapshot() {
        return this.source;
    }

    public long getRetainedHeapSize() {
        try {
            return this.source.getRetainedHeapSize(getObjectId());
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer(256);
        s.append(getClazz().getName());
        s.append(" [");
        appendFields(s);
        s.append("]");
        return s.toString();
    }

    protected StringBuffer appendFields(StringBuffer buf) {
        return buf.append("id=0x").append(Long.toHexString(getObjectAddress()));
    }

    public String getClassSpecificName() {
        return ClassSpecificNameResolverRegistry.resolve(this);
    }

    public String getTechnicalName() {
        StringBuilder builder = new StringBuilder(256);
        builder.append(getClazz().getName());
        builder.append(" @ 0x");
        builder.append(Long.toHexString(getObjectAddress()));
        return builder.toString();
    }

    public String getDisplayName() {
        String label = getClassSpecificName();
        if (label == null) {
            return getTechnicalName();
        }
        StringBuilder s = new StringBuilder(256).append(getTechnicalName()).append("  ");
        if (label.length() <= 256) {
            s.append(label);
        } else {
            s.append(label.substring(0, 256));
            s.append("...");
        }
        return s.toString();
    }

    public final Object resolveValue(String name) throws SnapshotException {
        int p = name.indexOf(46);
        Field f = internalGetField(p < 0 ? name : name.substring(0, p));
        if (f == null || f.getValue() == null) {
            return null;
        }
        if (p < 0) {
            Object answer = f.getValue();
            if (answer instanceof ObjectReference) {
                return ((ObjectReference) answer).getObject();
            }
            return answer;
        } else if (f.getValue() instanceof ObjectReference) {
            ObjectReference ref = (ObjectReference) f.getValue();
            if (ref == null) {
                return null;
            }
            int objectId = ref.getObjectId();
            if (objectId >= 0) {
                return this.source.getObject(objectId).resolveValue(name.substring(p + 1));
            }
            throw new SnapshotException(MessageUtil.format(Messages.AbstractObjectImpl_Error_FieldContainsIllegalReference, n, getTechnicalName(), Long.toHexString(ref.getObjectAddress())));
        } else {
            throw new SnapshotException(MessageUtil.format(Messages.AbstractObjectImpl_Error_FieldIsNotReference, n, getTechnicalName(), name.substring(p + 1)));
        }
    }

    public GCRootInfo[] getGCRootInfo() throws SnapshotException {
        return this.source.getGCRootInfo(getObjectId());
    }

    public boolean equals(Object obj) {
        return (obj instanceof IObject) && this.objectId == ((IObject) obj).getObjectId();
    }

    public int hashCode() {
        return this.objectId;
    }

    public static Comparator<AbstractObjectImpl> getComparatorForTechnicalName() {
        return null;
    }

    public static Comparator<AbstractObjectImpl> getComparatorForClassSpecificName() {
        return null;
    }

    public static Comparator<AbstractObjectImpl> getComparatorForUsedHeapSize() {
        return null;
    }

    protected static int alignUpTo8(int n) {
        return n % 8 == 0 ? n : (n + 8) - (n % 8);
    }
}
