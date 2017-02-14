package com.tencent.tinker.lib.tinker;

import android.content.Intent;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;
import java.util.HashMap;

public class TinkerApplicationHelper {
    private static final String TAG = "Tinker.TinkerApplicationHelper";

    public static boolean isTinkerEnableAll(ApplicationLike applicationLike) {
        if (applicationLike != null && applicationLike.getApplication() != null) {
            return ShareTinkerInternals.isTinkerEnabledAll(applicationLike.getTinkerFlags());
        }
        throw new TinkerRuntimeException("tinkerApplication is null");
    }

    public static boolean isTinkerEnableForDex(ApplicationLike applicationLike) {
        if (applicationLike != null && applicationLike.getApplication() != null) {
            return ShareTinkerInternals.isTinkerEnabledForDex(applicationLike.getTinkerFlags());
        }
        throw new TinkerRuntimeException("tinkerApplication is null");
    }

    public static boolean isTinkerEnableForNativeLib(ApplicationLike applicationLike) {
        if (applicationLike != null && applicationLike.getApplication() != null) {
            return ShareTinkerInternals.isTinkerEnabledForNativeLib(applicationLike
                    .getTinkerFlags());
        }
        throw new TinkerRuntimeException("tinkerApplication is null");
    }

    public static boolean isTinkerEnableForResource(ApplicationLike applicationLike) {
        if (applicationLike != null && applicationLike.getApplication() != null) {
            return ShareTinkerInternals.isTinkerEnabledForResource(applicationLike.getTinkerFlags
                    ());
        }
        throw new TinkerRuntimeException("tinkerApplication is null");
    }

    public static File getTinkerPatchDirectory(ApplicationLike applicationLike) {
        if (applicationLike != null && applicationLike.getApplication() != null) {
            return SharePatchFileUtil.getPatchDirectory(applicationLike.getApplication());
        }
        throw new TinkerRuntimeException("tinkerApplication is null");
    }

    public static boolean isTinkerLoadSuccess(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        Intent tinkerResultIntent = applicationLike.getTinkerResultIntent();
        if (tinkerResultIntent != null && ShareIntentUtil.getIntentReturnCode(tinkerResultIntent)
                == 0) {
            return true;
        }
        return false;
    }

    public static HashMap<String, String> getLoadDexesAndMd5(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        Intent tinkerResultIntent = applicationLike.getTinkerResultIntent();
        if (tinkerResultIntent != null && ShareIntentUtil.getIntentReturnCode(tinkerResultIntent)
                == 0) {
            return ShareIntentUtil.getIntentPatchDexPaths(tinkerResultIntent);
        }
        return null;
    }

    public static HashMap<String, String> getLoadLibraryAndMd5(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        Intent tinkerResultIntent = applicationLike.getTinkerResultIntent();
        if (tinkerResultIntent != null && ShareIntentUtil.getIntentReturnCode(tinkerResultIntent)
                == 0) {
            return ShareIntentUtil.getIntentPatchLibsPaths(tinkerResultIntent);
        }
        return null;
    }

    public static HashMap<String, String> getPackageConfigs(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        Intent tinkerResultIntent = applicationLike.getTinkerResultIntent();
        if (tinkerResultIntent != null && ShareIntentUtil.getIntentReturnCode(tinkerResultIntent)
                == 0) {
            return ShareIntentUtil.getIntentPackageConfig(tinkerResultIntent);
        }
        return null;
    }

    public static String getCurrentVersion(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        Intent tinkerResultIntent = applicationLike.getTinkerResultIntent();
        if (tinkerResultIntent == null) {
            return null;
        }
        String oldVersion = ShareIntentUtil.getStringExtra(tinkerResultIntent, ShareIntentUtil
                .INTENT_PATCH_OLD_VERSION);
        String newVersion = ShareIntentUtil.getStringExtra(tinkerResultIntent, ShareIntentUtil
                .INTENT_PATCH_NEW_VERSION);
        boolean isMainProcess = ShareTinkerInternals.isInMainProcess(applicationLike
                .getApplication());
        if (oldVersion == null || newVersion == null) {
            return null;
        }
        if (isMainProcess) {
            return newVersion;
        }
        return oldVersion;
    }

