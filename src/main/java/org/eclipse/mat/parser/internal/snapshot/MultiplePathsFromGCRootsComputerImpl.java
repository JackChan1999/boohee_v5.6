package org.eclipse.mat.parser.internal.snapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.QueueInt;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.internal.SnapshotImpl;
import org.eclipse.mat.snapshot.IMultiplePathsFromGCRootsComputer;
import org.eclipse.mat.snapshot.MultiplePathsFromGCRootsClassRecord;
import org.eclipse.mat.snapshot.MultiplePathsFromGCRootsRecord;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;

public class MultiplePathsFromGCRootsComputerImpl implements IMultiplePathsFromGCRootsComputer {
    private static final int NOT_VISITED = -2;
    private static final int NO_PARENT = -1;
    private BitField excludeInstances;
    private Map<IClass, Set<String>> excludeMap;
    int[] objectIds;
    IOne2ManyIndex outboundIndex;
    Object[] paths;
    private boolean pathsCalculated;
    SnapshotImpl snapshot;

    public MultiplePathsFromGCRootsComputerImpl(int[] objectIds, Map<IClass, Set<String>> excludeMap, SnapshotImpl snapshot) throws SnapshotException {
        this.snapshot = snapshot;
        this.objectIds = objectIds;
        this.excludeMap = excludeMap;
        this.outboundIndex = snapshot.getIndexManager().outbound;
        if (excludeMap != null) {
            initExcludeInstances();
        }
    }

    private void initExcludeInstances() throws SnapshotException {
        this.excludeInstances = new BitField(this.snapshot.getIndexManager().o2address().size());
        for (IClass clazz : this.excludeMap.keySet()) {
            for (int objId : clazz.getObjectIds()) {
                this.excludeInstances.set(objId);
            }
        }
    }

    private void computePaths(IProgressListener progressListener) throws SnapshotException {
        ArrayList<int[]> pathsList = new ArrayList();
        int[] parent = bfs(progressListener);
        for (int pathFromBFS : this.objectIds) {
            int[] path = getPathFromBFS(pathFromBFS, parent);
            if (path != null) {
                pathsList.add(path);
            }
        }
        this.pathsCalculated = true;
        this.paths = pathsList.toArray();
    }

    public MultiplePathsFromGCRootsRecord[] getPathsByGCRoot(IProgressListener progressListener) throws SnapshotException {
        if (!this.pathsCalculated) {
            computePaths(progressListener);
        }
        MultiplePathsFromGCRootsRecord dummy = new MultiplePathsFromGCRootsRecord(-1, -1, this.snapshot);
        for (Object obj : this.paths) {
            dummy.addPath((int[]) obj);
        }
        return dummy.nextLevel();
    }

    public Object[] getAllPaths(IProgressListener progressListener) throws SnapshotException {
        if (!this.pathsCalculated) {
            computePaths(progressListener);
        }
        return this.paths;
    }

    public MultiplePathsFromGCRootsClassRecord[] getPathsGroupedByClass(boolean startFromTheGCRoots, IProgressListener progressListener) throws SnapshotException {
        if (!this.pathsCalculated) {
            computePaths(progressListener);
        }
        MultiplePathsFromGCRootsClassRecord dummy = new MultiplePathsFromGCRootsClassRecord(null, -1, startFromTheGCRoots, this.snapshot);
        for (Object obj : this.paths) {
            dummy.addPath((int[]) obj);
        }
        return dummy.nextLevel();
    }

    private boolean refersOnlyThroughExcluded(int referrerId, int referentId) throws SnapshotException {
        if (!this.excludeInstances.get(referrerId)) {
            return false;
        }
        IObject referrerObject = this.snapshot.getObject(referrerId);
        Set<String> excludeFields = (Set) this.excludeMap.get(referrerObject.getClazz());
        if (excludeFields == null) {
            return true;
        }
        long referentAddr = this.snapshot.mapIdToAddress(referentId);
        for (NamedReference reference : referrerObject.getOutboundReferences()) {
            if (referentAddr == reference.getObjectAddress() && !excludeFields.contains(reference.getName())) {
                return false;
            }
        }
        return true;
    }

    private int[] bfs(IProgressListener progressListener) throws SnapshotException {
        int numObjects = this.snapshot.getSnapshotInfo().getNumberOfObjects();
        boolean skipReferences = this.excludeMap != null;
        int[] parent = new int[numObjects];
        Arrays.fill(parent, -2);
        boolean[] toBeChecked = new boolean[numObjects];
        int count = 0;
        for (int i : this.objectIds) {
            if (!toBeChecked[i]) {
                count++;
            }
            toBeChecked[i] = true;
        }
        QueueInt fifo = new QueueInt(numObjects / 8);
        for (int root : this.snapshot.getGCRoots()) {
            fifo.put(root);
            parent[root] = -1;
        }
        int countVisitedObjects = 0;
        int reportFrequency = Math.max(10, numObjects / 100);
        progressListener.beginTask(Messages.MultiplePathsFromGCRootsComputerImpl_FindingPaths, 100);
        while (fifo.size() > 0 && count > 0) {
            int objectId = fifo.get();
            if (toBeChecked[objectId]) {
                count--;
            }
            for (int child : this.outboundIndex.get(objectId)) {
                if (parent[child] == -2 && !(skipReferences && refersOnlyThroughExcluded(objectId, child))) {
                    parent[child] = objectId;
                    fifo.put(child);
                }
            }
            countVisitedObjects++;
            if (countVisitedObjects % reportFrequency == 0) {
                if (progressListener.isCanceled()) {
                    throw new OperationCanceledException();
                }
                progressListener.worked(1);
            }
        }
        progressListener.done();
        return parent;
    }

    private int[] getPathFromBFS(int objectId, int[] parent) {
        if (parent[objectId] == -2) {
            return null;
        }
        ArrayInt path = new ArrayInt();
        while (objectId != -1) {
            path.add(objectId);
            objectId = parent[objectId];
        }
        return path.toArray();
    }
}
