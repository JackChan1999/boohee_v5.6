package org.eclipse.mat.parser.internal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.index.IndexManager;
import org.eclipse.mat.parser.index.IndexManager.Index;
import org.eclipse.mat.parser.index.IndexWriter.InboundWriter;
import org.eclipse.mat.parser.index.IndexWriter.IntArray1NSortedWriter;
import org.eclipse.mat.parser.index.IndexWriter.IntIndexStreamer;
import org.eclipse.mat.parser.index.IndexWriter.KeyWriter;
import org.eclipse.mat.parser.index.IndexWriter.LongIndexStreamer;
import org.eclipse.mat.parser.internal.snapshot.ObjectMarker;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.XGCRootInfo;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.MessageUtil;
import org.eclipse.mat.util.SilentProgressListener;

class GarbageCleaner {

    private static abstract class NewObjectIterator {
        int[] $map = getMap();
        int nextIndex = -1;

        abstract int[] getMap();

        public NewObjectIterator() {
            findNext();
        }

        protected void findNext() {
            this.nextIndex++;
            while (this.nextIndex < this.$map.length && this.$map[this.nextIndex] < 0) {
                this.nextIndex++;
            }
        }

        public boolean hasNext() {
            return this.nextIndex < this.$map.length;
        }
    }

    private static abstract class NewObjectIntIterator extends NewObjectIterator implements IteratorInt {
        abstract int doGetNextInt(int i);

        private NewObjectIntIterator() {
        }

        public int next() {
            int answer = doGetNextInt(this.nextIndex);
            findNext();
            return answer;
        }
    }

    private static class KeyWriterImpl implements KeyWriter {
        HashMapIntObject<ClassImpl> classesByNewId;

        KeyWriterImpl(HashMapIntObject<ClassImpl> classesByNewId) {
            this.classesByNewId = classesByNewId;
        }

        public void storeKey(int index, Serializable key) {
            ((ClassImpl) this.classesByNewId.get(index)).setCacheEntry(key);
        }
    }

    GarbageCleaner() {
    }

