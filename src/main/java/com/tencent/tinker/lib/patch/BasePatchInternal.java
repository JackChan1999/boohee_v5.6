package com.tencent.tinker.lib.patch;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BasePatchInternal {
    protected static final String DEX_META_FILE        = "assets/dex_meta.txt";
    protected static final String DEX_OPTIMIZE_PATH    = "odex";
    protected static final String DEX_PATH             = "dex";
    protected static final int    MAX_EXTRACT_ATTEMPTS = 2;
    protected static final String RES_META_FILE        = "assets/res_meta.txt";
    protected static final String SO_META_FILE         = "assets/so_meta.txt";
    protected static final String SO_PATH              = "lib";
    protected static final String TAG                  = "Tinker.BasePatchInternal";
    protected static final int    TYPE_DEX             = 3;
    protected static final int    TYPE_DEX_FOR_ART     = 4;
    protected static final int    TYPE_Library         = 6;
    protected static final int    TYPE_RESOURCE        = 7;

    public static boolean extract(ZipFile zipFile, ZipEntry entryFile, File extractTo, String
            targetMd5, boolean isDex) throws IOException {
        int numAttempts = 0;
        boolean isExtractionSuccessful = false;
        while (numAttempts < 2 && !isExtractionSuccessful) {
            numAttempts++;
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entryFile));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(extractTo));
            TinkerLog.i(TAG, "try Extracting " + extractTo.getPath(), new Object[0]);
            try {
                byte[] buffer = new byte[16384];
                for (int length = bis.read(buffer); length != -1; length = bis.read(buffer)) {
                    out.write(buffer, 0, length);
                }
                if (isDex) {
                    isExtractionSuccessful = SharePatchFileUtil.verifyDexFileMd5(extractTo,
                            targetMd5);
                } else {
                    isExtractionSuccessful = SharePatchFileUtil.verifyFileMd5(extractTo, targetMd5);
                }
                TinkerLog.i(TAG, "isExtractionSuccessful: %b", Boolean.valueOf
                        (isExtractionSuccessful));
                if (!isExtractionSuccessful) {
                    extractTo.delete();
                    if (extractTo.exists()) {
                        TinkerLog.e(TAG, "Failed to delete corrupted dex " + extractTo.getPath(),
                                new Object[0]);
                    }
                }
            } finally {
                SharePatchFileUtil.closeQuietly(out);
                SharePatchFileUtil.closeQuietly(bis);
            }
        }
        return isExtractionSuccessful;
    }

    public static int getMetaCorruptedCode(int type) {
        if (type == 3 || type == 4) {
            return -3;
        }
        if (type == 6) {
            return -4;
        }
        if (type == 7) {
            return -8;
        }
        return 0;
    }
}
