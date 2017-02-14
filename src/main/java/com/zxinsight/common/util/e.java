package com.zxinsight.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;

import com.zxinsight.MWConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class e {
    public static String a(Context context) {
        return a(context, "mw_cache").getPath() + File.separator;
    }

    public static String a(String str, String str2) {
        Closeable inputStream;
        Closeable fileOutputStream;
        Exception e;
        Throwable th;
        Closeable closeable = null;
        c.b("FileUtilsstart down shared image");
        URLConnection openConnection = new URL(str).openConnection();
        openConnection.connect();
        try {
            inputStream = openConnection.getInputStream();
            try {
                if (openConnection.getContentLength() <= 0) {
                    String str3 = "RESULT_ERROR";
                    g.a(inputStream);
                    g.a(null);
                    return str3;
                }
                fileOutputStream = new FileOutputStream(new File(a(MWConfiguration.getContext())
                        + str2));
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
                    g.a(inputStream);
                    g.a(fileOutputStream);
                } catch (Exception e2) {
                    e = e2;
                    closeable = fileOutputStream;
                    fileOutputStream = inputStream;
                    try {
                        e.printStackTrace();
                        g.a(fileOutputStream);
                        g.a(closeable);
                        c.b("FileUtilsdown shared image complete");
                        return "SUCCESS";
                    } catch (Throwable th2) {
                        th = th2;
                        inputStream = fileOutputStream;
                        g.a(inputStream);
                        g.a(closeable);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    closeable = fileOutputStream;
                    g.a(inputStream);
                    g.a(closeable);
                    throw th;
                }
                c.b("FileUtilsdown shared image complete");
                return "SUCCESS";
            } catch (Exception e3) {
                e = e3;
                fileOutputStream = inputStream;
                e.printStackTrace();
                g.a(fileOutputStream);
                g.a(closeable);
                c.b("FileUtilsdown shared image complete");
                return "SUCCESS";
            } catch (Throwable th4) {
                th = th4;
                g.a(inputStream);
                g.a(closeable);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            fileOutputStream = null;
            e.printStackTrace();
            g.a(fileOutputStream);
            g.a(closeable);
            c.b("FileUtilsdown shared image complete");
            return "SUCCESS";
        } catch (Throwable th5) {
            th = th5;
            inputStream = null;
            g.a(inputStream);
            g.a(closeable);
            throw th;
        }
    }

    private static File a(Context context, String str) {
        File file;
        if (Environment.getExternalStorageState().equals("mounted") && o.a(context, "android" +
                ".permission.WRITE_EXTERNAL_STORAGE")) {
            file = new File(Environment.getExternalStorageDirectory(), str);
        } else {
            file = context.getCacheDir();
        }
        if (!(file.exists() || file.mkdirs())) {
            file = context.getCacheDir();
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }

    public static void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static File b(String str, String str2) {
        Bitmap c = c(str);
        Bitmap a = a(str, c);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        a.compress(CompressFormat.PNG, 60, byteArrayOutputStream);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        File file = null;
        try {
            file = a(toByteArray, str2);
            if (!a.isRecycled()) {
                a.recycle();
            }
            if (!(c == null || c.isRecycled())) {
                c.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!a.isRecycled()) {
                a.recycle();
            }
            if (!(c == null || c.isRecycled())) {
                c.recycle();
            }
        } catch (Throwable th) {
            if (!a.isRecycled()) {
                a.recycle();
            }
            if (!(c == null || c.isRecycled())) {
                c.recycle();
            }
        }
        return file;
    }

    private static Bitmap a(String str, Bitmap bitmap) {
        return a(b(str), bitmap);
    }

    public static Bitmap a(int i, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
    }

    public static int b(String str) {
        try {
            switch (new ExifInterface(str).getAttributeInt("Orientation", 1)) {
                case 3:
                    return 180;
                case 6:
                    return 90;
                case 8:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File a(byte[] r4, java.lang.String r5) {
        /*
        r3 = 0;
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0017, all -> 0x0022 }
        r1 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x002a, all -> 0x0022 }
        r1.<init>(r0);	 Catch:{ Exception -> 0x002a, all -> 0x0022 }
        r2 = new java.io.BufferedOutputStream;	 Catch:{ Exception -> 0x002a, all -> 0x0022 }
        r2.<init>(r1);	 Catch:{ Exception -> 0x002a, all -> 0x0022 }
        r2.write(r4);	 Catch:{ Exception -> 0x002d }
        com.zxinsight.common.util.g.a(r2);
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = move-exception;
        r1 = r0;
        r2 = r3;
        r0 = r3;
    L_0x001b:
        r1.printStackTrace();	 Catch:{ all -> 0x0027 }
        com.zxinsight.common.util.g.a(r2);
        goto L_0x0016;
    L_0x0022:
        r0 = move-exception;
    L_0x0023:
        com.zxinsight.common.util.g.a(r3);
        throw r0;
    L_0x0027:
        r0 = move-exception;
        r3 = r2;
        goto L_0x0023;
    L_0x002a:
        r1 = move-exception;
        r2 = r3;
        goto L_0x001b;
    L_0x002d:
        r1 = move-exception;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zxinsight.common.util" +
                ".e.a(byte[], java.lang.String):java.io.File");
    }

    public static Bitmap c(String str) {
        int i = 1;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig = Config.RGB_565;
        BitmapFactory.decodeFile(str, options);
        if (options.outHeight > 200 || options.outWidth > 200) {
            i = Math.min(Math.round(((float) options.outHeight) / 200.0f), Math.round(((float)
                    options.outWidth) / 200.0f));
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = i;
        return BitmapFactory.decodeFile(str, options).copy(Config.ARGB_8888, false);
    }

    public static String d(String str) {
        File file = new File(str);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
