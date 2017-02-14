package com.boohee.one.tinker.crash;

import android.content.SharedPreferences;
import android.os.SystemClock;

import com.boohee.one.tinker.TinkerManager;
import com.boohee.one.tinker.Utils;
import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.boohee.utility.Event;
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;

public class SampleUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final String                   DALVIK_XPOSED_CRASH = "Class ref in " +
            "pre-verified class resolved to unexpected implementation";
    public static final  int                      MAX_CRASH_COUNT     = 3;
    private static final long                     QUICK_CRASH_ELAPSE  = 10000;
    private static final String                   TAG                 = "Tinker" +
            ".SampleUncaughtExHandler";
    private final        UncaughtExceptionHandler ueh                 = Thread
            .getDefaultUncaughtExceptionHandler();

    public void uncaughtException(Thread thread, Throwable ex) {
        TinkerLog.e(TAG, "uncaughtException:" + ex.getMessage(), new Object[0]);
        tinkerFastCrashProtect();
        tinkerPreVerifiedCrashHandler(ex);
        this.ueh.uncaughtException(thread, ex);
    }

    private void tinkerPreVerifiedCrashHandler(Throwable ex) {
        if (Utils.isXposedExists(ex)) {
            ApplicationLike applicationLike = TinkerManager.getTinkerApplicationLike();
            if (applicationLike != null && applicationLike.getApplication() != null &&
                    TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
                boolean isCausedByXposed = false;
                if (ShareTinkerInternals.isVmArt()) {
                    isCausedByXposed = true;
                } else if ((ex instanceof IllegalAccessError) && ex.getMessage().contains
                        (DALVIK_XPOSED_CRASH)) {
                    isCausedByXposed = true;
                }
                if (isCausedByXposed) {
                    SampleTinkerReport.onXposedCrash();
                    TinkerLog.e(TAG, "have xposed: just clean tinker", new Object[0]);
                    ShareTinkerInternals.killAllOtherProcess(applicationLike.getApplication());
                    TinkerApplicationHelper.cleanPatch(applicationLike);
                    ShareTinkerInternals.setTinkerDisableWithSharedPreferences(applicationLike
                            .getApplication());
                }
            }
        }
    }

    private boolean tinkerFastCrashProtect() {
        ApplicationLike applicationLike = TinkerManager.getTinkerApplicationLike();
        if (applicationLike == null || applicationLike.getApplication() == null ||
                !TinkerApplicationHelper.isTinkerLoadSuccess(applicationLike)) {
            return false;
        }
        long elapsedTime = SystemClock.elapsedRealtime() - applicationLike
                .getApplicationStartElapsedTime();
        TinkerLog.d(TAG, "exlapsedTime :" + elapsedTime, new Object[0]);
        if (elapsedTime >= QUICK_CRASH_ELAPSE) {
            return false;
        }
        String currentVersion = TinkerApplicationHelper.getCurrentVersion(applicationLike);
        if (ShareTinkerInternals.isNullOrNil(currentVersion)) {
            return false;
        }
        SharedPreferences sp = applicationLike.getApplication().getSharedPreferences
                (ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, 4);
        int fastCrashCount = sp.getInt(currentVersion, 0);
        if (fastCrashCount >= 3) {
            MobclickAgent.onEvent(applicationLike.getApplication(), Event.tinker_crash_protect);
            SampleTinkerReport.onFastCrashProtect();
            TinkerApplicationHelper.cleanPatch(applicationLike);
            TinkerLog.e(TAG, "tinker has fast crash more than %d, we just clean patch!", Integer
                    .valueOf(fastCrashCount));
            return true;
        }
        sp.edit().putInt(currentVersion, fastCrashCount + 1).commit();
        TinkerLog.e(TAG, "tinker has fast crash %d times", Integer.valueOf(fastCrashCount));
        return false;
    }
}
