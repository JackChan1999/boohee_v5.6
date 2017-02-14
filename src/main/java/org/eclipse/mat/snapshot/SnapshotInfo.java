package org.eclipse.mat.snapshot;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SnapshotInfo implements Serializable {
    private static final long serialVersionUID = 4;
    protected Date creationDate;
    protected int identifierSize;
    protected String jvmInfo;
    protected int numberOfClassLoaders;
    protected int numberOfClasses;
    protected int numberOfGCRoots;
    protected int numberOfObjects;
    protected String path;
    protected String prefix;
    protected Map<String, Serializable> properties;
    protected long usedHeapSize;

    public SnapshotInfo(String path, String prefix, String jvmInfo, int identifierSize, Date creationDate, int numberOfObjects, int numberOfGCRoots, int numberOfClasses, int numberOfClassLoaders, long usedHeapSize) {
        this.path = path;
        this.prefix = prefix;
        this.jvmInfo = jvmInfo;
        this.identifierSize = identifierSize;
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
        this.numberOfObjects = numberOfObjects;
        this.numberOfGCRoots = numberOfGCRoots;
        this.numberOfClasses = numberOfClasses;
        this.numberOfClassLoaders = numberOfClassLoaders;
        this.usedHeapSize = usedHeapSize;
        this.properties = new HashMap();
    }

    @Deprecated
    public SnapshotInfo(String path, String jvmInfo, int identifierSize, Date creationDate, int numberOfObjects, int numberOfGCRoots, int numberOfClasses, int numberOfClassLoaders, long usedHeapSize) {
        this(path, prefix(path), jvmInfo, identifierSize, creationDate, numberOfObjects, numberOfGCRoots, numberOfClasses, numberOfClassLoaders, usedHeapSize);
    }

    private static String prefix(String path) {
        int p = path.lastIndexOf(46);
        return p >= 0 ? path.substring(0, p + 1) : path + '.';
    }

    public Serializable getProperty(String name) {
        return (Serializable) this.properties.get(name);
    }

    public Serializable setProperty(String name, Serializable value) {
        return (Serializable) this.properties.put(name, value);
    }

    public String getPath() {
        return this.path;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getJvmInfo() {
        return this.jvmInfo;
    }

    public int getIdentifierSize() {
        return this.identifierSize;
    }

    public Date getCreationDate() {
        return this.creationDate != null ? new Date(this.creationDate.getTime()) : null;
    }

    public int getNumberOfObjects() {
        return this.numberOfObjects;
    }

    public int getNumberOfGCRoots() {
        return this.numberOfGCRoots;
    }

    public int getNumberOfClasses() {
        return this.numberOfClasses;
    }

    public int getNumberOfClassLoaders() {
        return this.numberOfClassLoaders;
    }

    public long getUsedHeapSize() {
        return this.usedHeapSize;
    }

    public String toString() {
        StringBuilder summary = new StringBuilder();
        summary.append("Path: ");
        summary.append(this.path);
        summary.append("\r\nJVM Info: ");
        summary.append(this.jvmInfo);
        summary.append("\r\nIdentifier Size: ");
        summary.append(this.identifierSize);
        summary.append("\r\nCreation Date: ");
        summary.append(this.creationDate);
        summary.append("\r\nNumber of Objects: ");
        summary.append(this.numberOfObjects);
        summary.append("\r\nNumber of GC roots: ");
        summary.append(this.numberOfGCRoots);
        summary.append("\r\nNumber of Classes: ");
        summary.append(this.numberOfClasses);
        summary.append("\r\nNumber of ClassLoaders: ");
        summary.append(this.numberOfClassLoaders);
        summary.append("\r\nUsed Heap Size: ");
        summary.append(this.usedHeapSize);
        return summary.toString();
    }
}
