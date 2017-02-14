package com.tencent.tinker.loader;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.tencent.tinker.loader.shareutil.ShareDexDiffPatchInfo;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import dalvik.system.PathClassLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TinkerDexLoader {
    private static final String                           DEX_MEAT_FILE     = "assets/dex_meta.txt";
    private static final String                           DEX_OPTIMIZE_PATH = "odex";
    private static final String                           DEX_PATH          = "dex";
    private static final String                           TAG               = "Tinker" +
            ".TinkerDexLoader";
    private static final ArrayList<ShareDexDiffPatchInfo> dexList           = new ArrayList();

    private TinkerDexLoader() {
    }

    @TargetApi(14)
    public static boolean loadTinkerJars(Application application, boolean tinkerLoadVerifyFlag,
                                         String directory, Intent intentResult) {
        if (dexList.isEmpty()) {
            Log.w(TAG, "there is no dex to load");
            return true;
        }
        PathClassLoader classLoader = (PathClassLoader) TinkerDexLoader.class.getClassLoader();
        if (classLoader != null) {
            Log.i(TAG, "classloader: " + classLoader.toString());
            String dexPath = directory + "/" + "dex" + "/";
            File optimizeDir = new File(directory + "/" + "odex");
            ArrayList<File> legalFiles = new ArrayList();
            boolean isArtPlatForm = ShareTinkerInternals.isVmArt();
            Iterator it = dexList.iterator();
            while (it.hasNext()) {
                ShareDexDiffPatchInfo info = (ShareDexDiffPatchInfo) it.next();
                if (!isJustArtSupportDex(info)) {
                    File file = new File(dexPath + info.realName);
                    if (tinkerLoadVerifyFlag) {
                        long start = System.currentTimeMillis();
                        if (SharePatchFileUtil.verifyDexFileMd5(file, isArtPlatForm ? info
                                .destMd5InArt : info.destMd5InDvm)) {
                            Log.i(TAG, "verify dex file:" + file.getPath() + " md5, use time: " +
                                    (System.currentTimeMillis() - start));
                        } else {
                            ShareIntentUtil.setIntentReturnCode(intentResult, -14);
                            intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_MISMATCH_DEX_PATH,
                                    file.getAbsolutePath());
                            return false;
                        }
                    }
                    legalFiles.add(file);
                }
            }
            try {
                SystemClassLoaderAdder.installDexes(application, classLoader, optimizeDir,
                        legalFiles);
                Log.i(TAG, "after loaded classloader: " + application.getClassLoader().toString());
                return true;
            } catch (Throwable e) {
                Log.e(TAG, "install dexes failed");
                intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_EXCEPTION, e);
                ShareIntentUtil.setIntentReturnCode(intentResult, -15);
                return false;
            }
        }
        Log.e(TAG, "classloader is null");
        ShareIntentUtil.setIntentReturnCode(intentResult, -13);
        return false;
    }

    public static boolean checkComplete(String directory, ShareSecurityCheck securityCheck,
                                        Intent intentResult) {
        String meta = (String) securityCheck.getMetaContentMap().get("assets/dex_meta.txt");
        if (meta == null) {
            return true;
        }
        dexList.clear();
        ShareDexDiffPatchInfo.parseDexDiffPatchInfo(meta, dexList);
        if (dexList.isEmpty()) {
            return true;
        }
        HashMap<String, String> dexes = new HashMap();
        Iterator it = dexList.iterator();
        while (it.hasNext()) {
            ShareDexDiffPatchInfo info = (ShareDexDiffPatchInfo) it.next();
            if (!isJustArtSupportDex(info)) {
                if (ShareDexDiffPatchInfo.checkDexDiffPatchInfo(info)) {
                    dexes.put(info.realName, info.destMd5InDvm);
                } else {
                    intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_PACKAGE_PATCH_CHECK, -3);
                    ShareIntentUtil.setIntentReturnCode(intentResult, -9);
                    return false;
                }
            }
        }
        String dexDirectory = directory + "/" + "dex" + "/";
        File dexDir = new File(dexDirectory);
        if (dexDir.exists() && dexDir.isDirectory()) {
            File optimizeDexDirectoryFile = new File(directory + "/" + "odex" + "/");
            for (String name : dexes.keySet()) {
                File dexFile = new File(dexDirectory + name);
                if (dexFile.exists()) {
                    File dexOptFile = new File(SharePatchFileUtil.optimizedPathFor(dexFile,
                            optimizeDexDirectoryFile));
                    if (!dexOptFile.exists()) {
                        intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_MISSING_DEX_PATH,
                                dexOptFile.getAbsolutePath());
                        ShareIntentUtil.setIntentReturnCode(intentResult, -12);
                        return false;
                    }
                }
                intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_MISSING_DEX_PATH, dexFile
                        .getAbsolutePath());
                ShareIntentUtil.setIntentReturnCode(intentResult, -11);
                return false;
            }
            intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_DEXES_PATH, dexes);
            return true;
        }
        ShareIntentUtil.setIntentReturnCode(intentResult, -10);
        return false;
    }

    private static boolean isJustArtSupportDex(ShareDexDiffPatchInfo dexDiffPatchInfo) {
        if (!ShareTinkerInternals.isVmArt() && dexDiffPatchInfo.destMd5InDvm.equals("0")) {
            return true;
        }
        return false;
    }
}
