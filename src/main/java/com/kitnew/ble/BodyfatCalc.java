package com.kitnew.ble;

import com.boohee.myview.IntFloatWheelView;

public class BodyfatCalc {
    public static float getScore(float bodyfat, int gender) {
        float stand;
        if (gender == 1) {
            stand = 16.0f;
        } else {
            stand = 26.0f;
        }
        if (bodyfat == stand) {
            return 100.0f;
        }
        if (bodyfat > stand) {
            if (bodyfat > 45.0f) {
                return IntFloatWheelView.DEFAULT_VALUE;
            }
            return Calc.calcScore(stand, bodyfat, 45.0f);
        } else if (bodyfat > 5.0f) {
            return Calc.calcScore(stand, bodyfat, 5.0f);
        } else {
            if (0.0f >= bodyfat || bodyfat > 5.0f) {
                return 0.0f;
            }
            return 10.0f;
        }
    }
}
