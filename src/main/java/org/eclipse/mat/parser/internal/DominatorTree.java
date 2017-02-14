package org.eclipse.mat.parser.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayUtils;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IndexManager;
import org.eclipse.mat.parser.index.IndexManager.Index;
import org.eclipse.mat.parser.index.IndexWriter;
import org.eclipse.mat.parser.index.IndexWriter.IntArray1NWriter;
import org.eclipse.mat.parser.index.IndexWriter.IntIndexStreamer;
import org.eclipse.mat.parser.index.IndexWriter.LongIndexCollector;
import org.eclipse.mat.parser.internal.util.IntStack;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;
import org.eclipse.mat.util.SimpleMonitor;

public class DominatorTree {

    static class Calculator {
        private static int ROOT_VALUE = -1;
        private static int[] ROOT_VALUE_ARR = new int[]{ROOT_VALUE};
        private int[] anchestor;
        int[] bucket;
        private int[] dom;
        int[] gcRootsArray;
        private BitField gcRootsSet;
        IOne2ManyIndex inboundIndex;
        private int[] label;
        SimpleMonitor monitor;
        private int n;
        IOne2ManyIndex outboundIndex;
        private int[] parent;
        private int r;
        private int[] semi;
        SnapshotImpl snapshot;
        private int[] vertex;

        public class FlatDominatorTree {
            private static final int TEMP_ARR_LENGTH = 1000000;
            int[] dom;
            SnapshotImpl dump;
            int[] elements;
            int[] tempIntArray = new int[1000000];
            long[] tempLongArray = new long[1000000];
            long[] ts;

            class SuccessorsEnum {
                int nextIndex;
                int parent;

                SuccessorsEnum(int parent) {
                    this.parent = parent;
                    this.nextIndex = findFirstChildIndex(parent + 2);
                }

                public boolean hasMoreElements() {
                    return this.nextIndex > 0;
                }

                public int nextElement() {
                    if (this.nextIndex < 0) {
                        throw new NoSuchElementException();
                    }
                    int[] iArr = FlatDominatorTree.this.elements;
                    int i = this.nextIndex;
                    this.nextIndex = i + 1;
                    int res = iArr[i];
                    if (this.nextIndex >= FlatDominatorTree.this.dom.length || FlatDominatorTree.this.dom[this.nextIndex] != this.parent + 2) {
                        this.nextIndex = -1;
                    }
                    return res;
                }

                int findFirstChildIndex(int el) {
                    int i = Arrays.binarySearch(FlatDominatorTree.this.dom, el);
                    while (i > 1 && FlatDominatorTree.this.dom[i - 1] == el) {
                        i--;
                    }
                    return i;
                }
            }

            FlatDominatorTree(SnapshotImpl dump, int[] dom, int[] elements, int root) throws SnapshotException, IOException {
                this.dump = dump;
                this.dom = dom;
                this.elements = elements;
                this.ts = new long[dom.length];
                calculateTotalSizesIterative(root);
            }

            public SuccessorsEnum getSuccessorsEnum(int i) {
                return new SuccessorsEnum(i);
            }

            public int[] getSuccessorsArr(int parentId) {
                parentId += 2;
                int j = Arrays.binarySearch(this.dom, parentId);
                if (j < 0) {
                    return new int[0];
                }
                int i = j;
                while (i > 1 && this.dom[i - 1] == parentId) {
                    i--;
                }
                while (j < this.dom.length && this.dom[j] == parentId) {
                    j++;
                }
                int length = j - i;
                int[] result = new int[length];
                System.arraycopy(this.elements, i, result, 0, length);
                return result;
            }

            public void sortByTotalSize(int[] objectIds) {
                int length = objectIds.length;
                long[] totalSizes = new long[length];
                for (int i = 0; i < length; i++) {
                    totalSizes[i] = this.ts[objectIds[i] + 2];
                }
                if (totalSizes.length <= 1) {
                    return;
                }
                if (totalSizes.length > 1000000) {
                    ArrayUtils.sortDesc(totalSizes, objectIds);
                } else {
                    ArrayUtils.sortDesc(totalSizes, objectIds, this.tempLongArray, this.tempIntArray);
                }
            }

