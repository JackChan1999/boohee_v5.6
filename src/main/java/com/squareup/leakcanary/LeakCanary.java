package com.squareup.leakcanary;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import com.squareup.leakcanary.HeapDump.Listener;
import com.squareup.leakcanary.internal.DisplayLeakActivity;
import com.squareup.leakcanary.internal.HeapAnalyzerService;
import com.squareup.leakcanary.internal.LeakCanaryInternals;
import com.umeng.socialize.common.SocializeConstants;

public final class LeakCanary {
    public static RefWatcher install(Application application) {
        return install(application, DisplayLeakService.class, AndroidExcludedRefs
                .createAppDefaults().build());
    }

    public static RefWatcher install(Application application, Class<? extends
            AbstractAnalysisResultService> listenerServiceClass, ExcludedRefs excludedRefs) {
        if (isInAnalyzerProcess(application)) {
            return RefWatcher.DISABLED;
        }
        enableDisplayLeakActivity(application);
        RefWatcher refWatcher = androidWatcher(application, new ServiceHeapDumpListener
                (application, listenerServiceClass), excludedRefs);
        ActivityRefWatcher.installOnIcsPlus(application, refWatcher);
        return refWatcher;
    }

    public static RefWatcher androidWatcher(Context context, Listener heapDumpListener,
                                            ExcludedRefs excludedRefs) {
        DebuggerControl debuggerControl = new AndroidDebuggerControl();
        AndroidHeapDumper heapDumper = new AndroidHeapDumper(context);
        heapDumper.cleanup();
        return new RefWatcher(new AndroidWatchExecutor(), debuggerControl, GcTrigger.DEFAULT,
                heapDumper, heapDumpListener, excludedRefs);
    }

    public static void enableDisplayLeakActivity(Context context) {
        LeakCanaryInternals.setEnabled(context, DisplayLeakActivity.class, true);
    }

    public static String leakInfo(Context context, HeapDump heapDump, AnalysisResult result,
                                  boolean detailed) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String info = "In " + packageName + ":" + packageInfo.versionName + ":" + packageInfo
                    .versionCode + ".\n";
            String detailedString = "";
            if (result.leakFound) {
                if (result.excludedLeak) {
                    info = info + "* LEAK CAN BE IGNORED.\n";
                }
                info = info + "* " + result.className;
                if (!heapDump.referenceName.equals("")) {
                    info = info + " (" + heapDump.referenceName + SocializeConstants.OP_CLOSE_PAREN;
                }
                info = info + " has leaked:\n" + result.leakTrace.toString() + "\n";
                if (detailed) {
                    detailedString = "\n* Details:\n" + result.leakTrace.toDetailedString();
                }
            } else {
                info = result.failure != null ? info + "* FAILURE:\n" + Log.getStackTraceString
                        (result.failure) + "\n" : info + "* NO LEAK FOUND.\n\n";
            }
            return info + "* Reference Key: " + heapDump.referenceKey + "\n" + "* Device: " +
                    Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL + " " + Build
                    .PRODUCT + "\n" + "* Android Version: " + VERSION.RELEASE + " API: " +
                    VERSION.SDK_INT + " LeakCanary: " + BuildConfig.LIBRARY_VERSION + "\n" + "* " +
                    "Durations: watch=" + heapDump.watchDurationMs + "ms, gc=" + heapDump
                    .gcDurationMs + "ms, heap dump=" + heapDump.heapDumpDurationMs + "ms, " +
                    "analysis=" + result.analysisDurationMs + "ms" + "\n" + detailedString;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInAnalyzerProcess(Context context) {
        return LeakCanaryInternals.isInServiceProcess(context, HeapAnalyzerService.class);
    }

    private LeakCanary() {
        throw new AssertionError();
    }
}
