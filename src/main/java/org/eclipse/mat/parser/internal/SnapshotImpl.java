package org.eclipse.mat.parser.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.ArrayIntBig;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.collect.SetInt;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.IObjectReader;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.index.IndexManager;
import org.eclipse.mat.parser.internal.snapshot.MultiplePathsFromGCRootsComputerImpl;
import org.eclipse.mat.parser.internal.snapshot.ObjectCache;
import org.eclipse.mat.parser.internal.snapshot.ObjectMarker;
import org.eclipse.mat.parser.internal.snapshot.PathsFromGCRootsTreeBuilder;
import org.eclipse.mat.parser.internal.snapshot.RetainedSizeCache;
import org.eclipse.mat.parser.internal.util.IntStack;
import org.eclipse.mat.parser.model.AbstractObjectImpl;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.ClassLoaderImpl;
import org.eclipse.mat.parser.model.InstanceImpl;
import org.eclipse.mat.parser.model.XGCRootInfo;
import org.eclipse.mat.parser.model.XSnapshotInfo;
import org.eclipse.mat.snapshot.DominatorsSummary;
import org.eclipse.mat.snapshot.DominatorsSummary.ClassDominatorRecord;
import org.eclipse.mat.snapshot.ExcludedReferencesDescriptor;
import org.eclipse.mat.snapshot.IMultiplePathsFromGCRootsComputer;
import org.eclipse.mat.snapshot.IPathsFromGCRootsComputer;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.PathsFromGCRootsTree;
import org.eclipse.mat.snapshot.model.GCRootInfo;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IThreadStack;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.MessageUtil;
import org.eclipse.mat.util.VoidProgressListener;

public final class SnapshotImpl implements ISnapshot {
    private BitField arrayObjects;
    private HashMapIntObject<ClassImpl> classCache;
    private Map<String, List<IClass>> classCacheByName;
    private boolean dominatorTreeCalculated;
    private IObjectReader heapObjectReader;
    private IndexManager indexManager;
    private HashMapIntObject<String> loaderLabels;
    private ObjectCache<IObject> objectCache;
    private boolean parsedThreads = false;
    private RetainedSizeCache retainedSizeCache;
    private HashMapIntObject<XGCRootInfo[]> roots;
    private HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread;
    private XSnapshotInfo snapshotInfo;
    HashMapIntObject<IThreadStack> threadId2stack;

    private static final class HeapObjectCache extends ObjectCache<IObject> {
        SnapshotImpl snapshot;

        private HeapObjectCache(SnapshotImpl snapshot, int maxSize) {
            super(maxSize);
            this.snapshot = snapshot;
        }

        protected IObject load(int objectId) {
            IOException e;
            SnapshotException e2;
            IObject answer;
            try {
                if (this.snapshot.isArray(objectId)) {
                    answer = this.snapshot.heapObjectReader.read(objectId, this.snapshot);
                } else {
                    ClassImpl classImpl = (ClassImpl) this.snapshot.getObject(this.snapshot.indexManager.o2class().get(objectId));
                    if (this.snapshot.isClassLoader(objectId)) {
                        answer = new ClassLoaderImpl(objectId, Long.MIN_VALUE, classImpl, null);
                    } else {
                        answer = new InstanceImpl(objectId, Long.MIN_VALUE, classImpl, null);
                    }
                }
                try {
                    ((AbstractObjectImpl) answer).setSnapshot(this.snapshot);
                    return answer;
                } catch (IOException e3) {
                    e = e3;
                    throw new RuntimeException(e);
                } catch (SnapshotException e4) {
                    e2 = e4;
                    throw new RuntimeException(e2);
                }
            } catch (IOException e5) {
                e = e5;
                answer = null;
                throw new RuntimeException(e);
            } catch (SnapshotException e6) {
                e2 = e6;
                answer = null;
                throw new RuntimeException(e2);
            }
        }
    }

    private static class Path {
        int index;
        Path next;

        public Path(int index, Path next) {
            this.index = index;
            this.next = next;
        }

        public Path getNext() {
            return this.next;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean contains(long id) {
            for (Path p = this; p != null; p = p.next) {
                if (((long) p.index) == id) {
                    return true;
                }
            }
            return false;
        }
    }

    private class PathsFromGCRootsComputerImpl implements IPathsFromGCRootsComputer {
        int currentId;
        Path currentPath;
        int[] currentReferrers;
        int currentReferringThread;
        BitField excludeInstances;
        Map<IClass, Set<String>> excludeMap;
        LinkedList<Path> fifo = new LinkedList();
        int[] foundPath;
        IOne2ManyIndex inboundIndex;
        int lastReadReferrer;
        private int nextState;
        int objectId;
        int[] referringThreads;
        private int state;
        BitField visited = new BitField(SnapshotImpl.this.indexManager.o2address().size());

        public PathsFromGCRootsComputerImpl(int objectId, Map<IClass, Set<String>> excludeMap) throws SnapshotException {
            this.objectId = objectId;
            this.excludeMap = excludeMap;
            this.inboundIndex = SnapshotImpl.this.indexManager.inbound();
            if (excludeMap != null) {
                initExcludeInstances();
            }
            this.currentId = objectId;
            this.visited.set(objectId);
            if (SnapshotImpl.this.roots.get(objectId) == null) {
                this.fifo.add(new Path(objectId, null));
            }
        }