            public void calculateTotalSizesIterative(int e) throws SnapshotException, IOException {
                LongIndexCollector retained = new LongIndexCollector(this.dump.getSnapshotInfo().getNumberOfObjects(), IndexWriter.mostSignificantBit(this.dump.getSnapshotInfo().getUsedHeapSize()));
                int capacity = 2048;
                int[] stack = new int[2048];
                SuccessorsEnum[] succStack = new SuccessorsEnum[2048];
                int currentEntry = e;
                SuccessorsEnum currentSucc = getSuccessorsEnum(currentEntry);
                stack[0] = currentEntry;
                succStack[0] = currentSucc;
                int size = 0 + 1;
                IProgressListener progressListener = Calculator.this.monitor.nextMonitor();
                progressListener.beginTask(Messages.DominatorTree_CalculateRetainedSizes, this.dump.getSnapshotInfo().getNumberOfObjects() / 1000);
                int counter = 0;
                while (size > 0) {
                    currentEntry = stack[size - 1];
                    currentSucc = succStack[size - 1];
                    long[] jArr;
                    if (currentSucc.hasMoreElements()) {
                        long j;
                        int nextChild = currentSucc.nextElement();
                        currentSucc = getSuccessorsEnum(nextChild);
                        jArr = this.ts;
                        int i = nextChild + 2;
                        if (nextChild < 0) {
                            j = 0;
                        } else {
                            j = (long) Calculator.this.snapshot.getHeapSize(nextChild);
                        }
                        jArr[i] = j;
                        if (size == capacity) {
                            int newCapacity = capacity << 1;
                            int[] newArr = new int[newCapacity];
                            System.arraycopy(stack, 0, newArr, 0, capacity);
                            stack = newArr;
                            SuccessorsEnum[] newSuccessorsArr = new SuccessorsEnum[newCapacity];
                            System.arraycopy(succStack, 0, newSuccessorsArr, 0, capacity);
                            succStack = newSuccessorsArr;
                            capacity = newCapacity;
                        }
                        stack[size] = nextChild;
                        succStack[size] = currentSucc;
                        size++;
                    } else {
                        size--;
                        if (size > 0) {
                            jArr = this.ts;
                            int i2 = stack[size - 1] + 2;
                            jArr[i2] = jArr[i2] + this.ts[currentEntry + 2];
                        }
                        if (currentEntry >= 0) {
                            retained.set(currentEntry, this.ts[currentEntry + 2]);
                            counter++;
                            if (counter % 1000 != 0) {
                                continue;
                            } else if (progressListener.isCanceled()) {
                                throw new OperationCanceledException();
                            } else {
                                progressListener.worked(1);
                            }
                        } else {
                            continue;
                        }
                    }
                }
                this.dump.getIndexManager().setReader(Index.O2RETAINED, retained.writeTo(Index.O2RETAINED.getFile(this.dump.getSnapshotInfo().getPrefix())));
                progressListener.done();
            }
        }

        public Calculator(SnapshotImpl snapshot, IProgressListener listener) throws SnapshotException {
            this.snapshot = snapshot;
            this.inboundIndex = snapshot.getIndexManager().inbound();
            this.outboundIndex = snapshot.getIndexManager().outbound();
            this.monitor = new SimpleMonitor(Messages.DominatorTree_CalculatingDominatorTree.pattern, listener, new int[]{300, 300, 200, 200, 200});
            this.gcRootsArray = snapshot.getGCRoots();
            this.gcRootsSet = new BitField(snapshot.getSnapshotInfo().getNumberOfObjects());
            for (int id : this.gcRootsArray) {
                this.gcRootsSet.set(id);
            }
            IndexManager manager = this.snapshot.getIndexManager();
            try {
                manager.a2size().unload();
                manager.o2address().unload();
                manager.o2class().unload();
                this.n = snapshot.getSnapshotInfo().getNumberOfObjects() + 1;
                this.r = 1;
                this.dom = new int[(this.n + 1)];
                this.parent = new int[(this.n + 1)];
                this.anchestor = new int[(this.n + 1)];
                this.vertex = new int[(this.n + 1)];
                this.label = new int[(this.n + 1)];
                this.semi = new int[(this.n + 1)];
                this.bucket = new int[(this.n + 1)];
                Arrays.fill(this.bucket, -1);
            } catch (Throwable e) {
                throw new SnapshotException(e);
            }
        }

