package org.eclipse.mat.parser.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.GCRootInfo;
import org.eclipse.mat.snapshot.model.IInstance;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.snapshot.model.PseudoReference;
import org.eclipse.mat.snapshot.model.ThreadToLocalReference;

public class InstanceImpl extends AbstractObjectImpl implements IInstance {
    private static final long serialVersionUID = 1;
    private volatile List<Field> fields;
    private volatile Map<String, Field> name2field;

    public InstanceImpl(int objectId, long address, ClassImpl clazz, List<Field> fields) {
        super(objectId, address, clazz);
        this.fields = fields;
    }

    public long getObjectAddress() {
        try {
            long address = super.getObjectAddress();
            if (address != Long.MIN_VALUE) {
                return address;
            }
            address = this.source.mapIdToAddress(getObjectId());
            setObjectAddress(address);
            return address;
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        }
    }

    public int getObjectId() {
        try {
            int objectId = super.getObjectId();
            if (objectId >= 0) {
                return objectId;
            }
            objectId = this.source.mapAddressToId(getObjectAddress());
            setObjectId(objectId);
            return objectId;
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Field> getFields() {
        if (this.fields == null) {
            readFully();
        }
        return this.fields;
    }

    public Field getField(String name) {
        return internalGetField(name);
    }

    protected void setFields(List<Field> fields) {
        this.fields = fields;
    }

    protected synchronized void readFully() {
        if (this.fields == null) {
            try {
                InstanceImpl fullCopy = (InstanceImpl) this.source.getHeapObjectReader().read(getObjectId(), this.source);
                setObjectAddress(fullCopy.getObjectAddress());
                this.fields = fullCopy.fields;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SnapshotException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    public int getUsedHeapSize() {
        try {
            return getSnapshot().getHeapSize(getObjectId());
        } catch (SnapshotException e) {
            return this.classInstance.getHeapSizePerInstance();
        }
    }

    public ArrayLong getReferences() {
        List<Field> fields = getFields();
        ArrayLong list = new ArrayLong(fields.size() + 1);
        list.add(this.classInstance.getObjectAddress());
        HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> threadToLocalVars = this.source.getRootsPerThread();
        if (threadToLocalVars != null) {
            HashMapIntObject<XGCRootInfo[]> localVars = (HashMapIntObject) threadToLocalVars.get(getObjectId());
            if (localVars != null) {
                IteratorInt localsIds = localVars.keys();
                while (localsIds.hasNext()) {
                    list.add(((GCRootInfo[]) localVars.get(localsIds.next()))[0].getObjectAddress());
                }
            }
        }
        for (Field field : fields) {
            if (field.getValue() instanceof ObjectReference) {
                list.add(((ObjectReference) field.getValue()).getObjectAddress());
            }
        }
        return list;
    }

    public List<NamedReference> getOutboundReferences() {
        List<NamedReference> list = new ArrayList();
        list.add(new PseudoReference(this.source, this.classInstance.getObjectAddress(), "<class>"));
        HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> threadToLocalVars = this.source.getRootsPerThread();
        if (threadToLocalVars != null) {
            HashMapIntObject<XGCRootInfo[]> localVars = (HashMapIntObject) threadToLocalVars.get(getObjectId());
            if (localVars != null) {
                IteratorInt localsIds = localVars.keys();
                while (localsIds.hasNext()) {
                    int localId = localsIds.next();
                    GCRootInfo[] rootInfo = (GCRootInfo[]) localVars.get(localId);
                    list.add(new ThreadToLocalReference(this.source, rootInfo[0].getObjectAddress(), "<" + GCRootInfo.getTypeSetAsString(rootInfo) + ">", localId, rootInfo));
                }
            }
        }
        for (Field field : getFields()) {
            if (field.getValue() instanceof ObjectReference) {
                ObjectReference ref = (ObjectReference) field.getValue();
                list.add(new NamedReference(this.source, ref.getObjectAddress(), field.getName()));
            }
        }
        return list;
    }

    protected Field internalGetField(String name) {
        if (this.name2field == null) {
            List<Field> fields = getFields();
            Map<String, Field> n2f = new HashMap(fields.size());
            for (Field f : fields) {
                n2f.put(f.getName(), f);
            }
            this.name2field = n2f;
        }
        return (Field) this.name2field.get(name);
    }
}
