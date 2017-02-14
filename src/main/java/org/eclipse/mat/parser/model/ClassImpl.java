package org.eclipse.mat.parser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.FieldDescriptor;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.snapshot.model.PseudoReference;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.VoidProgressListener;

public class ClassImpl extends AbstractObjectImpl implements IClass, Comparable<ClassImpl> {
    public static final String JAVA_LANG_CLASS = "java.lang.Class";
    private static final long serialVersionUID = 22;
    private Serializable cacheEntry;
    protected long classLoaderAddress;
    protected int classLoaderId = -1;
    protected FieldDescriptor[] fields;
    protected int instanceCount;
    protected int instanceSize;
    protected boolean isArrayType;
    protected String name;
    protected Field[] staticFields;
    private List<IClass> subClasses;
    protected long superClassAddress;
    protected int superClassId = -1;
    protected long totalSize;
    protected int usedHeapSize;

    public ClassImpl(long address, String name, long superId, long loaderId, Field[] staticFields, FieldDescriptor[] fields) {
        super(-1, address, null);
        this.name = name;
        this.superClassAddress = superId;
        this.classLoaderAddress = loaderId;
        this.staticFields = staticFields;
        this.fields = fields;
        this.instanceSize = -1;
        this.totalSize = 0;
        this.isArrayType = name.endsWith("[]");
    }

    public Serializable getCacheEntry() {
        return this.cacheEntry;
    }

    public void setCacheEntry(Serializable cacheEntry) {
        this.cacheEntry = cacheEntry;
    }

    public void setSuperClassIndex(int superClassIndex) {
        this.superClassId = superClassIndex;
    }

    public void setClassLoaderIndex(int classLoaderIndex) {
        this.classLoaderId = classLoaderIndex;
    }

    public int[] getObjectIds() throws UnsupportedOperationException, SnapshotException {
        try {
            return this.source.getIndexManager().c2objects().getObjectsOf(this.cacheEntry);
        } catch (Throwable e) {
            throw new SnapshotException(e);
        }
    }

    public long getRetainedHeapSizeOfObjects(boolean calculateIfNotAvailable, boolean approximation, IProgressListener listener) throws SnapshotException {
        long answer = this.source.getRetainedSizeCache().get(getObjectId());
        if (answer > 0 || !calculateIfNotAvailable) {
            return answer;
        }
        if (answer < 0 && approximation) {
            return answer;
        }
        long retainedSize;
        if (listener == null) {
            listener = new VoidProgressListener();
        }
        ArrayInt ids = new ArrayInt();
        ids.add(getObjectId());
        ids.addAll(getObjectIds());
        if (approximation) {
            retainedSize = this.source.getMinRetainedSize(ids.toArray(), listener);
            if (listener.isCanceled()) {
                return 0;
            }
        }
        int[] retainedSet = this.source.getRetainedSet(ids.toArray(), listener);
        if (listener.isCanceled()) {
            return 0;
        }
        retainedSize = this.source.getHeapSize(retainedSet);
        if (approximation) {
            retainedSize = -retainedSize;
        }
        this.source.getRetainedSizeCache().put(getObjectId(), retainedSize);
        return retainedSize;
    }

    public int getUsedHeapSize() {
        return this.usedHeapSize;
    }

    public ArrayLong getReferences() {
        ArrayLong answer = new ArrayLong(this.staticFields.length);
        answer.add(this.classInstance.getObjectAddress());
        if (this.superClassAddress != 0) {
            answer.add(this.superClassAddress);
        }
        answer.add(this.classLoaderAddress);
        for (int ii = 0; ii < this.staticFields.length; ii++) {
            if (this.staticFields[ii].getValue() instanceof ObjectReference) {
                answer.add(((ObjectReference) this.staticFields[ii].getValue()).getObjectAddress());
            }
        }
        return answer;
    }

