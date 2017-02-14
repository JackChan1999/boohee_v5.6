package com.squareup.leakcanary;

import com.squareup.leakcanary.ExcludedRefs.Builder;
import com.squareup.leakcanary.HeapDump.Listener;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class RefWatcher {
    public static final RefWatcher DISABLED = new RefWatcher(new Executor() {
        public void execute(Runnable command) {
        }
    }, new DebuggerControl() {
        public boolean isDebuggerAttached() {
            return true;
        }
    }, GcTrigger.DEFAULT, new HeapDumper() {
        public File dumpHeap() {
            return null;
        }
    }, new Listener() {
        public void analyze(HeapDump heapDump) {
        }
    }, new Builder().build());
    private final DebuggerControl debuggerControl;
    private final ExcludedRefs    excludedRefs;
    private final GcTrigger       gcTrigger;
    private final HeapDumper      heapDumper;
    private final Listener        heapdumpListener;
    private final ReferenceQueue<Object> queue        = new ReferenceQueue();
    private final Set<String>            retainedKeys = new CopyOnWriteArraySet();
    private final Executor watchExecutor;

    public RefWatcher(Executor watchExecutor, DebuggerControl debuggerControl, GcTrigger
            gcTrigger, HeapDumper heapDumper, Listener heapdumpListener, ExcludedRefs
            excludedRefs) {
        this.watchExecutor = (Executor) Preconditions.checkNotNull(watchExecutor, "watchExecutor");
        this.debuggerControl = (DebuggerControl) Preconditions.checkNotNull(debuggerControl,
                "debuggerControl");
        this.gcTrigger = (GcTrigger) Preconditions.checkNotNull(gcTrigger, "gcTrigger");
        this.heapDumper = (HeapDumper) Preconditions.checkNotNull(heapDumper, "heapDumper");
        this.heapdumpListener = (Listener) Preconditions.checkNotNull(heapdumpListener,
                "heapdumpListener");
        this.excludedRefs = (ExcludedRefs) Preconditions.checkNotNull(excludedRefs, "excludedRefs");
    }

    public void watch(Object watchedReference) {
        watch(watchedReference, "");
    }

    public void watch(Object watchedReference, String referenceName) {
        Preconditions.checkNotNull(watchedReference, "watchedReference");
        Preconditions.checkNotNull(referenceName, "referenceName");
        if (!this.debuggerControl.isDebuggerAttached()) {
            final long watchStartNanoTime = System.nanoTime();
            String key = UUID.randomUUID().toString();
            this.retainedKeys.add(key);
            final KeyedWeakReference reference = new KeyedWeakReference(watchedReference, key,
                    referenceName, this.queue);
            this.watchExecutor.execute(new Runnable() {
                public void run() {
                    RefWatcher.this.ensureGone(reference, watchStartNanoTime);
                }
            });
        }
    }

    void ensureGone(KeyedWeakReference reference, long watchStartNanoTime) {
        long gcStartNanoTime = System.nanoTime();
        long watchDurationMs = TimeUnit.NANOSECONDS.toMillis(gcStartNanoTime - watchStartNanoTime);
        removeWeaklyReachableReferences();
        if (!gone(reference) && !this.debuggerControl.isDebuggerAttached()) {
            this.gcTrigger.runGc();
            removeWeaklyReachableReferences();
            if (!gone(reference)) {
                long startDumpHeap = System.nanoTime();
                long gcDurationMs = TimeUnit.NANOSECONDS.toMillis(startDumpHeap - gcStartNanoTime);
                File heapDumpFile = this.heapDumper.dumpHeap();
                if (heapDumpFile != HeapDumper.NO_DUMP) {
                    this.heapdumpListener.analyze(new HeapDump(heapDumpFile, reference.key,
                            reference.name, this.excludedRefs, watchDurationMs, gcDurationMs,
                            TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startDumpHeap)));
                }
            }
        }
    }

    private boolean gone(KeyedWeakReference reference) {
        return !this.retainedKeys.contains(reference.key);
    }

    private void removeWeaklyReachableReferences() {
        while (true) {
            KeyedWeakReference ref = (KeyedWeakReference) this.queue.poll();
            if (ref != null) {
                this.retainedKeys.remove(ref.key);
            } else {
                return;
            }
        }
    }
}
