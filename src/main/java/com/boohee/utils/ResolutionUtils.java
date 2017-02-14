package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

public class ResolutionUtils {
    static final String TAG = ResolutionUtils.class.getSimpleName();

    public static int getHeight(Context context, String height, String width) {
        return getHeight(context, Integer.parseInt(width), Integer.parseInt(height));
    }

    public static int getHeight(Context context, int width, int height) {
        return getHeight(context, getScreenWidth(context), width, height);
    }

    public static int getHeight(Context context, int originWidth, int width, int height) {
        return (height * originWidth) / width;
    }

    public static int getWidthBasedHeight(int width, int height, int baseHieght) {
        return (width / height) * baseHieght;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        float width = (float) bgimage.getWidth();
        float height = (float) bgimage.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / width, ((float) newHeight) / height);
        return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static Bitmap getViewBitmap(View v) {
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static Bitmap cutBitmap(Bitmap bmp, int cut, int pos) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        if (pos == 0) {
            return Bitmap.createBitmap(bmp, 0, cut, width, height - cut, new Matrix(), true);
        }
        return Bitmap.createBitmap(bmp, 0, 0, width, height - cut, new Matrix(), true);
    }
}
