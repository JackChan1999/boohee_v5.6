package com.boohee.utility;

import android.graphics.Color;
import android.support.annotation.ColorRes;

public class ColorUtils {
    private static int FIRST_COLOR  = Color.parseColor("#ff3300");
    private static int SECOND_COLOR = Color.parseColor("#ffd900");
    private static int THIRD_COLOR  = Color.parseColor("#44cef6");

    public static int getColor(float p) {
        int c0;
        int c1;
        if (p <= 0.5f) {
            p *= 2.0f;
            c0 = FIRST_COLOR;
            c1 = SECOND_COLOR;
        } else {
            p = (p - 0.5f) * 2.0f;
            c0 = SECOND_COLOR;
            c1 = THIRD_COLOR;
        }
        return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red
                (c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color
                .blue(c1), p));
    }

    private static int ave(int src, int dst, float p) {
        return Math.round(((float) (dst - src)) * p) + src;
    }

    public static int getColorWithAlpha(@ColorRes int color, float ratio) {
        if (ratio > 1.0f) {
            ratio = 1.0f;
        } else if (ratio < 0.0f) {
            ratio = 0.0f;
        }
        return Color.argb(Math.round(((float) Color.alpha(color)) * ratio), Color.red(color),
                Color.green(color), Color.blue(color));
    }
}
