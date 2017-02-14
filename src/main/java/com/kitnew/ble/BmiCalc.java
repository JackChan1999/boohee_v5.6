package com.kitnew.ble;

import com.boohee.myview.IntFloatWheelView;
import com.kitnew.ble.utils.NumberUtils;

public class BmiCalc {
    public static float getScore(float weight, float height) {
        float bmi = getBmi(weight, height);
        if (bmi == 22.0f) {
            return 100.0f;
        }
        if (bmi > 22.0f) {
            if (bmi >= 35.0f) {
                return IntFloatWheelView.DEFAULT_VALUE;
            }
            return Calc.calcScore(22.0f, bmi, 35.0f);
        } else if (bmi > 15.0f && bmi < 22.0f) {
            return Calc.calcScore(22.0f, bmi, 9.0f);
        } else {
            if (bmi >= 10.0f) {
                return 40.0f;
            }
            if (bmi >= 5.0f) {
                return 30.0f;
            }
            if (bmi >= 0.0f) {
                return 20.0f;
            }
            return 0.0f;
        }
    }

    public static float getBmi(float weight, float height) {
        height /= 100.0f;
        try {
            return NumberUtils.getOnePrecision(weight / (height * height));
        } catch (Exception e) {
            return 0.0f;
        }
    }
}
