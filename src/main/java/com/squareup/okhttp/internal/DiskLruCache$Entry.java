package com.squareup.okhttp.internal;

import com.squareup.okhttp.internal.DiskLruCache.Editor;
import com.squareup.okhttp.internal.DiskLruCache.Snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import okio.BufferedSink;
import okio.Source;

final class DiskLruCache$Entry {
    private final         File[]       cleanFiles;
    private               Editor       currentEditor;
    private final         File[]       dirtyFiles;
    private final         String       key;
    private final         long[]       lengths;
    private               boolean      readable;
    private               long         sequenceNumber;
    final /* synthetic */ DiskLruCache this$0;

    private DiskLruCache$Entry(DiskLruCache diskLruCache, String key) {
        this.this$0 = diskLruCache;
        this.key = key;
        this.lengths = new long[DiskLruCache.access$2300(diskLruCache)];
        this.cleanFiles = new File[DiskLruCache.access$2300(diskLruCache)];
        this.dirtyFiles = new File[DiskLruCache.access$2300(diskLruCache)];
        StringBuilder fileBuilder = new StringBuilder(key).append('.');
        int truncateTo = fileBuilder.length();
        for (int i = 0; i < DiskLruCache.access$2300(diskLruCache); i++) {
            fileBuilder.append(i);
            this.cleanFiles[i] = new File(DiskLruCache.access$2800(diskLruCache), fileBuilder
                    .toString());
            fileBuilder.append(".tmp");
            this.dirtyFiles[i] = new File(DiskLruCache.access$2800(diskLruCache), fileBuilder
                    .toString());
            fileBuilder.setLength(truncateTo);
        }
    }

    private void setLengths(String[] strings) throws IOException {
        if (strings.length != DiskLruCache.access$2300(this.this$0)) {
            throw invalidLengths(strings);
        }
        int i = 0;
        while (i < strings.length) {
            try {
                this.lengths[i] = Long.parseLong(strings[i]);
                i++;
            } catch (NumberFormatException e) {
                throw invalidLengths(strings);
            }
        }
    }

    void writeLengths(BufferedSink writer) throws IOException {
        for (long length : this.lengths) {
            writer.writeByte(32).writeDecimalLong(length);
        }
    }

    private IOException invalidLengths(String[] strings) throws IOException {
        throw new IOException("unexpected journal line: " + Arrays.toString(strings));
    }

    Snapshot snapshot() {
        if (Thread.holdsLock(this.this$0)) {
            Source[] sources = new Source[DiskLruCache.access$2300(this.this$0)];
            long[] lengths = (long[]) this.lengths.clone();
            int i = 0;
            while (i < DiskLruCache.access$2300(this.this$0)) {
                try {
                    sources[i] = DiskLruCache.access$2400(this.this$0).source(this.cleanFiles[i]);
                    i++;
                } catch (FileNotFoundException e) {
                    i = 0;
                    while (i < DiskLruCache.access$2300(this.this$0) && sources[i] != null) {
                        Util.closeQuietly(sources[i]);
                        i++;
                    }
                    return null;
                }
            }
            return new Snapshot(this.this$0, this.key, this.sequenceNumber, sources, lengths, null);
        }
        throw new AssertionError();
    }
}
