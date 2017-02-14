package com.boohee.one.tinker.reporter;

import android.content.Context;
import android.content.Intent;

import com.boohee.one.tinker.UpgradePatchRetry;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;

import java.io.File;

public class SamplePatchReporter extends DefaultPatchReporter {
    public SamplePatchReporter(Context context) {
        super(context);
    }

    public void onPatchServiceStart(Intent intent) {
        super.onPatchServiceStart(intent);
        SampleTinkerReport.onApplyPatchServiceStart();
        UpgradePatchRetry.getInstance(this.context).onPatchServiceStart(intent);
    }

    public void onPatchDexOptFail(File patchFile, File dexFile, String optDirectory, String
            dexName, Throwable t, boolean isUpgradePatch) {
        super.onPatchDexOptFail(patchFile, dexFile, optDirectory, dexName, t, isUpgradePatch);
        SampleTinkerReport.onApplyDexOptFail(t);
    }

    public void onPatchException(File patchFile, Throwable e, boolean isUpgradePatch) {
        super.onPatchException(patchFile, e, isUpgradePatch);
        SampleTinkerReport.onApplyCrash(e);
    }

    public void onPatchInfoCorrupted(File patchFile, String oldVersion, String newVersion,
                                     boolean isUpgradePatch) {
        super.onPatchInfoCorrupted(patchFile, oldVersion, newVersion, isUpgradePatch);
        SampleTinkerReport.onApplyInfoCorrupted();
    }

    public void onPatchPackageCheckFail(File patchFile, boolean isUpgradePatch, int errorCode) {
        super.onPatchPackageCheckFail(patchFile, isUpgradePatch, errorCode);
        SampleTinkerReport.onApplyPackageCheckFail(errorCode);
    }

    public void onPatchResult(File patchFile, boolean success, long cost, boolean isUpgradePatch) {
        super.onPatchResult(patchFile, success, cost, isUpgradePatch);
        SampleTinkerReport.onApplied(isUpgradePatch, cost, success);
        UpgradePatchRetry.getInstance(this.context).onPatchServiceResult(isUpgradePatch);
    }

    public void onPatchTypeExtractFail(File patchFile, File extractTo, String filename, int
            fileType, boolean isUpgradePatch) {
        super.onPatchTypeExtractFail(patchFile, extractTo, filename, fileType, isUpgradePatch);
        SampleTinkerReport.onApplyExtractFail(fileType);
    }

    public void onPatchVersionCheckFail(File patchFile, SharePatchInfo oldPatchInfo, String
            patchFileVersion, boolean isUpgradePatch) {
        super.onPatchVersionCheckFail(patchFile, oldPatchInfo, patchFileVersion, isUpgradePatch);
        SampleTinkerReport.onApplyVersionCheckFail();
    }
}
