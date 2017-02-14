package org.eclipse.mat.parser.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.snapshot.model.PseudoReference;

public class PrimitiveArrayImpl extends AbstractArrayImpl implements IPrimitiveArray {
    private static final long serialVersionUID = 2;
    private int type;

    public PrimitiveArrayImpl(int objectId, long address, ClassImpl classInstance, int length, int type) {
        super(objectId, address, classInstance, length);
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public Class<?> getComponentType() {
        return COMPONENT_TYPE[this.type];
    }

    public Object getValueAt(int index) {
        Object data = getValueArray(index, 1);
        return data != null ? Array.get(data, 0) : null;
    }

    public Object getValueArray() {
        try {
            return this.source.getHeapObjectReader().readPrimitiveArrayContent(this, 0, getLength());
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public Object getValueArray(int offset, int length) {
        try {
            return this.source.getHeapObjectReader().readPrimitiveArrayContent(this, offset, length);
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    protected Field internalGetField(String name) {
        return null;
    }

    public ArrayLong getReferences() {
        ArrayLong references = new ArrayLong(1);
        references.add(this.classInstance.getObjectAddress());
        return references;
    }

    public List<NamedReference> getOutboundReferences() {
        List<NamedReference> references = new ArrayList(1);
        references.add(new PseudoReference(this.source, this.classInstance.getObjectAddress(), "<class>"));
        return references;
    }

    protected StringBuffer appendFields(StringBuffer buf) {
        return super.appendFields(buf).append(";size=").append(getUsedHeapSize());
    }

    public int getUsedHeapSize() {
        try {
            return getSnapshot().getHeapSize(getObjectId());
        } catch (SnapshotException e) {
            return doGetUsedHeapSize(this.classInstance, this.length, this.type);
        }
    }

    public static int doGetUsedHeapSize(ClassImpl clazz, int length, int type) {
        return AbstractObjectImpl.alignUpTo8(((clazz.getHeapSizePerInstance() * 2) + 4) + (ELEMENT_SIZE[type] * length));
    }
}
