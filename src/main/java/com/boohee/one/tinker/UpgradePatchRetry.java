package com.boohee.one.tinker;

import android.content.Context;
import android.content.Intent;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UpgradePatchRetry {
    private static final String RETRY_COUNT_PROPERTY    = "times";
    private static final String RETRY_FILE_MD5_PROPERTY = "md5";
    private static final String RETRY_INFO_NAME         = "patch.retry";
    private static final int    RETRY_MAX_COUNT         = 2;
    private static final String TAG                     = "Tinker.UpgradePatchRetry";
    private static final String TEMP_PATCH_NAME         = "temp.apk";
    private static UpgradePatchRetry sInstance;
    private Context context       = null;
    private boolean isRetryEnable = false;
    private File    retryInfoFile = null;
    private File    tempPatchFile = null;

    static class RetryInfo {
        String md5;
        String times;

        RetryInfo(String md5, String times) {
            this.md5 = md5;
            this.times = times;
        }

        static RetryInfo readRetryProperty(File infoFile) {
            IOException e;
            Throwable th;
            String md5 = null;
            String times = null;
            Properties properties = new Properties();
            FileInputStream inputStream = null;
            try {
                FileInputStream inputStream2 = new FileInputStream(infoFile);
                try {
                    properties.load(inputStream2);
                    md5 = properties.getProperty(UpgradePatchRetry.RETRY_FILE_MD5_PROPERTY);
                    times = properties.getProperty(UpgradePatchRetry.RETRY_COUNT_PROPERTY);
                    SharePatchFileUtil.closeQuietly(inputStream2);
                    inputStream = inputStream2;
                } catch (IOException e2) {
                    e = e2;
                    inputStream = inputStream2;
                    try {
                        e.printStackTrace();
                        SharePatchFileUtil.closeQuietly(inputStream);
                        return new RetryInfo(md5, times);
                    } catch (Throwable th2) {
                        th = th2;
                        SharePatchFileUtil.closeQuietly(inputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = inputStream2;
                    SharePatchFileUtil.closeQuietly(inputStream);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                e.printStackTrace();
                SharePatchFileUtil.closeQuietly(inputStream);
                return new RetryInfo(md5, times);
            }
            return new RetryInfo(md5, times);
        }

        static void writeRetryProperty(File infoFile, RetryInfo info) {
            Exception e;
            Throwable th;
            if (info != null) {
                File parentFile = infoFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                Properties newProperties = new Properties();
                newProperties.put(UpgradePatchRetry.RETRY_FILE_MD5_PROPERTY, info.md5);
                newProperties.put(UpgradePatchRetry.RETRY_COUNT_PROPERTY, info.times);
                FileOutputStream outputStream = null;
                try {
                    FileOutputStream outputStream2 = new FileOutputStream(infoFile, false);
                    try {
                        newProperties.store(outputStream2, null);
                        SharePatchFileUtil.closeQuietly(outputStream2);
                        outputStream = outputStream2;
                    } catch (Exception e2) {
                        e = e2;
                        outputStream = outputStream2;
                        try {
                            TinkerLog.printErrStackTrace(UpgradePatchRetry.TAG, e, "retry write " +
                                    "property fail", new Object[0]);
                            SharePatchFileUtil.closeQuietly(outputStream);
                        } catch (Throwable th2) {
                            th = th2;
                            SharePatchFileUtil.closeQuietly(outputStream);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        outputStream = outputStream2;
                        SharePatchFileUtil.closeQuietly(outputStream);
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    TinkerLog.printErrStackTrace(UpgradePatchRetry.TAG, e, "retry write property " +
                            "fail", new Object[0]);
                    SharePatchFileUtil.closeQuietly(outputStream);
                }
            }
        }
    }

    public UpgradePatchRetry(Context context) {
        this.context = context;
        this.retryInfoFile = new File(SharePatchFileUtil.getPatchDirectory(context),
                RETRY_INFO_NAME);
        this.tempPatchFile = new File(SharePatchFileUtil.getPatchDirectory(context),
                TEMP_PATCH_NAME);
    }

    public static UpgradePatchRetry getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UpgradePatchRetry(context);
        }
        return sInstance;
    }

    public void onPatchRetryLoad() {
        if (!this.isRetryEnable) {
            TinkerLog.w(TAG, "onPatchRetryLoad retry disabled, just return", new Object[0]);
        } else if (!Tinker.with(this.context).isMainProcess()) {
            TinkerLog.w(TAG, "onPatchRetryLoad retry is not main process, just return", new
                    Object[0]);
        } else if (!this.retryInfoFile.exists()) {
            TinkerLog.w(TAG, "onPatchRetryLoad retry info not exist, just return", new Object[0]);
        } else if (TinkerServiceInternals.isTinkerPatchServiceRunning(this.context)) {
            TinkerLog.w(TAG, "onPatchRetryLoad tinker service is running, just return", new
                    Object[0]);
        } else {
            String path = this.tempPatchFile.getAbsolutePath();
            if (path == null || !new File(path).exists()) {
                TinkerLog.w(TAG, "onPatchRetryLoad patch file: %s is not exist, just return", path);
                return;
            }
            TinkerLog.w(TAG, "onPatchRetryLoad patch file: %s is exist, retry to patch", path);
            TinkerInstaller.onReceiveUpgradePatch(this.context, path);
            SampleTinkerReport.onReportRetryPatch();
        }
    }

    private void copyToTempFile(File patchFile) {
        if (!patchFile.getAbsolutePath().equals(this.tempPatchFile.getAbsolutePath())) {
            TinkerLog.w(TAG, "try copy file: %s to %s", patchFile.getAbsolutePath(), this
                    .tempPatchFile.getAbsolutePath());
            try {
                SharePatchFileUtil.copyFileUsingStream(patchFile, this.tempPatchFile);
            } catch (IOException e) {
            }
        }
    }

    public void onPatchServiceStart(Intent intent) {
        if (!this.isRetryEnable) {
            TinkerLog.w(TAG, "onPatchServiceStart retry disabled, just return", new Object[0]);
        } else if (intent == null) {
            TinkerLog.e(TAG, "onPatchServiceStart intent is null, just return", new Object[0]);
        } else if (TinkerPatchService.getPatchUpgradeExtra(intent)) {
            String path = TinkerPatchService.getPatchPathExtra(intent);
            if (path == null) {
                TinkerLog.w(TAG, "onPatchServiceStart patch path is null, just return", new
                        Object[0]);
                return;
            }
            RetryInfo retryInfo;
            File patchFile = new File(path);
            String patchMd5 = SharePatchFileUtil.getMD5(patchFile);
            if (this.retryInfoFile.exists()) {
                retryInfo = RetryInfo.readRetryProperty(this.retryInfoFile);
                if (retryInfo.md5 == null || retryInfo.times == null || !patchMd5.equals
                        (retryInfo.md5)) {
                    copyToTempFile(patchFile);
                    retryInfo.md5 = patchMd5;
                    retryInfo.times = "1";
                } else {
                    int nowTimes = Integer.parseInt(retryInfo.times);
                    if (nowTimes >= 2) {
                        SharePatchFileUtil.safeDeleteFile(this.retryInfoFile);
                        SharePatchFileUtil.safeDeleteFile(this.tempPatchFile);
                        TinkerLog.w(TAG, "onPatchServiceStart retry more than max count, delete " +
                                "retry info file!", new Object[0]);
                        return;
                    }
                    retryInfo.times = String.valueOf(nowTimes + 1);
                }
            } else {
                copyToTempFile(patchFile);
                retryInfo = new RetryInfo(patchMd5, "1");
            }
            RetryInfo.writeRetryProperty(this.retryInfoFile, retryInfo);
        } else {
            TinkerLog.w(TAG, "onPatchServiceStart is not upgrade patch, just return", new
                    Object[0]);
        }
    }

    public void onPatchServiceResult(boolean isUpgradePatch) {
        if (!this.isRetryEnable) {
            TinkerLog.w(TAG, "onPatchServiceResult retry disabled, just return", new Object[0]);
        } else if (isUpgradePatch) {
            if (this.retryInfoFile.exists()) {
                SharePatchFileUtil.safeDeleteFile(this.retryInfoFile);
            }
            if (this.tempPatchFile.exists()) {
                SharePatchFileUtil.safeDeleteFile(this.tempPatchFile);
            }
        } else {
            TinkerLog.w(TAG, "onPatchServiceResult is not upgrade patch, just return", new
                    Object[0]);
        }
    }

    public void setRetryEnable(boolean enable) {
        this.isRetryEnable = enable;
    }
}
