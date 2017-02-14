package com.tencent.tinker.loader.shareutil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.boohee.utils.Coder;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SharePatchFileUtil {
    private static final String TAG = "Tinker.PatchFileUtil";

    public static File getPatchDirectory(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (applicationInfo == null) {
            return null;
        }
        return new File(applicationInfo.dataDir, ShareConstants.PATCH_DIRECTORY_NAME);
    }

    public static File getPatchInfoFile(String patchDirectory) {
        return new File(patchDirectory + "/" + ShareConstants.PATCH_INFO_NAME);
    }

    public static File getPatchInfoLockFile(String patchDirectory) {
        return new File(patchDirectory + "/" + ShareConstants.PATCH_INFO_LOCK_NAME);
    }

    public static String getPatchVersionDirectory(String version) {
        if (version == null || version.length() != 32) {
            return null;
        }
        return ShareConstants.PATCH_BASE_NAME + version.substring(0, 8);
    }

    public static String getPatchVersionFile(String version) {
        if (version == null || version.length() != 32) {
            return null;
        }
        return getPatchVersionDirectory(version) + ShareConstants.PATCH_SUFFIX;
    }

    public static boolean checkIfMd5Valid(String object) {
        if (object == null || object.length() != 32) {
            return false;
        }
        return true;
    }

    public static final boolean fileExists(String filePath) {
        if (filePath != null && new File(filePath).exists()) {
            return true;
        }
        return false;
    }

    public static long getFileOrDirectorySize(File directory) {
        if (directory == null || !directory.exists()) {
            return 0;
        }
        if (directory.isFile()) {
            return directory.length();
        }
        long totalSize = 0;
        File[] fileList = directory.listFiles();
        if (fileList == null) {
            return 0;
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                totalSize += getFileOrDirectorySize(file);
            } else {
                totalSize += file.length();
            }
        }
        return totalSize;
    }

    public static final boolean safeDeleteFile(File file) {
        boolean z = true;
        if (file != null) {
            Log.i(TAG, "safeDeleteFile, try to delete path: " + file.getPath());
            if (file.exists()) {
                z = file.delete();
                if (!z) {
                    Log.e(TAG, "Failed to delete file, try to delete when exit. path: " + file
                            .getPath());
                    file.deleteOnExit();
                }
            }
        }
        return z;
    }

    public static final boolean deleteDir(String dir) {
        if (dir == null) {
            return false;
        }
        return deleteDir(new File(dir));
    }

    public static final boolean deleteDir(File file) {
        int i = 0;
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isFile()) {
            safeDeleteFile(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                int length = files.length;
                while (i < length) {
                    deleteDir(files[i]);
                    i++;
                }
                safeDeleteFile(file);
            }
        }
        return true;
    }

    public static boolean verifyFileMd5(File file, String md5) {
        if (md5 == null) {
            return false;
        }
        String fileMd5 = getMD5(file);
        if (fileMd5 != null) {
            return md5.equals(fileMd5);
        }
        return false;
    }

    public static boolean isRawDexFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        return fileName.endsWith(ShareConstants.DEX_SUFFIX);
    }

    public static boolean verifyDexFileMd5(File file, String md5) {
        Throwable th;
        if (file == null || md5 == null) {
            return false;
        }
        String fileMd5;
        if (isRawDexFile(file.getName())) {
            fileMd5 = getMD5(file);
        } else {
            ZipFile dexJar = null;
            try {
                ZipFile dexJar2 = new ZipFile(file);
                try {
                    ZipEntry classesDex = dexJar2.getEntry("classes.dex");
                    if (classesDex == null) {
                        closeZip(dexJar2);
                        return false;
                    }
                    fileMd5 = getMD5(dexJar2.getInputStream(classesDex));
                    closeZip(dexJar2);
                } catch (IOException e) {
                    dexJar = dexJar2;
                    closeZip(dexJar);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    dexJar = dexJar2;
                    closeZip(dexJar);
                    throw th;
                }
            } catch (IOException e2) {
                closeZip(dexJar);
                return false;
            } catch (Throwable th3) {
                th = th3;
                closeZip(dexJar);
                throw th;
            }
        }
        return md5.equals(fileMd5);
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        Throwable th;
        FileInputStream is = null;
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if (!(parent == null || parent.exists())) {
            parent.mkdirs();
        }
        try {
            FileOutputStream os2;
            FileInputStream is2 = new FileInputStream(source);
            try {
                os2 = new FileOutputStream(dest, false);
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                closeQuietly(is);
                closeQuietly(os);
                throw th;
            }
            try {
                byte[] buffer = new byte[16384];
                while (true) {
                    int length = is2.read(buffer);
                    if (length > 0) {
                        os2.write(buffer, 0, length);
                    } else {
                        closeQuietly(is2);
                        closeQuietly(os2);
                        return;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                os = os2;
                is = is2;
                closeQuietly(is);
                closeQuietly(os);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            closeQuietly(is);
            closeQuietly(os);
            throw th;
        }
    }

    public static String loadDigestes(JarFile jarFile, JarEntry je) throws Exception {
        Throwable th;
        InputStream bis = null;
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes = new byte[16384];
            InputStream bis2 = new BufferedInputStream(jarFile.getInputStream(je));
            while (true) {
                try {
                    int readBytes = bis2.read(bytes);
                    if (readBytes > 0) {
                        sb.append(new String(bytes, 0, readBytes));
                    } else {
                        closeQuietly(bis2);
                        return sb.toString();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bis = bis2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            closeQuietly(bis);
            throw th;
        }
    }

    public static final String getMD5(InputStream is) {
        String str = null;
        if (is != null) {
            try {
                BufferedInputStream bis = new BufferedInputStream(is);
                MessageDigest md = MessageDigest.getInstance(Coder.KEY_MD5);
                StringBuilder md5Str = new StringBuilder(32);
                byte[] buf = new byte[ShareConstants.MD5_FILE_BUF_LENGTH];
                while (true) {
                    int readCount = bis.read(buf);
                    if (readCount == -1) {
                        break;
                    }
                    md.update(buf, 0, readCount);
                }
                byte[] hashValue = md.digest();
                for (byte b : hashValue) {
                    md5Str.append(Integer.toString((b & 255) + 256, 16).substring(1));
                }
                str = md5Str.toString();
            } catch (Exception e) {
            }
        }
        return str;
    }

    public static String getMD5(File file) {
        InputStream fin;
        Throwable th;
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream fin2 = null;
        try {
            InputStream fin3 = new FileInputStream(file);
            try {
                String md5 = getMD5(fin3);
                fin3.close();
                if (fin3 == null) {
                    return md5;
                }
                try {
                    fin3.close();
                    return md5;
                } catch (IOException e) {
                    return md5;
                }
            } catch (Exception e2) {
                fin = fin3;
                if (fin2 != null) {
                    try {
                        fin2.close();
                    } catch (IOException e3) {
                    }
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                fin = fin3;
                if (fin2 != null) {
                    try {
                        fin2.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            if (fin2 != null) {
                fin2.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (fin2 != null) {
                fin2.close();
            }
            throw th;
        }
    }

    public static String optimizedPathFor(File path, File optimizedDirectory) {
        String fileName = path.getName();
        if (!fileName.endsWith(ShareConstants.DEX_SUFFIX)) {
            int lastDot = fileName.lastIndexOf(".");
            if (lastDot < 0) {
                fileName = fileName + ShareConstants.DEX_SUFFIX;
            } else {
                StringBuilder sb = new StringBuilder(lastDot + 4);
                sb.append(fileName, 0, lastDot);
                sb.append(ShareConstants.DEX_SUFFIX);
                fileName = sb.toString();
            }
        }
        return new File(optimizedDirectory, fileName).getPath();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.w(TAG, "Failed to close resource", e);
            }
        }
    }

    public static void closeZip(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException e) {
                Log.w(TAG, "Failed to close resource", e);
            }
        }
    }

    public static boolean checkResourceArscMd5(File resOutput, String destMd5) {
        Throwable e;
        Throwable th;
        ZipFile resourceZip = null;
        try {
            ZipFile resourceZip2 = new ZipFile(resOutput);
            try {
                ZipEntry arscEntry = resourceZip2.getEntry(ShareConstants.RES_ARSC);
                if (arscEntry == null) {
                    Log.i(TAG, "checkResourceArscMd5 resources.arsc not found");
                    closeZip(resourceZip2);
                    resourceZip = resourceZip2;
                    return false;
                }
                InputStream inputStream = resourceZip2.getInputStream(arscEntry);
                String md5 = getMD5(inputStream);
                if (md5 == null || !md5.equals(destMd5)) {
                    closeQuietly(inputStream);
                    closeZip(resourceZip2);
                    resourceZip = resourceZip2;
                    return false;
                }
                closeQuietly(inputStream);
                closeZip(resourceZip2);
                resourceZip = resourceZip2;
                return true;
            } catch (Throwable th2) {
                th = th2;
                resourceZip = resourceZip2;
                closeZip(resourceZip);
                throw th;
            }
        } catch (Throwable th3) {
            e = th3;
            Log.i(TAG, "checkResourceArscMd5 throwable:" + e.getMessage());
            closeZip(resourceZip);
            return false;
        }
    }

    public static void ensureFileDirectory(File file) {
        if (file != null) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
    }
}
