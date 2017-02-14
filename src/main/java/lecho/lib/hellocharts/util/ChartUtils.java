package lecho.lib.hellocharts.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;

public abstract class ChartUtils {
    public static final int[] COLORS = new int[]{COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED};
    public static final int COLOR_BACKGROUND = Color.parseColor("#F7F7F0");
    public static final int COLOR_BLUE = Color.parseColor("#33B5E5");
    public static final int COLOR_GREEN = Color.parseColor("#99CC00");
    private static int COLOR_INDEX = 0;
    public static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
    public static final int COLOR_RED = Color.parseColor("#FF4444");
    public static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
    private static final float DARKEN_INTENSITY = 0.9f;
    private static final float DARKEN_SATURATION = 1.1f;
    public static final int DEFAULT_AREA_COLOR = Color.parseColor("#1962CC74");
    public static final int DEFAULT_COLOR = Color.parseColor("#00aef0");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int DEFAULT_TARGET_COLOR = Color.parseColor("#62CC74");

    public static final int pickColor() {
        return COLORS[(int) Math.round(Math.random() * ((double) (COLORS.length - 1)))];
    }

    public static final int nextColor() {
        if (COLOR_INDEX >= COLORS.length) {
            COLOR_INDEX = 0;
        }
        int[] iArr = COLORS;
        int i = COLOR_INDEX;
        COLOR_INDEX = i + 1;
        return iArr[i];
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) ((((float) dp) * density) + 0.5f);
    }

    public static int dp2px(float density, float dp) {
        if (dp == 0.0f) {
            return 1;
        }
        return (int) ((dp * density) + 0.5f);
    }

    public static int px2dp(float density, int px) {
        return (int) Math.ceil((double) (((float) px) / density));
    }

    public static int sp2px(float scaledDensity, int sp) {
        if (sp == 0) {
            return 0;
        }
        return (int) ((((float) sp) * scaledDensity) + 0.5f);
    }

    public static int px2sp(float scaledDensity, int px) {
        return (int) Math.ceil((double) (((float) px) / scaledDensity));
    }

    public static int mm2px(Context context, int mm) {
        return (int) (TypedValue.applyDimension(5, (float) mm, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int darkenColor(int color) {
        hsv = new float[3];
        int alpha = Color.alpha(color);
        Color.colorToHSV(color, hsv);
        hsv[1] = Math.min(hsv[1] * DARKEN_SATURATION, 1.0f);
        hsv[2] = hsv[2] * DARKEN_INTENSITY;
        int tempColor = Color.HSVToColor(hsv);
        return Color.argb(alpha, Color.red(tempColor), Color.green(tempColor), Color.blue(tempColor));
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
