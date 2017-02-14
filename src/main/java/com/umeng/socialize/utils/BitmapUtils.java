package com.umeng.socialize.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.boohee.utility.TimeLinePatterns;
import com.boohee.widgets.PathListView;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.net.utils.AesHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class BitmapUtils {
    private static final int    CACHE_SIZE    = 10;
    public static final  int    COMPRESS_FLAG = 3145728;
    public static final  String FOLDER        = "umeng_cache";
    private static final int    FREE_SD_SPACE = 40;
    private static final int    MB            = 1048576;
    public static        String PATH          = "/mnt/sdcard/";
    private static final String TAG           = BitmapUtils.class.getName();

    private static class FileLastModifSort implements Comparator<File> {
        private FileLastModifSort() {
        }

        public int compare(File file, File file2) {
            if (file.lastModified() > file2.lastModified()) {
                return 1;
            }
            if (file.lastModified() == file2.lastModified()) {
                return 0;
            }
            return -1;
        }
    }

    static {
        init();
    }

    public static void init() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + FOLDER
                    + File.separator;
        } else {
            PATH = Environment.getDataDirectory().getPath() + File.separator + FOLDER + File
                    .separator;
        }
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            remove40PercentCache(PATH);
        } catch (Exception e) {
            Log.d("BitmapUtils", "清除缓存抛出异常 ： " + e.toString());
        }
        System.gc();
    }

    private static Options getScaleBitmapOptions(String str, int i, int i2) {
        Options options = null;
        InputStream bitmapStream = getBitmapStream(str);
        if (bitmapStream != null) {
            options = new Options();
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(bitmapStream, null, options);
                int ceil = (int) Math.ceil((double) (options.outHeight / i2));
                int ceil2 = (int) Math.ceil((double) (options.outWidth / i));
                if (ceil > 1 && ceil2 > 1) {
                    if (ceil > ceil2) {
                        options.inSampleSize = ceil;
                    } else {
                        options.inSampleSize = ceil2;
                    }
                }
                options.inJustDecodeBounds = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeInputStream(bitmapStream);
        }
        return options;
    }

    private static InputStream getBitmapStream(String str) {
        InputStream fileInputStream;
        InputStream openStream;
        try {
            if (SocializeConfig.getSocializeConfig().getCacheValidStatus()) {
                fileInputStream = new FileInputStream(new File(getFileName(str)));
                if (fileInputStream != null) {
                    try {
                        if (fileInputStream.available() > 0) {
                            return fileInputStream;
                        }
                    } catch (Exception e) {
                        e = e;
                        Log.e("BitmapUtil", "读取图片流出错" + e.toString());
                        return fileInputStream;
                    }
                }
                openStream = new URL(str).openStream();
                saveInputStream(getFileName(str), openStream);
                return new FileInputStream(new File(getFileName(str)));
            }
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
            } catch (Exception e22) {
                Exception e3;
                Exception exception = e22;
                fileInputStream = null;
                e3 = exception;
                Log.e("BitmapUtil", "读取图片流出错" + e3.toString());
                return fileInputStream;
            }
        }
        fileInputStream = null;
        if (fileInputStream != null) {
            if (fileInputStream.available() > 0) {
                return fileInputStream;
            }
        }
        openStream = new URL(str).openStream();
        try {
            saveInputStream(getFileName(str), openStream);
            return new FileInputStream(new File(getFileName(str)));
        } catch (Exception e222) {
            exception = e222;
            fileInputStream = openStream;
            e3 = exception;
        }
    }

    private static void saveInputStream(String str, InputStream inputStream) {
        FileOutputStream fileOutputStream;
        FileNotFoundException e;
        Throwable th;
        IOException e2;
        try {
            fileOutputStream = new FileOutputStream(new File(str));
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.flush();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                try {
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e7) {
                e2 = e7;
                e2.printStackTrace();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e8) {
                    }
                }
            }
        } catch (FileNotFoundException e9) {
            e = e9;
            fileOutputStream = null;
            e.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e10) {
            e2 = e10;
            fileOutputStream = null;
            e2.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    public static Bitmap loadImage(String str, int i, int i2) {
        InputStream bitmapStream;
        Exception e;
        Throwable th;
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                bitmapStream = getBitmapStream(str);
                try {
                    bitmap = BitmapFactory.decodeStream(bitmapStream, null, getScaleBitmapOptions
                            (str, i, i2));
                    closeInputStream(bitmapStream);
                } catch (Exception e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        closeInputStream(bitmapStream);
                        return bitmap;
                    } catch (Throwable th2) {
                        th = th2;
                        closeInputStream(bitmapStream);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                bitmapStream = bitmap;
                e.printStackTrace();
                closeInputStream(bitmapStream);
                return bitmap;
            } catch (Throwable th3) {
                bitmapStream = bitmap;
                th = th3;
                closeInputStream(bitmapStream);
                throw th;
            }
        }
        return bitmap;
    }

    public static boolean isFileExist(String str) {
        if (!TextUtils.isEmpty(str) && new File(getFileName(str)).exists()) {
            return true;
        }
        return false;
    }

    public static boolean isNeedScale(String str, int i) {
        File file = new File(getFileName(str));
        if (!file.exists() || file.length() < ((long) i)) {
            return false;
        }
        return true;
    }

    public static Bitmap getBitmapFromFile(String str) {
        InputStream bitmapStream = getBitmapStream(str);
        if (bitmapStream == null) {
            return null;
        }
        Bitmap decodeStream = BitmapFactory.decodeStream(bitmapStream, null, null);
        closeInputStream(bitmapStream);
        return decodeStream;
    }

    public static Bitmap getBitmapFromFile(String str, int i, int i2) {
        InputStream bitmapStream = getBitmapStream(str);
        if (bitmapStream == null) {
            return null;
        }
        Bitmap decodeStream = BitmapFactory.decodeStream(bitmapStream, null,
                getScaleBitmapOptions(str, i, i2));
        closeInputStream(bitmapStream);
        return decodeStream;
    }

    public static void saveBitmap(String str, Bitmap bitmap) {
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream
                    (getFileName(str)));
            int i = 100;
            if (bitmap.getRowBytes() * bitmap.getHeight() > COMPRESS_FLAG) {
                i = 80;
            }
            bitmap.compress(CompressFormat.PNG, i, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    public static String getFileName(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.startsWith(TimeLinePatterns.WEB_SCHEME) || str.startsWith("https://")) {
            return PATH + AesHelper.md5(str);
        }
        return str;
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream;
        Exception e;
        Throwable th;
        byte[] bArr = null;
        if (bitmap == null || bitmap.isRecycled()) {
            Log.d(TAG, "bitmap2Bytes  ==> bitmap == null or bitmap.isRecycled()");
        } else {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
                    bArr = byteArrayOutputStream.toByteArray();
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        Log.e(TAG, e.toString());
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
                e = e6;
                byteArrayOutputStream = bArr;
                Log.e(TAG, e.toString());
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                return bArr;
            } catch (Throwable th3) {
                byteArrayOutputStream = bArr;
                th = th3;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        }
        return bArr;
    }

    public static Options getBitmapOptions(byte[] bArr) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int ceil = (int) Math.ceil((double) (options.outWidth / UMImage.MAX_WIDTH));
        int ceil2 = (int) Math.ceil((double) (options.outHeight / UMImage.MAX_HEIGHT));
        if (ceil2 <= 1 || ceil <= 1) {
            if (ceil2 > 2) {
                options.inSampleSize = ceil2;
            } else if (ceil > 2) {
                options.inSampleSize = ceil;
            }
        } else if (ceil2 > ceil) {
            options.inSampleSize = ceil2;
        } else {
            options.inSampleSize = ceil;
        }
        options.inJustDecodeBounds = false;
        return options;
    }

    private static int freeSpaceOnSd() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (int) ((((double) statFs.getBlockSize()) * ((double) statFs.getAvailableBlocks()))
                / 1048576.0d);
    }

    private static void remove40PercentCache(String str) {
        int i = 0;
        File[] listFiles = new File(str).listFiles();
        if (listFiles.length != 0) {
            int length;
            int i2 = 0;
            for (File length2 : listFiles) {
                i2 = (int) (((long) i2) + length2.length());
            }
            if (i2 > 10485760 || 40 > freeSpaceOnSd()) {
                length = (int) ((0.4d * ((double) listFiles.length)) + PathListView.NO_ZOOM);
                Arrays.sort(listFiles, new FileLastModifSort());
                while (i < length) {
                    listFiles[i].delete();
                    i++;
                }
            }
        }
    }

    public static void cleanCache() {
        init();
    }
}
