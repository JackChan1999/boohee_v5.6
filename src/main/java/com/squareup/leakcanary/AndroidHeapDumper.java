package com.squareup.leakcanary;

import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.squareup.leakcanary.internal.FutureResult;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class AndroidHeapDumper implements HeapDumper {
    private static final String TAG = "AndroidHeapDumper";
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AndroidHeapDumper(Context context) {
        this.context = context.getApplicationContext();
    }

    public File dumpHeap() {
        if (!LeakCanaryInternals.isExternalStorageWritable()) {
            Log.d(TAG, "Could not dump heap, external storage not mounted.");
        }
        File heapDumpFile = getHeapDumpFile();
        if (heapDumpFile.exists()) {
            Log.d(TAG, "Could not dump heap, previous analysis still is in progress.");
            return NO_DUMP;
        }
        FutureResult<Toast> waitingForToast = new FutureResult();
        showToast(waitingForToast);
        if (waitingForToast.wait(5, TimeUnit.SECONDS)) {
            Toast toast = (Toast) waitingForToast.get();
            try {
                Debug.dumpHprofData(heapDumpFile.getAbsolutePath());
                cancelToast(toast);
                return heapDumpFile;
            } catch (IOException e) {
                cleanup();
                Log.e(TAG, "Could not perform heap dump", e);
                return NO_DUMP;
            }
        }
        Log.d(TAG, "Did not dump heap, too much time waiting for Toast.");
        return NO_DUMP;
    }

    public void cleanup() {
        LeakCanaryInternals.executeOnFileIoThread(new Runnable() {
            public void run() {
                if (LeakCanaryInternals.isExternalStorageWritable()) {
                    Log.d(AndroidHeapDumper.TAG, "Could not attempt cleanup, external storage not" +
                            " mounted.");
                }
                File heapDumpFile = AndroidHeapDumper.this.getHeapDumpFile();
                if (heapDumpFile.exists()) {
                    Log.d(AndroidHeapDumper.TAG, "Previous analysis did not complete correctly, " +
                            "cleaning: " + heapDumpFile);
                    heapDumpFile.delete();
                }
            }
        });
    }

    private File getHeapDumpFile() {
        return new File(LeakCanaryInternals.storageDirectory(), "suspected_leak_heapdump.hprof");
    }

    private void showToast(final FutureResult<Toast> waitingForToast) {
        this.mainHandler.post(new Runnable() {
            public void run() {
                final Toast toast = new Toast(AndroidHeapDumper.this.context);
                toast.setGravity(16, 0, 0);
                toast.setDuration(1);
                toast.setView(LayoutInflater.from(AndroidHeapDumper.this.context).inflate(R
                        .layout.__leak_canary_heap_dump_toast, null));
                toast.show();
                Looper.myQueue().addIdleHandler(new IdleHandler() {
                    public boolean queueIdle() {
                        waitingForToast.set(toast);
                        return false;
                    }
                });
            }
        });
    }

    private void cancelToast(final Toast toast) {
        this.mainHandler.post(new Runnable() {
            public void run() {
                toast.cancel();
            }
        });
    }
}
