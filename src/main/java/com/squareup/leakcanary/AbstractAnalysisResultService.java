package com.squareup.leakcanary;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public abstract class AbstractAnalysisResultService extends IntentService {
    private static final String HEAP_DUMP_EXTRA = "heap_dump_extra";
    private static final String RESULT_EXTRA    = "result_extra";

    protected abstract void onHeapAnalyzed(HeapDump heapDump, AnalysisResult analysisResult);

    public static void sendResultToListener(Context context, String listenerServiceClassName,
                                            HeapDump heapDump, AnalysisResult result) {
        try {
            Intent intent = new Intent(context, Class.forName(listenerServiceClassName));
            intent.putExtra(HEAP_DUMP_EXTRA, heapDump);
            intent.putExtra(RESULT_EXTRA, result);
            context.startService(intent);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractAnalysisResultService() {
        super(AbstractAnalysisResultService.class.getName());
    }

    protected final void onHandleIntent(Intent intent) {
        HeapDump heapDump = (HeapDump) intent.getSerializableExtra(HEAP_DUMP_EXTRA);
        try {
            onHeapAnalyzed(heapDump, (AnalysisResult) intent.getSerializableExtra(RESULT_EXTRA));
        } finally {
            heapDump.heapDumpFile.delete();
        }
    }
}
