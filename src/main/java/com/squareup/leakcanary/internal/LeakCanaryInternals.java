package com.squareup.leakcanary.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.boohee.model.ModelName;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class LeakCanaryInternals {
    public static final  String   LG             = "LGE";
    public static final  int      LOLLIPOP_MR1   = 22;
    public static final  String   MOTOROLA       = "motorola";
    public static final  String   NVIDIA         = "NVIDIA";
    public static final  String   SAMSUNG        = "samsung";
    private static final Executor fileIoExecutor = Executors.newSingleThreadExecutor();

    public static void executeOnFileIoThread(Runnable runnable) {
        fileIoExecutor.execute(runnable);
    }

    public static File storageDirectory() {
        File leakCanaryDirectory = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), "leakcanary");
        leakCanaryDirectory.mkdirs();
        return leakCanaryDirectory;
    }

    public static File detectedLeakDirectory() {
        File directory = new File(storageDirectory(), "detected_leaks");
        directory.mkdirs();
        return directory;
    }

    public static File leakResultFile(File heapdumpFile) {
        return new File(heapdumpFile.getParentFile(), heapdumpFile.getName() + ".result");
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static File findNextAvailableHprofFile(int maxFiles) {
        File directory = detectedLeakDirectory();
        for (int i = 0; i < maxFiles; i++) {
            File file = new File(directory, "heap_dump_" + i + ".hprof");
            if (!file.exists()) {
                return file;
            }
        }
        return null;
    }

    public static String classSimpleName(String className) {
        int separator = className.lastIndexOf(46);
        return separator == -1 ? className : className.substring(separator + 1);
    }

    public static void setEnabled(Context context, final Class<?> componentClass, final boolean
            enabled) {
        final Context appContext = context.getApplicationContext();
        executeOnFileIoThread(new Runnable() {
            public void run() {
                appContext.getPackageManager().setComponentEnabledSetting(new ComponentName
                        (appContext, componentClass), enabled ? 1 : 2, 1);
            }
        });
    }

    public static boolean isInServiceProcess(Context context, Class<? extends Service>
            serviceClass) {
        PackageManager packageManager = context.getPackageManager();
        try {
            String mainProcess = packageManager.getPackageInfo(context.getPackageName(), 4)
                    .applicationInfo.processName;
            try {
                ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName
                        (context, serviceClass), 0);
                if (serviceInfo.processName.equals(mainProcess)) {
                    Log.e("AndroidUtils", "Did not expect service " + serviceClass + " to run in " +
                            "main process " + mainProcess);
                    return false;
                }
                int myPid = Process.myPid();
                RunningAppProcessInfo myProcess = null;
                for (RunningAppProcessInfo process : ((ActivityManager) context.getSystemService
                        (ModelName.ACTIVITY)).getRunningAppProcesses()) {
                    if (process.pid == myPid) {
                        myProcess = process;
                        break;
                    }
                }
                if (myProcess != null) {
                    return myProcess.processName.equals(serviceInfo.processName);
                }
                Log.e("AndroidUtils", "Could not find running process for " + myPid);
                return false;
            } catch (NameNotFoundException e) {
                return false;
            }
        } catch (Exception e2) {
            Log.e("AndroidUtils", "Could not get package info for " + context.getPackageName(), e2);
            return false;
        }
    }

    private LeakCanaryInternals() {
        throw new AssertionError();
    }
}
