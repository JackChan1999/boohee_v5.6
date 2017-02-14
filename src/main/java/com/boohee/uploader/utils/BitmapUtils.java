package com.boohee.uploader.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class BitmapUtils {
    public static byte[] compressBitmap(String path, int maxSize, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            WeakReference<Bitmap> bitmap = cprsBmpBySize(path, rqsW, rqsH);
            WeakReference<Bitmap> rotatedBmp = autoRotateBitmap(path, bitmap);
            if (!(bitmap == null || rotatedBmp == null || bitmap.get() == rotatedBmp.get())) {
                ((Bitmap) bitmap.get()).recycle();
                bitmap.clear();
            }
            return cprsBmpByQuality(rotatedBmp, maxSize);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] cprsBmpByQuality(WeakReference<Bitmap> bmp, int maxSize) {
        if (bmp == null || bmp.get() == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ((Bitmap) bmp.get()).compress(CompressFormat.JPEG, 70, baos);
        ((Bitmap) bmp.get()).recycle();
        return baos.toByteArray();
    }

    private static WeakReference<Bitmap> cprsBmpBySize(String path, int rqsW, int rqsH) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return new WeakReference(BitmapFactory.decodeFile(path, options));
    }

    private static int caculateInSampleSize(Options options, int rqsW, int rqsH) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) {
            return 1;
        }
        if (height > rqsH || width > rqsW) {
            int heightRatio = Math.round(((float) height) / ((float) rqsH));
            int widthRatio = Math.round(((float) width) / ((float) rqsW));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        return inSampleSize;
    }

    public static int getDegress(String path) {
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
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

    public static WeakReference<Bitmap> rotateBitmap(WeakReference<Bitmap> bitmap, int degress) {
        if (bitmap == null || bitmap.get() == null) {
            return null;
        }
        Matrix m = new Matrix();
        m.postRotate((float) degress);
        return new WeakReference(Bitmap.createBitmap((Bitmap) bitmap.get(), 0, 0, ((Bitmap)
                bitmap.get()).getWidth(), ((Bitmap) bitmap.get()).getHeight(), m, true));
    }

    public static WeakReference<Bitmap> autoRotateBitmap(String path, WeakReference<Bitmap> bmp) {
        if (TextUtils.isEmpty(path) || bmp == null || bmp.get() == null) {
            return bmp;
        }
        int degree = getDegress(path);
        if (degree != 0) {
            bmp = rotateBitmap(bmp, degree);
        }
        return bmp;
    }
}
