package com.squareup.leakcanary.internal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.HeapAnalyzer;
import com.squareup.leakcanary.HeapDump;

public final class HeapAnalyzerService extends IntentService {
    private static final String HEAPDUMP_EXTRA       = "heapdump_extra";
    private static final String LISTENER_CLASS_EXTRA = "listener_class_extra";

    public static void runAnalysis(Context context, HeapDump heapDump, Class<? extends
            AbstractAnalysisResultService> listenerServiceClass) {
        Intent intent = new Intent(context, HeapAnalyzerService.class);
        intent.putExtra(LISTENER_CLASS_EXTRA, listenerServiceClass.getName());
        intent.putExtra(HEAPDUMP_EXTRA, heapDump);
        context.startService(intent);
    }

    public HeapAnalyzerService() {
        super(HeapAnalyzerService.class.getSimpleName());
    }

    protected void onHandleIntent(Intent intent) {
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAPDUMP_EXTRA);
        AbstractAnalysisResultService.sendResultToListener(this, intent.getStringExtra
                (LISTENER_CLASS_EXTRA), heapDump, new HeapAnalyzer(AndroidExcludedRefs
                .createAndroidDefaults().build(), heapDump.excludedRefs).checkForLeak(heapDump
                .heapDumpFile, heapDump.referenceKey));
    }
}
