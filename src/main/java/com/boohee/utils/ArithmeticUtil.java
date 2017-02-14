package com.boohee.utils;

import java.math.BigDecimal;

public class ArithmeticUtil {
    public static float round(float v, int scale) {
        if (scale >= 0) {
            return new BigDecimal((double) v).divide(new BigDecimal("1"), scale, 4).floatValue();
        }
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public static float add(float v1, float v2) {
        return new BigDecimal((double) v1).add(new BigDecimal((double) v2)).floatValue();
    }

    public static float addWithScale(float v1, float v2, int scale) {
        return new BigDecimal((double) v1).add(new BigDecimal((double) v2)).setScale(scale, 4)
                .floatValue();
    }

    public static float sub(float v1, float v2) {
        return new BigDecimal((double) v1).subtract(new BigDecimal((double) v2)).floatValue();
    }

    public static float mul(float v1, float v2) {
        return mulWithScale(v1, v2, 1);
    }

    public static float mulWithScale(float v1, float v2, int scale) {
        return round(new BigDecimal((double) v1).multiply(new BigDecimal((double) v2)).floatValue
                (), scale);
    }

    public static float div(float v1, float v2, int scale) {
        if (scale >= 0) {
            return new BigDecimal((double) v1).divide(new BigDecimal((double) v2), scale, 4)
                    .floatValue();
        }
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public static int remainder(float v1, float v2) {
        return Math.round(v1 * 100.0f) % Math.round(100.0f * v2);
    }

    public static boolean strCompareTo(String v1, String v2) {
        if (new BigDecimal(v1).compareTo(new BigDecimal(v2)) > 0) {
            return true;
        }
        return false;
    }

    public static float safeParseFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public static double roundOff(double num, int digit) {
        double multi = Math.pow(10.0d, (double) digit);
        return ((double) Math.round(num * multi)) / multi;
    }
}