        public void compute() throws IOException, SnapshotException, OperationCanceledException {
            int i;
            IProgressListener progressListener0 = this.monitor.nextMonitor();
            progressListener0.beginTask(Messages.DominatorTree_DominatorTreeCalculation, 3);
            this.n = 0;
            dfs(this.r);
            this.snapshot.getIndexManager().outbound().unload();
            IProgressListener progressListener = this.monitor.nextMonitor();
            progressListener.beginTask(Messages.DominatorTree_ComputingDominators.pattern, this.n / 1000);
            for (i = this.n; i >= 2; i--) {
                int v;
                int u;
                int w = this.vertex[i];
                for (int v2 : getPredecessors(w)) {
                    v2 = v2 + 2;
                    if (v2 >= 0) {
                        u = eval(v2);
                        if (this.semi[u] < this.semi[w]) {
                            this.semi[w] = this.semi[u];
                        }
                    }
                }
                this.bucket[w] = this.bucket[this.vertex[this.semi[w]]];
                this.bucket[this.vertex[this.semi[w]]] = w;
                link(this.parent[w], w);
                v2 = this.bucket[this.parent[w]];
                while (v2 != -1) {
                    u = eval(v2);
                    if (this.semi[u] < this.semi[v2]) {
                        this.dom[v2] = u;
                    } else {
                        this.dom[v2] = this.parent[w];
                    }
                    v2 = this.bucket[v2];
                }
                this.bucket[this.parent[w]] = -1;
                if (i % 1000 == 0) {
                    if (progressListener.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    progressListener.worked(1);
                }
            }
            for (i = 2; i <= this.n; i++) {
                w = this.vertex[i];
                if (this.dom[w] != this.vertex[this.semi[w]]) {
                    this.dom[w] = this.dom[this.dom[w]];
                }
            }
            this.dom[this.r] = 0;
            progressListener.done();
            this.bucket = null;
            this.semi = null;
            this.label = null;
            this.vertex = null;
            this.anchestor = null;
            this.parent = null;
            this.snapshot.getIndexManager().inbound().unload();
            if (progressListener0.isCanceled()) {
                throw new OperationCanceledException();
            }
            this.snapshot.getIndexManager().setReader(Index.DOMINATOR, new IntIndexStreamer().writeTo(Index.DOMINATOR.getFile(this.snapshot.getSnapshotInfo().getPrefix()), (IteratorInt) new IteratorInt() {
                int nextIndex = 2;

                public boolean hasNext() {
                    return this.nextIndex < Calculator.this.dom.length;
                }

                public int next() {
                    int[] access$000 = Calculator.this.dom;
                    int i = this.nextIndex;
                    this.nextIndex = i + 1;
                    return access$000[i];
                }
            }));
            int[] objectIds = new int[(this.snapshot.getSnapshotInfo().getNumberOfObjects() + 2)];
            for (i = 0; i < objectIds.length; i++) {
                objectIds[i] = i - 2;
            }
            objectIds[0] = -2;
            objectIds[1] = ROOT_VALUE;
            progressListener0.worked(1);
            ArrayUtils.sort(this.dom, objectIds, 2, this.dom.length - 2);
            progressListener0.worked(1);
            FlatDominatorTree tree = new FlatDominatorTree(this.snapshot, this.dom, objectIds, ROOT_VALUE);
            if (progressListener0.isCanceled()) {
                throw new OperationCanceledException();
            }
            writeIndexFiles(tree);
            progressListener0.done();
        }

        private void dfs(int root) throws UnsupportedOperationException {
            IProgressListener progressListener = this.monitor.nextMonitor();
            progressListener.beginTask(Messages.DominatorTree_DepthFirstSearch, this.snapshot.getSnapshotInfo().getNumberOfObjects() >> 16);
            int capacity = 2048;
            int[] currentElementStack = new int[2048];
            int[] currentSuccessorStack = new int[2048];
            Object[] successorsStack = new Object[2048];
            int v = root;
            int[] successors = this.gcRootsArray;
            currentElementStack[0] = root;
            successorsStack[0] = successors;
            currentSuccessorStack[0] = 0;
            int size = 0 + 1;
            while (size > 0) {
                v = currentElementStack[size - 1];
                successors = (int[]) successorsStack[size - 1];
                int currentSuccessor = currentSuccessorStack[size - 1];
                if (this.semi[v] == 0) {
                    this.n++;
                    this.semi[v] = this.n;
                    this.vertex[this.n] = v;
                    this.label[v] = v;
                    this.anchestor[v] = 0;
                }
                if (currentSuccessor < successors.length) {
                    int currentSuccessor2 = currentSuccessor + 1;
                    int w = successors[currentSuccessor] + 2;
                    currentSuccessorStack[size - 1] = currentSuccessor2;
                    if (this.semi[w] == 0) {
                        this.parent[w] = v;
                        successors = this.outboundIndex.get(w - 2);
                        if (size == capacity) {
                            int newCapacity = capacity << 1;
                            int[] newArr = new int[newCapacity];
                            System.arraycopy(currentElementStack, 0, newArr, 0, capacity);
                            currentElementStack = newArr;
                            newArr = new int[newCapacity];
                            System.arraycopy(currentSuccessorStack, 0, newArr, 0, capacity);
                            currentSuccessorStack = newArr;
                            Object[] newSuccessorsArr = new Object[newCapacity];
                            System.arraycopy(successorsStack, 0, newSuccessorsArr, 0, capacity);
                            successorsStack = newSuccessorsArr;
                            capacity = newCapacity;
                        }
                        currentElementStack[size] = w;
                        successorsStack[size] = successors;
                        currentSuccessorStack[size] = 0;
                        size++;
                        if ((this.n & 65535) == 0) {
                            if (progressListener.isCanceled()) {
                                throw new OperationCanceledException();
                            }
                            progressListener.worked(1);
                        }
                    }
                    currentSuccessor = currentSuccessor2;
                } else {
                    size--;
                }
            }
            progressListener.done();
        }

        private int[] getPredecessors(int v) {
            v -= 2;
            if (this.gcRootsSet.get(v)) {
                return ROOT_VALUE_ARR;
            }
            return this.inboundIndex.get(v);
        }

        private void compress(int v) {
            IntStack stack = new IntStack();
            while (this.anchestor[this.anchestor[v]] != 0) {
                stack.push(v);
                v = this.anchestor[v];
            }
            while (stack.size() > 0) {
                v = stack.pop();
                if (this.semi[this.label[this.anchestor[v]]] < this.semi[this.label[v]]) {
                    this.label[v] = this.label[this.anchestor[v]];
                }
                this.anchestor[v] = this.anchestor[this.anchestor[v]];
            }
        }

        private int eval(int v) {
            if (this.anchestor[v] == 0) {
                return v;
            }
            compress(v);
            return this.label[v];
        }

        private void link(int v, int w) {
            this.anchestor[w] = v;
        }

        private void writeIndexFiles(FlatDominatorTree tree) throws IOException {
            IntArray1NWriter writer = new IntArray1NWriter(this.dom.length - 1, Index.DOMINATED.getFile(this.snapshot.getSnapshotInfo().getPrefix()));
            int numberOfObjects = this.snapshot.getSnapshotInfo().getNumberOfObjects();
            IProgressListener progressListener = this.monitor.nextMonitor();
            progressListener.beginTask(Messages.DominatorTree_CreateDominatorsIndexFile, numberOfObjects / 1000);
            for (int i = -1; i < numberOfObjects; i++) {
                int[] successors = tree.getSuccessorsArr(i);
                tree.sortByTotalSize(successors);
                writer.log(i + 1, successors);
                if (i % 1000 == 0) {
                    if (progressListener.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    progressListener.worked(1);
                }
            }
            this.snapshot.getIndexManager().setReader(Index.DOMINATED, writer.flush());
            progressListener.done();
        }
    }

    public static void calculate(SnapshotImpl snapshot, IProgressListener listener) throws SnapshotException, IOException {
        new Calculator(snapshot, listener).compute();
    }
}
