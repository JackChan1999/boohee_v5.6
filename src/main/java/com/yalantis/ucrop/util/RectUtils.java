package com.yalantis.ucrop.util;

import android.graphics.RectF;

import com.boohee.widgets.PathListView;

public class RectUtils {
    public static float[] getCornersFromRect(RectF r) {
        return new float[]{r.left, r.top, r.right, r.top, r.right, r.bottom, r.left, r.bottom};
    }

    public static float[] getRectSidesFromCorners(float[] corners) {
        return new float[]{(float) Math.sqrt(Math.pow((double) (corners[0] - corners[2]),
                PathListView.ZOOM_X2) + Math.pow((double) (corners[1] - corners[3]), PathListView
                .ZOOM_X2)), (float) Math.sqrt(Math.pow((double) (corners[2] - corners[4]),
                PathListView.ZOOM_X2) + Math.pow((double) (corners[3] - corners[5]), PathListView
                .ZOOM_X2))};
    }

    public static float[] getCenterFromRect(RectF r) {
        return new float[]{r.centerX(), r.centerY()};
    }

    public static RectF trapToRect(float[] array) {
        RectF r = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float
                .NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (int i = 1; i < array.length; i += 2) {
            float x = array[i - 1];
            float y = array[i];
            r.left = x < r.left ? x : r.left;
            r.top = y < r.top ? y : r.top;
            if (x <= r.right) {
                x = r.right;
            }
            r.right = x;
            if (y <= r.bottom) {
                y = r.bottom;
            }
            r.bottom = y;
        }
        r.sort();
        return r;
    }
}