    public static int[] clean(PreliminaryIndexImpl idx, SnapshotImplBuilder builder, Map<String, String> map, IProgressListener listener) throws IOException {
        IndexManager idxManager = new IndexManager();
        try {
            int[] iArr;
            listener.beginTask(Messages.GarbageCleaner_RemovingUnreachableObjects, 11);
            listener.subTask(Messages.GarbageCleaner_SearchingForUnreachableObjects.pattern);
            int oldNoOfObjects = idx.identifiers.size();
            boolean[] reachable = new boolean[oldNoOfObjects];
            int newNoOfObjects = 0;
            int[] newRoots = idx.gcRoots.getAllKeys();
            IOne2LongIndex identifiers = idx.identifiers;
            IOne2ManyIndex preOutbound = idx.outbound;
            IOne2OneIndex object2classId = idx.object2classId;
            HashMapIntObject<ClassImpl> classesById = idx.classesById;
            int numProcessors = Runtime.getRuntime().availableProcessors();
            ObjectMarker objectMarker = new ObjectMarker(newRoots, reachable, preOutbound, new SilentProgressListener(listener));
            if (numProcessors > 1) {
                objectMarker.markMultiThreaded(numProcessors);
                for (boolean b : reachable) {
                    if (b) {
                        newNoOfObjects++;
                    }
                }
            } else {
                try {
                    newNoOfObjects = objectMarker.markSingleThreaded();
                } catch (OperationCanceledException e) {
                    iArr = null;
                    idx.delete();
                    if (idxManager != null && listener.isCanceled()) {
                        idxManager.delete();
                    }
                }
            }
            if (newNoOfObjects < oldNoOfObjects) {
                Serializable un = idx.getSnapshotInfo().getProperty("keep_unreachable_objects");
                if (un instanceof Integer) {
                    markUnreachbleAsGCRoots(idx, reachable, newNoOfObjects, ((Integer) un).intValue(), listener);
                    newNoOfObjects = oldNoOfObjects;
                }
            }
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            int jj;
            ClassImpl clazz;
            ClassImpl c;
            listener.worked(1);
            listener.subTask(Messages.GarbageCleaner_ReIndexingObjects.pattern);
            iArr = new int[oldNoOfObjects];
            long[] id2a = new long[newNoOfObjects];
            List<ClassImpl> classes2remove = new ArrayList();
            IOne2OneIndex preA2size = idx.array2size;
            int ii = 0;
            int jj2 = 0;
            while (ii < oldNoOfObjects) {
                if (reachable[ii]) {
                    iArr[ii] = jj2;
                    jj = jj2 + 1;
                    id2a[jj2] = identifiers.get(ii);
                } else {
                    iArr[ii] = -1;
                    clazz = (ClassImpl) classesById.get(object2classId.get(ii));
                    int arraySize = preA2size.get(ii);
                    if (arraySize > 0) {
                        clazz.removeInstance(arraySize);
                        jj = jj2;
                    } else {
                        c = (ClassImpl) classesById.get(ii);
                        if (c == null) {
                            clazz.removeInstance(clazz.getHeapSizePerInstance());
                            jj = jj2;
                        } else {
                            clazz.removeInstance(c.getUsedHeapSize());
                            classes2remove.add(c);
                            jj = jj2;
                        }
                    }
                }
                ii++;
                jj2 = jj;
            }
            for (ClassImpl c2 : classes2remove) {
                classesById.remove(c2.getObjectId());
                ClassImpl superclass = (ClassImpl) classesById.get(c2.getSuperClassId());
                if (superclass != null) {
                    superclass.removeSubClass(c2);
                }
            }
            identifiers.close();
            identifiers.delete();
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(Messages.GarbageCleaner_ReIndexingClasses.pattern);
            HashMapIntObject<ClassImpl> classesByNewId = new HashMapIntObject(classesById.size());
            Iterator<ClassImpl> iter = classesById.values();
            while (iter.hasNext()) {
                clazz = (ClassImpl) iter.next();
                int index = iArr[clazz.getObjectId()];
                clazz.setObjectId(index);
                if (clazz.getSuperClassId() >= 0) {
                    clazz.setSuperClassIndex(iArr[clazz.getSuperClassId()]);
                }
                clazz.setClassLoaderIndex(iArr[clazz.getClassLoaderId()]);
                classesByNewId.put(index, clazz);
            }
            idx.getSnapshotInfo().setNumberOfClasses(classesByNewId.size());
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(MessageUtil.format(Messages.GarbageCleaner_Writing, Index.IDENTIFIER.getFile(idx.snapshotInfo.getPrefix()).getAbsolutePath()));
            idxManager.setReader(Index.IDENTIFIER, new LongIndexStreamer().writeTo(indexFile, id2a));
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(MessageUtil.format(Messages.GarbageCleaner_Writing, Index.O2CLASS.getFile(idx.snapshotInfo.getPrefix()).getAbsolutePath()));
            final int[] iArr2 = iArr;
            final PreliminaryIndexImpl preliminaryIndexImpl = idx;
            idxManager.setReader(Index.O2CLASS, new IntIndexStreamer().writeTo(indexFile, (IteratorInt) new NewObjectIntIterator() {
                int doGetNextInt(int index) {
                    return iArr2[preliminaryIndexImpl.object2classId.get(this.nextIndex)];
                }

                int[] getMap() {
                    return iArr2;
                }
            }));
            object2classId.close();
            object2classId.delete();
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(MessageUtil.format(Messages.GarbageCleaner_Writing, Index.A2SIZE.getFile(idx.snapshotInfo.getPrefix()).getAbsolutePath()));
            final BitField arrayObjects = new BitField(newNoOfObjects);
            final IOne2OneIndex iOne2OneIndex = preA2size;
            final int[] iArr3 = iArr;
            idxManager.setReader(Index.A2SIZE, new IntIndexStreamer().writeTo(indexFile, (IteratorInt) new NewObjectIntIterator() {
                IOne2OneIndex a2size = iOne2OneIndex;
                int newIndex = 0;

                int doGetNextInt(int index) {
                    int size = this.a2size.get(this.nextIndex);
                    if (size > 0) {
                        arrayObjects.set(this.newIndex);
                    }
                    this.newIndex++;
                    return size;
                }

                int[] getMap() {
                    return iArr3;
                }
            }));
            preA2size.close();
            preA2size.delete();
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(Messages.GarbageCleaner_ReIndexingOutboundIndex.pattern);
            IntArray1NSortedWriter intArray1NSortedWriter = new IntArray1NSortedWriter(newNoOfObjects, Index.OUTBOUND.getFile(idx.snapshotInfo.getPrefix()));
            InboundWriter inboundWriter = new InboundWriter(newNoOfObjects, Index.INBOUND.getFile(idx.snapshotInfo.getPrefix()));
            for (ii = 0; ii < oldNoOfObjects; ii++) {
                int k = iArr[ii];
                if (k >= 0) {
                    int[] a = preOutbound.get(ii);
                    int[] tl = new int[a.length];
                    for (jj = 0; jj < a.length; jj++) {
                        boolean z;
                        int t = iArr[a[jj]];
                        tl[jj] = t;
                        if (jj == 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        inboundWriter.log(t, k, z);
                    }
                    intArray1NSortedWriter.log(k, tl);
                }
            }
            preOutbound.close();
            preOutbound.delete();
            if (listener.isCanceled()) {
                inboundWriter.cancel();
                intArray1NSortedWriter.cancel();
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(MessageUtil.format(Messages.GarbageCleaner_Writing, inboundWriter.getIndexFile().getAbsolutePath()));
            idxManager.setReader(Index.INBOUND, inboundWriter.flush(listener, new KeyWriterImpl(classesByNewId)));
            if (listener.isCanceled()) {
                intArray1NSortedWriter.cancel();
                throw new OperationCanceledException();
            }
            listener.worked(1);
            listener.subTask(MessageUtil.format(Messages.GarbageCleaner_Writing, intArray1NSortedWriter.getIndexFile().getAbsolutePath()));
            idxManager.setReader(Index.OUTBOUND, intArray1NSortedWriter.flush());
            if (listener.isCanceled()) {
                throw new OperationCanceledException();
            }
            listener.worked(1);
            HashMapIntObject<XGCRootInfo[]> roots = fix(idx.gcRoots, iArr);
            idx.getSnapshotInfo().setNumberOfGCRoots(roots.size());
            HashMapIntObject<HashMapIntObject<XGCRootInfo[]>> rootsPerThread = new HashMapIntObject();
            IteratorInt iter2 = idx.thread2objects2roots.keys();
            while (iter2.hasNext()) {
                int threadId = iter2.next();
                int fixedThreadId = iArr[threadId];
                if (fixedThreadId >= 0) {
                    rootsPerThread.put(fixedThreadId, fix((HashMapIntObject) idx.thread2objects2roots.get(threadId), iArr));
                }
            }
            builder.setIndexManager(idxManager);
            builder.setClassCache(classesByNewId);
            builder.setArrayObjects(arrayObjects);
            builder.setRoots(roots);
            builder.setRootsPerThread(rootsPerThread);
            idx.delete();
            if (idxManager != null && listener.isCanceled()) {
                idxManager.delete();
            }
            return iArr;
        } catch (Throwable e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (Throwable th) {
            idx.delete();
            if (idxManager != null && listener.isCanceled()) {
                idxManager.delete();
            }
        }
    }

    private static HashMapIntObject<XGCRootInfo[]> fix(HashMapIntObject<List<XGCRootInfo>> roots, int[] map) {
        HashMapIntObject<XGCRootInfo[]> answer = new HashMapIntObject(roots.size());
        Iterator<List<XGCRootInfo>> iter = roots.values();
        while (iter.hasNext()) {
            List<XGCRootInfo> r = (List) iter.next();
            XGCRootInfo[] a = new XGCRootInfo[r.size()];
            for (int ii = 0; ii < a.length; ii++) {
                a[ii] = (XGCRootInfo) r.get(ii);
                a[ii].setObjectId(map[a[ii].getObjectId()]);
                if (a[ii].getContextAddress() != 0) {
                    a[ii].setContextId(map[a[ii].getContextId()]);
                }
            }
            answer.put(a[0].getObjectId(), a);
        }
        return answer;
    }

    private static void markUnreachbleAsGCRoots(PreliminaryIndexImpl idx, boolean[] reachable, int noReachableObjects, int extraRootType, IProgressListener listener) {
        int ii;
        int noOfObjects = reachable.length;
        IOne2LongIndex identifiers = idx.identifiers;
        IOne2ManyIndex preOutbound = idx.outbound;
        int[] root = new int[1];
        ObjectMarker objectMarker = new ObjectMarker(root, reachable, preOutbound, new SilentProgressListener(listener));
        boolean[] inbounds = new boolean[noOfObjects];
        for (ii = 0; ii < noOfObjects; ii++) {
            if (!reachable[ii]) {
                for (int out : preOutbound.get(ii)) {
                    inbounds[out] = true;
                }
            }
        }
        int pass = 0;
        while (pass < 2) {
            ii = 0;
            while (ii < noOfObjects && noReachableObjects < noOfObjects) {
                if (!reachable[ii] && (pass == 1 || !inbounds[ii])) {
                    root[0] = ii;
                    XGCRootInfo xgc = new XGCRootInfo(identifiers.get(ii), 0, extraRootType);
                    xgc.setObjectId(ii);
                    ArrayList<XGCRootInfo> arrayList = new ArrayList(1);
                    arrayList.add(xgc);
                    idx.gcRoots.put(ii, arrayList);
                    noReachableObjects += objectMarker.markSingleThreaded();
                }
                ii++;
            }
            pass++;
        }
        idx.setGcRoots(idx.gcRoots);
        idx.getSnapshotInfo().setNumberOfGCRoots(idx.gcRoots.size());
    }
}
