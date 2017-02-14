package com.squareup.leakcanary;

import java.io.File;
import java.io.Serializable;

public final class HeapDump implements Serializable {
    public final ExcludedRefs excludedRefs;
    public final long         gcDurationMs;
    public final long         heapDumpDurationMs;
    public final File         heapDumpFile;
    public final String       referenceKey;
    public final String       referenceName;
    public final long         watchDurationMs;

    public interface Listener {
        void analyze(HeapDump heapDump);
    }

    public HeapDump(File heapDumpFile, String referenceKey, String referenceName, ExcludedRefs
            excludedRefs, long watchDurationMs, long gcDurationMs, long heapDumpDurationMs) {
        this.heapDumpFile = (File) Preconditions.checkNotNull(heapDumpFile, "heapDumpFile");
        this.referenceKey = (String) Preconditions.checkNotNull(referenceKey, "referenceKey");
        this.referenceName = (String) Preconditions.checkNotNull(referenceName, "referenceName");
        this.excludedRefs = (ExcludedRefs) Preconditions.checkNotNull(excludedRefs, "excludedRefs");
        this.watchDurationMs = watchDurationMs;
        this.gcDurationMs = gcDurationMs;
        this.heapDumpDurationMs = heapDumpDurationMs;
    }

    public HeapDump renameFile(File newFile) {
        this.heapDumpFile.renameTo(newFile);
        return new HeapDump(newFile, this.referenceKey, this.referenceName, this.excludedRefs,
                this.watchDurationMs, this.gcDurationMs, this.heapDumpDurationMs);
    }
}
