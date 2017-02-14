package com.tencent.tinker.lib.reporter;

import android.content.Context;
import android.content.Intent;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class DefaultPatchReporter implements PatchReporter {
    private static final String TAG = "Tinker.DefaultPatchReporter";
    protected final Context context;

    public DefaultPatchReporter(Context context) {
        this.context = context;
    }

    public void onPatchServiceStart(Intent intent) {
        TinkerLog.i(TAG, "patchReporter: patch service start", new Object[0]);
    }

    public void onPatchPackageCheckFail(File patchFile, boolean isUpgradePatch, int errorCode) {
        TinkerLog.i(TAG, "patchReporter: package check failed. path:%s, isUpgrade:%b, code:%d",
                patchFile.getAbsolutePath(), Boolean.valueOf(isUpgradePatch), Integer.valueOf
                        (errorCode));
        if (errorCode == -3 || errorCode == -4 || errorCode == -8) {
            Tinker.with(this.context).cleanPatchByVersion(patchFile);
        }
    }

    public void onPatchVersionCheckFail(File patchFile, SharePatchInfo oldPatchInfo, String
            patchFileVersion, boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: patch version exist. path:%s, version:%s, isUpgrade:%b",
                patchFile.getAbsolutePath(), patchFileVersion, Boolean.valueOf(isUpgradePatch));
    }

    public void onPatchTypeExtractFail(File patchFile, File extractTo, String filename, int
            fileType, boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: file extract fail type:%s, path:%s, extractTo:%s, " +
                "filename:%s, isUpgrade:%b", ShareTinkerInternals.getTypeString(fileType),
                patchFile.getPath(), extractTo.getPath(), filename, Boolean.valueOf
                        (isUpgradePatch));
        Tinker.with(this.context).cleanPatchByVersion(patchFile);
    }

    public void onPatchDexOptFail(File patchFile, File dexFile, String optDirectory, String
            dexName, Throwable t, boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: dex opt fail path:%s, dexPath:%s, optDir:%s, dexName:%s," +
                " isUpgrade:%b", patchFile.getAbsolutePath(), dexFile.getPath(), optDirectory,
                dexName, Boolean.valueOf(isUpgradePatch));
        TinkerLog.printErrStackTrace(TAG, t, "onPatchDexOptFail:", new Object[0]);
        Tinker.with(this.context).cleanPatchByVersion(patchFile);
    }

    public void onPatchResult(File patchFile, boolean success, long cost, boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: patch all result path:%s, success:%b, cost:%d, " +
                "isUpgrade:%b", patchFile.getAbsolutePath(), Boolean.valueOf(success), Long
                .valueOf(cost), Boolean.valueOf(isUpgradePatch));
    }

    public void onPatchInfoCorrupted(File patchFile, String oldVersion, String newVersion,
                                     boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: patch info is corrupted. old:%s, new:%s, isUpgradeP:%b",
                oldVersion, newVersion, Boolean.valueOf(isUpgradePatch));
        Tinker.with(this.context).cleanPatch();
    }

    public void onPatchException(File patchFile, Throwable e, boolean isUpgradePatch) {
        TinkerLog.i(TAG, "patchReporter: patch exception path:%s, throwable:%s, isUpgrade:%b",
                patchFile.getAbsolutePath(), e.getMessage(), Boolean.valueOf(isUpgradePatch));
        TinkerLog.printErrStackTrace(TAG, e, "tinker patch exception", new Object[0]);
        Tinker.with(this.context).setTinkerDisable();
        Tinker.with(this.context).cleanPatchByVersion(patchFile);
    }
}
