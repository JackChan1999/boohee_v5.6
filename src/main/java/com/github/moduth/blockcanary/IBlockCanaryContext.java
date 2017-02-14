package com.github.moduth.blockcanary;

import android.content.Context;

import java.io.File;

public interface IBlockCanaryContext {
    int getConfigBlockThreshold();

    Context getContext();

    String getLogPath();

    String getNetworkType();

    String getQualifier();

    String getStackFoldPrefix();

    String getUid();

    boolean isNeedDisplay();

    void uploadLogFile(File file);

    boolean zipLogFile(File[] fileArr, File file);
}
