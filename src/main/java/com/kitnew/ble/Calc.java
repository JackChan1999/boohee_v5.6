package com.kitnew.ble;

import com.boohee.myview.IntFloatWheelView;
import com.kitnew.ble.utils.NumberUtils;

public class Calc {
    public static float calcScore(float standValue, float value, float offsetValue) {
        return 100.0f - ((IntFloatWheelView.DEFAULT_VALUE / Math.abs(standValue - offsetValue)) *
                Math.abs(standValue - value));
    }

    public static float calcAvgValue(float... values) {
        float sum = 0.0f;
        for (float f : values) {
            sum += f;
        }
        return sum / ((float) values.length);
    }

    public static float calcMaxValue(float... values) {
        float max = -1.0f;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
            }
        }
        return max;
    }

    public static float calcRank(float left, float total) {
        return NumberUtils.getOnePrecision((left * 100.0f) / total);
    }
}
