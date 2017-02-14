package com.tencent.tinker.lib.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.SystemClock;

import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;

import java.io.File;

public class TinkerPatchService extends IntentService {
    private static final String        PATCH_NEW_EXTRA       = "patch_new_extra";
    private static final String        PATCH_PATH_EXTRA      = "patch_path_extra";
    private static final String        TAG                   = "Tinker.TinkerPatchService";
    private static       int           notificationId        = ShareConstants
            .TINKER_PATCH_SERVICE_NOTIFICATION;
    private static       AbstractPatch repairPatchProcessor  = null;
    private static       AbstractPatch upgradePatchProcessor = null;

    public static class InnerService extends Service {
        public void onCreate() {
            super.onCreate();
            try {
                startForeground(TinkerPatchService.notificationId, new Notification());
            } catch (NullPointerException e) {
                TinkerLog.e(TinkerPatchService.TAG, "InnerService set service for push " +
                        "exception:%s.", e);
            }
            stopSelf();
        }

        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }

        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public TinkerPatchService() {
        super(TinkerPatchService.class.getSimpleName());
    }

    public static void runPatchService(Context context, String path, boolean isUpgradePatch) {
        Intent intent = new Intent(context, TinkerPatchService.class);
        intent.putExtra(PATCH_PATH_EXTRA, path);
        intent.putExtra(PATCH_NEW_EXTRA, isUpgradePatch);
        context.startService(intent);
    }

    public static void setPatchProcessor(AbstractPatch upgradePatch, AbstractPatch repairPatch) {
        upgradePatchProcessor = upgradePatch;
        repairPatchProcessor = repairPatch;
    }

    public static String getPatchPathExtra(Intent intent) {
        if (intent != null) {
            return ShareIntentUtil.getStringExtra(intent, PATCH_PATH_EXTRA);
        }
        throw new TinkerRuntimeException("getPatchPathExtra, but intent is null");
    }

    public static boolean getPatchUpgradeExtra(Intent intent) {
        if (intent != null) {
            return ShareIntentUtil.getBooleanExtra(intent, PATCH_NEW_EXTRA, false);
        }
        throw new TinkerRuntimeException("getPatchUpgradeExtra, but intent is null");
    }

    public static void setTinkerNotificationId(int id) {
        notificationId = id;
    }

    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        Tinker tinker = Tinker.with(context);
        tinker.getPatchReporter().onPatchServiceStart(intent);
        if (intent == null) {
            TinkerLog.e(TAG, "TinkerPatchService received a null intent, ignoring.", new Object[0]);
            return;
        }
        String path = getPatchPathExtra(intent);
        if (path == null) {
            TinkerLog.e(TAG, "TinkerPatchService can't get the path extra, ignoring.", new
                    Object[0]);
            return;
        }
        boolean result;
        File patchFile = new File(path);
        boolean isUpgradePatch = getPatchUpgradeExtra(intent);
        long begin = SystemClock.elapsedRealtime();
        Throwable e = null;
        increasingPriority();
        PatchResult patchResult = new PatchResult();
        if (isUpgradePatch) {
            try {
                if (upgradePatchProcessor == null) {
                    throw new TinkerRuntimeException("upgradePatchProcessor is null.");
                }
                result = upgradePatchProcessor.tryPatch(context, path, patchResult);
            } catch (Throwable throwable) {
                e = throwable;
                result = false;
                tinker.getPatchReporter().onPatchException(patchFile, e, isUpgradePatch);
            }
        } else if (repairPatchProcessor == null) {
            throw new TinkerRuntimeException("upgradePatchProcessor is null.");
        } else {
            result = repairPatchProcessor.tryPatch(context, path, patchResult);
        }
        long cost = SystemClock.elapsedRealtime() - begin;
        tinker.getPatchReporter().onPatchResult(patchFile, result, cost, isUpgradePatch);
        patchResult.isSuccess = result;
        patchResult.isUpgradePatch = isUpgradePatch;
        patchResult.rawPatchFilePath = path;
        patchResult.costTime = cost;
        patchResult.e = e;
        AbstractResultService.runResultService(context, patchResult);
    }

    private void increasingPriority() {
        TinkerLog.i(TAG, "try to increase patch process priority", new Object[0]);
        Notification notification = new Notification();
        if (VERSION.SDK_INT < 18) {
            startForeground(notificationId, notification);
            return;
        }
        startForeground(notificationId, notification);
        startService(new Intent(this, InnerService.class));
    }
}
