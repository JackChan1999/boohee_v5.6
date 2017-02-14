package com.tencent.tinker.lib.patch;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;

import com.tencent.tinker.bsdiff.BSPatch;
import com.tencent.tinker.commons.resutil.ResUtil;
import com.tencent.tinker.commons.ziputil.TinkerZipEntry;
import com.tencent.tinker.commons.ziputil.TinkerZipFile;
import com.tencent.tinker.commons.ziputil.TinkerZipOutputStream;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareResPatchInfo;
import com.tencent.tinker.loader.shareutil.ShareResPatchInfo.LargeModeInfo;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResDiffPatchInternal extends BasePatchInternal {
    protected static final String TAG = "Tinker.ResDiffPatchInternal";

    protected static boolean tryRecoverResourceFiles(Tinker manager, ShareSecurityCheck checker,
                                                     Context context, String
                                                             patchVersionDirectory, File
                                                             patchFile, boolean isUpgradePatch) {
        if (manager.isEnabledForResource()) {
            String resourceMeta = (String) checker.getMetaContentMap().get(ShareConstants
                    .RES_META_FILE);
            if (resourceMeta == null || resourceMeta.length() == 0) {
                TinkerLog.w(TAG, "patch recover, resource is not contained", new Object[0]);
                return true;
            }
            long begin = SystemClock.elapsedRealtime();
            long cost = SystemClock.elapsedRealtime() - begin;
            TinkerLog.i(TAG, "recover resource result:%b, cost:%d, isNewPatch:%b", Boolean
                    .valueOf(patchResourceExtractViaResourceDiff(context, patchVersionDirectory,
                            resourceMeta, patchFile, isUpgradePatch)), Long.valueOf(cost),
                    Boolean.valueOf(isUpgradePatch));
            return patchResourceExtractViaResourceDiff(context, patchVersionDirectory,
                    resourceMeta, patchFile, isUpgradePatch);
        }
        TinkerLog.w(TAG, "patch recover, resource is not enabled", new Object[0]);
        return true;
    }

    private static boolean patchResourceExtractViaResourceDiff(Context context, String
            patchVersionDirectory, String meta, File patchFile, boolean isUpgradePatch) {
        if (extractResourceDiffInternals(context, patchVersionDirectory + "/" + ShareConstants
                .RES_PATH + "/", meta, patchFile, 7, isUpgradePatch)) {
            return true;
        }
        TinkerLog.w(TAG, "patch recover, extractDiffInternals fail", new Object[0]);
        return false;
    }

    private static boolean extractResourceDiffInternals(Context context, String dir, String meta,
                                                        File patchFile, int type, boolean
                                                                isUpgradePatch) {
        Throwable th;
        LargeModeInfo largeModeInfo;
        ShareResPatchInfo resPatchInfo = new ShareResPatchInfo();
        ShareResPatchInfo.parseAllResPatchInfo(meta, resPatchInfo);
        TinkerLog.i(TAG, "res dir: %s, meta: %s", dir, resPatchInfo.toString());
        Tinker manager = Tinker.with(context);
        if (SharePatchFileUtil.checkIfMd5Valid(resPatchInfo.resArscMd5)) {
            File directory = new File(dir);
            File resOutput = new File(directory, ShareConstants.RES_NAME);
            if (!resOutput.exists()) {
                resOutput.getParentFile().mkdirs();
            } else if (SharePatchFileUtil.checkResourceArscMd5(resOutput, resPatchInfo
                    .resArscMd5)) {
                TinkerLog.w(TAG, "resource file %s is already exist, and md5 match, just return " +
                        "true", resOutput.getPath());
                return true;
            } else {
                TinkerLog.w(TAG, "have a mismatch corrupted resource " + resOutput.getPath(), new
                        Object[0]);
                resOutput.delete();
            }
            try {
                ApplicationInfo applicationInfo = context.getApplicationInfo();
                if (applicationInfo == null) {
                    TinkerLog.w(TAG, "applicationInfo == null!!!!", new Object[0]);
                    return false;
                }
                String apkPath = applicationInfo.sourceDir;
                if (!checkAndExtractResourceLargeFile(context, apkPath, directory, patchFile,
                        resPatchInfo, type, isUpgradePatch)) {
                    return false;
                }
                TinkerZipOutputStream out = null;
                TinkerZipFile oldApk = null;
                TinkerZipFile newApk = null;
                int totalEntryCount = 0;
                try {
                    TinkerZipFile tinkerZipFile;
                    TinkerZipOutputStream tinkerZipOutputStream = new TinkerZipOutputStream(new
                            BufferedOutputStream(new FileOutputStream(resOutput)));
                    try {
                        tinkerZipFile = new TinkerZipFile(apkPath);
                    } catch (Throwable th2) {
                        th = th2;
                        out = tinkerZipOutputStream;
                        if (out != null) {
                            out.close();
                        }
                        if (oldApk != null) {
                            oldApk.close();
                        }
                        if (newApk != null) {
                            newApk.close();
                        }
                        for (LargeModeInfo largeModeInfo2 : resPatchInfo.largeModMap.values()) {
                            SharePatchFileUtil.safeDeleteFile(largeModeInfo2.file);
                        }
                        throw th;
                    }
                    try {
                        String name;
                        tinkerZipFile = new TinkerZipFile(patchFile);
                        Enumeration<? extends TinkerZipEntry> entries = tinkerZipFile.entries();
                        while (entries.hasMoreElements()) {
                            TinkerZipEntry zipEntry = (TinkerZipEntry) entries.nextElement();
                            if (zipEntry == null) {
                                throw new TinkerRuntimeException("zipEntry is null when get from " +
                                        "oldApk");
                            }
                            try {
                                name = zipEntry.getName();
                                if (!(!ShareResPatchInfo.checkFileInPattern(resPatchInfo
                                        .patterns, name) || resPatchInfo.deleteRes.contains(name)
                                        || resPatchInfo.modRes.contains(name) || resPatchInfo
                                        .largeModRes.contains(name) || name.equals(ShareConstants
                                        .RES_MANIFEST))) {
                                    ResUtil.extractTinkerEntry(tinkerZipFile, zipEntry,
                                            tinkerZipOutputStream);
                                    totalEntryCount++;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                newApk = tinkerZipFile;
                                oldApk = tinkerZipFile;
                                out = tinkerZipOutputStream;
                            }
                        }
                        TinkerZipEntry manifestZipEntry = tinkerZipFile.getEntry(ShareConstants
                                .RES_MANIFEST);
                        if (manifestZipEntry == null) {
                            TinkerLog.w(TAG, "manifest patch entry is null. path:AndroidManifest" +
                                    ".xml", new Object[0]);
                            manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                    resOutput, ShareConstants.RES_MANIFEST, type, isUpgradePatch);
                            if (tinkerZipOutputStream != null) {
                                tinkerZipOutputStream.close();
                            }
                            if (tinkerZipFile != null) {
                                tinkerZipFile.close();
                            }
                            if (tinkerZipFile != null) {
                                tinkerZipFile.close();
                            }
                            for (LargeModeInfo largeModeInfo22 : resPatchInfo.largeModMap.values
                                    ()) {
                                SharePatchFileUtil.safeDeleteFile(largeModeInfo22.file);
                            }
                            return false;
                        }
                        ResUtil.extractTinkerEntry(tinkerZipFile, manifestZipEntry,
                                tinkerZipOutputStream);
                        totalEntryCount++;
                        Iterator it = resPatchInfo.largeModRes.iterator();
                        while (it.hasNext()) {
                            name = (String) it.next();
                            TinkerZipEntry largeZipEntry = tinkerZipFile.getEntry(name);
                            if (largeZipEntry == null) {
                                TinkerLog.w(TAG, "large patch entry is null. path:" + name, new
                                        Object[0]);
                                manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                        resOutput, name, type, isUpgradePatch);
                                if (tinkerZipOutputStream != null) {
                                    tinkerZipOutputStream.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                for (LargeModeInfo largeModeInfo222 : resPatchInfo.largeModMap
                                        .values()) {
                                    SharePatchFileUtil.safeDeleteFile(largeModeInfo222.file);
                                }
                                return false;
                            }
                            largeModeInfo222 = (LargeModeInfo) resPatchInfo.largeModMap.get(name);
                            ResUtil.extractLargeModifyFile(largeZipEntry, largeModeInfo222.file,
                                    largeModeInfo222.crc, tinkerZipOutputStream);
                            totalEntryCount++;
                        }
                        it = resPatchInfo.addRes.iterator();
                        while (it.hasNext()) {
                            name = (String) it.next();
                            TinkerZipEntry addZipEntry = tinkerZipFile.getEntry(name);
                            if (addZipEntry == null) {
                                TinkerLog.w(TAG, "add patch entry is null. path:" + name, new
                                        Object[0]);
                                manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                        resOutput, name, type, isUpgradePatch);
                                if (tinkerZipOutputStream != null) {
                                    tinkerZipOutputStream.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                for (LargeModeInfo largeModeInfo2222 : resPatchInfo.largeModMap
                                        .values()) {
                                    SharePatchFileUtil.safeDeleteFile(largeModeInfo2222.file);
                                }
                                return false;
                            }
                            ResUtil.extractTinkerEntry(tinkerZipFile, addZipEntry,
                                    tinkerZipOutputStream);
                            totalEntryCount++;
                        }
                        it = resPatchInfo.modRes.iterator();
                        while (it.hasNext()) {
                            name = (String) it.next();
                            TinkerZipEntry modZipEntry = tinkerZipFile.getEntry(name);
                            if (modZipEntry == null) {
                                TinkerLog.w(TAG, "mod patch entry is null. path:" + name, new
                                        Object[0]);
                                manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                        resOutput, name, type, isUpgradePatch);
                                if (tinkerZipOutputStream != null) {
                                    tinkerZipOutputStream.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                if (tinkerZipFile != null) {
                                    tinkerZipFile.close();
                                }
                                for (LargeModeInfo largeModeInfo22222 : resPatchInfo.largeModMap
                                        .values()) {
                                    SharePatchFileUtil.safeDeleteFile(largeModeInfo22222.file);
                                }
                                return false;
                            }
                            ResUtil.extractTinkerEntry(tinkerZipFile, modZipEntry,
                                    tinkerZipOutputStream);
                            totalEntryCount++;
                        }
                        if (tinkerZipOutputStream != null) {
                            tinkerZipOutputStream.close();
                        }
                        if (tinkerZipFile != null) {
                            tinkerZipFile.close();
                        }
                        if (tinkerZipFile != null) {
                            tinkerZipFile.close();
                        }
                        for (LargeModeInfo largeModeInfo222222 : resPatchInfo.largeModMap.values
                                ()) {
                            SharePatchFileUtil.safeDeleteFile(largeModeInfo222222.file);
                        }
                        if (SharePatchFileUtil.checkResourceArscMd5(resOutput, resPatchInfo
                                .resArscMd5)) {
                            TinkerLog.i(TAG, "final new resource file:%s, entry count:%d, " +
                                    "size:%d", resOutput.getAbsolutePath(), Integer.valueOf
                                    (totalEntryCount), Long.valueOf(resOutput.length()));
                            return true;
                        }
                        TinkerLog.i(TAG, "check final new resource file fail path:%s, entry " +
                                "count:%d, size:%d", resOutput.getAbsolutePath(), Integer.valueOf
                                (totalEntryCount), Long.valueOf(resOutput.length()));
                        SharePatchFileUtil.safeDeleteFile(resOutput);
                        manager.getPatchReporter().onPatchTypeExtractFail(patchFile, resOutput,
                                ShareConstants.RES_NAME, type, isUpgradePatch);
                        return false;
                    } catch (Throwable th4) {
                        th = th4;
                        oldApk = tinkerZipFile;
                        out = tinkerZipOutputStream;
                        if (out != null) {
                            out.close();
                        }
                        if (oldApk != null) {
                            oldApk.close();
                        }
                        if (newApk != null) {
                            newApk.close();
                        }
                        while (r7.hasNext()) {
                            SharePatchFileUtil.safeDeleteFile(largeModeInfo222222.file);
                        }
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    if (out != null) {
                        out.close();
                    }
                    if (oldApk != null) {
                        oldApk.close();
                    }
                    if (newApk != null) {
                        newApk.close();
                    }
                    while (r7.hasNext()) {
                        SharePatchFileUtil.safeDeleteFile(largeModeInfo222222.file);
                    }
                    throw th;
                }
            } catch (Throwable e) {
                TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException("patch" +
                        " " + ShareTinkerInternals.getTypeString(type) + " extract failed (" + e
                        .getMessage() + ").", e);
            }
        } else {
            TinkerLog.w(TAG, "resource meta file md5 mismatch, type:%s, md5: %s",
                    ShareTinkerInternals.getTypeString(type), resPatchInfo.resArscMd5);
            manager.getPatchReporter().onPatchPackageCheckFail(patchFile, isUpgradePatch,
                    BasePatchInternal.getMetaCorruptedCode(type));
            return false;
        }
    }

    private static boolean checkAndExtractResourceLargeFile(Context context, String apkPath, File
            directory, File patchFile, ShareResPatchInfo resPatchInfo, int type, boolean
            isUpgradePatch) {
        InputStream oldStream;
        InputStream newStream;
        Throwable e;
        Throwable th;
        long start = System.currentTimeMillis();
        Tinker manager = Tinker.with(context);
        ZipFile apkFile = null;
        ZipFile patchZipFile = null;
        try {
            ZipFile apkFile2 = new ZipFile(apkPath);
            try {
                ZipEntry arscEntry = apkFile2.getEntry(ShareConstants.RES_ARSC);
                File arscFile = new File(directory, ShareConstants.RES_ARSC);
                if (arscEntry == null) {
                    TinkerLog.w(TAG, "resources apk entry is null. path:resources.arsc", new
                            Object[0]);
                    manager.getPatchReporter().onPatchTypeExtractFail(patchFile, arscFile,
                            ShareConstants.RES_ARSC, type, isUpgradePatch);
                    SharePatchFileUtil.closeZip(apkFile2);
                    SharePatchFileUtil.closeZip(null);
                    return false;
                }
                if (!String.valueOf(arscEntry.getCrc()).equals(resPatchInfo.arscBaseCrc)) {
                    TinkerLog.e(TAG, "resources.arsc's crc is not equal, expect crc: %s, got crc:" +
                            " %s", resPatchInfo.arscBaseCrc, String.valueOf(arscEntry.getCrc()));
                    manager.getPatchReporter().onPatchTypeExtractFail(patchFile, arscFile,
                            ShareConstants.RES_ARSC, type, isUpgradePatch);
                    SharePatchFileUtil.closeZip(apkFile2);
                    SharePatchFileUtil.closeZip(null);
                    return false;
                } else if (resPatchInfo.largeModRes.isEmpty()) {
                    TinkerLog.i(TAG, "no large modify resources, just return", new Object[0]);
                    SharePatchFileUtil.closeZip(apkFile2);
                    SharePatchFileUtil.closeZip(null);
                    return true;
                } else {
                    Iterator it = resPatchInfo.largeModRes.iterator();
                    ZipFile patchZipFile2 = null;
                    while (it.hasNext()) {
                        try {
                            String name = (String) it.next();
                            long largeStart = System.currentTimeMillis();
                            LargeModeInfo largeModeInfo = (LargeModeInfo) resPatchInfo
                                    .largeModMap.get(name);
                            if (largeModeInfo == null) {
                                TinkerLog.w(TAG, "resource not found largeModeInfo, type:%s, " +
                                        "name: %s", ShareTinkerInternals.getTypeString(type), name);
                                manager.getPatchReporter().onPatchPackageCheckFail(patchFile,
                                        isUpgradePatch, BasePatchInternal.getMetaCorruptedCode
                                                (type));
                                SharePatchFileUtil.closeZip(apkFile2);
                                SharePatchFileUtil.closeZip(patchZipFile2);
                                patchZipFile = patchZipFile2;
                                return false;
                            }
                            largeModeInfo.file = new File(directory, name);
                            SharePatchFileUtil.ensureFileDirectory(largeModeInfo.file);
                            if (SharePatchFileUtil.checkIfMd5Valid(largeModeInfo.md5)) {
                                ZipFile zipFile = new ZipFile(patchFile);
                                ZipEntry patchEntry = zipFile.getEntry(name);
                                if (patchEntry == null) {
                                    TinkerLog.w(TAG, "large mod patch entry is null. path:" +
                                            name, new Object[0]);
                                    manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                            largeModeInfo.file, name, type, isUpgradePatch);
                                    SharePatchFileUtil.closeZip(apkFile2);
                                    SharePatchFileUtil.closeZip(zipFile);
                                    return false;
                                }
                                ZipEntry baseEntry = apkFile2.getEntry(name);
                                if (baseEntry == null) {
                                    TinkerLog.w(TAG, "resources apk entry is null. path:" + name,
                                            new Object[0]);
                                    manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                            largeModeInfo.file, name, type, isUpgradePatch);
                                    SharePatchFileUtil.closeZip(apkFile2);
                                    SharePatchFileUtil.closeZip(zipFile);
                                    return false;
                                }
                                oldStream = null;
                                newStream = null;
                                oldStream = apkFile2.getInputStream(baseEntry);
                                newStream = zipFile.getInputStream(patchEntry);
                                BSPatch.patchFast(oldStream, newStream, largeModeInfo.file);
                                SharePatchFileUtil.closeQuietly(oldStream);
                                SharePatchFileUtil.closeQuietly(newStream);
                                if (SharePatchFileUtil.verifyFileMd5(largeModeInfo.file,
                                        largeModeInfo.md5)) {
                                    TinkerLog.w(TAG, "success recover large modify file:%s , file" +
                                            " size:%d, use time:%d", largeModeInfo.file.getPath()
                                            , Long.valueOf(largeModeInfo.file.length()), Long
                                                    .valueOf(System.currentTimeMillis() -
                                                            largeStart));
                                    patchZipFile2 = zipFile;
                                } else {
                                    TinkerLog.w(TAG, "Failed to recover large modify file:%s",
                                            largeModeInfo.file.getPath());
                                    SharePatchFileUtil.safeDeleteFile(largeModeInfo.file);
                                    manager.getPatchReporter().onPatchTypeExtractFail(patchFile,
                                            largeModeInfo.file, name, type, isUpgradePatch);
                                    SharePatchFileUtil.closeZip(apkFile2);
                                    SharePatchFileUtil.closeZip(zipFile);
                                    return false;
                                }
                            }
                            TinkerLog.w(TAG, "resource meta file md5 mismatch, type:%s, name: %s," +
                                    " md5: %s", ShareTinkerInternals.getTypeString(type), name,
                                    largeModeInfo.md5);
                            manager.getPatchReporter().onPatchPackageCheckFail(patchFile,
                                    isUpgradePatch, BasePatchInternal.getMetaCorruptedCode(type));
                            SharePatchFileUtil.closeZip(apkFile2);
                            SharePatchFileUtil.closeZip(patchZipFile2);
                            patchZipFile = patchZipFile2;
                            return false;
                        } catch (Throwable th2) {
                            th = th2;
                            patchZipFile = patchZipFile2;
                            apkFile = apkFile2;
                        }
                    }
                    TinkerLog.w(TAG, "success recover all large modify use time:%d", Long.valueOf
                            (System.currentTimeMillis() - start));
                    SharePatchFileUtil.closeZip(apkFile2);
                    SharePatchFileUtil.closeZip(patchZipFile2);
                    patchZipFile = patchZipFile2;
                    return true;
                }
            } catch (Throwable th3) {
                th = th3;
                apkFile = apkFile2;
            }
        } catch (Throwable th4) {
            e = th4;
            try {
                throw new TinkerRuntimeException("patch " + ShareTinkerInternals.getTypeString
                        (type) + " extract failed (" + e.getMessage() + ").", e);
            } catch (Throwable th5) {
                th = th5;
                SharePatchFileUtil.closeZip(apkFile);
                SharePatchFileUtil.closeZip(patchZipFile);
                throw th;
            }
        }
    }
}
