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
import java.io.IOException;

public class UpgradePatch extends AbstractPatch {
    private static final String TAG = "Tinker.UpgradePatch";

    public boolean tryPatch(Context context, String tempPatchPath, PatchResult patchResult) {
        Tinker manager = Tinker.with(context);
        File patchFile = new File(tempPatchPath);
        if (!manager.isTinkerEnabled() || !ShareTinkerInternals
                .isTinkerEnableWithSharedPreferences(context)) {
            TinkerLog.e(TAG, "UpgradePatch tryPatch:patch is disabled, just return", new Object[0]);
            return false;
        } else if (patchFile.isFile() && patchFile.exists()) {
            ShareSecurityCheck signatureCheck = new ShareSecurityCheck(context);
            int returnCode = ShareTinkerInternals.checkSignatureAndTinkerID(context, patchFile,
                    signatureCheck);
            if (returnCode != 0) {
                TinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchPackageCheckFail", new Object[0]);
                manager.getPatchReporter().onPatchPackageCheckFail(patchFile, true, returnCode);
                return false;
            }
            patchResult.patchTinkerID = signatureCheck.getNewTinkerID();
            patchResult.baseTinkerID = signatureCheck.getTinkerID();
            SharePatchInfo oldInfo = manager.getTinkerLoadResultIfPresent().patchInfo;
            String patchMd5 = SharePatchFileUtil.getMD5(patchFile);
            if (patchMd5 == null) {
                TinkerLog.e(TAG, "UpgradePatch tryPatch:patch md5 is null, just return", new
                        Object[0]);
                return false;
            }
            SharePatchInfo newInfo;
            patchResult.patchVersion = patchMd5;
            if (oldInfo == null) {
                newInfo = new SharePatchInfo("", patchMd5);
            } else if (oldInfo.oldVersion == null || oldInfo.newVersion == null) {
                TinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchInfoCorrupted", new Object[0]);
                manager.getPatchReporter().onPatchInfoCorrupted(patchFile, oldInfo.oldVersion,
                        oldInfo.newVersion, true);
                return false;
            } else if (oldInfo.oldVersion.equals(patchMd5) || oldInfo.newVersion.equals(patchMd5)) {
                TinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchVersionCheckFail", new Object[0]);
                manager.getPatchReporter().onPatchVersionCheckFail(patchFile, oldInfo, patchMd5,
                        true);
                return false;
            } else {
                newInfo = new SharePatchInfo(oldInfo.oldVersion, patchMd5);
            }
            String patchDirectory = manager.getPatchDirectory().getAbsolutePath();
            TinkerLog.i(TAG, "UpgradePatch tryPatch:dexDiffMd5:%s", patchMd5);
            String patchVersionDirectory = patchDirectory + "/" + SharePatchFileUtil
                    .getPatchVersionDirectory(patchMd5);
            TinkerLog.i(TAG, "UpgradePatch tryPatch:patchVersionDirectory:%s",
                    patchVersionDirectory);
            File destPatchFile = new File(patchVersionDirectory + "/" + SharePatchFileUtil
                    .getPatchVersionFile(patchMd5));
            try {
                SharePatchFileUtil.copyFileUsingStream(patchFile, destPatchFile);
                TinkerLog.w(TAG, "UpgradePatch after %s size:%d, %s size:%d", patchFile
                        .getAbsolutePath(), Long.valueOf(patchFile.length()), destPatchFile
                        .getAbsolutePath(), Long.valueOf(destPatchFile.length()));
                if (!DexDiffPatchInternal.tryRecoverDexFiles(manager, signatureCheck, context,
                        patchVersionDirectory, destPatchFile, true)) {
                    TinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch dex " +
                            "failed", new Object[0]);
                    return false;
                } else if (!BsDiffPatchInternal.tryRecoverLibraryFiles(manager, signatureCheck,
                        context, patchVersionDirectory, destPatchFile, true)) {
                    TinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch library " +
                            "failed", new Object[0]);
                    return false;
                } else if (!ResDiffPatchInternal.tryRecoverResourceFiles(manager, signatureCheck,
                        context, patchVersionDirectory, destPatchFile, true)) {
                    TinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch resource" +
                            " failed", new Object[0]);
                    return false;
                } else if (SharePatchInfo.rewritePatchInfoFileWithLock(manager.getPatchInfoFile()
                        , newInfo, SharePatchFileUtil.getPatchInfoLockFile(patchDirectory))) {
                    TinkerLog.w(TAG, "UpgradePatch tryPatch: done, it is ok", new Object[0]);
                    return true;
                } else {
                    TinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, rewrite patch info" +
                            " failed", new Object[0]);
                    manager.getPatchReporter().onPatchInfoCorrupted(patchFile, newInfo
                            .oldVersion, newInfo.newVersion, true);
                    return false;
                }
            } catch (IOException e) {
                TinkerLog.e(TAG, "UpgradePatch tryPatch:copy patch file fail from %s to %s",
                        patchFile.getPath(), destPatchFile.getPath());
                manager.getPatchReporter().onPatchTypeExtractFail(patchFile, destPatchFile,
                        patchFile.getName(), 1, true);
                return false;
            }
        } else {
            TinkerLog.e(TAG, "UpgradePatch tryPatch:patch file is not found, just return", new
                    Object[0]);
            return false;
        }
    }
}
