package org.eclipse.mat.parser.internal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.parser.index.IndexManager;
import org.eclipse.mat.parser.internal.util.ParserRegistry.Parser;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.XGCRootInfo;
import org.eclipse.mat.parser.model.XSnapshotInfo;
import org.eclipse.mat.snapshot.model.IClass;

public class SnapshotImplBuilder {
    BitField arrayObjects;
    HashMapIntObject<ClassImpl> classCache;
    Map<String, List<IClass>> classCacheByName;
    IndexManager indexManager;
    private HashMapIntObject<XGCRootInfo[]> roots;
    private HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread;
    private XSnapshotInfo snapshotInfo;

    public SnapshotImplBuilder(XSnapshotInfo snapshotInfo) {
        this.snapshotInfo = snapshotInfo;
    }

    public XSnapshotInfo getSnapshotInfo() {
        return this.snapshotInfo;
    }

    public void setIndexManager(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    public IndexManager getIndexManager() {
        return this.indexManager;
    }

    public void setClassCache(HashMapIntObject<ClassImpl> classCache) {
        this.classCache = classCache;
    }

    public HashMapIntObject<ClassImpl> getClassCache() {
        return this.classCache;
    }

    public void setRoots(HashMapIntObject<XGCRootInfo[]> roots) {
        this.roots = roots;
    }

    public HashMapIntObject<XGCRootInfo[]> getRoots() {
        return this.roots;
    }

    public void setRootsPerThread(HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread) {
        this.rootsPerThread = rootsPerThread;
    }

    public void setArrayObjects(BitField arrayObjects) {
        this.arrayObjects = arrayObjects;
    }

    public SnapshotImpl create(Parser parser) throws IOException, SnapshotException {
        return SnapshotImpl.create(this.snapshotInfo, parser.getObjectReader(), this.classCache, this.roots, this.rootsPerThread, this.arrayObjects, this.indexManager);
    }
}
