package org.eclipse.mat.snapshot.model;

import org.eclipse.mat.snapshot.ISnapshot;

public class ThreadToLocalReference extends PseudoReference {
    private static final long serialVersionUID = 1;
    private GCRootInfo[] gcRootInfo;
    private int localObjectId;

    public ThreadToLocalReference(ISnapshot snapshot, long address, String name, int localObjectId, GCRootInfo[] gcRootInfo) {
        super(snapshot, address, name);
        this.localObjectId = localObjectId;
        this.gcRootInfo = gcRootInfo;
    }

    public int getObjectId() {
        return this.localObjectId;
    }

    public GCRootInfo[] getGcRootInfo() {
        return this.gcRootInfo;
    }
}
