package com.tencent.tinker.lib.tinker;

import android.content.Context;
import android.content.Intent;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;
import java.util.HashMap;

public class TinkerLoadResult {
    private static final String TAG = "Tinker.TinkerLoadResult";
    public long                    costTime;
    public String                  currentVersion;
    public File                    dexDirectory;
    public HashMap<String, String> dexes;
    public File                    libraryDirectory;
    public HashMap<String, String> libs;
    public int                     loadCode;
    public HashMap<String, String> packageConfig;
    public SharePatchInfo          patchInfo;
    public File                    patchVersionDirectory;
    public File                    patchVersionFile;
    public File                    resourceDirectory;
    public File                    resourceFile;
    public boolean                 versionChanged;

    public boolean parseTinkerResult(Context context, Intent intentResult) {
        Tinker tinker = Tinker.with(context);
        this.loadCode = ShareIntentUtil.getIntentReturnCode(intentResult);
        TinkerLog.i(TAG, "parseTinkerResult loadCode:%d", Integer.valueOf(this.loadCode));
        this.costTime = ShareIntentUtil.getIntentPatchCostTime(intentResult);
        String oldVersion = ShareIntentUtil.getStringExtra(intentResult, ShareIntentUtil
                .INTENT_PATCH_OLD_VERSION);
        String newVersion = ShareIntentUtil.getStringExtra(intentResult, ShareIntentUtil
                .INTENT_PATCH_NEW_VERSION);
        File patchDirectory = tinker.getPatchDirectory();
        File patchInfoFile = tinker.getPatchInfoFile();
        boolean isMainProcess = tinker.isMainProcess();
        if (!(oldVersion == null || newVersion == null)) {
            if (isMainProcess) {
                this.currentVersion = newVersion;
            } else {
                this.currentVersion = oldVersion;
            }
            TinkerLog.i(TAG, "parseTinkerResult oldVersion:%s, newVersion:%s, current:%s",
                    oldVersion, newVersion, this.currentVersion);
            String patchName = SharePatchFileUtil.getPatchVersionDirectory(this.currentVersion);
            if (!ShareTinkerInternals.isNullOrNil(patchName)) {
                this.patchVersionDirectory = new File(patchDirectory.getAbsolutePath() + "/" +
                        patchName);
                this.patchVersionFile = new File(this.patchVersionDirectory.getAbsolutePath(),
                        SharePatchFileUtil.getPatchVersionFile(this.currentVersion));
                this.dexDirectory = new File(this.patchVersionDirectory, ShareConstants.DEX_PATH);
                this.libraryDirectory = new File(this.patchVersionDirectory, ShareConstants
                        .SO_PATH);
                this.resourceDirectory = new File(this.patchVersionDirectory, ShareConstants
                        .RES_PATH);
                this.resourceFile = new File(this.resourceDirectory, ShareConstants.RES_NAME);
            }
            this.patchInfo = new SharePatchInfo(oldVersion, newVersion);
            this.versionChanged = !oldVersion.equals(newVersion);
        }
        Throwable exception = ShareIntentUtil.getIntentPatchException(intentResult);
        if (exception != null) {
            TinkerLog.i(TAG, "Tinker load have exception loadCode:%d", Integer.valueOf(this
                    .loadCode));
            int errorCode = -1;
            switch (this.loadCode) {
                case ShareConstants.ERROR_LOAD_PATCH_UNCAUGHT_EXCEPTION /*-24*/:
                    errorCode = -4;
                    break;
                case ShareConstants.ERROR_LOAD_PATCH_VERSION_RESOURCE_LOAD_EXCEPTION /*-22*/:
                    errorCode = -3;
                    break;
                case ShareConstants.ERROR_LOAD_PATCH_UNKNOWN_EXCEPTION /*-19*/:
                    errorCode = -1;
                    break;
                case -15:
                    errorCode = -2;
                    break;
            }
            tinker.getLoadReporter().onLoadException(exception, errorCode);
            return false;
        }
        switch (this.loadCode) {
            case ShareConstants.ERROR_LOAD_GET_INTENT_FAIL /*-10000*/:
                TinkerLog.e(TAG, "can't get the right intent return code", new Object[0]);
                throw new TinkerRuntimeException("can't get the right intent return code");
            case ShareConstants.ERROR_LOAD_PATCH_VERSION_RESOURCE_MD5_MISMATCH /*-23*/:
                if (this.resourceFile != null) {
                    TinkerLog.e(TAG, "patch resource file md5 is mismatch: %s", this.resourceFile
                            .getAbsolutePath());
                    tinker.getLoadReporter().onLoadFileMd5Mismatch(this.resourceFile, 7);
                    break;
                }
                TinkerLog.e(TAG, "resource file md5 mismatch, but patch resource file not " +
                        "found!", new Object[0]);
                throw new TinkerRuntimeException("resource file md5 mismatch, but patch resource " +
                        "file not found!");
            case ShareConstants.ERROR_LOAD_PATCH_VERSION_RESOURCE_FILE_NOT_EXIST /*-21*/:
                if (this.patchVersionDirectory != null) {
                    TinkerLog.e(TAG, "patch resource file not found:%s", this.resourceFile
                            .getAbsolutePath());
                    tinker.getLoadReporter().onLoadFileNotFound(this.resourceFile, 7, false);
                    break;
                }
                TinkerLog.e(TAG, "patch resource file not found, warning why the path is " +
                        "null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch resource file not found, warning why the " +
                        "path is null!!!!");
            case ShareConstants.ERROR_LOAD_PATCH_VERSION_RESOURCE_DIRECTORY_NOT_EXIST /*-20*/:
                if (this.patchVersionDirectory != null) {
                    TinkerLog.e(TAG, "patch resource file directory not found:%s", this
                            .resourceDirectory.getAbsolutePath());
                    tinker.getLoadReporter().onLoadFileNotFound(this.resourceDirectory, 7, true);
                    break;
                }
                TinkerLog.e(TAG, "patch resource file directory not found, warning why the path " +
                        "is null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch resource file directory not found, " +
                        "warning why the path is null!!!!");
            case ShareConstants.ERROR_LOAD_PATCH_REWRITE_PATCH_INFO_FAIL /*-18*/:
                TinkerLog.i(TAG, "rewrite patch info file corrupted", new Object[0]);
                tinker.getLoadReporter().onLoadPatchInfoCorrupted(oldVersion, newVersion,
                        patchInfoFile);
                break;
            case ShareConstants.ERROR_LOAD_PATCH_VERSION_LIB_FILE_NOT_EXIST /*-17*/:
                String libPath = ShareIntentUtil.getStringExtra(intentResult, ShareIntentUtil
                        .INTENT_PATCH_MISSING_LIB_PATH);
                if (libPath != null) {
                    TinkerLog.e(TAG, "patch lib file not found:%s", libPath);
                    tinker.getLoadReporter().onLoadFileNotFound(new File(libPath), 6, false);
                    break;
                }
                TinkerLog.e(TAG, "patch lib file not found, but path is null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch lib file not found, but path is null!!!!");
            case ShareConstants.ERROR_LOAD_PATCH_VERSION_LIB_DIRECTORY_NOT_EXIST /*-16*/:
                if (this.patchVersionDirectory != null) {
                    TinkerLog.e(TAG, "patch lib file directory not found:%s", this
                            .libraryDirectory.getAbsolutePath());
                    tinker.getLoadReporter().onLoadFileNotFound(this.libraryDirectory, 6, true);
                    break;
                }
                TinkerLog.e(TAG, "patch lib file directory not found, warning why the path is " +
                        "null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch lib file directory not found, warning why" +
                        " the path is null!!!!");
            case -14:
                String mismatchPath = ShareIntentUtil.getStringExtra(intentResult,
                        ShareIntentUtil.INTENT_PATCH_MISMATCH_DEX_PATH);
                if (mismatchPath != null) {
                    TinkerLog.e(TAG, "patch dex file md5 is mismatch: %s", mismatchPath);
                    tinker.getLoadReporter().onLoadFileMd5Mismatch(new File(mismatchPath), 3);
                    break;
                }
                TinkerLog.e(TAG, "patch dex file md5 is mismatch, but path is null!!!!", new
                        Object[0]);
                throw new TinkerRuntimeException("patch dex file md5 is mismatch, but path is " +
                        "null!!!!");
            case -13:
                TinkerLog.e(TAG, "patch dex load fail, classloader is null", new Object[0]);
                break;
            case -12:
                String dexOptPath = ShareIntentUtil.getStringExtra(intentResult, ShareIntentUtil
                        .INTENT_PATCH_MISSING_DEX_PATH);
                if (dexOptPath != null) {
                    TinkerLog.e(TAG, "patch dex opt file not found:%s", dexOptPath);
                    tinker.getLoadReporter().onLoadFileNotFound(new File(dexOptPath), 5, false);
                    break;
                }
                TinkerLog.e(TAG, "patch dex opt file not found, but path is null!!!!", new
                        Object[0]);
                throw new TinkerRuntimeException("patch dex opt file not found, but path is " +
                        "null!!!!");
            case -11:
                String dexPath = ShareIntentUtil.getStringExtra(intentResult, ShareIntentUtil
                        .INTENT_PATCH_MISSING_DEX_PATH);
                if (dexPath != null) {
                    TinkerLog.e(TAG, "patch dex file not found:%s", dexPath);
                    tinker.getLoadReporter().onLoadFileNotFound(new File(dexPath), 3, false);
                    break;
                }
                TinkerLog.e(TAG, "patch dex file not found, but path is null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch dex file not found, but path is null!!!!");
            case -10:
                if (this.dexDirectory != null) {
                    TinkerLog.e(TAG, "patch dex file directory not found:%s", this.dexDirectory
                            .getAbsolutePath());
                    tinker.getLoadReporter().onLoadFileNotFound(this.dexDirectory, 3, true);
                    break;
                }
                TinkerLog.e(TAG, "patch dex file directory not found, warning why the path is " +
                        "null!!!!", new Object[0]);
                throw new TinkerRuntimeException("patch dex file directory not found, warning why" +
                        " the path is null!!!!");
            case -9:
                TinkerLog.i(TAG, "patch package check fail", new Object[0]);
                if (this.patchVersionFile != null) {
                    tinker.getLoadReporter().onLoadPackageCheckFail(this.patchVersionFile,
                            intentResult.getIntExtra(ShareIntentUtil
                                    .INTENT_PATCH_PACKAGE_PATCH_CHECK, ShareConstants
                                    .ERROR_LOAD_GET_INTENT_FAIL));
                    break;
                }
                throw new TinkerRuntimeException("error patch package check fail , but file is " +
                        "null");
            case -7:
                TinkerLog.e(TAG, "patch version file not found, current version:%s", this
                        .currentVersion);
                if (this.patchVersionFile != null) {
                    tinker.getLoadReporter().onLoadFileNotFound(this.patchVersionFile, 1, false);
                    break;
                }
                throw new TinkerRuntimeException("error load patch version file not exist, but " +
                        "file is null");
            case -6:
                TinkerLog.e(TAG, "patch version directory not found, current version:%s", this
                        .currentVersion);
                tinker.getLoadReporter().onLoadFileNotFound(this.patchVersionDirectory, 1, true);
                break;
            case -5:
                TinkerLog.e(TAG, "path info blank, wait main process to restart", new Object[0]);
                break;
            case -4:
                TinkerLog.e(TAG, "path info corrupted", new Object[0]);
                tinker.getLoadReporter().onLoadPatchInfoCorrupted(oldVersion, newVersion,
                        patchInfoFile);
                break;
            case -3:
            case -2:
                TinkerLog.w(TAG, "can't find patch file, is ok, just return", new Object[0]);
                break;
            case -1:
                TinkerLog.w(TAG, "tinker is disable, just return", new Object[0]);
                break;
            case 0:
                TinkerLog.i(TAG, "oh yeah, tinker load all success", new Object[0]);
                tinker.setTinkerLoaded(true);
                this.dexes = ShareIntentUtil.getIntentPatchDexPaths(intentResult);
                this.libs = ShareIntentUtil.getIntentPatchLibsPaths(intentResult);
                this.packageConfig = ShareIntentUtil.getIntentPackageConfig(intentResult);
                if (isMainProcess && this.versionChanged) {
                    this.patchInfo.oldVersion = this.currentVersion;
                    tinker.getLoadReporter().onLoadPatchVersionChanged(oldVersion, newVersion,
                            patchDirectory, this.patchVersionDirectory.getName());
                }
                return true;
        }
        return false;
    }

    public String getTinkerID() {
        if (this.packageConfig != null) {
            return (String) this.packageConfig.get(ShareConstants.TINKER_ID);
        }
        return null;
    }

    public String getNewTinkerID() {
        if (this.packageConfig != null) {
            return (String) this.packageConfig.get(ShareConstants.NEW_TINKER_ID);
        }
        return null;
    }

    public String getPackageConfigByName(String name) {
        if (this.packageConfig != null) {
            return (String) this.packageConfig.get(name);
        }
        return null;
    }
}
