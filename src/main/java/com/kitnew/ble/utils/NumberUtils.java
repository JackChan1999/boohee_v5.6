package com.kitnew.ble.utils;

import java.math.BigDecimal;

public class NumberUtils {
    public static float getOnePrecision(float f) {
        return new BigDecimal(String.valueOf(f)).setScale(1, 4).floatValue();
    }

    public static float getTwoPrecision(float d) {
        return new BigDecimal(String.valueOf(d)).setScale(2, 4).floatValue();
    }
}
