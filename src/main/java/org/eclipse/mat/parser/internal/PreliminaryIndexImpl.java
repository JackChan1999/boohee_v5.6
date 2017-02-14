package org.eclipse.mat.parser.internal;

import java.util.List;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.parser.IPreliminaryIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.XGCRootInfo;
import org.eclipse.mat.parser.model.XSnapshotInfo;

class PreliminaryIndexImpl implements IPreliminaryIndex {
    IOne2OneIndex array2size = null;
    HashMapIntObject<ClassImpl> classesById;
    HashMapIntObject<List<XGCRootInfo>> gcRoots;
    IOne2LongIndex identifiers = null;
    IOne2OneIndex object2classId = null;
    IOne2ManyIndex outbound = null;
    XSnapshotInfo snapshotInfo;
    HashMapIntObject<HashMapIntObject<List<XGCRootInfo>>> thread2objects2roots;

    public PreliminaryIndexImpl(XSnapshotInfo snapshotInfo) {
        this.snapshotInfo = snapshotInfo;
    }

    public XSnapshotInfo getSnapshotInfo() {
        return this.snapshotInfo;
    }

    public void setClassesById(HashMapIntObject<ClassImpl> classesById) {
        this.classesById = classesById;
    }

    public void setGcRoots(HashMapIntObject<List<XGCRootInfo>> gcRoots) {
        this.gcRoots = gcRoots;
    }

    public void setThread2objects2roots(HashMapIntObject<HashMapIntObject<List<XGCRootInfo>>> thread2objects2roots) {
        this.thread2objects2roots = thread2objects2roots;
    }

    public void setOutbound(IOne2ManyIndex outbound) {
        this.outbound = outbound;
    }

    public void setIdentifiers(IOne2LongIndex identifiers) {
        this.identifiers = identifiers;
    }

    public void setObject2classId(IOne2OneIndex object2classId) {
        this.object2classId = object2classId;
    }

    public void setArray2size(IOne2OneIndex array2size) {
        this.array2size = array2size;
    }

    public void delete() {
    }
}