        private void initExcludeInstances() throws SnapshotException {
            this.excludeInstances = new BitField(SnapshotImpl.this.indexManager.o2address().size());
            for (IClass clazz : this.excludeMap.keySet()) {
                for (int objId : clazz.getObjectIds()) {
                    this.excludeInstances.set(objId);
                }
            }
        }

        private boolean refersOnlyThroughExcluded(int referrerId, int referentId) throws SnapshotException {
            if (!this.excludeInstances.get(referrerId)) {
                return false;
            }
            IObject referrerObject = SnapshotImpl.this.getObject(referrerId);
            Set<String> excludeFields = (Set) this.excludeMap.get(referrerObject.getClazz());
            if (excludeFields == null) {
                return true;
            }
            long referentAddr = SnapshotImpl.this.mapIdToAddress(referentId);
            for (NamedReference reference : referrerObject.getOutboundReferences()) {
                if (referentAddr == reference.getObjectAddress() && !excludeFields.contains(reference.getName())) {
                    return false;
                }
            }
            return true;
        }

        public int[] getNextShortestPath() throws SnapshotException {
            switch (this.state) {
                case 0:
                    if (SnapshotImpl.this.roots.containsKey(this.currentId)) {
                        this.referringThreads = null;
                        this.state = 2;
                        this.nextState = 1;
                        this.foundPath = new int[]{this.currentId};
                        return getNextShortestPath();
                    }
                    this.state = 3;
                    return getNextShortestPath();
                case 1:
                    return null;
                case 2:
                    if (this.referringThreads == null) {
                        this.referringThreads = getReferringTreads(SnapshotImpl.this.getGCRootInfo(this.foundPath[this.foundPath.length - 1]));
                        this.currentReferringThread = 0;
                        if (this.referringThreads.length == 0) {
                            this.state = this.nextState;
                            return this.foundPath;
                        }
                    }
                    if (this.currentReferringThread < this.referringThreads.length) {
                        int[] result = new int[(this.foundPath.length + 1)];
                        System.arraycopy(this.foundPath, 0, result, 0, this.foundPath.length);
                        result[result.length - 1] = this.referringThreads[this.currentReferringThread];
                        this.currentReferringThread++;
                        return result;
                    }
                    this.state = this.nextState;
                    return getNextShortestPath();
                case 3:
                    int[] res;
                    if (this.currentReferrers != null) {
                        res = processCurrentReferrefs(this.lastReadReferrer + 1);
                        if (res != null) {
                            return res;
                        }
                    }
                    while (this.fifo.size() > 0) {
                        this.currentPath = (Path) this.fifo.getFirst();
                        this.fifo.removeFirst();
                        this.currentId = this.currentPath.getIndex();
                        this.currentReferrers = this.inboundIndex.get(this.currentId);
                        if (this.currentReferrers != null) {
                            res = processCurrentReferrefs(0);
                            if (res != null) {
                                return res;
                            }
                        }
                    }
                    return null;
                default:
                    throw new RuntimeException(Messages.SnapshotImpl_Error_UnrecognizedState.pattern + this.state);
            }
        }

        private int[] getReferringTreads(GCRootInfo[] rootInfos) {
            SetInt threads = new SetInt();
            for (GCRootInfo info : rootInfos) {
                if (!(info.getContextAddress() == 0 || info.getObjectAddress() == info.getContextAddress())) {
                    threads.add(info.getContextId());
                }
            }
            return threads.toArray();
        }

        public PathsFromGCRootsTree getTree(Collection<int[]> paths) {
            PathsFromGCRootsTreeBuilder rootBuilder = new PathsFromGCRootsTreeBuilder(this.objectId);
            for (int[] path : paths) {
                PathsFromGCRootsTreeBuilder current = rootBuilder;
                for (int k = 1; k < path.length; k++) {
                    int childId = path[k];
                    PathsFromGCRootsTreeBuilder child = (PathsFromGCRootsTreeBuilder) current.getObjectReferers().get(Integer.valueOf(childId));
                    if (child == null) {
                        child = new PathsFromGCRootsTreeBuilder(childId);
                        current.addObjectReferer(child);
                    }
                    current = child;
                }
            }
            return rootBuilder.toPathsFromGCRootsTree();
        }

        private int[] path2Int(Path p) {
            IntStack s = new IntStack();
            while (p != null) {
                s.push(p.getIndex());
                p = p.getNext();
            }
            int[] res = new int[s.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = s.pop();
            }
            return res;
        }

        private int[] processCurrentReferrefs(int fromIndex) throws SnapshotException {
            for (int i = fromIndex; i < this.currentReferrers.length; i++) {
                if (((GCRootInfo[]) SnapshotImpl.this.roots.get(this.currentReferrers[i])) != null) {
                    Path p;
                    if (this.excludeMap == null) {
                        this.lastReadReferrer = i;
                        p = new Path(this.currentReferrers[i], this.currentPath);
                        this.referringThreads = null;
                        this.state = 2;
                        this.nextState = 3;
                        this.foundPath = path2Int(p);
                        return getNextShortestPath();
                    } else if (!refersOnlyThroughExcluded(this.currentReferrers[i], this.currentId)) {
                        this.lastReadReferrer = i;
                        p = new Path(this.currentReferrers[i], this.currentPath);
                        this.referringThreads = null;
                        this.state = 2;
                        this.nextState = 3;
                        this.foundPath = path2Int(p);
                        return getNextShortestPath();
                    }
                }
            }
            for (int referrer : this.currentReferrers) {
                if (!(referrer < 0 || this.visited.get(referrer) || SnapshotImpl.this.roots.containsKey(referrer))) {
                    if (this.excludeMap == null) {
                        this.fifo.add(new Path(referrer, this.currentPath));
                        this.visited.set(referrer);
                    } else if (!refersOnlyThroughExcluded(referrer, this.currentId)) {
                        this.fifo.add(new Path(referrer, this.currentPath));
                        this.visited.set(referrer);
                    }
                }
            }
            return null;
        }
    }

