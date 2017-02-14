package com.tencent.tinker.lib.reporter;

import java.io.File;

public interface LoadReporter {
    void onLoadException(Throwable th, int i);

    void onLoadFileMd5Mismatch(File file, int i);

    void onLoadFileNotFound(File file, int i, boolean z);

    void onLoadPackageCheckFail(File file, int i);

    void onLoadPatchInfoCorrupted(String str, String str2, File file);

    void onLoadPatchListenerReceiveFail(File file, int i, boolean z);

    void onLoadPatchVersionChanged(String str, String str2, File file, String str3);

    void onLoadResult(File file, int i, long j);
}
