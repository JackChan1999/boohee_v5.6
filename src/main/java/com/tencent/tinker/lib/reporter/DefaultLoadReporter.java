package com.tencent.tinker.lib.reporter;

import android.content.Context;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class DefaultLoadReporter implements LoadReporter {
    private static final String TAG = "Tinker.DefaultLoadReporter";
    protected final Context context;

    public DefaultLoadReporter(Context context) {
        this.context = context;
    }

    public void onLoadPatchListenerReceiveFail(File patchFile, int errorCode, boolean isUpgrade) {
        TinkerLog.i(TAG, "patch load Reporter: patch receive fail:%s, code:%d, isUpgrade:%b",
                patchFile.getAbsolutePath(), Integer.valueOf(errorCode), Boolean.valueOf
                        (isUpgrade));
    }

    public void onLoadPatchVersionChanged(String oldVersion, String newVersion, File
            patchDirectoryFile, String currentPatchName) {
        int i = 0;
        TinkerLog.i(TAG, "patch version change from " + oldVersion + " to " + newVersion, new
                Object[0]);
        if (oldVersion != null && newVersion != null && !oldVersion.equals(newVersion) && Tinker
                .with(this.context).isMainProcess()) {
            TinkerLog.i(TAG, "try kill all other process", new Object[0]);
            ShareTinkerInternals.killAllOtherProcess(this.context);
            File[] files = patchDirectoryFile.listFiles();
            if (files != null) {
                int length = files.length;
                while (i < length) {
                    File file = files[i];
                    String name = file.getName();
                    if (file.isDirectory() && !name.equals(currentPatchName)) {
                        SharePatchFileUtil.deleteDir(file);
                    }
                    i++;
                }
            }
        }
    }

    public void onLoadFileNotFound(File file, int fileType, boolean isDirectory) {
        TinkerLog.i(TAG, "patch file not found: %s, fileType:%d, isDirectory:%b", file
                .getAbsolutePath(), Integer.valueOf(fileType), Boolean.valueOf(isDirectory));
        if (fileType == 3 || fileType == 5 || fileType == 6 || fileType == 7) {
            Tinker tinker = Tinker.with(this.context);
            if (!tinker.isPatchProcess()) {
                File patchVersionFile = tinker.getTinkerLoadResultIfPresent().patchVersionFile;
                if (patchVersionFile != null) {
                    TinkerInstaller.onReceiveRepairPatch(this.context, patchVersionFile
                            .getAbsolutePath());
                }
            }
        } else if (fileType == 1 || fileType == 2) {
            Tinker.with(this.context).cleanPatch();
        }
    }

    public void onLoadFileMd5Mismatch(File file, int fileType) {
        TinkerLog.i(TAG, "patch file md5 mismatch file: %s, fileType:%d", file.getAbsolutePath(),
                Integer.valueOf(fileType));
        Tinker.with(this.context).cleanPatch();
    }

    public void onLoadPatchInfoCorrupted(String oldVersion, String newVersion, File patchInfoFile) {
        TinkerLog.i(TAG, "patch info file damage: %s", patchInfoFile.getAbsolutePath());
        TinkerLog.i(TAG, "patch info file damage from version: %s to version: %s", oldVersion,
                newVersion);
        Tinker.with(this.context).cleanPatch();
    }

    public void onLoadResult(File patchDirectory, int loadCode, long cost) {
        TinkerLog.i(TAG, "patch load result, path:%s, code:%d, cost:%d", patchDirectory
                .getAbsolutePath(), Integer.valueOf(loadCode), Long.valueOf(cost));
    }

    public void onLoadException(Throwable e, int errorCode) {
        switch (errorCode) {
            case -4:
                TinkerLog.i(TAG, "patch load unCatch exception: %s", e);
                ShareTinkerInternals.setTinkerDisableWithSharedPreferences(this.context);
                TinkerLog.i(TAG, "unCaught exception disable tinker forever with sp", new
                        Object[0]);
                break;
            case -3:
                TinkerLog.i(TAG, "patch load resource exception: %s", e);
                break;
            case -2:
                if (e.getMessage().contains(ShareConstants.CHECK_DEX_INSTALL_FAIL)) {
                    TinkerLog.e(TAG, "tinker dex check fail:" + e.getMessage(), new Object[0]);
                } else {
                    TinkerLog.i(TAG, "patch load dex exception: %s", e);
                }
                ShareTinkerInternals.setTinkerDisableWithSharedPreferences(this.context);
                TinkerLog.i(TAG, "dex exception disable tinker forever with sp", new Object[0]);
                break;
            case -1:
                TinkerLog.i(TAG, "patch load unknown exception: %s", e);
                break;
        }
        TinkerLog.printErrStackTrace(TAG, e, "tinker load exception", new Object[0]);
        Tinker.with(this.context).setTinkerDisable();
        Tinker.with(this.context).cleanPatch();
    }

    public void onLoadPackageCheckFail(File patchFile, int errorCode) {
        TinkerLog.i(TAG, "load patch package check fail file path:%s, errorCode:%d", patchFile
                .getAbsolutePath(), Integer.valueOf(errorCode));
        Tinker.with(this.context).cleanPatch();
    }
}
