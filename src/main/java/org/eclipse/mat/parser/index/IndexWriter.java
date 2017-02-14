package org.eclipse.mat.parser.index;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import com.umeng.socialize.common.SocializeConstants;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.eclipse.mat.collect.ArrayInt;
import org.eclipse.mat.collect.ArrayIntCompressed;
import org.eclipse.mat.collect.ArrayLong;
import org.eclipse.mat.collect.ArrayLongCompressed;
import org.eclipse.mat.collect.ArrayUtils;
import org.eclipse.mat.collect.BitField;
import org.eclipse.mat.collect.HashMapIntLong;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.collect.IteratorLong;
import org.eclipse.mat.collect.SetInt;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyObjectsIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.index.IndexReader.IntIndex1NSortedReader;
import org.eclipse.mat.parser.index.IndexReader.IntIndexReader;
import org.eclipse.mat.parser.index.IndexReader.LongIndexReader;
import org.eclipse.mat.parser.io.BitInputStream;
import org.eclipse.mat.parser.io.BitOutputStream;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.OperationCanceledException;

public abstract class IndexWriter {
    public static final int PAGE_SIZE_INT = 1000000;
    public static final int PAGE_SIZE_LONG = 500000;

    static abstract class IntIndex<V> {
        int pageSize;
        Pages<V> pages;
        int size;

        protected abstract ArrayIntCompressed getPage(int i);

        protected IntIndex() {
        }

        protected IntIndex(int size) {
            init(size, IndexWriter.PAGE_SIZE_INT);
        }

        protected void init(int size, int pageSize) {
            this.size = size;
            this.pageSize = pageSize;
            this.pages = new Pages((size / pageSize) + 1);
        }

        public int get(int index) {
            return getPage(index / this.pageSize).get(index % this.pageSize);
        }

        public int[] getNext(int index, int length) {
            int[] answer = new int[length];
            int page = index / this.pageSize;
            int pageIndex = index % this.pageSize;
            ArrayIntCompressed array = getPage(page);
            int ii = 0;
            int pageIndex2 = pageIndex;
            while (ii < length) {
                pageIndex = pageIndex2 + 1;
                answer[ii] = array.get(pageIndex2);
                if (pageIndex >= this.pageSize) {
                    page++;
                    array = getPage(page);
                    pageIndex = 0;
                }
                ii++;
                pageIndex2 = pageIndex;
            }
            return answer;
        }

        public int[] getAll(int[] index) {
            int[] answer = new int[index.length];
            int page = -1;
            ArrayIntCompressed array = null;
            for (int ii = 0; ii < answer.length; ii++) {
                int p = index[ii] / this.pageSize;
                if (p != page) {
                    page = p;
                    array = getPage(p);
                }
                answer[ii] = array.get(index[ii] % this.pageSize);
            }
            return answer;
        }

        public void set(int index, int value) {
            getPage(index / this.pageSize).set(index % this.pageSize, value);
        }

        public synchronized void unload() {
            this.pages = new Pages((this.size / this.pageSize) + 1);
        }

        public int size() {
            return this.size;
        }

        public IteratorInt iterator() {
            return new IntIndexIterator(this);
        }
    }

    static abstract class LongIndex {
        private static final int DEPTH = 10;
        HashMapIntLong binarySearchCache = new HashMapIntLong(1024);
        int pageSize;
        HashMapIntObject<Object> pages;
        int size;

        protected abstract ArrayLongCompressed getPage(int i);

        protected LongIndex() {
        }

        protected LongIndex(int size) {
            init(size, IndexWriter.PAGE_SIZE_LONG);
        }

        protected void init(int size, int pageSize) {
            this.size = size;
            this.pageSize = pageSize;
            this.pages = new HashMapIntObject((size / pageSize) + 1);
        }

        public long get(int index) {
            return getPage(index / this.pageSize).get(index % this.pageSize);
        }

        public long[] getNext(int index, int length) {
            long[] answer = new long[length];
            int page = index / this.pageSize;
            int pageIndex = index % this.pageSize;
            ArrayLongCompressed array = getPage(page);
            int ii = 0;
            int pageIndex2 = pageIndex;
            while (ii < length) {
                pageIndex = pageIndex2 + 1;
                answer[ii] = array.get(pageIndex2);
                if (pageIndex >= this.pageSize) {
                    page++;
                    array = getPage(page);
                    pageIndex = 0;
                }
                ii++;
                pageIndex2 = pageIndex;
            }
            return answer;
        }

