package org.eclipse.mat.parser.index;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.ArrayIntCompressed;
import org.eclipse.mat.collect.ArrayLongCompressed;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.IteratorInt;
import org.eclipse.mat.collect.IteratorLong;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2ManyObjectsIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2OneIndex;
import org.eclipse.mat.parser.io.SimpleBufferedRandomAccessInputStream;

public abstract class IndexReader {
    public static final boolean DEBUG = false;

    static class IntIndex1NReader implements IOne2ManyIndex {
        IntIndexReader body;
        IntIndexReader header;
        SimpleBufferedRandomAccessInputStream in;
        File indexFile;

        public IntIndex1NReader(File indexFile) throws IOException {
            try {
                this.indexFile = indexFile;
                open();
                long indexLength = indexFile.length();
                this.in.seek(indexLength - 8);
                long divider = this.in.readLong();
                this.header = new IntIndexReader(this.in, divider, (indexLength - divider) - 8);
                this.body = new IntIndexReader(this.in, 0, divider);
                this.body.LOCK = this.header.LOCK;
            } catch (RuntimeException e) {
                close();
                throw e;
            }
        }

        public IntIndex1NReader(File indexFile, IOne2OneIndex header, IOne2OneIndex body) {
            this.indexFile = indexFile;
            this.header = (IntIndexReader) header;
            this.body = (IntIndexReader) body;
            this.body.LOCK = this.header.LOCK;
            open();
        }

        public int[] get(int index) {
            int p = this.header.get(index);
            return this.body.getNext(p + 1, this.body.get(p));
        }

