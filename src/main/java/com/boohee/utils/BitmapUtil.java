package com.boohee.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.boohee.utility.DensityUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BitmapUtil {
    public static final int    MAX_HEIGHT = 1600;
    public static final int    MAX_WIDTH  = 800;
    static final        String TAG        = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {
    }

    public static Bitmap getResizedBitmapWithPath(Context ctx, String filePath) {
        return getResizedBitmapWithPath(ctx, filePath, 800, MAX_HEIGHT);
    }

    public static Bitmap getResizedBitmapWithPath(Context ctx, String filePath, int reqWidth, int
            reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getResizedBitmapWithUri(Context ctx, Uri uri) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        MediaStoreUtils.getBitmapFromUri(ctx, uri, options);
        options.inSampleSize = calculateInSampleSize(options, 800, MAX_HEIGHT);
        options.inJustDecodeBounds = false;
        return MediaStoreUtils.getBitmapFromUri(ctx, uri, options);
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            inSampleSize = 2;
            while (true) {
                if (halfHeight / inSampleSize <= reqHeight && halfWidth / inSampleSize <=
                        reqWidth) {
                    break;
                }
                inSampleSize *= 2;
            }
        }
        Helper.showLog("Bitmap Util:", inSampleSize + "");
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, float ratio) {
        if (ratio > 1.0f || ratio < 0.0f) {
            ratio = 1.0f;
        }
        int reqWidth = (int) (((float) res.getDisplayMetrics().widthPixels) * ratio);
        int reqHeight = (int) (((float) res.getDisplayMetrics().heightPixels) * ratio);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap getBitmapWithUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        if (!(uri == null || context == null)) {
            bitmap = null;
            new Options().inJustDecodeBounds = true;
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream
                        (uri), null, getBitmapOptions(context, uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Options getBitmapOptions(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        Options opt = new Options();
        opt.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opt);
        } catch (FileNotFoundException e) {
        }
        opt.inSampleSize = calculateInSampleSize(opt, 800, MAX_HEIGHT);
        opt.inJustDecodeBounds = false;
        return opt;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 70, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public static Uri newImageUri(Context context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        Bitmap bmp = getBitmapWithUri(context, uri);
        try {
            String fileName = context.getExternalCacheDir().toString() + File.separatorChar +
                    System.currentTimeMillis() + ".png";
            saveBitmap(fileName, bmp);
            return Uri.fromFile(new File(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @SuppressLint({"NewApi"})
    public static Bitmap takeScreenShot(Activity pActivity) {
        View view = pActivity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
        Point size = new Point();
        Display display = pActivity.getWindowManager().getDefaultDisplay();
        if (VERSION.SDK_INT < 13) {
            size.set(display.getWidth(), display.getHeight());
        } else {
            pActivity.getWindowManager().getDefaultDisplay().getSize(size);
        }
        return Bitmap.createBitmap(bitmap, 0, stautsHeight, size.x, size.y - stautsHeight);
    }

    @SuppressLint({"NewApi"})
    public static Bitmap doBlur(Context context, Bitmap bitmap) {
        bitmap = Blur.fastblur(context, zoomBitmap(bitmap, bitmap.getWidth() / 6, bitmap
                .getHeight() / 6), 8);
        return zoomBitmap(bitmap, bitmap.getWidth() * 6, bitmap.getHeight() * 6);
    }

    public static Bitmap viewToBitmap(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate((float) (-v.getScrollX()), (float) (-v.getScrollY()));
        v.draw(c);
        return screenshot;
    }

    public static void saveBitmap(String fileName, Bitmap bmp) {
        Exception e;
        Throwable th;
        FileOutputStream out = null;
        try {
            FileOutputStream out2 = new FileOutputStream(fileName);
            try {
                bmp.compress(CompressFormat.PNG, 70, out2);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (Throwable th2) {
                        out = out2;
                        return;
                    }
                }
                out = out2;
            } catch (Exception e2) {
                e = e2;
                out = out2;
                try {
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable th3) {
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable th5) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th6) {
                th = th6;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (out != null) {
                out.close();
            }
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() > 0) {
            return null;
        }
        v.measure(MeasureSpec.makeMeasureSpec((int) DensityUtil.getScreenWidth(v.getContext()),
                1073741824), MeasureSpec.makeMeasureSpec(0, 0));
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Config
                .ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Config.ARGB_8888);
        scrollView.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static Bitmap getBitmapByView(LinearLayout linearLayout) {
        int h = 0;
        int i = 0;
        while (i < linearLayout.getChildCount()) {
            try {
                if (linearLayout.getChildAt(i).getVisibility() == 0) {
                    h += linearLayout.getChildAt(i).getHeight();
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(), h, Config.ARGB_8888);
        linearLayout.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, id)).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
