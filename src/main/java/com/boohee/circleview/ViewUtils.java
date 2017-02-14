package com.boohee.circleview;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class ViewUtils {
    public static float getCirclePathLength(float radius, float angle) {
        return (float) (((3.141592653589793d * ((double) radius)) * ((double) changeAngleToSingle
                (angle))) / 180.0d);
    }

    public static float changeAngleToSingle(float angle) {
        while (angle >= 360.0f) {
            angle -= 360.0f;
        }
        while (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil((double) widths[j]);
            }
        }
        return iRet;
    }

    public static int getTextHeight(Paint paint) {
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil((double) (fm.descent - fm.ascent));
    }
}
