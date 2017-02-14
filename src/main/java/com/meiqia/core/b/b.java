package com.meiqia.core.b;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class b {
    private static long a(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        List linkedList = new LinkedList();
        linkedList.add(file);
        long j = 0;
        while (!linkedList.isEmpty()) {
            File file2 = (File) linkedList.remove(0);
            if (file2.exists()) {
                File[] listFiles = file2.listFiles();
                if (!(listFiles == null || listFiles.length == 0)) {
                    int length = listFiles.length;
                    long j2 = j;
                    int i = 0;
                    while (i < length) {
                        File file3 = listFiles[i];
                        long length2 = file3.length() + j2;
                        if (file3.isDirectory()) {
                            linkedList.add(file3);
                        }
                        i++;
                        j2 = length2;
                    }
                    j = j2;
                }
            }
        }
        return j;
    }

    public static Bitmap a(Bitmap bitmap) {
        Bitmap bitmap2 = bitmap;
        while (true) {
            float width = (float) bitmap2.getWidth();
            float height = (float) bitmap2.getHeight();
            float f = 1024.0f / (width > height ? width : height);
            if (width <= 1024.0f && height <= 1024.0f) {
                break;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(f, f);
            bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, (int) width, (int) height, matrix, true);
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap2.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        int i = 80;
        while (byteArrayOutputStream.toByteArray().length / 1024 > 150) {
            byteArrayOutputStream.reset();
            bitmap2.compress(CompressFormat.JPEG, i, byteArrayOutputStream);
            i -= 20;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream
                .toByteArray()));
    }

    private static Bitmap a(String str) {
        long a;
        int i = 2;
        try {
            a = a(new File(str));
        } catch (Exception e) {
            a = 0;
        }
        if (a == 0) {
            return null;
        }
        ExifInterface exifInterface;
        a /= 1000;
        if ((a > 250 || a < 150) && (a <= 251 || a >= 1500)) {
            i = (a < 1500 || a >= 3000) ? (a < 3000 || a > 4500) ? a >= 4500 ? 8 : 1 : 4 : 4;
        }
        Options options = new Options();
        options.inSampleSize = i;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e2) {
            e2.printStackTrace();
            exifInterface = null;
        }
        int attributeInt = exifInterface != null ? exifInterface.getAttributeInt("Orientation",
                1) : 0;
        Matrix matrix = new Matrix();
        if (attributeInt == 3) {
            matrix.postRotate(180.0f);
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile
                    .getHeight(), matrix, true);
        } else if (attributeInt == 6) {
            matrix.postRotate(90.0f);
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile
                    .getHeight(), matrix, true);
        } else if (attributeInt == 8) {
            matrix.postRotate(270.0f);
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile
                    .getHeight(), matrix, true);
        } else {
            matrix.postRotate(0.0f);
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile
                    .getHeight(), matrix, true);
        }
    }

    private static void a(Bitmap bitmap, File file) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 80;
        bitmap.compress(CompressFormat.JPEG, 80, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
            byteArrayOutputStream.reset();
            i -= 10;
            bitmap.compress(CompressFormat.JPEG, i, byteArrayOutputStream);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(File file, File file2) {
        a(a(a(file.getAbsolutePath())), file2);
    }
}
