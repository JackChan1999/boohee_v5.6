package com.boohee.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.boohee.one.cache.FileCache;
import com.boohee.one.sport.DownloadHelper;
import com.boohee.utility.App;
import com.boohee.utility.Channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.util.EncodingUtils;

public class FileUtil {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission" +
            ".WRITE_EXTERNAL_STORAGE";
    static final         String TAG                         = FileUtil.class.getName();

    public static void copyDB(Context ctx, String dbName) {
        IOException e;
        Throwable th;
        String dbFilename = App.DB_PATH + dbName;
        InputStream assetsDB = null;
        FileOutputStream fileOutputStream = null;
        try {
            File dir = new File(App.DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            assetsDB = ctx.getAssets().open(dbName);
            FileOutputStream fos = new FileOutputStream(dbFilename);
            try {
                byte[] buffer = new byte[1048576];
                while (true) {
                    int count = assetsDB.read(buffer);
                    if (count <= 0) {
                        break;
                    }
                    fos.write(buffer, 0, count);
                }
                Helper.showLog(TAG, "copy:" + dbName);
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fileOutputStream = fos;
                        return;
                    }
                }
                if (assetsDB != null) {
                    assetsDB.close();
                }
                fileOutputStream = fos;
            } catch (IOException e3) {
                e2 = e3;
                fileOutputStream = fos;
                try {
                    e2.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return;
                        }
                    }
                    if (assetsDB != null) {
                        assetsDB.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            throw th;
                        }
                    }
                    if (assetsDB != null) {
                        assetsDB.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = fos;
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (assetsDB != null) {
                    assetsDB.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            if (assetsDB != null) {
                assetsDB.close();
            }
        }
    }

