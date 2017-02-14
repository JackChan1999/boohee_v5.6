package org.eclipse.mat.snapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.SetInt;
import org.eclipse.mat.snapshot.model.IClass;

public class MultiplePathsFromGCRootsClassRecord {
    private IClass clazz;
    private SetInt distinctObjects;
    private boolean fromRoots;
    private int level;
    private List<int[]> paths = new ArrayList();
    private long referencedSize = -1;
    private ISnapshot snapshot;

    public MultiplePathsFromGCRootsClassRecord(IClass clazz, int level, boolean fromRoots, ISnapshot snapshot) {
        this.clazz = clazz;
        this.level = level;
        this.fromRoots = fromRoots;
        this.snapshot = snapshot;
    }

    public MultiplePathsFromGCRootsClassRecord[] nextLevel() throws SnapshotException {
        int nextLevel = this.level + 1;
        if (nextLevel < 0) {
            return null;
        }
        HashMapIntObject<MultiplePathsFromGCRootsClassRecord> nextLevelRecords = new HashMapIntObject();
        for (int[] path : this.paths) {
            if (path != null) {
                int newIndex;
                if (this.fromRoots) {
                    newIndex = (path.length - nextLevel) - 1;
                } else {
                    newIndex = nextLevel;
                }
                if (newIndex >= 0 && newIndex < path.length) {
                    IClass clazz = this.snapshot.getClassOf(path[newIndex]);
                    MultiplePathsFromGCRootsClassRecord record = (MultiplePathsFromGCRootsClassRecord) nextLevelRecords.get(clazz.getObjectId());
                    if (record == null) {
                        record = new MultiplePathsFromGCRootsClassRecord(clazz, nextLevel, this.fromRoots, this.snapshot);
                        nextLevelRecords.put(clazz.getObjectId(), record);
                    }
                    record.addPath(path);
                }
            }
        }
        return (MultiplePathsFromGCRootsClassRecord[]) nextLevelRecords.getAllValues(new MultiplePathsFromGCRootsClassRecord[0]);
    }

    public void addPath(int[] path) {
        this.paths.add(path);
    }

    public List<int[]> getPaths() {
        return this.paths;
    }

    public int getCount() {
        return this.paths.size();
    }

    public int getDistinctCount() {
        if (this.distinctObjects == null) {
            this.distinctObjects = new SetInt();
            for (int[] path : this.paths) {
                this.distinctObjects.add(path[this.fromRoots ? (path.length - this.level) - 1 : this.level]);
            }
        }
        return this.distinctObjects.size();
    }

    public long getReferencedHeapSize() throws SnapshotException {
        if (this.referencedSize == -1) {
            this.referencedSize = this.snapshot.getHeapSize(getReferencedObjects());
        }
        return this.referencedSize;
    }

    public int[] getReferencedObjects() {
        int[] result = new int[this.paths.size()];
        int i = 0;
        for (int[] path : this.paths) {
            int i2 = i + 1;
            result[i] = path[0];
            i = i2;
        }
        return result;
    }

    public static Comparator<MultiplePathsFromGCRootsClassRecord> getComparatorByNumberOfReferencedObjects() {
        return new Comparator<MultiplePathsFromGCRootsClassRecord>() {
            public int compare(MultiplePathsFromGCRootsClassRecord o1, MultiplePathsFromGCRootsClassRecord o2) {
                if (o1.paths.size() < o2.paths.size()) {
                    return 1;
                }
                if (o1.paths.size() > o2.paths.size()) {
                    return -1;
                }
                return 0;
            }
        };
    }

    public static Comparator<MultiplePathsFromGCRootsClassRecord> getComparatorByReferencedHeapSize() {
        return new Comparator<MultiplePathsFromGCRootsClassRecord>() {
            public int compare(MultiplePathsFromGCRootsClassRecord o1, MultiplePathsFromGCRootsClassRecord o2) {
                try {
                    if (o1.getReferencedHeapSize() < o2.getReferencedHeapSize()) {
                        return 1;
                    }
                    if (o1.getReferencedHeapSize() > o2.getReferencedHeapSize()) {
                        return -1;
                    }
                    return 0;
                } catch (SnapshotException e) {
                    return 0;
                }
            }
        };
    }

    public IClass getClazz() {
        return this.clazz;
    }

    public boolean isFromRoots() {
        return this.fromRoots;
    }

    public int getLevel() {
        return this.level;
    }
}
