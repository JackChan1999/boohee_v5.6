package com.kitnew.ble;

import com.boohee.myview.IntFloatWheelView;

public class WeightCalc {
    public static float getStand(int gender, float height) {
        if (gender == 0) {
            return ((1.37f * height) - 110.0f) * 0.45f;
        }
        return (height - 80.0f) * 0.7f;
    }

    public static float getScore(float weight, int gender, int height) {
        float standWeight = getStand(gender, (float) height);
        float min = standWeight * 0.7f;
        float max = standWeight * 1.3f;
        if (weight == standWeight) {
            return 100.0f;
        }
        if (weight > standWeight) {
            if (weight > max) {
                return IntFloatWheelView.DEFAULT_VALUE;
            }
            return Calc.calcScore(standWeight, weight, max);
        } else if (weight >= standWeight) {
            return 0.0f;
        } else {
            if (weight > min) {
                return Calc.calcScore(standWeight, weight, min);
            }
            if (((double) weight) >= ((double) standWeight) * 0.6d) {
                return IntFloatWheelView.DEFAULT_VALUE;
            }
            if (((double) weight) >= ((double) standWeight) * 0.5d) {
                return 40.0f;
            }
            if (((double) weight) >= ((double) standWeight) * 0.4d) {
                return 30.0f;
            }
            if (((double) weight) >= ((double) standWeight) * 0.3d) {
                return 20.0f;
            }
            if (((double) weight) >= ((double) standWeight) * 0.0d) {
                return 10.0f;
            }
            return 0.0f;
        }
    }
}
