package org.eclipse.mat.parser.model;

import java.util.Date;
import org.eclipse.mat.snapshot.SnapshotInfo;

public final class XSnapshotInfo extends SnapshotInfo {
    private static final long serialVersionUID = 3;

    public XSnapshotInfo() {
        super(null, null, null, 0, null, 0, 0, 0, 0, 0);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = new Date(creationDate.getTime());
    }

    public void setIdentifierSize(int identifierSize) {
        this.identifierSize = identifierSize;
    }

    public void setJvmInfo(String jvmInfo) {
        this.jvmInfo = jvmInfo;
    }

    public void setNumberOfClasses(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public void setNumberOfClassLoaders(int numberOfClassLoaders) {
        this.numberOfClassLoaders = numberOfClassLoaders;
    }

    public void setNumberOfGCRoots(int numberOfGCRoots) {
        this.numberOfGCRoots = numberOfGCRoots;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    public void setUsedHeapSize(long usedHeapSize) {
        this.usedHeapSize = usedHeapSize;
    }
}
