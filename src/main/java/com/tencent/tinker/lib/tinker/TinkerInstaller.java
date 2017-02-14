package com.tencent.tinker.lib.tinker;

import android.content.Context;

import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.service.AbstractResultService;
import com.tencent.tinker.lib.tinker.Tinker.Builder;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerLog.TinkerLogImp;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.File;

public class TinkerInstaller {
    private static final String TAG = "Tinker.TinkerInstaller";

    public static void install(ApplicationLike applicationLike) {
        Tinker tinker = new Builder(applicationLike.getApplication()).build();
        Tinker.create(tinker);
        tinker.install(applicationLike.getTinkerResultIntent());
    }

    public static void install(ApplicationLike applicationLike, LoadReporter loadReporter,
                               PatchReporter patchReporter, PatchListener listener, Class<?
            extends AbstractResultService> resultServiceClass, AbstractPatch
                                       upgradePatchProcessor, AbstractPatch repairPatchProcessor) {
        Tinker tinker = new Builder(applicationLike.getApplication()).tinkerFlags(applicationLike
                .getTinkerFlags()).loadReport(loadReporter).listener(listener).patchReporter
                (patchReporter).tinkerLoadVerifyFlag(Boolean.valueOf(applicationLike
                .getTinkerLoadVerifyFlag())).build();
        Tinker.create(tinker);
        tinker.install(applicationLike.getTinkerResultIntent(), resultServiceClass,
                upgradePatchProcessor, repairPatchProcessor);
    }

    public static void cleanPatch(Context context) {
        Tinker.with(context).cleanPatch();
    }

    public static void onReceiveUpgradePatch(Context context, String patchLocation) {
        Tinker.with(context).getPatchListener().onPatchReceived(patchLocation, true);
    }

    public static void onReceiveRepairPatch(Context context, String patchLocation) {
        Tinker.with(context).getPatchListener().onPatchReceived(patchLocation, false);
    }

    public static void setLogIml(TinkerLogImp imp) {
        TinkerLog.setTinkerLogImp(imp);
    }

    public static boolean loadLibraryFromTinker(Context context, String relativePath, String
            libname) throws UnsatisfiedLinkError {
        Tinker tinker = Tinker.with(context);
        if (!libname.startsWith(ShareConstants.SO_PATH)) {
            libname = ShareConstants.SO_PATH + libname;
        }
        if (!libname.endsWith(".so")) {
            libname = libname + ".so";
        }
        String relativeLibPath = relativePath + "/" + libname;
        if (tinker.isEnabledForNativeLib() && tinker.isTinkerLoaded()) {
            TinkerLoadResult loadResult = tinker.getTinkerLoadResultIfPresent();
            if (loadResult.libs != null) {
                for (String name : loadResult.libs.keySet()) {
                    if (name.equals(relativeLibPath)) {
                        String patchLibraryPath = loadResult.libraryDirectory + "/" + name;
                        File library = new File(patchLibraryPath);
                        if (!library.exists()) {
                            continue;
                        } else if (!tinker.isTinkerLoadVerify() || SharePatchFileUtil
                                .verifyFileMd5(library, (String) loadResult.libs.get(name))) {
                            System.load(patchLibraryPath);
                            TinkerLog.i(TAG, "loadLibraryFromTinker success:" + patchLibraryPath,
                                    new Object[0]);
                            return true;
                        } else {
                            tinker.getLoadReporter().onLoadFileMd5Mismatch(library, 6);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void loadArmLibrary(Context context, String libName) {
        if (libName == null || libName.isEmpty() || context == null) {
            throw new TinkerRuntimeException("libName or context is null!");
        } else if (!Tinker.with(context).isEnabledForNativeLib() || !loadLibraryFromTinker
                (context, "lib/armeabi", libName)) {
            System.loadLibrary(libName);
        }
    }

    public static void loadArmV7Library(Context context, String libName) {
        if (libName == null || libName.isEmpty() || context == null) {
            throw new TinkerRuntimeException("libName or context is null!");
        } else if (!Tinker.with(context).isEnabledForNativeLib() || !loadLibraryFromTinker
                (context, "lib/armeabi-v7a", libName)) {
            System.loadLibrary(libName);
        }
    }
}
