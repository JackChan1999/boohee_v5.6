package com.tencent.tinker.loader.shareutil;

import android.util.Log;

import com.tencent.tinker.loader.TinkerRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SharePatchInfo {
    public static final  int    MAX_EXTRACT_ATTEMPTS = 2;
    public static final  String NEW_VERSION          = "new";
    public static final  String OLD_VERSION          = "old";
    private static final String TAG                  = "PatchInfo";
    public String newVersion;
    public String oldVersion;

    public SharePatchInfo(String oldVer, String newVew) {
        this.oldVersion = oldVer;
        this.newVersion = newVew;
    }

    public static SharePatchInfo readAndCheckPropertyWithLock(File pathInfoFile, File lockFile) {
        File lockParentFile = lockFile.getParentFile();
        if (!lockParentFile.exists()) {
            lockParentFile.mkdirs();
        }
        ShareFileLockHelper fileLock = null;
        try {
            fileLock = ShareFileLockHelper.getFileLock(lockFile);
            SharePatchInfo patchInfo = readAndCheckProperty(pathInfoFile);
            if (fileLock != null) {
                try {
                    fileLock.close();
                } catch (IOException e) {
                    Log.i(TAG, "releaseInfoLock error", e);
                }
            }
            return patchInfo;
        } catch (Exception e2) {
            throw new TinkerRuntimeException("readAndCheckPropertyWithLock fail", e2);
        } catch (Throwable th) {
            if (fileLock != null) {
                try {
                    fileLock.close();
                } catch (IOException e3) {
                    Log.i(TAG, "releaseInfoLock error", e3);
                }
            }
        }
    }

    public static boolean rewritePatchInfoFileWithLock(File pathInfoFile, SharePatchInfo info,
                                                       File lockFile) {
        File lockParentFile = lockFile.getParentFile();
        if (!lockParentFile.exists()) {
            lockParentFile.mkdirs();
        }
        ShareFileLockHelper fileLock = null;
        try {
            fileLock = ShareFileLockHelper.getFileLock(lockFile);
            boolean rewriteSuccess = rewritePatchInfoFile(pathInfoFile, info);
            if (fileLock != null) {
                try {
                    fileLock.close();
                } catch (IOException e) {
                    Log.i(TAG, "releaseInfoLock error", e);
                }
            }
            return rewriteSuccess;
        } catch (Exception e2) {
            throw new TinkerRuntimeException("rewritePatchInfoFileWithLock fail", e2);
        } catch (Throwable th) {
            if (fileLock != null) {
                try {
                    fileLock.close();
                } catch (IOException e3) {
                    Log.i(TAG, "releaseInfoLock error", e3);
                }
            }
        }
    }

    private static SharePatchInfo readAndCheckProperty(File pathInfoFile) {
        IOException e;
        Throwable th;
        boolean isReadPatchSuccessful = false;
        int numAttempts = 0;
        String oldVer = null;
        String newVer = null;
        while (numAttempts < 2 && !isReadPatchSuccessful) {
            numAttempts++;
            Properties properties = new Properties();
            FileInputStream inputStream = null;
            try {
                FileInputStream inputStream2 = new FileInputStream(pathInfoFile);
                try {
                    properties.load(inputStream2);
                    oldVer = properties.getProperty("old");
                    newVer = properties.getProperty("new");
                    SharePatchFileUtil.closeQuietly(inputStream2);
                    inputStream = inputStream2;
                } catch (IOException e2) {
                    e = e2;
                    inputStream = inputStream2;
                    try {
                        e.printStackTrace();
                        SharePatchFileUtil.closeQuietly(inputStream);
                        if (!oldVer.equals("")) {
                        }
                        isReadPatchSuccessful = true;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = inputStream2;
                }
            } catch (IOException e3) {
                e = e3;
                e.printStackTrace();
                SharePatchFileUtil.closeQuietly(inputStream);
                if (oldVer.equals("")) {
                }
                isReadPatchSuccessful = true;
            }
            if (!(oldVer == null || newVer == null)) {
                if ((oldVer.equals("") || SharePatchFileUtil.checkIfMd5Valid(oldVer)) &&
                        SharePatchFileUtil.checkIfMd5Valid(newVer)) {
                    isReadPatchSuccessful = true;
                } else {
                    Log.w(TAG, "path info file  corrupted:" + pathInfoFile.getAbsolutePath());
                }
            }
        }
        if (isReadPatchSuccessful) {
            return new SharePatchInfo(oldVer, newVer);
        }
        return null;
        SharePatchFileUtil.closeQuietly(inputStream);
        throw th;
    }

    private static boolean rewritePatchInfoFile(File pathInfoFile, SharePatchInfo info) {
        Exception e;
        SharePatchInfo tempInfo;
        Throwable th;
        if (pathInfoFile == null || info == null) {
            return false;
        }
        Log.i(TAG, "rewritePatchInfoFile file path:" + pathInfoFile.getAbsolutePath() + " , " +
                "oldVer:" + info.oldVersion + ", newVer:" + info.newVersion);
        boolean isWritePatchSuccessful = false;
        int numAttempts = 0;
        File parentFile = pathInfoFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        while (numAttempts < 2 && !isWritePatchSuccessful) {
            numAttempts++;
            Properties newProperties = new Properties();
            newProperties.put("old", info.oldVersion);
            newProperties.put("new", info.newVersion);
            FileOutputStream outputStream = null;
            try {
                FileOutputStream outputStream2 = new FileOutputStream(pathInfoFile, false);
                try {
                    newProperties.store(outputStream2, "from old version:" + info.oldVersion + " " +
                            "to new version:" + info.newVersion);
                    SharePatchFileUtil.closeQuietly(outputStream2);
                    outputStream = outputStream2;
                } catch (Exception e2) {
                    e = e2;
                    outputStream = outputStream2;
                    try {
                        e.printStackTrace();
                        SharePatchFileUtil.closeQuietly(outputStream);
                        tempInfo = readAndCheckProperty(pathInfoFile);
                        if (tempInfo == null) {
                        }
                        isWritePatchSuccessful = false;
                        if (isWritePatchSuccessful) {
                            pathInfoFile.delete();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = outputStream2;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                SharePatchFileUtil.closeQuietly(outputStream);
                tempInfo = readAndCheckProperty(pathInfoFile);
                if (tempInfo == null) {
                }
                isWritePatchSuccessful = false;
                if (isWritePatchSuccessful) {
                    pathInfoFile.delete();
                }
            }
            tempInfo = readAndCheckProperty(pathInfoFile);
            if (tempInfo == null && tempInfo.oldVersion.equals(info.oldVersion) && tempInfo
                    .newVersion.equals(info.newVersion)) {
                isWritePatchSuccessful = true;
            } else {
                isWritePatchSuccessful = false;
            }
            if (isWritePatchSuccessful) {
                pathInfoFile.delete();
            }
        }
        if (isWritePatchSuccessful) {
            return true;
        }
        return false;
        SharePatchFileUtil.closeQuietly(outputStream);
        throw th;
    }
}
