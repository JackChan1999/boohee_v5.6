package com.boohee.one.tinker.reporter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;

import com.boohee.one.MyApplication;
import com.boohee.one.tinker.UpgradePatchRetry;
import com.boohee.utility.Event;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class SampleLoadReporter extends DefaultLoadReporter {
    private Handler handler = new Handler();

    public SampleLoadReporter(Context context) {
        super(context);
    }

    public void onLoadPatchListenerReceiveFail(final File patchFile, int errorCode, boolean
            isUpgrade) {
        super.onLoadPatchListenerReceiveFail(patchFile, errorCode, isUpgrade);
        switch (errorCode) {
            case -6:
                MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_error_no_space);
                break;
            case -3:
                if (isUpgrade) {
                    this.handler.postDelayed(new Runnable() {
                        public void run() {
                            TinkerInstaller.onReceiveUpgradePatch(SampleLoadReporter.this
                                    .context, patchFile.getAbsolutePath());
                        }
                    }, 60000);
                    break;
                }
                break;
            case -2:
                MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_error_patch_noexits);
                break;
        }
        SampleTinkerReport.onTryApplyFail(errorCode);
    }

    public void onLoadResult(File patchDirectory, int loadCode, long cost) {
        super.onLoadResult(patchDirectory, loadCode, cost);
        switch (loadCode) {
            case 0:
                SampleTinkerReport.onLoaded(cost);
                break;
        }
        Looper.getMainLooper();
        Looper.myQueue().addIdleHandler(new IdleHandler() {
            public boolean queueIdle() {
                UpgradePatchRetry.getInstance(SampleLoadReporter.this.context).onPatchRetryLoad();
                return false;
            }
        });
    }

    public void onLoadException(Throwable e, int errorCode) {
        super.onLoadException(e, errorCode);
        MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_load_exception);
        SampleTinkerReport.onLoadException(e, errorCode);
    }

    public void onLoadFileMd5Mismatch(File file, int fileType) {
        super.onLoadFileMd5Mismatch(file, fileType);
        SampleTinkerReport.onLoadFileMisMatch(fileType);
    }

    public void onLoadFileNotFound(File file, int fileType, boolean isDirectory) {
        super.onLoadFileNotFound(file, fileType, isDirectory);
        SampleTinkerReport.onLoadFileNotFound(fileType);
    }

    public void onLoadPackageCheckFail(File patchFile, int errorCode) {
        super.onLoadPackageCheckFail(patchFile, errorCode);
        SampleTinkerReport.onLoadPackageCheckFail(errorCode);
    }

    public void onLoadPatchInfoCorrupted(String oldVersion, String newVersion, File patchInfoFile) {
        super.onLoadPatchInfoCorrupted(oldVersion, newVersion, patchInfoFile);
        SampleTinkerReport.onLoadInfoCorrupted();
    }

    public void onLoadPatchVersionChanged(String oldVersion, String newVersion, File
            patchDirectoryFile, String currentPatchName) {
        super.onLoadPatchVersionChanged(oldVersion, newVersion, patchDirectoryFile,
                currentPatchName);
    }
}
