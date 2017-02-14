package org.eclipse.mat.snapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.HashMapIntObject;

public class MultiplePathsFromGCRootsRecord {
    final int level;
    final int objectId;
    List<int[]> paths;
    long referencedRetainedSize;
    long referencedSize = -1;
    final ISnapshot snapshot;

    public long getReferencedRetainedSize() {
        return this.referencedRetainedSize;
    }

    public void setReferencedRetainedSize(long referencedRetainedSize) {
        this.referencedRetainedSize = referencedRetainedSize;
    }

    public MultiplePathsFromGCRootsRecord(int objectId, int level, ISnapshot snapshot) {
        this.level = level;
        this.objectId = objectId;
        this.paths = new ArrayList();
        this.snapshot = snapshot;
    }

    public MultiplePathsFromGCRootsRecord[] nextLevel() {
        int new_level = this.level + 1;
        HashMapIntObject<MultiplePathsFromGCRootsRecord> nextLevelRecords = new HashMapIntObject();
        for (int[] path : this.paths) {
            if (path != null && (path.length - new_level) - 1 >= 0) {
                MultiplePathsFromGCRootsRecord record = (MultiplePathsFromGCRootsRecord) nextLevelRecords.get(path[(path.length - new_level) - 1]);
                if (record == null) {
                    record = new MultiplePathsFromGCRootsRecord(path[(path.length - new_level) - 1], new_level, this.snapshot);
                    nextLevelRecords.put(path[(path.length - new_level) - 1], record);
                }
                record.addPath(path);
            }
        }
        return (MultiplePathsFromGCRootsRecord[]) nextLevelRecords.getAllValues(new MultiplePathsFromGCRootsRecord[0]);
    }

    public void addPath(int[] path) {
        this.paths.add(path);
    }

    public List<int[]> getPaths() {
        return this.paths;
    }

    public int getObjectId() {
        return this.objectId;
    }

    public int getCount() {
        return this.paths.size();
    }

    public int getLevel() {
        return this.level;
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

    public long getReferencedHeapSize() throws SnapshotException {
        if (this.referencedSize == -1) {
            this.referencedSize = this.snapshot.getHeapSize(getReferencedObjects());
        }
        return this.referencedSize;
    }

    public static Comparator<MultiplePathsFromGCRootsRecord> getComparatorByNumberOfReferencedObjects() {
        return new Comparator<MultiplePathsFromGCRootsRecord>() {
            public int compare(MultiplePathsFromGCRootsRecord o1, MultiplePathsFromGCRootsRecord o2) {
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

    public static Comparator<MultiplePathsFromGCRootsRecord> getComparatorByReferencedHeapSize() {
        return new Comparator<MultiplePathsFromGCRootsRecord>() {
            public int compare(MultiplePathsFromGCRootsRecord o1, MultiplePathsFromGCRootsRecord o2) {
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

    public static Comparator<MultiplePathsFromGCRootsRecord> getComparatorByReferencedRetainedSize() {
        return new Comparator<MultiplePathsFromGCRootsRecord>() {
            public int compare(MultiplePathsFromGCRootsRecord o1, MultiplePathsFromGCRootsRecord o2) {
                if (o1.getReferencedRetainedSize() < o2.getReferencedRetainedSize()) {
                    return 1;
                }
                if (o1.getReferencedRetainedSize() > o2.getReferencedRetainedSize()) {
                    return -1;
                }
                return 0;
            }
        };
    }
}