    public static SnapshotImpl create(XSnapshotInfo snapshotInfo, IObjectReader heapObjectReader, HashMapIntObject<ClassImpl> classCache, HashMapIntObject<XGCRootInfo[]> roots, HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread, BitField arrayObjects, IndexManager indexManager) throws IOException, SnapshotException {
        SnapshotImpl answer = new SnapshotImpl(snapshotInfo, heapObjectReader, classCache, roots, rootsPerThread, null, arrayObjects, indexManager);
        answer.calculateLoaderLabels();
        return answer;
    }

    private SnapshotImpl(XSnapshotInfo snapshotInfo, IObjectReader heapObjectReader, HashMapIntObject<ClassImpl> classCache, HashMapIntObject<XGCRootInfo[]> roots, HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread, HashMapIntObject<String> loaderLabels, BitField arrayObjects, IndexManager indexManager) throws SnapshotException, IOException {
        boolean z = false;
        this.snapshotInfo = snapshotInfo;
        this.heapObjectReader = heapObjectReader;
        this.classCache = classCache;
        this.roots = roots;
        this.rootsPerThread = rootsPerThread;
        this.loaderLabels = loaderLabels;
        this.arrayObjects = arrayObjects;
        this.indexManager = indexManager;
        this.retainedSizeCache = new RetainedSizeCache(snapshotInfo);
        this.classCacheByName = new HashMap(this.classCache.size());
        Iterator<ClassImpl> iter = this.classCache.values();
        while (iter.hasNext()) {
            ClassImpl clasz = (ClassImpl) iter.next();
            clasz.setSnapshot(this);
            List<IClass> list = (List) this.classCacheByName.get(clasz.getName());
            if (list == null) {
                Map map = this.classCacheByName;
                String name = clasz.getName();
                list = new ArrayList();
                map.put(name, list);
            }
            list.add(clasz);
        }
        if (!(indexManager.dominated() == null || indexManager.o2retained() == null || indexManager.dominator() == null)) {
            z = true;
        }
        this.dominatorTreeCalculated = z;
        this.objectCache = new HeapObjectCache(1000);
        this.heapObjectReader.open(this);
    }

    private void calculateLoaderLabels() throws SnapshotException {
        this.loaderLabels = new HashMapIntObject();
        long usedHeapSize = 0;
        int systemClassLoaderId = this.indexManager.o2address().reverse(0);
        Object[] classes = this.classCache.getAllValues();
        for (ClassImpl clasz : classes) {
            String label;
            usedHeapSize += clasz.getTotalSize();
            int classLoaderId = clasz.getClassLoaderId();
            if (((String) this.loaderLabels.get(classLoaderId)) == null) {
                if (classLoaderId == systemClassLoaderId) {
                    label = "<system class loader>";
                } else {
                    label = getObject(classLoaderId).getClassSpecificName();
                    if (label == null) {
                        label = ClassLoaderImpl.NO_LABEL;
                    }
                }
                this.loaderLabels.put(classLoaderId, label);
            }
        }
        Collection<IClass> loaderClasses = getClassesByName(IClass.JAVA_LANG_CLASSLOADER, true);
        if (loaderClasses != null) {
            for (IClass clazz : loaderClasses) {
                for (int classLoaderId2 : clazz.getObjectIds()) {
                    if (((String) this.loaderLabels.get(classLoaderId2)) == null) {
                        if (classLoaderId2 == systemClassLoaderId) {
                            label = "<system class loader>";
                        } else {
                            label = getObject(classLoaderId2).getClassSpecificName();
                            if (label == null) {
                                label = ClassLoaderImpl.NO_LABEL;
                            }
                        }
                        this.loaderLabels.put(classLoaderId2, label);
                    }
                }
            }
        }
        this.snapshotInfo.setUsedHeapSize(usedHeapSize);
        this.snapshotInfo.setNumberOfObjects(this.indexManager.idx.size());
        this.snapshotInfo.setNumberOfClassLoaders(this.loaderLabels.size());
        this.snapshotInfo.setNumberOfGCRoots(this.roots.size());
        this.snapshotInfo.setNumberOfClasses(this.classCache.size());
        this.objectCache.clear();
    }

    public XSnapshotInfo getSnapshotInfo() {
        return this.snapshotInfo;
    }

    public int[] getGCRoots() throws SnapshotException {
        return this.roots.getAllKeys();
    }

    public Collection<IClass> getClasses() throws SnapshotException {
        return Arrays.asList(this.classCache.getAllValues(new IClass[this.classCache.size()]));
    }

    public Collection<IClass> getClassesByName(String name, boolean includeSubClasses) throws SnapshotException {
        List<IClass> list = (List) this.classCacheByName.get(name);
        if (list == null) {
            return null;
        }
        if (!includeSubClasses) {
            return Collections.unmodifiableCollection(list);
        }
        Collection<IClass> answer = new HashSet();
        answer.addAll(list);
        for (IClass clazz : list) {
            answer.addAll(clazz.getAllSubclasses());
        }
        return answer;
    }

