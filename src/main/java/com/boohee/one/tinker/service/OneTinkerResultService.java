package com.boohee.one.tinker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.boohee.one.MyApplication;
import com.boohee.one.tinker.Utils;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class OneTinkerResultService extends DefaultTinkerResultService {
    private static final String TAG = "Tinker.SampleResultService";

    static class ScreenState {

        interface IOnScreenOff {
            void onScreenOff();
        }

        ScreenState(Context context, final IOnScreenOff onScreenOffInterface) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.SCREEN_OFF");
            context.registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent in) {
                    TinkerLog.i(OneTinkerResultService.TAG, "ScreenReceiver action [%s] ", in ==
                            null ? "" : in.getAction());
                    if ("android.intent.action.SCREEN_OFF".equals(in == null ? "" : in.getAction
                            ())) {
                        context.unregisterReceiver(this);
                        if (onScreenOffInterface != null) {
                            onScreenOffInterface.onScreenOff();
                        }
                    }
                }
            }, filter);
        }
    }

    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "SampleResultService received null result!!!!", new Object[0]);
            return;
        }
        TinkerLog.i(TAG, "SampleResultService receive result: %s", result.toString());
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (result.isSuccess) {
                    MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_combine_success);
                    Helper.showLog(OneTinkerResultService.TAG, "patch success, please restart " +
                            "process");
                    return;
                }
                MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_combine_failed);
                Helper.showLog(OneTinkerResultService.TAG, "patch fail, please check reason");
            }
        });
        if (result.isSuccess && result.isUpgradePatch) {
            File rawFile = new File(result.rawPatchFilePath);
            if (rawFile.exists()) {
                TinkerLog.i(TAG, "save delete raw patch file", new Object[0]);
                SharePatchFileUtil.safeDeleteFile(rawFile);
            }
            if (!checkIfNeedKill(result)) {
                TinkerLog.i(TAG, "I have already install the newly patch version!", new Object[0]);
            } else if (Utils.isBackground()) {
                TinkerLog.i(TAG, "it is in background, just restart process", new Object[0]);
                restartProcess();
            } else {
                TinkerLog.i(TAG, "tinker wait screen to restart process", new Object[0]);
                ScreenState screenState = new ScreenState(getApplicationContext(), new
                        IOnScreenOff() {
                    public void onScreenOff() {
                        OneTinkerResultService.this.restartProcess();
                    }
                });
            }
        }
        if (!result.isSuccess && !result.isUpgradePatch) {
            Tinker.with(getApplicationContext()).cleanPatch();
        }
    }

    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly", new Object[0]);
        Process.killProcess(Process.myPid());
    }
}
