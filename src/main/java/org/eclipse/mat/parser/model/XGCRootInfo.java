package org.eclipse.mat.parser.model;

import org.eclipse.mat.snapshot.model.GCRootInfo;

public final class XGCRootInfo extends GCRootInfo {
    private static final long serialVersionUID = 1;

    public XGCRootInfo(long objectAddress, long contextAddress, int type) {
        super(objectAddress, contextAddress, type);
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void setContextId(int objectId) {
        this.contextId = objectId;
    }
}
