package com.github.moduth.blockcanary;

import android.content.Context;

import java.io.File;

public class BlockCanaryContext implements IBlockCanaryContext {
    private static Context sAppContext;
    private static BlockCanaryContext sInstance = null;

    public static void init(Context c, BlockCanaryContext g) {
        sAppContext = c;
        sInstance = g;
    }

    public static BlockCanaryContext get() {
        if (sInstance != null) {
            return sInstance;
        }
        throw new RuntimeException("BlockCanaryContext not init");
    }

    public Context getContext() {
        return sAppContext;
    }

    public String getQualifier() {
        return "Unspecified";
    }

    public String getUid() {
        return "0";
    }

    public String getNetworkType() {
        return "UNKNOWN";
    }

    public int getConfigDuration() {
        return 99999;
    }

    public int getConfigBlockThreshold() {
        return 1000;
    }

    public boolean isNeedDisplay() {
        return true;
    }

    public String getLogPath() {
        return "/blockcanary/performance";
    }

    public boolean zipLogFile(File[] src, File dest) {
        return false;
    }

    public void uploadLogFile(File zippedFile) {
        throw new UnsupportedOperationException();
    }

    public String getStackFoldPrefix() {
        return null;
    }
}
