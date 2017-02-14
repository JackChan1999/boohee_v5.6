package com.tencent.tinker.loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class TinkerLoader extends AbstractTinkerLoader {
    private static final String TAG = "Tinker.TinkerLoader";
    private SharePatchInfo patchInfo;

    public Intent tryLoad(TinkerApplication app, int tinkerFlag, boolean tinkerLoadVerifyFlag) {
        Intent resultIntent = new Intent();
        long begin = SystemClock.elapsedRealtime();
        tryLoadPatchFilesInternal(app, tinkerFlag, tinkerLoadVerifyFlag, resultIntent);
        ShareIntentUtil.setIntentPatchCostTime(resultIntent, SystemClock.elapsedRealtime() - begin);
        return resultIntent;
    }

    private void tryLoadPatchFilesInternal(TinkerApplication app, int tinkerFlag, boolean
            tinkerLoadVerifyFlag, Intent resultIntent) {
        if (ShareTinkerInternals.isTinkerEnabled(tinkerFlag)) {
            File patchDirectoryFile = SharePatchFileUtil.getPatchDirectory(app);
            if (patchDirectoryFile == null) {
                Log.w(TAG, "tryLoadPatchFiles:getPatchDirectory == null");
                ShareIntentUtil.setIntentReturnCode(resultIntent, -2);
                return;
            }
            String patchDirectoryPath = patchDirectoryFile.getAbsolutePath();
            if (patchDirectoryFile.exists()) {
                File patchInfoFile = SharePatchFileUtil.getPatchInfoFile(patchDirectoryPath);
                if (patchInfoFile.exists()) {
                    File patchInfoLockFile = SharePatchFileUtil.getPatchInfoLockFile
                            (patchDirectoryPath);
                    this.patchInfo = SharePatchInfo.readAndCheckPropertyWithLock(patchInfoFile,
                            patchInfoLockFile);
                    if (this.patchInfo == null) {
                        ShareIntentUtil.setIntentReturnCode(resultIntent, -4);
                        return;
                    }
                    String oldVersion = this.patchInfo.oldVersion;
                    String newVersion = this.patchInfo.newVersion;
                    if (oldVersion == null || newVersion == null) {
                        Log.w(TAG, "tryLoadPatchFiles:onPatchInfoCorrupted");
                        ShareIntentUtil.setIntentReturnCode(resultIntent, -4);
                        return;
                    }
                    resultIntent.putExtra(ShareIntentUtil.INTENT_PATCH_OLD_VERSION, oldVersion);
                    resultIntent.putExtra(ShareIntentUtil.INTENT_PATCH_NEW_VERSION, newVersion);
                    boolean mainProcess = ShareTinkerInternals.isInMainProcess(app);
                    boolean versionChanged = !oldVersion.equals(newVersion);
                    String version = oldVersion;
                    if (versionChanged && mainProcess) {
                        version = newVersion;
                    }
                    if (ShareTinkerInternals.isNullOrNil(version)) {
                        Log.w(TAG, "tryLoadPatchFiles:version is blank, wait main process to " +
                                "restart");
                        ShareIntentUtil.setIntentReturnCode(resultIntent, -5);
                        return;
                    }
                    String patchVersionDirectory = patchDirectoryPath + "/" + SharePatchFileUtil
                            .getPatchVersionDirectory(version);
                    File file = new File(patchVersionDirectory);
                    if (file.exists()) {
                        file = new File(file.getAbsolutePath(), SharePatchFileUtil
                                .getPatchVersionFile(version));
                        if (file.exists()) {
                            ShareSecurityCheck shareSecurityCheck = new ShareSecurityCheck(app);
                            int returnCode = ShareTinkerInternals.checkSignatureAndTinkerID(app,
                                    file, shareSecurityCheck);
                            if (returnCode != 0) {
                                Log.w(TAG, "tryLoadPatchFiles:checkSignatureAndTinkerID");
                                resultIntent.putExtra(ShareIntentUtil
                                        .INTENT_PATCH_PACKAGE_PATCH_CHECK, returnCode);
                                ShareIntentUtil.setIntentReturnCode(resultIntent, -9);
                                return;
                            }
                            resultIntent.putExtra(ShareIntentUtil.INTENT_PATCH_PACKAGE_CONFIG,
                                    shareSecurityCheck.getPackagePropertiesIfPresent());
                            boolean isEnabledForDex = ShareTinkerInternals.isTinkerEnabledForDex
                                    (tinkerFlag);
                            if (isEnabledForDex && !TinkerDexLoader.checkComplete
                                    (patchVersionDirectory, shareSecurityCheck, resultIntent)) {
                                Log.w(TAG, "tryLoadPatchFiles:dex check fail");
                                return;
                            } else if (!ShareTinkerInternals.isTinkerEnabledForNativeLib
                                    (tinkerFlag) || TinkerSoLoader.checkComplete
                                    (patchVersionDirectory, shareSecurityCheck, resultIntent)) {
                                boolean isEnabledForResource = ShareTinkerInternals
                                        .isTinkerEnabledForResource(tinkerFlag);
                                Log.w(TAG, "tryLoadPatchFiles:isEnabledForResource:" +
                                        isEnabledForResource);
                                if (!isEnabledForResource || TinkerResourceLoader.checkComplete
                                        (app, patchVersionDirectory, shareSecurityCheck,
                                                resultIntent)) {
                                    if (mainProcess && versionChanged) {
                                        this.patchInfo.oldVersion = version;
                                        if (!SharePatchInfo.rewritePatchInfoFileWithLock
                                                (patchInfoFile, this.patchInfo,
                                                        patchInfoLockFile)) {
                                            ShareIntentUtil.setIntentReturnCode(resultIntent, -18);
                                            Log.w(TAG,
                                                    "tryLoadPatchFiles:onReWritePatchInfoCorrupted");
                                            return;
                                        }
                                    }
                                    if (!checkSafeModeCount(app)) {
                                        resultIntent.putExtra(ShareIntentUtil
                                                .INTENT_PATCH_EXCEPTION, new
                                                TinkerRuntimeException("checkSafeModeCount fail"));
                                        ShareIntentUtil.setIntentReturnCode(resultIntent, -24);
                                        Log.w(TAG, "tryLoadPatchFiles:checkSafeModeCount fail");
                                        return;
                                    } else if (isEnabledForDex && !TinkerDexLoader.loadTinkerJars
                                            (app, tinkerLoadVerifyFlag, patchVersionDirectory,
                                                    resultIntent)) {
                                        Log.w(TAG, "tryLoadPatchFiles:onPatchLoadDexesFail");
                                        return;
                                    } else if (!isEnabledForResource || TinkerResourceLoader
                                            .loadTinkerResources(tinkerLoadVerifyFlag,
                                                    patchVersionDirectory, resultIntent)) {
                                        ShareIntentUtil.setIntentReturnCode(resultIntent, 0);
                                        Log.i(TAG, "tryLoadPatchFiles: load end, ok!");
                                        return;
                                    } else {
                                        Log.w(TAG, "tryLoadPatchFiles:onPatchLoadResourcesFail");
                                        return;
                                    }
                                }
                                Log.w(TAG, "tryLoadPatchFiles:resource check fail");
                                return;
                            } else {
                                Log.w(TAG, "tryLoadPatchFiles:native lib check fail");
                                return;
                            }
                        }
                        Log.w(TAG, "tryLoadPatchFiles:onPatchVersionFileNotFound");
                        ShareIntentUtil.setIntentReturnCode(resultIntent, -7);
                        return;
                    }
                    Log.w(TAG, "tryLoadPatchFiles:onPatchVersionDirectoryNotFound");
                    ShareIntentUtil.setIntentReturnCode(resultIntent, -6);
                    return;
                }
                Log.w(TAG, "tryLoadPatchFiles:patch info not exist:" + patchInfoFile
                        .getAbsolutePath());
                ShareIntentUtil.setIntentReturnCode(resultIntent, -3);
                return;
            }
            Log.w(TAG, "tryLoadPatchFiles:patch dir not exist:" + patchDirectoryPath);
            ShareIntentUtil.setIntentReturnCode(resultIntent, -2);
            return;
        }
        ShareIntentUtil.setIntentReturnCode(resultIntent, -1);
    }

    private boolean checkSafeModeCount(TinkerApplication application) {
        String preferName = ShareConstants.TINKER_OWN_PREFERENCE_CONFIG + ShareTinkerInternals
                .getProcessName(application);
        SharedPreferences sp = application.getSharedPreferences(preferName, 0);
        int count = sp.getInt(ShareConstants.TINKER_SAFE_MODE_COUNT, 0);
        Log.w(TAG, "tinker safe mode preferName:" + preferName + " count:" + count);
        if (count >= 3) {
            sp.edit().putInt(ShareConstants.TINKER_SAFE_MODE_COUNT, 0).commit();
            return false;
        }
        application.setUseSafeMode(true);
        count++;
        sp.edit().putInt(ShareConstants.TINKER_SAFE_MODE_COUNT, count).commit();
        Log.w(TAG, "after tinker safe mode count:" + count);
        return true;
    }
}