    public Collection<IClass> getClassesByName(Pattern namePattern, boolean includeSubClasses) throws SnapshotException {
        Set<IClass> result = new HashSet();
        Object[] classes = this.classCache.getAllValues();
        for (IClass clazz : classes) {
            if (namePattern.matcher(clazz.getName()).matches()) {
                result.add(clazz);
                if (includeSubClasses) {
                    result.addAll(clazz.getAllSubclasses());
                }
            }
        }
        return result;
    }

    public int[] getInboundRefererIds(int objectId) throws SnapshotException {
        return this.indexManager.inbound().get(objectId);
    }

    public int[] getOutboundReferentIds(int objectId) throws SnapshotException {
        return this.indexManager.outbound().get(objectId);
    }

    public int[] getInboundRefererIds(int[] objectIds, IProgressListener progressMonitor) throws SnapshotException {
        if (progressMonitor == null) {
            progressMonitor = new VoidProgressListener();
        }
        IOne2ManyIndex inbound = this.indexManager.inbound();
        SetInt result = new SetInt();
        progressMonitor.beginTask(Messages.SnapshotImpl_ReadingInboundReferrers, objectIds.length / 100);
        for (int ii = 0; ii < objectIds.length; ii++) {
            for (int refereeId : inbound.get(objectIds[ii])) {
                result.add(refereeId);
            }
            if (ii % 100 == 0) {
                if (progressMonitor.isCanceled()) {
                    return null;
                }
                progressMonitor.worked(1);
            }
        }
        int[] endResult = result.toArray();
        progressMonitor.done();
        return endResult;
    }

    public int[] getOutboundReferentIds(int[] objectIds, IProgressListener progressMonitor) throws SnapshotException {
        if (progressMonitor == null) {
            progressMonitor = new VoidProgressListener();
        }
        IOne2ManyIndex outbound = this.indexManager.outbound();
        SetInt result = new SetInt();
        progressMonitor.beginTask(Messages.SnapshotImpl_ReadingOutboundReferrers, objectIds.length / 100);
        for (int ii = 0; ii < objectIds.length; ii++) {
            for (int refereeId : outbound.get(objectIds[ii])) {
                result.add(refereeId);
            }
            if (ii % 100 == 0) {
                if (progressMonitor.isCanceled()) {
                    return null;
                }
                progressMonitor.worked(1);
            }
        }
        int[] endResult = result.toArray();
        progressMonitor.done();
        return endResult;
    }

    public IPathsFromGCRootsComputer getPathsFromGCRoots(int objectId, Map<IClass, Set<String>> excludeList) throws SnapshotException {
        return new PathsFromGCRootsComputerImpl(objectId, excludeList);
    }

    public IMultiplePathsFromGCRootsComputer getMultiplePathsFromGCRoots(int[] objectIds, Map<IClass, Set<String>> excludeList) throws SnapshotException {
        return new MultiplePathsFromGCRootsComputerImpl(objectIds, excludeList, this);
    }

    int[] getRetainedSetSingleThreaded(int[] objectIds, IProgressListener progressMonitor) throws SnapshotException {
        if (objectIds.length == 0) {
            return new int[0];
        }
        if (objectIds.length == 1) {
            return getSingleObjectRetainedSet(objectIds[0]);
        }
        int numberOfObjects = this.snapshotInfo.getNumberOfObjects();
        if (progressMonitor == null) {
            progressMonitor = new VoidProgressListener();
        }
        boolean[] reachable = new boolean[numberOfObjects];
        for (int objId : objectIds) {
            reachable[objId] = true;
        }
        try {
            int[] retained = new int[(numberOfObjects - new ObjectMarker(this.roots.getAllKeys(), reachable, this.indexManager.outbound(), progressMonitor).markSingleThreaded())];
            for (int objId2 : objectIds) {
                reachable[objId2] = false;
            }
            int i = 0;
            int j = 0;
            while (i < numberOfObjects) {
                int j2;
                if (reachable[i]) {
                    j2 = j;
                } else {
                    j2 = j + 1;
                    retained[j] = i;
                }
                i++;
                j = j2;
            }
            return retained;
        } catch (OperationCanceledException e) {
            return null;
        }
    }

    private int[] getRetainedSetMultiThreaded(int[] objectIds, int availableProcessors, IProgressListener progressMonitor) throws SnapshotException {
        if (objectIds.length == 0) {
            return new int[0];
        }
        if (objectIds.length == 1) {
            return getSingleObjectRetainedSet(objectIds[0]);
        }
        int numberOfObjects = this.snapshotInfo.getNumberOfObjects();
        if (progressMonitor == null) {
            progressMonitor = new VoidProgressListener();
        }
        boolean[] reachable = new boolean[numberOfObjects];
        for (int objId : objectIds) {
            reachable[objId] = true;
        }
        try {
            new ObjectMarker(this.roots.getAllKeys(), reachable, this.indexManager.outbound(), progressMonitor).markMultiThreaded(availableProcessors);
            for (int objId2 : objectIds) {
                reachable[objId2] = false;
            }
            ArrayIntBig retained = new ArrayIntBig();
            for (int i = 0; i < numberOfObjects; i++) {
                if (!reachable[i]) {
                    retained.add(i);
                }
            }
            return retained.toArray();
        } catch (Throwable e) {
            throw new SnapshotException(e);
        }
    }

