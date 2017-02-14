package org.eclipse.mat.parser.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.IObjectArray;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.snapshot.model.PseudoReference;
import org.eclipse.mat.util.MessageUtil;

public class ObjectArrayImpl extends AbstractArrayImpl implements IObjectArray {
    private static final long serialVersionUID = 2;

    public ObjectArrayImpl(int objectId, long address, ClassImpl classInstance, int length) {
        super(objectId, address, classInstance, length);
    }

    public int getUsedHeapSize() {
        try {
            return getSnapshot().getHeapSize(getObjectId());
        } catch (SnapshotException e) {
            return doGetUsedHeapSize(this.classInstance, this.length);
        }
    }

    public static int doGetUsedHeapSize(ClassImpl clazz, int length) {
        return AbstractObjectImpl.alignUpTo8(((clazz.getHeapSizePerInstance() * 2) + 4) + (clazz.getHeapSizePerInstance() * length));
    }

    public long[] getReferenceArray() {
        try {
            return this.source.getHeapObjectReader().readObjectArrayContent(this, 0, getLength());
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public long[] getReferenceArray(int offset, int length) {
        try {
            return this.source.getHeapObjectReader().readObjectArrayContent(this, offset, length);
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public ArrayLong getReferences() {
        ArrayLong answer = new ArrayLong(getLength() + 1);
        answer.add(this.classInstance.getObjectAddress());
        long[] refs = getReferenceArray();
        for (int i = 0; i < refs.length; i++) {
            if (refs[i] != 0) {
                answer.add(refs[i]);
            }
        }
        return answer;
    }

    protected Field internalGetField(String name) {
        if (name.charAt(0) != '[' || name.charAt(name.length() - 1) != ']') {
            return null;
        }
        try {
            int index = Integer.parseInt(name.substring(1, name.length() - 1));
            if (index < 0 || index > this.length) {
                throw new IndexOutOfBoundsException(MessageUtil.format(Messages.ObjectArrayImpl_forArray, Integer.valueOf(index), getTechnicalName()));
            }
            return new Field(name, 2, new ObjectReference(this.source, this.source.getHeapObjectReader().readObjectArrayContent(this, index, 1)[0]));
        } catch (SnapshotException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public List<NamedReference> getOutboundReferences() {
        List<NamedReference> answer = new ArrayList(getLength() + 1);
        answer.add(new PseudoReference(this.source, this.classInstance.getObjectAddress(), "<class>"));
        long[] refs = getReferenceArray();
        for (int i = 0; i < refs.length; i++) {
            if (refs[i] != 0) {
                StringBuilder builder = new StringBuilder();
                builder.append('[').append(i).append(']');
                answer.add(new NamedReference(this.source, refs[i], builder.toString()));
            }
        }
        return answer;
    }
}
