package com.boohee.one.tinker;

import com.boohee.one.tinker.crash.SampleUncaughtExceptionHandler;
import com.boohee.one.tinker.reporter.SampleLoadReporter;
import com.boohee.one.tinker.reporter.SamplePatchListener;
import com.boohee.one.tinker.reporter.SamplePatchReporter;
import com.boohee.one.tinker.service.OneTinkerResultService;
import com.tencent.tinker.lib.patch.RepairPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.app.ApplicationLike;

public class TinkerManager {
    private static final String TAG = "Tinker.TinkerManager";
    private static ApplicationLike applicationLike;
    private static boolean isInstalled = false;
    private static SampleUncaughtExceptionHandler uncaughtExceptionHandler;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static void initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = new SampleUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }

    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore", new Object[0]);
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;
    }

    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore", new Object[0]);
            return;
        }
        ApplicationLike applicationLike = appLike;
        TinkerInstaller.install(applicationLike, new SampleLoadReporter(appLike.getApplication())
                , new SamplePatchReporter(appLike.getApplication()), new SamplePatchListener
                        (appLike.getApplication()), OneTinkerResultService.class, new
                        UpgradePatch(), new RepairPatch());
        isInstalled = true;
    }
}