    public static File saveCameraImage(Bitmap btp) {
        FileNotFoundException e;
        Throwable th;
        File tempFile = new File(SDcard.getImagesDir(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fileOut = null;
        try {
            FileOutputStream fileOut2 = new FileOutputStream(tempFile);
            try {
                btp.compress(CompressFormat.JPEG, 100, fileOut2);
                if (fileOut2 != null) {
                    try {
                        fileOut2.flush();
                        fileOut2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fileOut = fileOut2;
                    }
                }
                fileOut = fileOut2;
            } catch (FileNotFoundException e3) {
                e = e3;
                fileOut = fileOut2;
                try {
                    e.printStackTrace();
                    if (fileOut != null) {
                        try {
                            fileOut.flush();
                            fileOut.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return tempFile;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOut != null) {
                        try {
                            fileOut.flush();
                            fileOut.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOut = fileOut2;
                if (fileOut != null) {
                    fileOut.flush();
                    fileOut.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
            e = e4;
            e.printStackTrace();
            if (fileOut != null) {
                fileOut.flush();
                fileOut.close();
            }
            return tempFile;
        }
        return tempFile;
    }

    public static File saveTempFile(Bitmap btp) {
        FileNotFoundException e;
        Throwable th;
        File tempFile = SDcard.getTempImage();
        if (tempFile != null) {
            FileOutputStream fileOut = null;
            try {
                FileOutputStream fileOut2 = new FileOutputStream(tempFile);
                try {
                    btp.compress(CompressFormat.JPEG, 90, fileOut2);
                    if (fileOut2 != null) {
                        try {
                            fileOut2.flush();
                            fileOut2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e3) {
                    e = e3;
                    fileOut = fileOut2;
                    try {
                        e.printStackTrace();
                        if (fileOut != null) {
                            try {
                                fileOut.flush();
                                fileOut.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        return tempFile;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOut != null) {
                            try {
                                fileOut.flush();
                                fileOut.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileOut = fileOut2;
                    if (fileOut != null) {
                        fileOut.flush();
                        fileOut.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                e.printStackTrace();
                if (fileOut != null) {
                    fileOut.flush();
                    fileOut.close();
                }
                return tempFile;
            }
        }
        return tempFile;
    }

    public static String downloadImage2Gallery(Context context, Bitmap bmp, String fileName) {
        if (!"mounted".equals(Environment.getExternalStorageState()) ||
                !hasExternalStoragePermission(context)) {
            return null;
        }
        File file = saveImage(context, bmp, fileName);
        if (file == null || !file.exists()) {
            return null;
        }
        String path = file.getAbsolutePath();
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri
                .parse("file://" + path)));
        return path;
    }

    public static String getPNGImagePath(Context context, Bitmap bmp, String fileName) {
        if (!"mounted".equals(Environment.getExternalStorageState()) ||
                !hasExternalStoragePermission(context)) {
            return null;
        }
        File file = savePNGImage(context, bmp, fileName);
        if (file == null || !file.exists()) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static File saveImage(Context context, Bitmap bmp, String fileName) {
        Exception e;
        Throwable th;
        File appDir = new File(Environment.getExternalStorageDirectory(), Channel.BOOHEE);
        if (!(appDir == null || appDir.exists())) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".jpg");
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                bmp.compress(CompressFormat.JPEG, 100, fos2);
                Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName,
                        null);
                if (fos2 != null) {
                    try {
                        fos2.flush();
                        fos2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fos = fos2;
                        return null;
                    }
                }
                fos = fos2;
                return file;
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return null;
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            return null;
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            return null;
        }
    }

    public static File savePNGImage(Context context, Bitmap bmp, String fileName) {
        Exception e;
        Throwable th;
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            return null;
        }
        File appDir = new File(cacheDir, Channel.BOOHEE);
        if (!(appDir == null || appDir.exists())) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName + ".png");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                bmp.compress(CompressFormat.PNG, 100, fos2);
                if (fos2 == null) {
                    return file;
                }
                try {
                    fos2.flush();
                    fos2.close();
                    return file;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return null;
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            return null;
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            return null;
        }
    }

    public static void delFile(String fileName) {
        delFile(new File(fileName));
    }

    public static void delFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (File deleteAllFilesOfDir : files) {
            deleteAllFilesOfDir(deleteAllFilesOfDir);
        }
        path.delete();
    }

    public static boolean hasExternalStoragePermission(Context context) {
        return context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION) == 0;
    }

    public static void saveStrToAPP(String contentString, String dir, String fileName) {
        Exception e;
        Throwable th;
        FileOutputStream outStream = null;
        try {
            FileOutputStream outStream2 = new FileOutputStream(new File(dir, fileName));
            try {
                outStream2.write(contentString.getBytes());
                if (outStream2 != null) {
                    try {
                        outStream2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        outStream = outStream2;
                        return;
                    }
                }
                outStream = outStream2;
            } catch (Exception e3) {
                e = e3;
                outStream = outStream2;
                try {
                    e.printStackTrace();
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outStream = outStream2;
                if (outStream != null) {
                    outStream.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    public static String readStrFromAPP(String dir, String fileName) {
        Exception exception;
        String str = "";
        try {
            File file = new File(dir, fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                FileInputStream fileInputStream;
                try {
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    str = EncodingUtils.getString(buffer, "UTF-8");
                    fileInputStream = fis;
                } catch (Exception e) {
                    exception = e;
                    fileInputStream = fis;
                    exception.printStackTrace();
                    return str;
                }
            }
            return str;
        } catch (Exception e2) {
            exception = e2;
            exception.printStackTrace();
            return str;
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            if (!file.exists()) {
                return 0;
            }
            if (!file.isDirectory()) {
                return file.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            }
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]) * PlaybackStateCompat
                            .ACTION_PLAY_FROM_MEDIA_ID;
                } else {
                    size += fileList[i].length();
                }
            }
            return size / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static void copyFileWithWrite(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out;
        try {
            out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                } else {
                    out.close();
                    in.close();
                    return;
                }
            }
        } catch (Throwable th) {
            in.close();
        }
    }

    public static void removeDirectoryToAnother(File srcDir, File destDir) {
        if (srcDir != null && srcDir.exists() && srcDir.isDirectory()) {
            srcDir.renameTo(destDir);
        }
    }

    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (!sourceLocation.isDirectory()) {
            File directory = targetLocation.getParentFile();
            if (directory == null || directory.exists() || directory.mkdirs()) {
                copyFile(sourceLocation, targetLocation);
                return;
            }
            throw new IOException("Cannot create dir " + directory.getAbsolutePath());
        } else if (targetLocation.exists() || targetLocation.mkdirs()) {
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation,
                        children[i]));
            }
        } else {
            throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
        }
    }

    public static File getVideoCacheDir(Context context) {
        File cacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) &&
                hasExternalStoragePermission(context)) {
            cacheDir = new File(new File(new File(new File(Environment
                    .getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()
            ), DownloadHelper.VIDEO_DOWNLOAD_PATH);
        }
        if (cacheDir == null) {
            cacheDir = FileCache.getCacheDirFile(context);
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (preferExternal && "mounted".equals(Environment.getExternalStorageState()) &&
                hasExternalStoragePermission(context)) {
            appCacheDir = context.getExternalCacheDir();
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            return context.getCacheDir();
        }
        return appCacheDir;
    }

    public static File getFileDirectory(Context context, String dir, boolean preferExternal) {
        File appFileDir = null;
        if (preferExternal && "mounted".equals(Environment.getExternalStorageState()) &&
                hasExternalStoragePermission(context)) {
            appFileDir = context.getExternalFilesDir(dir);
        }
        if (appFileDir == null || !(appFileDir.exists() || appFileDir.mkdirs())) {
            appFileDir = new File(context.getFilesDir(), dir);
        }
        if (appFileDir.exists() || appFileDir.mkdirs()) {
            return appFileDir;
        }
        return context.getFilesDir();
    }

    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null || !updateFile.exists()) {
            Helper.simpleLog(TAG, "MD5 string empty or updateFile null");
            return false;
        }
        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            Helper.simpleLog(TAG, "calculatedDigest null");
            return false;
        }
        Helper.simpleLog(TAG, "Calculated digest: " + calculatedDigest);
        Helper.simpleLog(TAG, "Provided digest: " + md5);
        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(File updateFile) {
        String str = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(Coder.KEY_MD5);
            try {
                InputStream is = new FileInputStream(updateFile);
                byte[] buffer = new byte[8192];
                while (true) {
                    try {
                        int read = is.read(buffer);
                        if (read <= 0) {
                            break;
                        }
                        digest.update(buffer, 0, read);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to process file for MD5", e);
                    } catch (Throwable th) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                            Helper.simpleLog(TAG, "Exception on closing MD5 input stream");
                        }
                    }
                }
                str = String.format("%32s", new Object[]{new BigInteger(1, digest.digest())
                        .toString(16)}).replace(' ', '0');
                try {
                    is.close();
                } catch (IOException e3) {
                    Helper.simpleLog(TAG, "Exception on closing MD5 input stream");
                }
            } catch (FileNotFoundException e4) {
                Helper.simpleLog(TAG, "Exception while getting FileInputStream");
            }
        } catch (NoSuchAlgorithmException e5) {
            Helper.simpleLog(TAG, "Exception while getting digest");
        }
        return str;
    }
}
