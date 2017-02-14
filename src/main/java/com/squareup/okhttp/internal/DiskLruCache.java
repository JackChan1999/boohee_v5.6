package com.squareup.okhttp.internal;

import com.alipay.sdk.sys.a;
import com.squareup.okhttp.internal.io.FileSystem;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import lecho.lib.hellocharts.model.ColumnChartData;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class DiskLruCache implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = (!DiskLruCache.class
            .desiredAssertionStatus());
    static final                 long    ANY_SEQUENCE_NUMBER = -1;
    private static final         String  CLEAN               = "CLEAN";
    private static final         String  DIRTY               = "DIRTY";
    static final                 String  JOURNAL_FILE        = "journal";
    static final                 String  JOURNAL_FILE_BACKUP = "journal.bkp";
    static final                 String  JOURNAL_FILE_TEMP   = "journal.tmp";
    static final                 Pattern LEGAL_KEY_PATTERN   = Pattern.compile("[a-z0-9_-]{1,120}");
    static final                 String  MAGIC               = "libcore.io.DiskLruCache";
    private static final         Sink    NULL_SINK           = new
    4();
    private static final String READ      = "READ";
    private static final String REMOVE    = "REMOVE";
    static final         String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable = new
    1(this);
    private       boolean      closed;
    private final File         directory;
    private final Executor     executor;
    private final FileSystem   fileSystem;
    private       boolean      hasJournalErrors;
    private       boolean      initialized;
    private final File         journalFile;
    private final File         journalFileBackup;
    private final File         journalFileTmp;
    private       BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, ColumnChartData
            .DEFAULT_FILL_RATIO, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    private int redundantOpCount;
    private long size = 0;
    private final int valueCount;

    public final class Editor {
        private       boolean   committed;
        private final Entry     entry;
        private       boolean   hasErrors;
        private final boolean[] written;

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = Entry.access$800(entry) ? null : new boolean[DiskLruCache.this
                    .valueCount];
        }

        public Source newSource(int index) throws IOException {
            Source source = null;
            synchronized (DiskLruCache.this) {
                if (Entry.access$900(this.entry) != this) {
                    throw new IllegalStateException();
                } else if (Entry.access$800(this.entry)) {
                    try {
                        source = DiskLruCache.this.fileSystem.source(Entry.access$1300(this
                                .entry)[index]);
                    } catch (FileNotFoundException e) {
                    }
                }
            }
            return source;
        }

        public Sink newSink(int index) throws IOException {
            Sink 1;
            synchronized (DiskLruCache.this) {
                if (Entry.access$900(this.entry) != this) {
                    throw new IllegalStateException();
                }
                if (!Entry.access$800(this.entry)) {
                    this.written[index] = true;
                }
                try {
                    1 = new 1
                    (this, DiskLruCache.this.fileSystem.sink(Entry.access$1400(this.entry)[index]));
                } catch (FileNotFoundException e) {
                    1 = DiskLruCache.NULL_SINK;
                }
            }
            return 1;
        }

        public void commit() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.hasErrors) {
                    DiskLruCache.this.completeEdit(this, false);
                    DiskLruCache.this.removeEntry(this.entry);
                } else {
                    DiskLruCache.this.completeEdit(this, true);
                }
                this.committed = true;
            }
        }

        public void abort() throws IOException {
            synchronized (DiskLruCache.this) {
                DiskLruCache.this.completeEdit(this, false);
            }
        }

        public void abortUnlessCommitted() {
            synchronized (DiskLruCache.this) {
                if (!this.committed) {
                    try {
                        DiskLruCache.this.completeEdit(this, false);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public final class Snapshot implements Closeable {
        private final String   key;
        private final long[]   lengths;
        private final long     sequenceNumber;
        private final Source[] sources;

        private Snapshot(String key, long sequenceNumber, Source[] sources, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.sources = sources;
            this.lengths = lengths;
        }

        public String key() {
            return this.key;
        }

        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public Source getSource(int index) {
            return this.sources[index];
        }

        public long getLength(int index) {
            return this.lengths[index];
        }

        public void close() {
            for (Closeable in : this.sources) {
                Util.closeQuietly(in);
            }
        }
    }

    DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long
            maxSize, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
        this.executor = executor;
    }

    public synchronized void initialize() throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                } catch (IOException journalIsCorrupt) {
                    Platform.get().logW("DiskLruCache " + this.directory + " is corrupt: " +
                            journalIsCorrupt.getMessage() + ", removing");
                    delete();
                    this.closed = false;
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion, int
            valueCount, long maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, new
                    ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(),
                    Util.threadFactory("OkHttp DiskLruCache", true)));
        }
    }

    private void readJournal() throws IOException {
        Closeable source = Okio.buffer(this.fileSystem.source(this.journalFile));
        int lineCount;
        try {
            String magic = source.readUtf8LineStrict();
            String version = source.readUtf8LineStrict();
            String appVersionString = source.readUtf8LineStrict();
            String valueCountString = source.readUtf8LineStrict();
            String blank = source.readUtf8LineStrict();
            if (MAGIC.equals(magic) && "1".equals(version) && Integer.toString(this.appVersion)
                    .equals(appVersionString) && Integer.toString(this.valueCount).equals
                    (valueCountString) && "".equals(blank)) {
                lineCount = 0;
                while (true) {
                    readJournalLine(source.readUtf8LineStrict());
                    lineCount++;
                }
            } else {
                throw new IOException("unexpected journal header: [" + magic + ", " + version +
                        ", " + valueCountString + ", " + blank + "]");
            }
        } catch (EOFException e) {
            this.redundantOpCount = lineCount - this.lruEntries.size();
            if (source.exhausted()) {
                this.journalWriter = newJournalWriter();
            } else {
                rebuildJournal();
            }
            Util.closeQuietly(source);
        } catch (Throwable th) {
            Util.closeQuietly(source);
        }
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new 2 (this, this.fileSystem.appendingSink(this.journalFile)));
    }

    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(32);
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        String key;
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(32, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        }
        key = line.substring(keyBegin, secondSpace);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(this, key, null);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            Entry.access$802(entry, true);
            Entry.access$902(entry, null);
            Entry.access$1000(entry, parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            Entry.access$902(entry, new Editor(entry));
        } else if (secondSpace != -1 || firstSpace != READ.length() || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            int t;
            if (Entry.access$900(entry) == null) {
                for (t = 0; t < this.valueCount; t++) {
                    this.size += Entry.access$1200(entry)[t];
                }
            } else {
                Entry.access$902(entry, null);
                for (t = 0; t < this.valueCount; t++) {
                    this.fileSystem.delete(Entry.access$1300(entry)[t]);
                    this.fileSystem.delete(Entry.access$1400(entry)[t]);
                }
                i.remove();
            }
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedSink writer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            writer.writeUtf8(MAGIC).writeByte(10);
            writer.writeUtf8("1").writeByte(10);
            writer.writeDecimalLong((long) this.appVersion).writeByte(10);
            writer.writeDecimalLong((long) this.valueCount).writeByte(10);
            writer.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (Entry.access$900(entry) != null) {
                    writer.writeUtf8(DIRTY).writeByte(32);
                    writer.writeUtf8(Entry.access$1500(entry));
                    writer.writeByte(10);
                } else {
                    writer.writeUtf8(CLEAN).writeByte(32);
                    writer.writeUtf8(Entry.access$1500(entry));
                    entry.writeLengths(writer);
                    writer.writeByte(10);
                }
            }
            writer.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = false;
        } catch (Throwable th) {
            writer.close();
        }
    }

    public synchronized Snapshot get(String key) throws IOException {
        Snapshot snapshot;
        initialize();
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null || !Entry.access$800(entry)) {
            snapshot = null;
        } else {
            snapshot = entry.snapshot();
            if (snapshot == null) {
                snapshot = null;
            } else {
                this.redundantOpCount++;
                this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(key).writeByte(10);
                if (journalRebuildRequired()) {
                    this.executor.execute(this.cleanupRunnable);
                }
            }
        }
        return snapshot;
    }

    public Editor edit(String key) throws IOException {
        return edit(key, -1);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized com.squareup.okhttp.internal.DiskLruCache.Editor edit(java.lang.String
                                                                                       r5, long
            r6) throws java.io.IOException {
        /*
        r4 = this;
        r0 = 0;
        monitor-enter(r4);
        r4.initialize();	 Catch:{ all -> 0x0065 }
        r4.checkNotClosed();	 Catch:{ all -> 0x0065 }
        r4.validateKey(r5);	 Catch:{ all -> 0x0065 }
        r2 = r4.lruEntries;	 Catch:{ all -> 0x0065 }
        r1 = r2.get(r5);	 Catch:{ all -> 0x0065 }
        r1 = (com.squareup.okhttp.internal.DiskLruCache.Entry) r1;	 Catch:{ all -> 0x0065 }
        r2 = -1;
        r2 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0025;
    L_0x0019:
        if (r1 == 0) goto L_0x0023;
    L_0x001b:
        r2 = com.squareup.okhttp.internal.DiskLruCache.Entry.access$1600(r1);	 Catch:{ all ->
        0x0065 }
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 == 0) goto L_0x0025;
    L_0x0023:
        monitor-exit(r4);
        return r0;
    L_0x0025:
        if (r1 == 0) goto L_0x002d;
    L_0x0027:
        r2 = com.squareup.okhttp.internal.DiskLruCache.Entry.access$900(r1);	 Catch:{ all ->
        0x0065 }
        if (r2 != 0) goto L_0x0023;
    L_0x002d:
        r2 = r4.journalWriter;	 Catch:{ all -> 0x0065 }
        r3 = "DIRTY";
        r2 = r2.writeUtf8(r3);	 Catch:{ all -> 0x0065 }
        r3 = 32;
        r2 = r2.writeByte(r3);	 Catch:{ all -> 0x0065 }
        r2 = r2.writeUtf8(r5);	 Catch:{ all -> 0x0065 }
        r3 = 10;
        r2.writeByte(r3);	 Catch:{ all -> 0x0065 }
        r2 = r4.journalWriter;	 Catch:{ all -> 0x0065 }
        r2.flush();	 Catch:{ all -> 0x0065 }
        r2 = r4.hasJournalErrors;	 Catch:{ all -> 0x0065 }
        if (r2 != 0) goto L_0x0023;
    L_0x004e:
        if (r1 != 0) goto L_0x005b;
    L_0x0050:
        r1 = new com.squareup.okhttp.internal.DiskLruCache$Entry;	 Catch:{ all -> 0x0065 }
        r2 = 0;
        r1.<init>(r4, r5, r2);	 Catch:{ all -> 0x0065 }
        r2 = r4.lruEntries;	 Catch:{ all -> 0x0065 }
        r2.put(r5, r1);	 Catch:{ all -> 0x0065 }
    L_0x005b:
        r0 = new com.squareup.okhttp.internal.DiskLruCache$Editor;	 Catch:{ all -> 0x0065 }
        r2 = 0;
        r0.<init>(r1);	 Catch:{ all -> 0x0065 }
        com.squareup.okhttp.internal.DiskLruCache.Entry.access$902(r1, r0);	 Catch:{ all ->
        0x0065 }
        goto L_0x0023;
    L_0x0065:
        r2 = move-exception;
        monitor-exit(r4);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp" +
                ".internal.DiskLruCache.edit(java.lang.String, long):com.squareup.okhttp.internal" +
                ".DiskLruCache$Editor");
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (Entry.access$900(entry) != editor) {
            throw new IllegalStateException();
        }
        int i;
        if (success) {
            if (!Entry.access$800(entry)) {
                i = 0;
                while (i < this.valueCount) {
                    if (!editor.written[i]) {
                        editor.abort();
                        throw new IllegalStateException("Newly created entry didn't create value " +
                                "for index " + i);
                    } else if (!this.fileSystem.exists(Entry.access$1400(entry)[i])) {
                        editor.abort();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        for (i = 0; i < this.valueCount; i++) {
            File dirty = Entry.access$1400(entry)[i];
            if (!success) {
                this.fileSystem.delete(dirty);
            } else if (this.fileSystem.exists(dirty)) {
                File clean = Entry.access$1300(entry)[i];
                this.fileSystem.rename(dirty, clean);
                long oldLength = Entry.access$1200(entry)[i];
                long newLength = this.fileSystem.size(clean);
                Entry.access$1200(entry)[i] = newLength;
                this.size = (this.size - oldLength) + newLength;
            }
        }
        this.redundantOpCount++;
        Entry.access$902(entry, null);
        if ((Entry.access$800(entry) | success) != 0) {
            Entry.access$802(entry, true);
            this.journalWriter.writeUtf8(CLEAN).writeByte(32);
            this.journalWriter.writeUtf8(Entry.access$1500(entry));
            entry.writeLengths(this.journalWriter);
            this.journalWriter.writeByte(10);
            if (success) {
                long j = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j;
                Entry.access$1602(entry, j);
            }
        } else {
            this.lruEntries.remove(Entry.access$1500(entry));
            this.journalWriter.writeUtf8(REMOVE).writeByte(32);
            this.journalWriter.writeUtf8(Entry.access$1500(entry));
            this.journalWriter.writeByte(10);
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean z;
        initialize();
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            z = false;
        } else {
            z = removeEntry(entry);
        }
        return z;
    }

    private boolean removeEntry(Entry entry) throws IOException {
        if (Entry.access$900(entry) != null) {
            Entry.access$900(entry).hasErrors = true;
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(Entry.access$1300(entry)[i]);
            this.size -= Entry.access$1200(entry)[i];
            Entry.access$1200(entry)[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(Entry.access$1500(entry))
                .writeByte(10);
        this.lruEntries.remove(Entry.access$1500(entry));
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public synchronized void close() throws IOException {
        if (!this.initialized || this.closed) {
            this.closed = true;
        } else {
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this
                    .lruEntries.size()])) {
                if (Entry.access$900(entry) != null) {
                    Entry.access$900(entry).abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
            this.closed = true;
        }
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeEntry((Entry) this.lruEntries.values().iterator().next());
        }
    }

    public void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public synchronized void evictAll() throws IOException {
        initialize();
        for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries
                .size()])) {
            removeEntry(entry);
        }
    }

    private void validateKey(String key) {
        if (!LEGAL_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + key + a.e);
        }
    }

    public synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new 3 (this);
    }
}