    public List<NamedReference> getOutboundReferences() {
        List<NamedReference> answer = new LinkedList();
        answer.add(new PseudoReference(this.source, this.classInstance.getObjectAddress(), "<class>"));
        if (this.superClassAddress != 0) {
            answer.add(new PseudoReference(this.source, this.superClassAddress, "<super>"));
        }
        answer.add(new PseudoReference(this.source, this.classLoaderAddress, "<classloader>"));
        for (int ii = 0; ii < this.staticFields.length; ii++) {
            if (this.staticFields[ii].getValue() instanceof ObjectReference) {
                ObjectReference ref = (ObjectReference) this.staticFields[ii].getValue();
                String fieldName = this.staticFields[ii].getName();
                if (fieldName.startsWith("<")) {
                    answer.add(new PseudoReference(this.source, ref.getObjectAddress(), fieldName));
                } else {
                    answer.add(new NamedReference(this.source, ref.getObjectAddress(), fieldName));
                }
            }
        }
        return answer;
    }

    public long getClassLoaderAddress() {
        return this.classLoaderAddress;
    }

    public void setClassLoaderAddress(long address) {
        this.classLoaderAddress = address;
    }

    public List<FieldDescriptor> getFieldDescriptors() {
        return Arrays.asList(this.fields);
    }

    public int getNumberOfObjects() {
        return this.instanceCount;
    }

    public int getHeapSizePerInstance() {
        return this.instanceSize;
    }

    public void setHeapSizePerInstance(int size) {
        this.instanceSize = size;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getStaticFields() {
        return Arrays.asList(this.staticFields);
    }

    public long getSuperClassAddress() {
        return this.superClassAddress;
    }

    public int getSuperClassId() {
        return this.superClassId;
    }

    public ClassImpl getSuperClass() {
        try {
            return this.superClassAddress != 0 ? (ClassImpl) this.source.getObject(this.superClassId) : null;
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        }
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public boolean hasSuperClass() {
        return this.superClassAddress != 0;
    }

    public int compareTo(ClassImpl other) {
        long myAddress = getObjectAddress();
        long otherAddress = other.getObjectAddress();
        if (myAddress > otherAddress) {
            return 1;
        }
        return myAddress == otherAddress ? 0 : -1;
    }

    public void addInstance(int usedHeapSize) {
        this.instanceCount++;
        this.totalSize += (long) usedHeapSize;
    }

    public void removeInstance(int heapSizePerInstance) {
        this.instanceCount--;
        this.totalSize -= (long) heapSizePerInstance;
    }

    public List<IClass> getSubclasses() {
        return this.subClasses != null ? this.subClasses : Collections.EMPTY_LIST;
    }

    public List<IClass> getAllSubclasses() {
        if (this.subClasses == null || this.subClasses.isEmpty()) {
            return new ArrayList();
        }
        List<IClass> answer = new ArrayList(this.subClasses.size() * 2);
        answer.addAll(this.subClasses);
        for (IClass subClass : this.subClasses) {
            answer.addAll(subClass.getAllSubclasses());
        }
        return answer;
    }

    protected StringBuffer appendFields(StringBuffer buf) {
        return super.appendFields(buf).append(";name=").append(getName());
    }

    public boolean isArrayType() {
        return this.isArrayType;
    }

    public String getTechnicalName() {
        StringBuilder builder = new StringBuilder(256);
        builder.append("class ");
        builder.append(getName());
        builder.append(" @ 0x");
        builder.append(Long.toHexString(getObjectAddress()));
        return builder.toString();
    }

    protected Field internalGetField(String name) {
        for (Field f : this.staticFields) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public int getClassLoaderId() {
        return this.classLoaderId;
    }

    public void addSubClass(ClassImpl clazz) {
        if (this.subClasses == null) {
            this.subClasses = new ArrayList();
        }
        this.subClasses.add(clazz);
    }

    public void removeSubClass(ClassImpl clazz) {
        this.subClasses.remove(clazz);
    }

    public void setUsedHeapSize(int usedHeapSize) {
        this.usedHeapSize = usedHeapSize;
    }

    public boolean doesExtend(String className) throws SnapshotException {
        if (className.equals(this.name)) {
            return true;
        }
        return (!hasSuperClass() || this.source == null) ? false : ((ClassImpl) this.source.getObject(this.superClassId)).doesExtend(className);
    }

    public void setSnapshot(ISnapshot dump) {
        super.setSnapshot(dump);
        for (Field f : this.staticFields) {
            if (f.getValue() instanceof ObjectReference) {
                f.setValue(new ObjectReference(dump, ((ObjectReference) f.getValue()).getObjectAddress()));
            }
        }
    }
}
