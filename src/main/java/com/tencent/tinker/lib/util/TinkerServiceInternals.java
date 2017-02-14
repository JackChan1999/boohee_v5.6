package com.tencent.tinker.lib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import android.util.Log;

import com.boohee.model.ModelName;
import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

public class TinkerServiceInternals extends ShareTinkerInternals {
    private static final String TAG                     = "Tinker.ServiceInternals";
    private static       String patchServiceProcessName = null;

    public static void killTinkerPatchServiceProcess(Context context) {
        String serverProcessName = getTinkerPatchServiceName(context);
        if (serverProcessName != null) {
            for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService
                    (ModelName.ACTIVITY)).getRunningAppProcesses()) {
                if (appProcess.processName.equals(serverProcessName)) {
                    Process.killProcess(appProcess.pid);
                }
            }
        }
    }

    public static boolean isTinkerPatchServiceRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ModelName.ACTIVITY);
        String serverName = getTinkerPatchServiceName(context);
        if (serverName == null) {
            return false;
        }
        try {
            for (RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
                if (appProcess.processName.equals(serverName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "isTinkerPatchServiceRunning Exception: " + e.toString());
            return false;
        } catch (Error e2) {
            Log.e(TAG, "isTinkerPatchServiceRunning Error: " + e2.toString());
            return false;
        }
    }

    public static String getTinkerPatchServiceName(Context context) {
        if (patchServiceProcessName != null) {
            return patchServiceProcessName;
        }
        String serviceName = getServiceProcessName(context, TinkerPatchService.class);
        if (serviceName == null) {
            return null;
        }
        patchServiceProcessName = serviceName;
        return patchServiceProcessName;
    }

    public static boolean isInTinkerPatchServiceProcess(Context context) {
        String process = ShareTinkerInternals.getProcessName(context);
        String service = getTinkerPatchServiceName(context);
        if (service == null || service.length() == 0) {
            return false;
        }
        return process.equals(service);
    }

    private static String getServiceProcessName(Context context, Class<? extends Service>
            serviceClass) {
        try {
            return context.getPackageManager().getServiceInfo(new ComponentName(context,
                    serviceClass), 0).processName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }
}
