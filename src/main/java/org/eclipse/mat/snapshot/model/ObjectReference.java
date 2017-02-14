package org.eclipse.mat.snapshot.model;

import java.io.Serializable;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.snapshot.ISnapshot;

public class ObjectReference implements Serializable {
    private static final long serialVersionUID = 1;
    private long address;
    private transient ISnapshot snapshot;

    public ObjectReference(ISnapshot snapshot, long address) {
        this.snapshot = snapshot;
        this.address = address;
    }

    public long getObjectAddress() {
        return this.address;
    }

    public int getObjectId() throws SnapshotException {
        return this.snapshot.mapAddressToId(this.address);
    }

    public IObject getObject() throws SnapshotException {
        return this.snapshot.getObject(getObjectId());
    }

    public String toString() {
        return "0x" + Long.toHexString(this.address);
    }
}