    public int[] getRetainedSet(int[] objectIds, IProgressListener progressMonitor) throws SnapshotException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (availableProcessors > 1) {
            return getRetainedSetMultiThreaded(objectIds, availableProcessors, progressMonitor);
        }
        return getRetainedSetSingleThreaded(objectIds, progressMonitor);
    }

    public int[] getRetainedSet(int[] objectIds, String[] fieldNames, IProgressListener listener) throws SnapshotException {
        if (objectIds.length == 0) {
            return new int[0];
        }
        int numberOfObjects = this.indexManager.o2address().size();
        if (listener == null) {
            listener = new VoidProgressListener();
        }
        BitField initialSet = new BitField(numberOfObjects);
        for (int objId : objectIds) {
            initialSet.set(objId);
        }
        if (listener.isCanceled()) {
            return null;
        }
        BitField reachable = new BitField(numberOfObjects);
        int[] retained = new int[(numberOfObjects - dfs2(reachable, initialSet, fieldNames))];
        int i = 0;
        int j = 0;
        while (i < numberOfObjects) {
            int j2;
            if (reachable.get(i)) {
                j2 = j;
            } else {
                j2 = j + 1;
                retained[j] = i;
            }
            i++;
            j = j2;
        }
        return retained;
    }

    public int[] getRetainedSet(int[] objectIds, ExcludedReferencesDescriptor[] excludedReferences, IProgressListener progressMonitor) throws SnapshotException {
        boolean[] firstPass = new boolean[getSnapshotInfo().getNumberOfObjects()];
        for (int objId : objectIds) {
            firstPass[objId] = true;
        }
        new ObjectMarker(getGCRoots(), firstPass, getIndexManager().outbound, new VoidProgressListener()).markSingleThreaded(excludedReferences, this);
        for (int objId2 : objectIds) {
            firstPass[objId2] = false;
        }
        boolean[] secondPass = new boolean[firstPass.length];
        System.arraycopy(firstPass, 0, secondPass, 0, firstPass.length);
        new ObjectMarker(objectIds, secondPass, getIndexManager().outbound, new VoidProgressListener()).markSingleThreaded();
        int numObjects = getSnapshotInfo().getNumberOfObjects();
        ArrayIntBig retainedSet = new ArrayIntBig();
        int i = 0;
        while (i < numObjects) {
            if (!firstPass[i] && secondPass[i]) {
                retainedSet.add(i);
            }
            i++;
        }
        return retainedSet.toArray();
    }

    public long getMinRetainedSize(int[] objectIds, IProgressListener progressMonitor) throws UnsupportedOperationException, SnapshotException {
        if (objectIds.length == 1) {
            return getRetainedHeapSize(objectIds[0]);
        }
        if (objectIds.length == 0) {
            return 0;
        }
        long result = 0;
        for (int topAncestorId : getTopAncestorsInDominatorTree(objectIds, progressMonitor)) {
            result += getRetainedHeapSize(topAncestorId);
        }
        return result;
    }

