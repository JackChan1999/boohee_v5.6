package com.boohee.utils;

import java.text.DecimalFormat;

public class NumberUtils {
    public static int safeParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float safeParseFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static String safeToString(DecimalFormat format, double number) {
        try {
            return format.format(number);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
