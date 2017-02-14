package org.eclipse.mat.parser.internal.snapshot;

import java.util.Set;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.internal.util.IntStack;
import org.eclipse.mat.snapshot.ExcludedReferencesDescriptor;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;

public class ObjectMarker {
    boolean[] bits;
    IOne2ManyIndex outbound;
    IProgressListener progressListener;
    int[] roots;

    public class DfsThread implements Runnable {
        int[] data = new int[10240];
        IntStack rootsStack;
        int size = 0;

        public DfsThread(IntStack roots) {
            this.rootsStack = roots;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r12 = this;
            r11 = 1;
            r10 = 0;
        L_0x0002:
            r7 = r12.rootsStack;
            monitor-enter(r7);
            r6 = org.eclipse.mat.parser.internal.snapshot.ObjectMarker.this;	 Catch:{ all -> 0x0081 }
            r6 = r6.progressListener;	 Catch:{ all -> 0x0081 }
            r8 = 1;
            r6.worked(r8);	 Catch:{ all -> 0x0081 }
            r6 = org.eclipse.mat.parser.internal.snapshot.ObjectMarker.this;	 Catch:{ all -> 0x0081 }
            r6 = r6.progressListener;	 Catch:{ all -> 0x0081 }
            r6 = r6.isCanceled();	 Catch:{ all -> 0x0081 }
            if (r6 == 0) goto L_0x0019;
        L_0x0017:
            monitor-exit(r7);	 Catch:{ all -> 0x0081 }
        L_0x0018:
            return;
        L_0x0019:
            r6 = r12.rootsStack;	 Catch:{ all -> 0x0081 }
            r6 = r6.size();	 Catch:{ all -> 0x0081 }
            if (r6 <= 0) goto L_0x007f;
        L_0x0021:
            r6 = r12.data;	 Catch:{ all -> 0x0081 }
            r8 = 0;
            r9 = r12.rootsStack;	 Catch:{ all -> 0x0081 }
            r9 = r9.pop();	 Catch:{ all -> 0x0081 }
            r6[r8] = r9;	 Catch:{ all -> 0x0081 }
            r6 = 1;
            r12.size = r6;	 Catch:{ all -> 0x0081 }
            monitor-exit(r7);	 Catch:{ all -> 0x0081 }
        L_0x0030:
            r6 = r12.size;
            if (r6 <= 0) goto L_0x0002;
        L_0x0034:
            r6 = r12.data;
            r7 = r12.size;
            r7 = r7 + -1;
            r12.size = r7;
            r2 = r6[r7];
            r6 = org.eclipse.mat.parser.internal.snapshot.ObjectMarker.this;
            r6 = r6.outbound;
            r0 = r6.get(r2);
            r4 = r0.length;
            r3 = 0;
        L_0x0048:
            if (r3 >= r4) goto L_0x0030;
        L_0x004a:
            r1 = r0[r3];
            r6 = org.eclipse.mat.parser.internal.snapshot.ObjectMarker.this;
            r6 = r6.bits;
            r6 = r6[r1];
            if (r6 != 0) goto L_0x007c;
        L_0x0054:
            r6 = org.eclipse.mat.parser.internal.snapshot.ObjectMarker.this;
            r6 = r6.bits;
            r6[r1] = r11;
            r6 = r12.size;
            r7 = r12.data;
            r7 = r7.length;
            if (r6 != r7) goto L_0x0072;
        L_0x0061:
            r6 = r12.data;
            r6 = r6.length;
            r6 = r6 << 1;
            r5 = new int[r6];
            r6 = r12.data;
            r7 = r12.data;
            r7 = r7.length;
            java.lang.System.arraycopy(r6, r10, r5, r10, r7);
            r12.data = r5;
        L_0x0072:
            r6 = r12.data;
            r7 = r12.size;
            r8 = r7 + 1;
            r12.size = r8;
            r6[r7] = r1;
        L_0x007c:
            r3 = r3 + 1;
            goto L_0x0048;
        L_0x007f:
            monitor-exit(r7);	 Catch:{ all -> 0x0081 }
            goto L_0x0018;
        L_0x0081:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0081 }
            throw r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.eclipse.mat.parser.internal.snapshot.ObjectMarker.DfsThread.run():void");
        }
    }

    public ObjectMarker(int[] roots, boolean[] bits, IOne2ManyIndex outbound, IProgressListener progressListener) {
        this.roots = roots;
        this.bits = bits;
        this.outbound = outbound;
        this.progressListener = progressListener;
    }

    public int markSingleThreaded() throws OperationCanceledException {
        int size;
        int count = 0;
        int[] data = new int[10240];
        int rootsToProcess = 0;
        int[] arr$ = this.roots;
        int len$ = arr$.length;
        int i$ = 0;
        int size2 = 0;
        while (i$ < len$) {
            int rootId = arr$[i$];
            if (this.bits[rootId]) {
                size = size2;
            } else {
                if (size2 == data.length) {
                    int[] newArr = new int[(data.length << 1)];
                    System.arraycopy(data, 0, newArr, 0, data.length);
                    data = newArr;
                }
                size = size2 + 1;
                data[size2] = rootId;
                this.bits[rootId] = true;
                count++;
                rootsToProcess++;
            }
            i$++;
            size2 = size;
        }
        this.progressListener.beginTask(Messages.ObjectMarker_CalculateRetainedSize, rootsToProcess);
        size = size2;
        while (size > 0) {
            size--;
            int current = data[size];
            if (size <= rootsToProcess) {
                rootsToProcess--;
                this.progressListener.worked(1);
                if (this.progressListener.isCanceled()) {
                    throw new OperationCanceledException();
                }
            }
            arr$ = this.outbound.get(current);
            len$ = arr$.length;
            i$ = 0;
            size2 = size;
            while (i$ < len$) {
                int child = arr$[i$];
                if (this.bits[child]) {
                    size = size2;
                } else {
                    if (size2 == data.length) {
                        newArr = new int[(data.length << 1)];
                        System.arraycopy(data, 0, newArr, 0, data.length);
                        data = newArr;
                    }
                    size = size2 + 1;
                    data[size2] = child;
                    this.bits[child] = true;
                    count++;
                }
                i$++;
                size2 = size;
            }
            size = size2;
        }
        this.progressListener.done();
        return count;
    }

    public int markSingleThreaded(ExcludedReferencesDescriptor[] excludeSets, ISnapshot snapshot) throws SnapshotException, OperationCanceledException {
        int size;
        BitField excludeObjectsBF = new BitField(snapshot.getSnapshotInfo().getNumberOfObjects());
        for (ExcludedReferencesDescriptor set : excludeSets) {
            for (int k : set.getObjectIds()) {
                excludeObjectsBF.set(k);
            }
        }
        int count = 0;
        int rootsToProcess = 0;
        int[] data = new int[10240];
        int[] arr$ = this.roots;
        int len$ = arr$.length;
        int i$ = 0;
        int size2 = 0;
        while (i$ < len$) {
            int rootId = arr$[i$];
            if (this.bits[rootId]) {
                size = size2;
            } else {
                if (size2 == data.length) {
                    Object newArr = new int[(data.length << 1)];
                    System.arraycopy(data, 0, newArr, 0, data.length);
                    data = newArr;
                }
                size = size2 + 1;
                data[size2] = rootId;
                this.bits[rootId] = true;
                count++;
                rootsToProcess++;
            }
            i$++;
            size2 = size;
        }
        this.progressListener.beginTask(Messages.ObjectMarker_CalculateRetainedSize, rootsToProcess);
        size = size2;
        while (size > 0) {
            size--;
            int current = data[size];
            if (size <= rootsToProcess) {
                rootsToProcess--;
                this.progressListener.worked(1);
                if (this.progressListener.isCanceled()) {
                    throw new OperationCanceledException();
                }
            }
            arr$ = this.outbound.get(current);
            len$ = arr$.length;
            i$ = 0;
            size2 = size;
            while (i$ < len$) {
                int child = arr$[i$];
                if (this.bits[child] || refersOnlyThroughExcluded(current, child, excludeSets, excludeObjectsBF, snapshot)) {
                    size = size2;
                } else {
                    if (size2 == data.length) {
                        newArr = new int[(data.length << 1)];
                        System.arraycopy(data, 0, newArr, 0, data.length);
                        data = newArr;
                    }
                    size = size2 + 1;
                    data[size2] = child;
                    this.bits[child] = true;
                    count++;
                }
                i$++;
                size2 = size;
            }
            size = size2;
        }
        this.progressListener.done();
        return count;
    }

    public void markMultiThreaded(int numberOfThreads) throws InterruptedException {
        int i;
        IntStack rootsStack = new IntStack(this.roots.length);
        for (int rootId : this.roots) {
            if (!this.bits[rootId]) {
                rootsStack.push(rootId);
                this.bits[rootId] = true;
            }
        }
        this.progressListener.beginTask(Messages.ObjectMarker_CalculateRetainedSize, rootsStack.size());
        DfsThread[] dfsthreads = new DfsThread[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        for (i = 0; i < numberOfThreads; i++) {
            DfsThread dfsthread = new DfsThread(rootsStack);
            dfsthreads[i] = dfsthread;
            Thread thread = new Thread(dfsthread, "ObjectMarkerThread-" + (i + 1));
            thread.start();
            threads[i] = thread;
        }
        for (i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
        if (!this.progressListener.isCanceled()) {
            this.progressListener.done();
        }
    }

    private boolean refersOnlyThroughExcluded(int referrerId, int referentId, ExcludedReferencesDescriptor[] excludeSets, BitField excludeObjectsBF, ISnapshot snapshot) throws SnapshotException {
        if (!excludeObjectsBF.get(referrerId)) {
            return false;
        }
        IObject referrerObject = snapshot.getObject(referrerId);
        Set<String> excludeFields = null;
        for (ExcludedReferencesDescriptor set : excludeSets) {
            if (set.contains(referrerId)) {
                excludeFields = set.getFields();
                break;
            }
        }
        if (excludeFields == null) {
            return true;
        }
        long referentAddr = snapshot.mapIdToAddress(referentId);
        for (NamedReference reference : referrerObject.getOutboundReferences()) {
            if (referentAddr == reference.getObjectAddress() && !excludeFields.contains(reference.getName())) {
                return false;
            }
        }
        return true;
    }
}
