package com.squareup.leakcanary;

import android.content.Context;

import com.squareup.leakcanary.HeapDump.Listener;
import com.squareup.leakcanary.internal.HeapAnalyzerService;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

public final class ServiceHeapDumpListener implements Listener {
    private final Context                                        context;
    private final Class<? extends AbstractAnalysisResultService> listenerServiceClass;

    public ServiceHeapDumpListener(Context context, Class<? extends
            AbstractAnalysisResultService> listenerServiceClass) {
        LeakCanaryInternals.setEnabled(context, listenerServiceClass, true);
        LeakCanaryInternals.setEnabled(context, HeapAnalyzerService.class, true);
        this.listenerServiceClass = (Class) Preconditions.checkNotNull(listenerServiceClass,
                "listenerServiceClass");
        this.context = ((Context) Preconditions.checkNotNull(context, "context"))
                .getApplicationContext();
    }

    public void analyze(HeapDump heapDump) {
        Preconditions.checkNotNull(heapDump, "heapDump");
        HeapAnalyzerService.runAnalysis(this.context, heapDump, this.listenerServiceClass);
    }
}