        protected synchronized void open() {
            try {
                if (this.in == null) {
                    this.in = new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(this.indexFile, "r"));
                    if (this.header != null) {
                        this.header.in = this.in;
                    }
                    if (this.body != null) {
                        this.body.in = this.in;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void close() {
            this.header.unload();
            this.body.unload();
            if (this.in != null) {
                try {
                    this.in.close();
                    this.in = null;
                    if (this.header != null) {
                        this.header.in = null;
                    }
                    if (this.body != null) {
                        this.body.in = null;
                    }
                } catch (IOException e) {
                    this.in = null;
                    if (this.header != null) {
                        this.header.in = null;
                    }
                    if (this.body != null) {
                        this.body.in = null;
                    }
                } catch (Throwable th) {
                    this.in = null;
                    if (this.header != null) {
                        this.header.in = null;
                    }
                    if (this.body != null) {
                        this.body.in = null;
                    }
                }
            }
        }

        public void unload() throws IOException {
            this.header.unload();
            this.body.unload();
        }

        public int size() {
            return this.header.size();
        }

        public void delete() {
            close();
            if (this.indexFile != null) {
                this.indexFile.delete();
            }
        }
    }

    public static class IntIndex1NSortedReader extends IntIndex1NReader {
        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        public /* bridge */ /* synthetic */ void delete() {
            super.delete();
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ void unload() throws IOException {
            super.unload();
        }

        public IntIndex1NSortedReader(File indexFile) throws IOException {
            super(indexFile);
        }

        public IntIndex1NSortedReader(File indexFile, IOne2OneIndex header, IOne2OneIndex body) throws IOException {
            super(indexFile, header, body);
        }

        public int[] get(int index) {
            int[] p;
            if (index + 1 < this.header.size()) {
                int index2 = index + 1;
                p = this.header.getNext(index, 2);
                if (p[0] == 0) {
                    index = index2;
                    return new int[0];
                }
                index = index2 + 1;
                while (p[1] < p[0] && index < this.header.size()) {
                    p[1] = this.header.get(index);
                    index++;
                }
                if (p[1] < p[0]) {
                    p[1] = this.body.size() + 1;
                }
            } else {
                p = new int[]{this.header.get(index), 0};
                if (p[0] == 0) {
                    return new int[0];
                }
                p[1] = this.body.size() + 1;
            }
            return this.body.getNext(p[0] - 1, p[1] - p[0]);
        }
    }

    static class InboundReader extends IntIndex1NSortedReader implements IOne2ManyObjectsIndex {
        public InboundReader(File indexFile) throws IOException {
            super(indexFile);
        }

        public InboundReader(File indexFile, IOne2OneIndex header, IOne2OneIndex body) throws IOException {
            super(indexFile, header, body);
        }

        public int[] getObjectsOf(Serializable key) throws SnapshotException, IOException {
            if (key == null) {
                return new int[0];
            }
            int[] next;
            int[] pos = (int[]) key;
            synchronized (this) {
                next = this.body.getNext(pos[0], pos[1]);
            }
            return next;
        }
    }

    public static class IntIndexReader extends IntIndex<SoftReference<ArrayIntCompressed>> implements IOne2OneIndex {
        public Object LOCK;
        public SimpleBufferedRandomAccessInputStream in;
        File indexFile;
        long[] pageStart;

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

        public IntIndexReader(File indexFile, Pages<SoftReference<ArrayIntCompressed>> pages, int size, int pageSize, long[] pageStart) {
            this.LOCK = new Object();
            this.size = size;
            this.pageSize = pageSize;
            this.pages = pages;
            this.indexFile = indexFile;
            this.pageStart = pageStart;
            if (indexFile != null) {
                open();
            }
        }

        public IntIndexReader(File indexFile) throws IOException {
            this(new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(indexFile, "r")), 0, indexFile.length());
            this.indexFile = indexFile;
        }

        public IntIndexReader(SimpleBufferedRandomAccessInputStream in, long start, long length) throws IOException {
            this.LOCK = new Object();
            this.in = in;
            this.in.seek((start + length) - 8);
            int pageSize = this.in.readInt();
            int size = this.in.readInt();
            init(size, pageSize);
            this.pageStart = new long[((size / pageSize) + (size % pageSize > 0 ? 2 : 1))];
            this.in.seek(((start + length) - 8) - ((long) (this.pageStart.length * 8)));
            this.in.readLongArray(this.pageStart);
        }

        private synchronized void open() {
            try {
                if (this.in == null) {
                    if (this.indexFile == null) {
                        throw new IOException(Messages.IndexReader_Error_IndexIsEmbedded.pattern);
                    }
                    this.in = new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(this.indexFile, "r"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void close() {
            unload();
            if (this.in != null) {
                try {
                    this.in.close();
                    this.in = null;
                } catch (IOException e) {
                    this.in = null;
                } catch (Throwable th) {
                    this.in = null;
                }
            }
        }

        protected ArrayIntCompressed getPage(int page) {
            IOException e;
            Throwable th;
            SoftReference<ArrayIntCompressed> ref = (SoftReference) this.pages.get(page);
            ArrayIntCompressed arrayIntCompressed = ref == null ? null : (ArrayIntCompressed) ref.get();
            if (arrayIntCompressed == null) {
                synchronized (this.LOCK) {
                    try {
                        ref = (SoftReference) this.pages.get(page);
                        if (ref == null) {
                            arrayIntCompressed = null;
                        } else {
                            arrayIntCompressed = (ArrayIntCompressed) ref.get();
                        }
                        if (arrayIntCompressed == null) {
                            try {
                                this.in.seek(this.pageStart[page]);
                                byte[] buffer = new byte[((int) (this.pageStart[page + 1] - this.pageStart[page]))];
                                if (this.in.read(buffer) != buffer.length) {
                                    throw new IOException();
                                }
                                ArrayIntCompressed array = new ArrayIntCompressed(buffer);
                                try {
                                    synchronized (this.pages) {
                                        this.pages.put(page, new SoftReference(array));
                                    }
                                    arrayIntCompressed = array;
                                } catch (IOException e2) {
                                    e = e2;
                                } catch (Throwable th2) {
                                    th = th2;
                                    arrayIntCompressed = array;
                                    throw th;
                                }
                            } catch (IOException e3) {
                                e = e3;
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            }
            return arrayIntCompressed;
        }

        public void delete() {
            close();
            if (this.indexFile != null) {
                this.indexFile.delete();
            }
        }
    }

    public static class LongIndex1NReader implements IIndexReader {
        LongIndexReader body;
        IntIndexReader header;
        SimpleBufferedRandomAccessInputStream in;
        File indexFile;

        public LongIndex1NReader(File indexFile) throws IOException {
            this.indexFile = indexFile;
            open();
            long indexLength = indexFile.length();
            this.in.seek(indexLength - 8);
            long divider = this.in.readLong();
            this.header = new IntIndexReader(this.in, divider, (indexLength - divider) - 8);
            this.body = new LongIndexReader(this.in, 0, divider);
        }

        public long[] get(int index) {
            int p = this.header.get(index);
            if (p == 0) {
                return new long[0];
            }
            return this.body.getNext(p, (int) this.body.get(p - 1));
        }

        protected synchronized void open() {
            try {
                if (this.in == null) {
                    this.in = new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(this.indexFile, "r"));
                    if (this.header != null) {
                        this.header.in = this.in;
                    }
                    if (this.body != null) {
                        this.body.in = this.in;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void close() {
            IntIndexReader intIndexReader;
            unload();
            if (this.in != null) {
                try {
                    this.in.close();
                    intIndexReader = this.header;
                    this.body.in = null;
                    intIndexReader.in = null;
                    this.in = null;
                } catch (IOException e) {
                    intIndexReader = this.header;
                    this.body.in = null;
                    intIndexReader.in = null;
                    this.in = null;
                } catch (Throwable th) {
                    IntIndexReader intIndexReader2 = this.header;
                    this.body.in = null;
                    intIndexReader2.in = null;
                    this.in = null;
                }
            }
        }

        public void unload() {
            this.header.unload();
            this.body.unload();
        }

        public int size() {
            return this.header.size();
        }

        public void delete() {
            close();
            if (this.indexFile != null) {
                this.indexFile.delete();
            }
        }
    }

    public static class LongIndexReader extends LongIndex implements IOne2LongIndex {
        Object LOCK = this.header.LOCK;
        SimpleBufferedRandomAccessInputStream in;
        File indexFile;
        long[] pageStart;

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

        public LongIndexReader(File indexFile, HashMapIntObject<Object> pages, int size, int pageSize, long[] pageStart) throws IOException {
            this.LOCK = new Object();
            this.size = size;
            this.pageSize = pageSize;
            this.pages = pages;
            this.indexFile = indexFile;
            this.pageStart = pageStart;
            open();
        }

        public LongIndexReader(File indexFile) throws IOException {
            this(new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(indexFile, "r")), 0, indexFile.length());
            this.indexFile = indexFile;
            open();
        }

        protected LongIndexReader(SimpleBufferedRandomAccessInputStream in, long start, long length) throws IOException {
            this.LOCK = new Object();
            this.in = in;
            this.in.seek((start + length) - 8);
            int pageSize = this.in.readInt();
            int size = this.in.readInt();
            init(size, pageSize);
            this.pageStart = new long[((size / pageSize) + (size % pageSize > 0 ? 2 : 1))];
            this.in.seek(((start + length) - 8) - ((long) (this.pageStart.length * 8)));
            this.in.readLongArray(this.pageStart);
        }

        private synchronized void open() throws IOException {
            if (this.in == null) {
                if (this.indexFile == null) {
                    throw new IOException(Messages.IndexReader_Error_IndexIsEmbedded.pattern);
                }
                this.in = new SimpleBufferedRandomAccessInputStream(new RandomAccessFile(this.indexFile, "r"));
            }
        }

        public synchronized void close() {
            unload();
            if (this.in != null) {
                try {
                    this.in.close();
                    this.in = null;
                } catch (IOException e) {
                    this.in = null;
                } catch (Throwable th) {
                    this.in = null;
                }
            }
        }

        protected ArrayLongCompressed getPage(int page) {
            IOException e;
            Throwable th;
            SoftReference<ArrayLongCompressed> ref = (SoftReference) this.pages.get(page);
            ArrayLongCompressed arrayLongCompressed = ref == null ? null : (ArrayLongCompressed) ref.get();
            if (arrayLongCompressed == null) {
                synchronized (this.LOCK) {
                    try {
                        ref = (SoftReference) this.pages.get(page);
                        if (ref == null) {
                            arrayLongCompressed = null;
                        } else {
                            arrayLongCompressed = (ArrayLongCompressed) ref.get();
                        }
                        if (arrayLongCompressed == null) {
                            try {
                                this.in.seek(this.pageStart[page]);
                                byte[] buffer = new byte[((int) (this.pageStart[page + 1] - this.pageStart[page]))];
                                if (this.in.read(buffer) != buffer.length) {
                                    throw new IOException();
                                }
                                ArrayLongCompressed array = new ArrayLongCompressed(buffer);
                                try {
                                    synchronized (this.pages) {
                                        this.pages.put(page, new SoftReference(array));
                                    }
                                    arrayLongCompressed = array;
                                } catch (IOException e2) {
                                    e = e2;
                                } catch (Throwable th2) {
                                    th = th2;
                                    arrayLongCompressed = array;
                                    throw th;
                                }
                            } catch (IOException e3) {
                                e = e3;
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            }
            return arrayLongCompressed;
        }

        public void delete() {
            close();
            if (this.indexFile != null) {
                this.indexFile.delete();
            }
        }
    }
}