    public int[] getMinRetainedSet(int[] objectIds, IProgressListener progressMonitor) throws UnsupportedOperationException, SnapshotException {
        if (objectIds.length == 1) {
            return getSingleObjectRetainedSet(objectIds[0]);
        }
        int i$;
        SetInt setInt = new SetInt(objectIds.length * 2);
        for (int i : objectIds) {
            int i2;
            setInt.add(i2);
        }
        setInt = new SetInt(objectIds.length * 2);
        int tempSize = 0;
        int tempCapacity = 10240;
        int[] temp = new int[10240];
        IOne2OneIndex dominatorIdx = this.indexManager.dominator();
        IOne2ManyIndex dominated = this.indexManager.dominated();
        int capacity = 10240;
        int[] stack = new int[10240];
        int iterations = 0;
        int[] iArr = objectIds;
        int len$ = iArr.length;
        int i$2 = 0;
        int size = 0;
        while (i$2 < len$) {
            int objectId = iArr[i$2];
            iterations++;
            if ((65535 & iterations) == 0 && progressMonitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            int size2;
            int dominatorId = dominatorIdx.get(objectId) - 2;
            boolean save = true;
            int tempSize2 = tempSize;
            while (dominatorId > -1) {
                if (tempSize2 == tempCapacity) {
                    int newCapacity = tempCapacity << 1;
                    Object newArr = new int[newCapacity];
                    System.arraycopy(temp, 0, newArr, 0, tempCapacity);
                    temp = newArr;
                    tempCapacity = newCapacity;
                }
                tempSize = tempSize2 + 1;
                temp[tempSize2] = dominatorId;
                if (!setInt.contains(dominatorId)) {
                    if (setInt.contains(dominatorId)) {
                        break;
                    }
                    dominatorId = dominatorIdx.get(dominatorId) - 2;
                    tempSize2 = tempSize;
                } else {
                    save = false;
                    break;
                }
            }
            tempSize = tempSize2;
            if (save) {
                while (tempSize > 0) {
                    tempSize--;
                    setInt.add(temp[tempSize]);
                }
                size2 = size + 1;
                stack[size] = objectId;
                while (size2 > 0) {
                    size2--;
                    int current = stack[size2];
                    setInt.add(current);
                    int[] arr$ = dominated.get(current + 1);
                    int len$2 = arr$.length;
                    i$ = 0;
                    size = size2;
                    while (i$ < len$2) {
                        i2 = arr$[i$];
                        if (size == capacity) {
                            newCapacity = capacity << 1;
                            newArr = new int[newCapacity];
                            System.arraycopy(stack, 0, newArr, 0, capacity);
                            stack = newArr;
                            capacity = newCapacity;
                        }
                        size2 = size + 1;
                        stack[size] = i2;
                        i$++;
                        size = size2;
                    }
                    size2 = size;
                }
            } else {
                size2 = size;
            }
            i$2++;
            size = size2;
        }
        return setInt.toArray();
    }

    public int[] getTopAncestorsInDominatorTree(int[] objectIds, IProgressListener listener) throws SnapshotException {
        if (isDominatorTreeCalculated()) {
            if (listener == null) {
                listener = new VoidProgressListener();
            }
            if (objectIds.length > 1000000) {
                return getTopAncestorsWithBooleanCache(objectIds, listener);
            }
            SetInt negativeCache = new SetInt(objectIds.length);
            SetInt positiveCache = new SetInt(objectIds.length * 2);
            for (int i : objectIds) {
                positiveCache.add(i);
            }
            ArrayInt result = new ArrayInt();
            int tempSize = 0;
            int tempCapacity = 10240;
            int[] temp = new int[10240];
            IOne2OneIndex dominatorIdx = this.indexManager.dominator();
            int iterations = 0;
            for (int objectId : objectIds) {
                iterations++;
                if ((65535 & iterations) == 0 && listener.isCanceled()) {
                    throw new OperationCanceledException();
                }
                int dominatorId = dominatorIdx.get(objectId) - 2;
                boolean save = true;
                int tempSize2 = tempSize;
                while (dominatorId > -1) {
                    if (tempSize2 == tempCapacity) {
                        int newCapacity = tempCapacity << 1;
                        int[] newArr = new int[newCapacity];
                        System.arraycopy(temp, 0, newArr, 0, tempCapacity);
                        temp = newArr;
                        tempCapacity = newCapacity;
                    }
                    tempSize = tempSize2 + 1;
                    temp[tempSize2] = dominatorId;
                    if (positiveCache.contains(dominatorId)) {
                        save = false;
                        while (tempSize > 0) {
                            tempSize--;
                            positiveCache.add(temp[tempSize]);
                        }
                    } else if (negativeCache.contains(dominatorId)) {
                        break;
                    } else {
                        dominatorId = dominatorIdx.get(dominatorId) - 2;
                        tempSize2 = tempSize;
                    }
                }
                tempSize = tempSize2;
                if (save) {
                    result.add(objectId);
                    while (tempSize > 0) {
                        tempSize--;
                        negativeCache.add(temp[tempSize]);
                    }
                }
            }
            return result.toArray();
        }
        throw new SnapshotException(Messages.SnapshotImpl_Error_DomTreeNotAvailable);
    }

    private int[] getTopAncestorsWithBooleanCache(int[] objectIds, IProgressListener listener) {
        boolean[] negativeCache = new boolean[this.snapshotInfo.getNumberOfObjects()];
        boolean[] positiveCache = new boolean[this.snapshotInfo.getNumberOfObjects()];
        for (int i : objectIds) {
            positiveCache[i] = true;
        }
        ArrayInt result = new ArrayInt();
        int tempSize = 0;
        int tempCapacity = 10240;
        int[] temp = new int[10240];
        IOne2OneIndex dominatorIdx = this.indexManager.dominator();
        int iterations = 0;
        for (int objectId : objectIds) {
            iterations++;
            if ((65535 & iterations) == 0 && listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            int dominatorId = dominatorIdx.get(objectId) - 2;
            boolean save = true;
            int tempSize2 = tempSize;
            while (dominatorId > -1) {
                if (tempSize2 == tempCapacity) {
                    int newCapacity = tempCapacity << 1;
                    int[] newArr = new int[newCapacity];
                    System.arraycopy(temp, 0, newArr, 0, tempCapacity);
                    temp = newArr;
                    tempCapacity = newCapacity;
                }
                tempSize = tempSize2 + 1;
                temp[tempSize2] = dominatorId;
                if (positiveCache[dominatorId]) {
                    save = false;
                    while (tempSize > 0) {
                        tempSize--;
                        positiveCache[temp[tempSize]] = true;
                    }
                } else if (negativeCache[dominatorId]) {
                    break;
                } else {
                    dominatorId = dominatorIdx.get(dominatorId) - 2;
                    tempSize2 = tempSize;
                }
            }
            tempSize = tempSize2;
            if (save) {
                result.add(objectId);
                while (tempSize > 0) {
                    tempSize--;
                    negativeCache[temp[tempSize]] = true;
                }
            }
        }
        return result.toArray();
    }

    private boolean isDominatorTreeCalculated() {
        return this.dominatorTreeCalculated;
    }

    public void calculateDominatorTree(IProgressListener listener) throws SnapshotException, OperationCanceledException {
        try {
            DominatorTree.calculate(this, listener);
            boolean z = (this.indexManager.dominated() == null || this.indexManager.o2retained() == null || this.indexManager.dominator() == null) ? false : true;
            this.dominatorTreeCalculated = z;
        } catch (Throwable e) {
            throw new SnapshotException(e);
        }
    }

    public int[] getImmediateDominatedIds(int objectId) throws SnapshotException {
        if (isDominatorTreeCalculated()) {
            return this.indexManager.dominated().get(objectId + 1);
        }
        throw new SnapshotException(Messages.SnapshotImpl_Error_DomTreeNotAvailable);
    }

    public int getImmediateDominatorId(int objectId) throws SnapshotException {
        if (isDominatorTreeCalculated()) {
            return this.indexManager.dominator().get(objectId) - 2;
        }
        throw new SnapshotException(Messages.SnapshotImpl_Error_DomTreeNotAvailable);
    }

    public DominatorsSummary getDominatorsOf(int[] objectIds, Pattern excludePattern, IProgressListener progressListener) throws SnapshotException {
        if (isDominatorTreeCalculated()) {
            if (progressListener == null) {
                progressListener = new VoidProgressListener();
            }
            IOne2OneIndex dominatorIndex = this.indexManager.dominator();
            IOne2OneIndex o2classIndex = this.indexManager.o2class();
            SetInt excludeSet = new SetInt();
            SetInt includeSet = new SetInt();
            progressListener.beginTask(Messages.SnapshotImpl_RetrievingDominators, objectIds.length / 10);
            Map<IClass, ClassDominatorRecord> map = new HashMap();
            for (int ii = 0; ii < objectIds.length; ii++) {
                IClass clasz;
                String domClassName;
                int domClassId;
                int objectId = objectIds[ii];
                int dominatorId = dominatorIndex.get(objectId) - 2;
                if (dominatorId == -1) {
                    clasz = null;
                    domClassName = "<ROOT>";
                    domClassId = -1;
                } else {
                    domClassId = o2classIndex.get(dominatorId);
                    clasz = (IClass) this.classCache.get(domClassId);
                    domClassName = clasz.getName();
                }
                if (excludePattern != null && dominatorId >= 0) {
                    boolean exclude = true;
                    while (exclude) {
                        if (progressListener.isCanceled()) {
                            throw new OperationCanceledException();
                        } else if (excludeSet.contains(domClassId)) {
                            dominatorId = dominatorIndex.get(dominatorId) - 2;
                            if (dominatorId == -1) {
                                clasz = null;
                                domClassName = "<ROOT>";
                                domClassId = -1;
                            } else {
                                domClassId = o2classIndex.get(dominatorId);
                                clasz = (IClass) this.classCache.get(domClassId);
                                domClassName = clasz.getName();
                            }
                        } else if (includeSet.contains(domClassId)) {
                            exclude = false;
                        } else if (!excludePattern.matcher(domClassName).matches() || dominatorId < 0) {
                            includeSet.add(domClassId);
                            exclude = false;
                        } else {
                            excludeSet.add(domClassId);
                        }
                    }
                }
                ClassDominatorRecord record = (ClassDominatorRecord) map.get(clasz);
                if (record == null) {
                    record = new ClassDominatorRecord();
                    map.put(clasz, record);
                    record.setClassName(domClassName);
                    record.setClassId(domClassId);
                    int classLoaderId = (dominatorId == -1 || clasz == null) ? -1 : clasz.getClassLoaderId();
                    record.setClassloaderId(classLoaderId);
                }
                if (record.addDominator(dominatorId) && dominatorId != -1) {
                    record.addDominatorNetSize((long) getHeapSize(dominatorId));
                }
                if (record.addDominated(objectId)) {
                    record.addDominatedNetSize((long) getHeapSize(objectId));
                }
                if (ii % 10 == 0) {
                    if (progressListener.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    progressListener.worked(1);
                }
            }
            ClassDominatorRecord[] records = (ClassDominatorRecord[]) map.values().toArray(new ClassDominatorRecord[0]);
            progressListener.done();
            return new DominatorsSummary(records, this);
        }
        throw new SnapshotException(Messages.SnapshotImpl_Error_DomTreeNotAvailable);
    }

    public IObject getObject(int objectId) throws SnapshotException {
        IObject answer = (IObject) this.classCache.get(objectId);
        return answer != null ? answer : (IObject) this.objectCache.get(objectId);
    }

    public GCRootInfo[] getGCRootInfo(int objectId) throws SnapshotException {
        return (GCRootInfo[]) this.roots.get(objectId);
    }

    public IClass getClassOf(int objectId) throws SnapshotException {
        if (isClass(objectId)) {
            return getObject(objectId).getClazz();
        }
        return (IClass) getObject(this.indexManager.o2class().get(objectId));
    }

    public long mapIdToAddress(int objectId) throws SnapshotException {
        return this.indexManager.o2address().get(objectId);
    }

    public int getHeapSize(int objectId) throws SnapshotException {
        if (this.arrayObjects.get(objectId)) {
            return this.indexManager.a2size().get(objectId);
        }
        IClass clazz = (IClass) this.classCache.get(objectId);
        if (clazz != null) {
            return clazz.getUsedHeapSize();
        }
        return ((IClass) this.classCache.get(this.indexManager.o2class().get(objectId))).getHeapSizePerInstance();
    }

    public long getHeapSize(int[] objectIds) throws UnsupportedOperationException, SnapshotException {
        long total = 0;
        IOne2OneIndex o2class = this.indexManager.o2class();
        IOne2OneIndex a2size = this.indexManager.a2size();
        for (int objectId : objectIds) {
            if (this.arrayObjects.get(objectId)) {
                total += (long) a2size.get(objectId);
            } else {
                IClass clazz = (IClass) this.classCache.get(objectId);
                if (clazz != null) {
                    total += (long) clazz.getUsedHeapSize();
                } else {
                    total += (long) ((IClass) this.classCache.get(o2class.get(objectId))).getHeapSizePerInstance();
                }
            }
        }
        return total;
    }

    public long getRetainedHeapSize(int objectId) throws SnapshotException {
        if (isDominatorTreeCalculated()) {
            return this.indexManager.o2retained().get(objectId);
        }
        return 0;
    }

    public boolean isArray(int objectId) {
        if (this.arrayObjects.get(objectId) && ((IClass) this.classCache.get(this.indexManager.o2class().get(objectId))).isArrayType()) {
            return true;
        }
        return false;
    }

    public boolean isClass(int objectId) {
        return this.classCache.containsKey(objectId);
    }

    public boolean isGCRoot(int objectId) {
        return this.roots.containsKey(objectId);
    }

    public int mapAddressToId(long objectAddress) throws SnapshotException {
        int objectId = this.indexManager.o2address().reverse(objectAddress);
        if (objectId >= 0) {
            return objectId;
        }
        throw new SnapshotException(MessageUtil.format(Messages.SnapshotImpl_Error_ObjectNotFound, "0x" + Long.toHexString(objectAddress)));
    }

    public void dispose() {
        IOException error = null;
        try {
            this.heapObjectReader.close();
        } catch (IOException e1) {
            error = e1;
        }
        try {
            this.indexManager.close();
        } catch (IOException e12) {
            error = e12;
        }
        this.retainedSizeCache.close();
        if (error != null) {
            throw new RuntimeException(error);
        }
    }

    public List<IClass> resolveClassHierarchy(int classIndex) {
        IClass clazz = (IClass) this.classCache.get(classIndex);
        if (clazz == null) {
            return null;
        }
        List<IClass> answer = new ArrayList();
        answer.add(clazz);
        while (clazz.hasSuperClass()) {
            clazz = (IClass) this.classCache.get(clazz.getSuperClassId());
            if (clazz == null) {
                return null;
            }
            answer.add(clazz);
        }
        return answer;
    }

    public boolean isClassLoader(int objectId) {
        return this.loaderLabels.containsKey(objectId);
    }

    public String getClassLoaderLabel(int objectId) {
        return (String) this.loaderLabels.get(objectId);
    }

    public void setClassLoaderLabel(int objectId, String label) {
        if (label == null) {
            throw new NullPointerException(Messages.SnapshotImpl_Label.pattern);
        } else if (((String) this.loaderLabels.put(objectId, label)) == null) {
            throw new RuntimeException(Messages.SnapshotImpl_Error_ReplacingNonExistentClassLoader.pattern);
        }
    }

    private int dfs2(BitField bits, BitField exclude, String[] fieldNames) throws SnapshotException {
        int i;
        int count = 0;
        HashSet<String> fieldNamesSet = new HashSet(fieldNames.length);
        for (String add : fieldNames) {
            fieldNamesSet.add(add);
        }
        IOne2ManyIndex outbound = this.indexManager.outbound();
        IntStack stack = new IntStack();
        IteratorInt en = this.roots.keys();
        while (en.hasNext()) {
            i = en.next();
            stack.push(i);
            bits.set(i);
            count++;
        }
        while (stack.size() > 0) {
            int current = stack.pop();
            if (exclude.get(current)) {
                for (int child : outbound.get(current)) {
                    IObject obj = getObject(current);
                    long childAddress = mapIdToAddress(child);
                    for (NamedReference reference : obj.getOutboundReferences()) {
                        if (!(bits.get(child) || reference.getObjectAddress() != childAddress || fieldNamesSet.contains(reference.getName()))) {
                            stack.push(child);
                            bits.set(child);
                            count++;
                        }
                    }
                }
            } else {
                for (int child2 : outbound.get(current)) {
                    if (!bits.get(child2)) {
                        stack.push(child2);
                        bits.set(child2);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int[] getSingleObjectRetainedSet(int objectId) throws SnapshotException {
        ArrayIntBig result = new ArrayIntBig();
        IntStack stack = new IntStack();
        stack.push(objectId);
        while (stack.size() > 0) {
            int current = stack.pop();
            result.add(current);
            for (int i : getImmediateDominatedIds(current)) {
                stack.push(i);
            }
        }
        return result.toArray();
    }

    public IndexManager getIndexManager() {
        return this.indexManager;
    }

    public IObjectReader getHeapObjectReader() {
        return this.heapObjectReader;
    }

    public RetainedSizeCache getRetainedSizeCache() {
        return this.retainedSizeCache;
    }

    public HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> getRootsPerThread() {
        return this.rootsPerThread;
    }

    public <A> A getSnapshotAddons(Class<A> addon) throws SnapshotException {
        return this.heapObjectReader.getAddon(addon);
    }

    public IThreadStack getThreadStack(int objectId) throws SnapshotException {
        if (!this.parsedThreads) {
            this.threadId2stack = ThreadStackHelper.loadThreadsData(this);
            this.parsedThreads = true;
        }
        if (this.threadId2stack != null) {
            return (IThreadStack) this.threadId2stack.get(objectId);
        }
        return null;
    }
}
