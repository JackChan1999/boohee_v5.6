package org.eclipse.mat.parser.internal.snapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.eclipse.mat.snapshot.PathsFromGCRootsTree;

public class PathsFromGCRootsTreeBuilder {
    private ArrayList<Integer> objectIds = new ArrayList();
    private HashMap<Integer, PathsFromGCRootsTreeBuilder> objectInboundReferers;
    private int ownId;

    public PathsFromGCRootsTreeBuilder(int ownId) {
        this.ownId = ownId;
        this.objectInboundReferers = new HashMap();
    }

    public HashMap<Integer, PathsFromGCRootsTreeBuilder> getObjectReferers() {
        return this.objectInboundReferers;
    }

    public PathsFromGCRootsTree toPathsFromGCRootsTree() {
        HashMap<Integer, PathsFromGCRootsTree> data = new HashMap(this.objectInboundReferers.size());
        for (Entry<Integer, PathsFromGCRootsTreeBuilder> entry : this.objectInboundReferers.entrySet()) {
            data.put(entry.getKey(), ((PathsFromGCRootsTreeBuilder) entry.getValue()).toPathsFromGCRootsTree());
        }
        int[] children = new int[this.objectIds.size()];
        for (int i = 0; i < children.length; i++) {
            children[i] = ((Integer) this.objectIds.get(i)).intValue();
        }
        return new PathsFromGCRootsTree(this.ownId, data, children);
    }

    public int getOwnId() {
        return this.ownId;
    }

    public void addObjectReferer(PathsFromGCRootsTreeBuilder referer) {
        if (this.objectInboundReferers.put(Integer.valueOf(referer.getOwnId()), referer) == null) {
            this.objectIds.add(Integer.valueOf(referer.getOwnId()));
        }
    }
}
