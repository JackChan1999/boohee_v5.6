package org.eclipse.mat.snapshot;

import java.util.HashMap;

public final class PathsFromGCRootsTree {
    private int[] objectIds;
    private HashMap<Integer, PathsFromGCRootsTree> objectInboundReferers;
    private int ownId;

    public PathsFromGCRootsTree(int ownId, HashMap<Integer, PathsFromGCRootsTree> objectInboundReferers, int[] objectIds) {
        this.ownId = ownId;
        this.objectInboundReferers = objectInboundReferers;
        this.objectIds = objectIds;
    }

    public int getOwnId() {
        return this.ownId;
    }

    public int[] getObjectIds() {
        return this.objectIds;
    }

    public PathsFromGCRootsTree getBranch(int objId) {
        return (PathsFromGCRootsTree) this.objectInboundReferers.get(Integer.valueOf(objId));
    }
}
