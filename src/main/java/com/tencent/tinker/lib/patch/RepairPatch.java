package com.tencent.tinker.lib.patch;

import android.content.Context;

import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class RepairPatch extends AbstractPatch {
    private static final String TAG = "Tinker.RepairPatch";

    public boolean tryPatch(Context context, String tempPatchPath, PatchResult patchResult) {
        Tinker manager = Tinker.with(context);
        File patchFile = new File(tempPatchPath);
        if (!manager.isTinkerEnabled() || !ShareTinkerInternals
                .isTinkerEnableWithSharedPreferences(context)) {
            TinkerLog.e(TAG, "RepairPatch tryPatch:patch is disabled, just return", new Object[0]);
            return false;
        } else if (patchFile.isFile() && patchFile.exists()) {
            ShareSecurityCheck signatureCheck = new ShareSecurityCheck(context);
            int returnCode = ShareTinkerInternals.checkSignatureAndTinkerID(context, patchFile,
                    signatureCheck);
            if (returnCode != 0) {
                TinkerLog.e(TAG, "RepairPatch tryPatch:onPatchPackageCheckFail", new Object[0]);
                manager.getPatchReporter().onPatchPackageCheckFail(patchFile, false, returnCode);
                return false;
            }
            patchResult.patchTinkerID = signatureCheck.getNewTinkerID();
            patchResult.baseTinkerID = signatureCheck.getTinkerID();
            SharePatchInfo oldInfo = manager.getTinkerLoadResultIfPresent().patchInfo;
            String patchMd5 = SharePatchFileUtil.getMD5(patchFile);
            patchResult.patchVersion = patchMd5;
            if (oldInfo == null) {
                TinkerLog.e(TAG, "OldPatchProcessor tryPatch:onPatchVersionCheckFail, oldInfo is " +
                        "null", new Object[0]);
                manager.getPatchReporter().onPatchVersionCheckFail(patchFile, oldInfo, patchMd5,
                        false);
                return false;
            } else if (oldInfo.oldVersion == null || oldInfo.newVersion == null) {
                TinkerLog.e(TAG, "RepairPatch tryPatch:onPatchInfoCorrupted", new Object[0]);
                manager.getPatchReporter().onPatchInfoCorrupted(patchFile, oldInfo.oldVersion,
                        oldInfo.newVersion, false);
                return false;
            } else if (oldInfo.oldVersion.equals(patchMd5) && oldInfo.newVersion.equals(patchMd5)) {
                String patchDirectory = manager.getPatchDirectory().getAbsolutePath();
                String patchVersionDirectory = patchDirectory + "/" + SharePatchFileUtil
                        .getPatchVersionDirectory(patchMd5);
                if (!DexDiffPatchInternal.tryRecoverDexFiles(manager, signatureCheck, context,
                        patchVersionDirectory, patchFile, false)) {
                    TinkerLog.e(TAG, "RepairPatch tryPatch:try patch dex failed", new Object[0]);
                    return false;
                } else if (!BsDiffPatchInternal.tryRecoverLibraryFiles(manager, signatureCheck,
                        context, patchVersionDirectory, patchFile, false)) {
                    TinkerLog.e(TAG, "RepairPatch tryPatch:try patch library failed", new
                            Object[0]);
                    return false;
                } else if (ResDiffPatchInternal.tryRecoverResourceFiles(manager, signatureCheck,
                        context, patchVersionDirectory, patchFile, false)) {
                    return true;
                } else {
                    TinkerLog.e(TAG, "RepairPatch tryPatch:try patch resource failed", new
                            Object[0]);
                    return false;
                }
            } else {
                TinkerLog.e(TAG, "RepairPatch tryPatch:onPatchVersionCheckFail", new Object[0]);
                manager.getPatchReporter().onPatchVersionCheckFail(patchFile, oldInfo, patchMd5,
                        false);
                return false;
            }
        } else {
            TinkerLog.e(TAG, "RepairPatch tryPatch:patch file is not found, just return", new
                    Object[0]);
            return false;
        }
    }
}
