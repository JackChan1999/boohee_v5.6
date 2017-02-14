package com.tencent.tinker.lib.reporter;

import android.content.Intent;

import com.tencent.tinker.loader.shareutil.SharePatchInfo;

import java.io.File;

public interface PatchReporter {
    void onPatchDexOptFail(File file, File file2, String str, String str2, Throwable th, boolean z);

    void onPatchException(File file, Throwable th, boolean z);

    void onPatchInfoCorrupted(File file, String str, String str2, boolean z);

    void onPatchPackageCheckFail(File file, boolean z, int i);

    void onPatchResult(File file, boolean z, long j, boolean z2);

    void onPatchServiceStart(Intent intent);

    void onPatchTypeExtractFail(File file, File file2, String str, int i, boolean z);

    void onPatchVersionCheckFail(File file, SharePatchInfo sharePatchInfo, String str, boolean z);
}