        public int reverse(long value) {
            int depth;
            long midVal;
            int low = 0;
            int high = this.size - 1;
            int page = -1;
            ArrayLongCompressed array = null;
            int depth2 = 0;
            while (low <= high) {
                int i = (low + high) >> 1;
                depth = depth2 + 1;
                int p;
                if (depth2 < 10) {
                    try {
                        midVal = this.binarySearchCache.get(i);
                    } catch (NoSuchElementException e) {
                        p = i / this.pageSize;
                        if (p != page) {
                            page = p;
                            array = getPage(p);
                        }
                        midVal = array.get(i % this.pageSize);
                        this.binarySearchCache.put(i, midVal);
                    }
                } else {
                    p = i / this.pageSize;
                    if (p != page) {
                        page = p;
                        array = getPage(p);
                    }
                    midVal = array.get(i % this.pageSize);
                }
                if (midVal < value) {
                    low = i + 1;
                } else if (midVal <= value) {
                    return i;
                } else {
                    high = i - 1;
                }
                depth2 = depth;
            }
            depth = depth2;
            return -(low + 1);
        }

        public void set(int index, long value) {
            getPage(index / this.pageSize).set(index % this.pageSize, value);
        }

        public synchronized void unload() {
            this.pages = new HashMapIntObject((this.size / this.pageSize) + 1);
            this.binarySearchCache = new HashMapIntLong(1024);
        }

        public int size() {
            return this.size;
        }

        public IteratorLong iterator() {
            return new LongIndexIterator(this);
        }
    }

    public static class Identifier implements IOne2LongIndex {
        long[] identifiers;
        int size;

        public void add(long id) {
            if (this.identifiers == null) {
                this.identifiers = new long[10000];
                this.size = 0;
            }
            if (this.size + 1 > this.identifiers.length) {
                int newCapacity = ((this.identifiers.length * 3) / 2) + 1;
                if (newCapacity < this.size + 1) {
                    newCapacity = this.size + 1;
                }
                this.identifiers = IndexWriter.copyOf(this.identifiers, newCapacity);
            }
            long[] jArr = this.identifiers;
            int i = this.size;
            this.size = i + 1;
            jArr[i] = id;
        }

        public void sort() {
            Arrays.sort(this.identifiers, 0, this.size);
        }

        public int size() {
            return this.size;
        }

        public long get(int index) {
            if (index >= 0 && index < this.size) {
                return this.identifiers[index];
            }
            throw new IndexOutOfBoundsException();
        }

        public int reverse(long val) {
            int a = 0;
            int c = this.size;
            while (a < c) {
                int i = (a + c) >>> 1;
                long probeVal = get(i);
                if (val < probeVal) {
                    c = i;
                } else if (probeVal >= val) {
                    return i;
                } else {
                    a = i + 1;
                }
            }
            return -1 - a;
        }

        public IteratorLong iterator() {
            return new IteratorLong() {
                int index = 0;

                public boolean hasNext() {
                    return this.index < Identifier.this.size;
                }

                public long next() {
                    long[] jArr = Identifier.this.identifiers;
                    int i = this.index;
                    this.index = i + 1;
                    return jArr[i];
                }
            };
        }

        public long[] getNext(int index, int length) {
            long[] answer = new long[length];
            for (int ii = 0; ii < length; ii++) {
                answer[ii] = this.identifiers[index + ii];
            }
            return answer;
        }

        public void close() throws IOException {
        }

        public void delete() {
            this.identifiers = null;
        }

