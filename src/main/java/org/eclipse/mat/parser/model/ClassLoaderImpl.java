package org.eclipse.mat.parser.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.parser.internal.SnapshotImpl;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IClassLoader;
import org.eclipse.mat.snapshot.registry.ClassSpecificNameResolverRegistry;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.VoidProgressListener;

public class ClassLoaderImpl extends InstanceImpl implements IClassLoader {
    public static final String NO_LABEL = "__none__";
    private static final long serialVersionUID = 1;
    private volatile transient List<IClass> definedClasses = null;

    public ClassLoaderImpl(int objectId, long address, ClassImpl clazz, List<Field> fields) {
        super(objectId, address, clazz, fields);
    }

    protected synchronized void readFully() {
        if (getObjectAddress() == 0) {
            setFields(new ArrayList());
        } else {
            super.readFully();
        }
    }

    public String getClassSpecificName() {
        String label = this.source.getClassLoaderLabel(getObjectId());
        if (NO_LABEL.equals(label)) {
            label = ClassSpecificNameResolverRegistry.resolve(this);
            if (label != null) {
                this.source.setClassLoaderLabel(getObjectId(), label);
            }
        }
        return label;
    }

    public List<IClass> getDefinedClasses() throws SnapshotException {
        List<IClass> result = this.definedClasses;
        if (result == null) {
            synchronized (this) {
                if (result == null) {
                    result = doGetDefinedClasses(this.source, getObjectId());
                    this.definedClasses = result;
                }
            }
        }
        return result;
    }

    public long getRetainedHeapSizeOfObjects(boolean calculateIfNotAvailable, boolean calculateMinRetainedSize, IProgressListener listener) throws SnapshotException {
        return doGetRetainedHeapSizeOfObjects(this.source, getObjectId(), calculateIfNotAvailable, calculateMinRetainedSize, listener);
    }

    public static final List<IClass> doGetDefinedClasses(ISnapshot dump, int classLoaderId) throws SnapshotException {
        List<IClass> answer = new ArrayList();
        for (IClass clasz : dump.getClasses()) {
            if (clasz.getClassLoaderId() == classLoaderId) {
                answer.add(clasz);
            }
        }
        return answer;
    }

    public static final long doGetRetainedHeapSizeOfObjects(ISnapshot dump, int classLoaderId, boolean calculateIfNotAvailable, boolean calculateMinRetainedSize, IProgressListener listener) throws SnapshotException {
        long answer = ((SnapshotImpl) dump).getRetainedSizeCache().get(classLoaderId);
        if (answer > 0 || !calculateIfNotAvailable) {
            return answer;
        }
        if (answer < 0 && calculateMinRetainedSize) {
            return answer;
        }
        long retainedSize;
        if (listener == null) {
            listener = new VoidProgressListener();
        }
        ArrayInt objectIds = new ArrayInt();
        objectIds.add(classLoaderId);
        for (IClass clasz : doGetDefinedClasses(dump, classLoaderId)) {
            objectIds.add(clasz.getObjectId());
            objectIds.addAll(clasz.getObjectIds());
        }
        if (calculateMinRetainedSize) {
            retainedSize = dump.getMinRetainedSize(objectIds.toArray(), listener);
            if (listener.isCanceled()) {
                return 0;
            }
        }
        int[] retainedSet = dump.getRetainedSet(objectIds.toArray(), listener);
        if (listener.isCanceled()) {
            return 0;
        }
        retainedSize = dump.getHeapSize(retainedSet);
        if (calculateMinRetainedSize) {
            retainedSize = -retainedSize;
        }
        ((SnapshotImpl) dump).getRetainedSizeCache().put(classLoaderId, retainedSize);
        return retainedSize;
    }
}