    public static void cleanPatch(ApplicationLike applicationLike) {
        if (applicationLike == null || applicationLike.getApplication() == null) {
            throw new TinkerRuntimeException("tinkerApplication is null");
        }
        if (isTinkerLoadSuccess(applicationLike)) {
            TinkerLog.e(TAG, "it is not safety to clean patch when tinker is loaded, you should " +
                    "kill all your process after clean!", new Object[0]);
        }
        SharePatchFileUtil.deleteDir(SharePatchFileUtil.getPatchDirectory(applicationLike
                .getApplication()));
    }

    public static void loadArmV7aLibrary(ApplicationLike applicationLike, String libName) {
        if (libName == null || libName.isEmpty() || applicationLike == null) {
            throw new TinkerRuntimeException("libName or context is null!");
        } else if (!isTinkerEnableForNativeLib(applicationLike) || !loadLibraryFromTinker
                (applicationLike, "lib/armeabi-v7a", libName)) {
            System.loadLibrary(libName);
        }
    }

    public static void loadArmLibrary(ApplicationLike applicationLike, String libName) {
        if (libName == null || libName.isEmpty() || applicationLike == null) {
            throw new TinkerRuntimeException("libName or context is null!");
        } else if (!isTinkerEnableForNativeLib(applicationLike) || !loadLibraryFromTinker
                (applicationLike, "lib/armeabi", libName)) {
            System.loadLibrary(libName);
        }
    }

    public static boolean loadLibraryFromTinker(ApplicationLike applicationLike, String
            relativePath, String libname) throws UnsatisfiedLinkError {
        if (!libname.startsWith(ShareConstants.SO_PATH)) {
            libname = ShareConstants.SO_PATH + libname;
        }
        if (!libname.endsWith(".so")) {
            libname = libname + ".so";
        }
        String relativeLibPath = relativePath + "/" + libname;
        if (isTinkerEnableForNativeLib(applicationLike) && isTinkerLoadSuccess(applicationLike)) {
            HashMap<String, String> loadLibraries = getLoadLibraryAndMd5(applicationLike);
            if (loadLibraries != null) {
                String currentVersion = getCurrentVersion(applicationLike);
                if (ShareTinkerInternals.isNullOrNil(currentVersion)) {
                    return false;
                }
                File patchDirectory = SharePatchFileUtil.getPatchDirectory(applicationLike
                        .getApplication());
                if (patchDirectory == null) {
                    return false;
                }
                String libPrePath = new File(patchDirectory.getAbsolutePath() + "/" +
                        SharePatchFileUtil.getPatchVersionDirectory(currentVersion))
                        .getAbsolutePath() + "/" + ShareConstants.SO_PATH;
                for (String name : loadLibraries.keySet()) {
                    if (name.equals(relativeLibPath)) {
                        String patchLibraryPath = libPrePath + "/" + name;
                        File library = new File(patchLibraryPath);
                        if (!library.exists()) {
                            continue;
                        } else if (!applicationLike.getTinkerLoadVerifyFlag() ||
                                SharePatchFileUtil.verifyFileMd5(library, (String) loadLibraries
                                        .get(name))) {
                            System.load(patchLibraryPath);
                            TinkerLog.i(TAG, "loadLibraryFromTinker success:" + patchLibraryPath,
                                    new Object[0]);
                            return true;
                        } else {
                            TinkerLog.i(TAG, "loadLibraryFromTinker md5mismatch fail:" +
                                    patchLibraryPath, new Object[0]);
                        }
                    }
                }
            }
        }
        return false;
    }
}