        public void unload() throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    public static class InboundWriter {
        int bitLength;
        File indexFile;
        int pageSize;
        int[] segmentSizes;
        BitOutputStream[] segments;
        int size;

        public InboundWriter(int size, File indexFile) throws IOException {
            this.size = size;
            this.indexFile = indexFile;
            int segments = 1;
            while (segments < (size / IndexWriter.PAGE_SIZE_LONG) + 1) {
                segments <<= 1;
            }
            this.bitLength = IndexWriter.mostSignificantBit(size) + 1;
            this.pageSize = (size / segments) + 1;
            this.segments = new BitOutputStream[segments];
            this.segmentSizes = new int[segments];
        }

        public void log(int objectIndex, int refIndex, boolean isPseudo) throws IOException {
            int segment = objectIndex / this.pageSize;
            if (this.segments[segment] == null) {
                this.segments[segment] = new BitOutputStream(new FileOutputStream(new File(this.indexFile.getAbsolutePath() + segment + ".log")));
            }
            this.segments[segment].writeBit(isPseudo ? 1 : 0);
            this.segments[segment].writeInt(objectIndex, this.bitLength);
            this.segments[segment].writeInt(refIndex, this.bitLength);
            int[] iArr = this.segmentSizes;
            iArr[segment] = iArr[segment] + 1;
        }

        public IOne2ManyObjectsIndex flush(IProgressListener monitor, KeyWriter keyWriter) throws IOException {
            Throwable th;
            close();
            int[] header = new int[this.size];
            DataOutputStream index = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.indexFile), 262144));
            BitInputStream bitInputStream = null;
            try {
                IntIndexStreamer body = new IntIndexStreamer();
                body.openStream(index, 0);
                int segment = 0;
                BitInputStream segmentIn = null;
                while (segment < this.segments.length) {
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    try {
                        File file = new File(this.indexFile.getAbsolutePath() + segment + ".log");
                        if (file.exists()) {
                            BitInputStream bitInputStream2 = new BitInputStream(new FileInputStream(file));
                            int[] objIndex = new int[this.segmentSizes[segment]];
                            int[] refIndex = new int[this.segmentSizes[segment]];
                            for (int ii = 0; ii < this.segmentSizes[segment]; ii++) {
                                boolean isPseudo = bitInputStream2.readBit() == 1;
                                objIndex[ii] = bitInputStream2.readInt(this.bitLength);
                                refIndex[ii] = bitInputStream2.readInt(this.bitLength);
                                if (isPseudo) {
                                    refIndex[ii] = -1 - refIndex[ii];
                                }
                            }
                            bitInputStream2.close();
                            bitInputStream = null;
                            if (monitor.isCanceled()) {
                                throw new OperationCanceledException();
                            }
                            file.delete();
                            processSegment(monitor, keyWriter, header, body, objIndex, refIndex);
                        } else {
                            bitInputStream = segmentIn;
                        }
                        segment++;
                        segmentIn = bitInputStream;
                    } catch (Throwable th2) {
                        th = th2;
                        bitInputStream = segmentIn;
                    }
                }
                long divider = body.closeStream();
                IOne2OneIndex headerIndex = new IntIndexStreamer().writeTo(index, divider, header);
                index.writeLong(divider);
                index.flush();
                index.close();
                index = null;
                IOne2ManyObjectsIndex inboundReader = new InboundReader(this.indexFile, headerIndex, body.getReader(null));
                if (index != null) {
                    try {
                        index.close();
                    } catch (IOException e) {
                    }
                }
                if (segmentIn != null) {
                    try {
                        segmentIn.close();
                    } catch (IOException e2) {
                    }
                }
                if (monitor.isCanceled()) {
                    cancel();
                }
                return inboundReader;
            } catch (Throwable th3) {
                th = th3;
            }
            if (index != null) {
                try {
                    index.close();
                } catch (IOException e3) {
                }
            }
            if (bitInputStream != null) {
                try {
                    bitInputStream.close();
                } catch (IOException e4) {
                }
            }
            if (monitor.isCanceled()) {
                cancel();
            }
            throw th;
            if (bitInputStream != null) {
                bitInputStream.close();
            }
            if (monitor.isCanceled()) {
                cancel();
            }
            throw th;
            if (monitor.isCanceled()) {
                cancel();
            }
            throw th;
        }

        private void processSegment(IProgressListener monitor, KeyWriter keyWriter, int[] header, IntIndexStreamer body, int[] objIndex, int[] refIndex) throws IOException {
            ArrayUtils.sort(objIndex, refIndex);
            int start = 0;
            int previous = -1;
            int ii = 0;
            while (ii <= objIndex.length) {
                if (ii == 0) {
                    start = ii;
                    previous = objIndex[ii];
                } else if (ii == objIndex.length || previous != objIndex[ii]) {
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    header[previous] = body.size() + 1;
                    processObject(keyWriter, header, body, previous, refIndex, start, ii);
                    if (ii < objIndex.length) {
                        previous = objIndex[ii];
                        start = ii;
                    }
                }
                ii++;
            }
        }

        private void processObject(KeyWriter keyWriter, int[] header, IntIndexStreamer body, int objectId, int[] refIndex, int fromIndex, int toIndex) throws IOException {
            Arrays.sort(refIndex, fromIndex, toIndex);
            int endPseudo = fromIndex;
            int jj;
            if (toIndex - fromIndex > 100000) {
                BitField duplicates = new BitField(this.size);
                jj = fromIndex;
                while (jj < toIndex && refIndex[jj] < 0) {
                    endPseudo++;
                    refIndex[jj] = (-refIndex[jj]) - 1;
                    if (!duplicates.get(refIndex[jj])) {
                        body.add(refIndex[jj]);
                        duplicates.set(refIndex[jj]);
                    }
                    jj++;
                }
                while (jj < toIndex) {
                    if ((jj == fromIndex || refIndex[jj - 1] != refIndex[jj]) && !duplicates.get(refIndex[jj])) {
                        body.add(refIndex[jj]);
                    }
                    jj++;
                }
            } else {
                SetInt duplicates2 = new SetInt(toIndex - fromIndex);
                jj = fromIndex;
                while (jj < toIndex && refIndex[jj] < 0) {
                    endPseudo++;
                    refIndex[jj] = (-refIndex[jj]) - 1;
                    if (duplicates2.add(refIndex[jj])) {
                        body.add(refIndex[jj]);
                    }
                    jj++;
                }
                while (jj < toIndex) {
                    if ((jj == fromIndex || refIndex[jj - 1] != refIndex[jj]) && !duplicates2.contains(refIndex[jj])) {
                        body.add(refIndex[jj]);
                    }
                    jj++;
                }
            }
            if (endPseudo > fromIndex) {
                keyWriter.storeKey(objectId, new int[]{header[objectId] - 1, endPseudo - fromIndex});
            }
        }

        public synchronized void cancel() {
            try {
                close();
                if (this.segments != null) {
                    for (int ii = 0; ii < this.segments.length; ii++) {
                        new File(this.indexFile.getAbsolutePath() + ii + ".log").delete();
                    }
                }
                this.indexFile.delete();
            } catch (IOException e) {
                this.indexFile.delete();
            } catch (Throwable th) {
                this.indexFile.delete();
            }
        }

        public synchronized void close() throws IOException {
            if (this.segments != null) {
                for (int ii = 0; ii < this.segments.length; ii++) {
                    if (this.segments[ii] != null) {
                        this.segments[ii].flush();
                        this.segments[ii].close();
                        this.segments[ii] = null;
                    }
                }
            }
        }

        public File getIndexFile() {
            return this.indexFile;
        }
    }

    public static class IntArray1NWriter {
        IntIndexStreamer body = new IntIndexStreamer();
        int[] header;
        File indexFile;
        DataOutputStream out;

        public void cancel() {
            /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r2 = this;
            r0 = r2.out;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            if (r0 == 0) goto L_0x000f;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
        L_0x0004:
            r0 = r2.out;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0.close();	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0 = 0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r2.body = r0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0 = 0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r2.out = r0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
        L_0x000f:
            r0 = r2.indexFile;
            r0 = r0.exists();
            if (r0 == 0) goto L_0x001c;
        L_0x0017:
            r0 = r2.indexFile;
            r0.delete();
        L_0x001c:
            return;
        L_0x001d:
            r0 = move-exception;
            r0 = r2.indexFile;
            r0 = r0.exists();
            if (r0 == 0) goto L_0x001c;
        L_0x0026:
            r0 = r2.indexFile;
            r0.delete();
            goto L_0x001c;
        L_0x002c:
            r0 = move-exception;
            r1 = r2.indexFile;
            r1 = r1.exists();
            if (r1 == 0) goto L_0x003a;
        L_0x0035:
            r1 = r2.indexFile;
            r1.delete();
        L_0x003a:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.eclipse.mat.parser.index.IndexWriter.IntArray1NWriter.cancel():void");
        }

        public IntArray1NWriter(int size, File indexFile) throws IOException {
            this.header = new int[size];
            this.indexFile = indexFile;
            this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            this.body.openStream(this.out, 0);
        }

        public void log(Identifier identifer, int index, ArrayLong references) throws IOException {
            long pseudo = references.firstElement();
            references.sort();
            int[] objectIds = new int[references.size()];
            int length = 1;
            long last = references.firstElement() - 1;
            for (int ii = 0; ii < objectIds.length; ii++) {
                long current = references.get(ii);
                if (last != current) {
                    int objectId = identifer.reverse(current);
                    if (objectId >= 0) {
                        int jj;
                        if (current == pseudo) {
                            jj = 0;
                        } else {
                            jj = length;
                            length++;
                        }
                        objectIds[jj] = objectId;
                    }
                }
                last = current;
            }
            set(index, objectIds, 0, length);
        }

        public void log(int index, ArrayInt references) throws IOException {
            set(index, references.toArray(), 0, references.size());
        }

        public void log(int index, int[] values) throws IOException {
            set(index, values, 0, values.length);
        }

        protected void set(int index, int[] values, int offset, int length) throws IOException {
            this.header[index] = this.body.size();
            this.body.add(length);
            this.body.addAll(values, offset, length);
        }

        public IOne2ManyIndex flush() throws IOException {
            long divider = this.body.closeStream();
            IOne2OneIndex headerIndex = new IntIndexStreamer().writeTo(this.out, divider, this.header);
            this.out.writeLong(divider);
            this.out.close();
            this.out = null;
            return createReader(headerIndex, this.body.getReader(null));
        }

        protected IOne2ManyIndex createReader(IOne2OneIndex headerIndex, IOne2OneIndex bodyIndex) throws IOException {
            return new IntIndex1NReader(this.indexFile, headerIndex, bodyIndex);
        }

        public File getIndexFile() {
            return this.indexFile;
        }
    }

    public static class IntArray1NSortedWriter extends IntArray1NWriter {
        public IntArray1NSortedWriter(int size, File indexFile) throws IOException {
            super(size, indexFile);
        }

        protected void set(int index, int[] values, int offset, int length) throws IOException {
            this.header[index] = this.body.size() + 1;
            this.body.addAll(values, offset, length);
        }

        protected IOne2ManyIndex createReader(IOne2OneIndex headerIndex, IOne2OneIndex bodyIndex) throws IOException {
            return new IntIndex1NSortedReader(this.indexFile, headerIndex, bodyIndex);
        }
    }

    public static class IntArray1NUncompressedCollector {
        int[][] elements;
        File indexFile;

        public IntArray1NUncompressedCollector(int size, File indexFile) throws IOException {
            this.elements = new int[size][];
            this.indexFile = indexFile;
        }

        public void log(int classId, int methodId) {
            if (this.elements[classId] == null) {
                this.elements[classId] = new int[]{methodId};
                return;
            }
            int[] newChildren = new int[(this.elements[classId].length + 1)];
            System.arraycopy(this.elements[classId], 0, newChildren, 0, this.elements[classId].length);
            newChildren[this.elements[classId].length] = methodId;
            this.elements[classId] = newChildren;
        }

        public File getIndexFile() {
            return this.indexFile;
        }

        public IOne2ManyIndex flush() throws IOException {
            IntArray1NSortedWriter writer = new IntArray1NSortedWriter(this.elements.length, this.indexFile);
            for (int ii = 0; ii < this.elements.length; ii++) {
                if (this.elements[ii] != null) {
                    writer.log(ii, this.elements[ii]);
                }
            }
            return writer.flush();
        }
    }

    public static class IntIndexCollector extends IntIndex<ArrayIntCompressed> implements IOne2OneIndex {
        int mostSignificantBit;

        public /* bridge */ /* synthetic */ int get(int x0) {
            return super.get(x0);
        }

        public /* bridge */ /* synthetic */ int[] getAll(int[] x0) {
            return super.getAll(x0);
        }

        public /* bridge */ /* synthetic */ int[] getNext(int x0, int x1) {
            return super.getNext(x0, x1);
        }

        public /* bridge */ /* synthetic */ IteratorInt iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ void set(int x0, int x1) {
            super.set(x0, x1);
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ void unload() {
            super.unload();
        }

        public IntIndexCollector(int size, int mostSignificantBit) {
            super(size);
            this.mostSignificantBit = mostSignificantBit;
        }

        protected ArrayIntCompressed getPage(int page) {
            ArrayIntCompressed array = (ArrayIntCompressed) this.pages.get(page);
            if (array != null) {
                return array;
            }
            array = new ArrayIntCompressed(page < this.size / this.pageSize ? this.pageSize : this.size % this.pageSize, 31 - this.mostSignificantBit, 0);
            this.pages.put(page, array);
            return array;
        }

        public IOne2OneIndex writeTo(File indexFile) throws IOException {
            return new IntIndexStreamer().writeTo(indexFile, iterator());
        }

        public IOne2OneIndex writeTo(DataOutputStream out, long position) throws IOException {
            return new IntIndexStreamer().writeTo(out, position, iterator());
        }

        public void close() throws IOException {
        }

        public void delete() {
            this.pages = null;
        }
    }

    public static class IntIndexCollectorUncompressed {
        int[] dataElements;

        public IntIndexCollectorUncompressed(int size) {
            this.dataElements = new int[size];
        }

        public void set(int index, int value) {
            this.dataElements[index] = value;
        }

        public int get(int index) {
            return this.dataElements[index];
        }

        public IOne2OneIndex writeTo(File indexFile) throws IOException {
            return new IntIndexStreamer().writeTo(indexFile, this.dataElements);
        }
    }

    static class IntIndexIterator implements IteratorInt {
        IntIndex<?> intArray;
        int nextIndex = 0;

        public IntIndexIterator(IntIndex<?> intArray) {
            this.intArray = intArray;
        }

        public int next() {
            IntIndex intIndex = this.intArray;
            int i = this.nextIndex;
            this.nextIndex = i + 1;
            return intIndex.get(i);
        }

        public boolean hasNext() {
            return this.nextIndex < this.intArray.size();
        }
    }

    public static class IntIndexStreamer extends IntIndex<SoftReference<ArrayIntCompressed>> {
        int left;
        DataOutputStream out;
        int[] page;
        ArrayLong pageStart;

        public /* bridge */ /* synthetic */ int get(int x0) {
            return super.get(x0);
        }

        public /* bridge */ /* synthetic */ int[] getAll(int[] x0) {
            return super.getAll(x0);
        }

        public /* bridge */ /* synthetic */ int[] getNext(int x0, int x1) {
            return super.getNext(x0, x1);
        }

        public /* bridge */ /* synthetic */ IteratorInt iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ void set(int x0, int x1) {
            super.set(x0, x1);
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ void unload() {
            super.unload();
        }

        public IOne2OneIndex writeTo(File indexFile, IteratorInt iterator) throws IOException {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            openStream(out, 0);
            addAll(iterator);
            closeStream();
            out.close();
            return getReader(indexFile);
        }

        public IOne2OneIndex writeTo(File indexFile, int[] array) throws IOException {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            openStream(out, 0);
            addAll(array);
            closeStream();
            out.close();
            return getReader(indexFile);
        }

        public IOne2OneIndex writeTo(DataOutputStream out, long position, IteratorInt iterator) throws IOException {
            openStream(out, position);
            addAll(iterator);
            closeStream();
            return getReader(null);
        }

        public IOne2OneIndex writeTo(DataOutputStream out, long position, int[] array) throws IOException {
            openStream(out, position);
            addAll(array);
            closeStream();
            return getReader(null);
        }

        void openStream(DataOutputStream out, long position) {
            this.out = out;
            init(0, IndexWriter.PAGE_SIZE_INT);
            this.page = new int[this.pageSize];
            this.pageStart = new ArrayLong();
            this.pageStart.add(position);
            this.left = this.page.length;
        }

        long closeStream() throws IOException {
            if (this.left < this.page.length) {
                addPage();
            }
            for (int jj = 0; jj < this.pageStart.size(); jj++) {
                this.out.writeLong(this.pageStart.get(jj));
            }
            this.out.writeInt(this.pageSize);
            this.out.writeInt(this.size);
            this.page = null;
            this.out = null;
            return ((this.pageStart.lastElement() + ((long) (this.pageStart.size() * 8))) + 8) - this.pageStart.firstElement();
        }

        IntIndexReader getReader(File indexFile) {
            return new IntIndexReader(indexFile, this.pages, this.size, this.pageSize, this.pageStart.toArray());
        }

        void addAll(IteratorInt iterator) throws IOException {
            while (iterator.hasNext()) {
                add(iterator.next());
            }
        }

        void add(int value) throws IOException {
            if (this.left == 0) {
                addPage();
            }
            int[] iArr = this.page;
            int length = this.page.length;
            int i = this.left;
            this.left = i - 1;
            iArr[length - i] = value;
            this.size++;
        }

        void addAll(int[] values) throws IOException {
            addAll(values, 0, values.length);
        }

        void addAll(int[] values, int offset, int length) throws IOException {
            while (length > 0) {
                if (this.left == 0) {
                    addPage();
                }
                int chunk = Math.min(this.left, length);
                System.arraycopy(values, offset, this.page, this.page.length - this.left, chunk);
                this.left -= chunk;
                this.size += chunk;
                length -= chunk;
                offset += chunk;
            }
        }

        private void addPage() throws IOException {
            ArrayIntCompressed array = new ArrayIntCompressed(this.page, 0, this.page.length - this.left);
            byte[] buffer = array.toByteArray();
            this.out.write(buffer);
            int written = buffer.length;
            this.pages.put(this.pages.size(), new SoftReference(array));
            this.pageStart.add(this.pageStart.lastElement() + ((long) written));
            this.left = this.page.length;
        }

        protected ArrayIntCompressed getPage(int page) {
            throw new UnsupportedOperationException();
        }
    }

    public interface KeyWriter {
        void storeKey(int i, Serializable serializable);
    }

    public static class LongArray1NWriter {
        LongIndexStreamer body = new LongIndexStreamer();
        int[] header;
        File indexFile;
        DataOutputStream out;

        public void cancel() {
            /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r2 = this;
            r0 = r2.out;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            if (r0 == 0) goto L_0x000f;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
        L_0x0004:
            r0 = r2.out;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0.close();	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0 = 0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r2.body = r0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r0 = 0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
            r2.out = r0;	 Catch:{ IOException -> 0x001d, all -> 0x002c }
        L_0x000f:
            r0 = r2.indexFile;
            r0 = r0.exists();
            if (r0 == 0) goto L_0x001c;
        L_0x0017:
            r0 = r2.indexFile;
            r0.delete();
        L_0x001c:
            return;
        L_0x001d:
            r0 = move-exception;
            r0 = r2.indexFile;
            r0 = r0.exists();
            if (r0 == 0) goto L_0x001c;
        L_0x0026:
            r0 = r2.indexFile;
            r0.delete();
            goto L_0x001c;
        L_0x002c:
            r0 = move-exception;
            r1 = r2.indexFile;
            r1 = r1.exists();
            if (r1 == 0) goto L_0x003a;
        L_0x0035:
            r1 = r2.indexFile;
            r1.delete();
        L_0x003a:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.eclipse.mat.parser.index.IndexWriter.LongArray1NWriter.cancel():void");
        }

        public LongArray1NWriter(int size, File indexFile) throws IOException {
            this.header = new int[size];
            this.indexFile = indexFile;
            this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            this.body.openStream(this.out, 0);
        }

        public void log(int index, long[] values) throws IOException {
            set(index, values, 0, values.length);
        }

        protected void set(int index, long[] values, int offset, int length) throws IOException {
            this.header[index] = this.body.size() + 1;
            this.body.add((long) length);
            this.body.addAll(values, offset, length);
        }

        public void flush() throws IOException {
            long divider = this.body.closeStream();
            new IntIndexStreamer().writeTo(this.out, divider, this.header).close();
            this.out.writeLong(divider);
            this.out.close();
            this.out = null;
        }

        public File getIndexFile() {
            return this.indexFile;
        }
    }

    public static class LongIndexCollector extends LongIndex {
        int mostSignificantBit;

        public /* bridge */ /* synthetic */ long get(int x0) {
            return super.get(x0);
        }

        public /* bridge */ /* synthetic */ long[] getNext(int x0, int x1) {
            return super.getNext(x0, x1);
        }

        public /* bridge */ /* synthetic */ IteratorLong iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ int reverse(long x0) {
            return super.reverse(x0);
        }

        public /* bridge */ /* synthetic */ void set(int x0, long x1) {
            super.set(x0, x1);
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ void unload() {
            super.unload();
        }

        public LongIndexCollector(int size, int mostSignificantBit) {
            super(size);
            this.mostSignificantBit = mostSignificantBit;
        }

        protected ArrayLongCompressed getPage(int page) {
            ArrayLongCompressed array = (ArrayLongCompressed) this.pages.get(page);
            if (array != null) {
                return array;
            }
            array = new ArrayLongCompressed(page < this.size / this.pageSize ? this.pageSize : this.size % this.pageSize, 63 - this.mostSignificantBit, 0);
            this.pages.put(page, array);
            return array;
        }

        public IOne2LongIndex writeTo(File indexFile) throws IOException {
            return new LongIndexStreamer().writeTo(indexFile, this.size, this.pages, this.pageSize);
        }
    }

    public static class LongIndexCollectorUncompressed {
        long[] dataElements;

        public LongIndexCollectorUncompressed(int size) {
            this.dataElements = new long[size];
        }

        public void set(int index, long value) {
            this.dataElements[index] = value;
        }

        public long get(int index) {
            return this.dataElements[index];
        }

        public IOne2LongIndex writeTo(File indexFile) throws IOException {
            return new LongIndexStreamer().writeTo(indexFile, this.dataElements);
        }
    }

    static class LongIndexIterator implements IteratorLong {
        LongIndex longArray;
        int nextIndex = 0;

        public LongIndexIterator(LongIndex longArray) {
            this.longArray = longArray;
        }

        public long next() {
            LongIndex longIndex = this.longArray;
            int i = this.nextIndex;
            this.nextIndex = i + 1;
            return longIndex.get(i);
        }

        public boolean hasNext() {
            return this.nextIndex < this.longArray.size();
        }
    }

    public static class LongIndexStreamer extends LongIndex {
        int left;
        DataOutputStream out;
        long[] page;
        ArrayLong pageStart;

        public /* bridge */ /* synthetic */ long get(int x0) {
            return super.get(x0);
        }

        public /* bridge */ /* synthetic */ long[] getNext(int x0, int x1) {
            return super.getNext(x0, x1);
        }

        public /* bridge */ /* synthetic */ IteratorLong iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ int reverse(long x0) {
            return super.reverse(x0);
        }

        public /* bridge */ /* synthetic */ void set(int x0, long x1) {
            super.set(x0, x1);
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ void unload() {
            super.unload();
        }

        public LongIndexStreamer(File indexFile) throws IOException {
            openStream(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile))), 0);
        }

        public void close() throws IOException {
            DataOutputStream out = this.out;
            closeStream();
            out.close();
        }

        public IOne2LongIndex writeTo(File indexFile, int size, HashMapIntObject<Object> pages, int pageSize) throws IOException {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            openStream(out, 0);
            int noOfPages = (size / pageSize) + (size % pageSize > 0 ? 1 : 0);
            for (int ii = 0; ii < noOfPages; ii++) {
                ArrayLongCompressed a = (ArrayLongCompressed) pages.get(ii);
                int len = ii + 1 < noOfPages ? pageSize : size % pageSize;
                if (a == null) {
                    addAll(new long[len]);
                } else {
                    for (int jj = 0; jj < len; jj++) {
                        add(a.get(jj));
                    }
                }
            }
            closeStream();
            out.close();
            return getReader(indexFile);
        }

        public IOne2LongIndex writeTo(File indexFile, long[] array) throws IOException {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            openStream(out, 0);
            addAll(array);
            closeStream();
            out.close();
            return getReader(indexFile);
        }

        public IOne2LongIndex writeTo(File indexFile, IteratorLong iterator) throws IOException {
            FileOutputStream fos = new FileOutputStream(indexFile);
            try {
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(fos));
                openStream(out, 0);
                addAll(iterator);
                closeStream();
                out.flush();
                IOne2LongIndex reader = getReader(indexFile);
                return reader;
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }

        public IOne2LongIndex writeTo(File indexFile, ArrayLong array) throws IOException {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indexFile)));
            openStream(out, 0);
            addAll(array);
            closeStream();
            out.close();
            return getReader(indexFile);
        }

        void openStream(DataOutputStream out, long position) {
            this.out = out;
            init(0, IndexWriter.PAGE_SIZE_LONG);
            this.page = new long[this.pageSize];
            this.pageStart = new ArrayLong();
            this.pageStart.add(position);
            this.left = this.page.length;
        }

        long closeStream() throws IOException {
            if (this.left < this.page.length) {
                addPage();
            }
            for (int jj = 0; jj < this.pageStart.size(); jj++) {
                this.out.writeLong(this.pageStart.get(jj));
            }
            this.out.writeInt(this.pageSize);
            this.out.writeInt(this.size);
            this.page = null;
            this.out = null;
            return ((this.pageStart.lastElement() + ((long) (this.pageStart.size() * 8))) + 8) - this.pageStart.firstElement();
        }

        LongIndexReader getReader(File indexFile) throws IOException {
            return new LongIndexReader(indexFile, this.pages, this.size, this.pageSize, this.pageStart.toArray());
        }

        public void addAll(IteratorLong iterator) throws IOException {
            while (iterator.hasNext()) {
                add(iterator.next());
            }
        }

        public void addAll(ArrayLong array) throws IOException {
            IteratorLong e = array.iterator();
            while (e.hasNext()) {
                add(e.next());
            }
        }

        public void add(long value) throws IOException {
            if (this.left == 0) {
                addPage();
            }
            long[] jArr = this.page;
            int length = this.page.length;
            int i = this.left;
            this.left = i - 1;
            jArr[length - i] = value;
            this.size++;
        }

        public void addAll(long[] values) throws IOException {
            addAll(values, 0, values.length);
        }

        public void addAll(long[] values, int offset, int length) throws IOException {
            while (length > 0) {
                if (this.left == 0) {
                    addPage();
                }
                int chunk = Math.min(this.left, length);
                System.arraycopy(values, offset, this.page, this.page.length - this.left, chunk);
                this.left -= chunk;
                this.size += chunk;
                length -= chunk;
                offset += chunk;
            }
        }

        private void addPage() throws IOException {
            ArrayLongCompressed array = new ArrayLongCompressed(this.page, 0, this.page.length - this.left);
            byte[] buffer = array.toByteArray();
            this.out.write(buffer);
            int written = buffer.length;
            this.pages.put(this.pages.size(), new SoftReference(array));
            this.pageStart.add(this.pageStart.lastElement() + ((long) written));
            this.left = this.page.length;
        }

        protected ArrayLongCompressed getPage(int page) {
            throw new UnsupportedOperationException();
        }
    }

    static class Pages<V> {
        Object[] elements;
        int size = 0;

        public Pages(int initialSize) {
            this.elements = new Object[initialSize];
        }

        private void ensureCapacity(int minCapacity) {
            int oldCapacity = this.elements.length;
            if (minCapacity > oldCapacity) {
                int newCapacity = ((oldCapacity * 3) / 2) + 1;
                if (newCapacity < minCapacity) {
                    newCapacity = minCapacity;
                }
                Object[] copy = new Object[newCapacity];
                System.arraycopy(this.elements, 0, copy, 0, Math.min(this.elements.length, newCapacity));
                this.elements = copy;
            }
        }

        public V get(int key) {
            return key >= this.elements.length ? null : this.elements[key];
        }

        public void put(int key, V value) {
            ensureCapacity(key + 1);
            this.elements[key] = value;
            this.size = Math.max(this.size, key + 1);
        }

        public int size() {
            return this.size;
        }
    }

    public static long[] copyOf(long[] original, int newLength) {
        long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static int mostSignificantBit(int x) {
        int length = 0;
        if ((SupportMenu.CATEGORY_MASK & x) != 0) {
            length = 0 + 16;
            x >>= 16;
        }
        if ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & x) != 0) {
            length += 8;
            x >>= 8;
        }
        if ((x & SocializeConstants.MASK_USER_CENTER_HIDE_AREA) != 0) {
            length += 4;
            x >>= 4;
        }
        if ((x & 12) != 0) {
            length += 2;
            x >>= 2;
        }
        if ((x & 2) != 0) {
            length++;
            x >>= 1;
        }
        if ((x & 1) != 0) {
            length++;
        }
        return length - 1;
    }

    public static int mostSignificantBit(long x) {
        long lead = x >>> 32;
        return lead == 0 ? mostSignificantBit((int) x) : mostSignificantBit((int) lead) + 32;
    }
}
