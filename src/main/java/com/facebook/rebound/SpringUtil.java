package com.facebook.rebound;

public class SpringUtil {
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh,
                                                  double toLow, double toHigh) {
        return (((value - fromLow) / (fromHigh - fromLow)) * (toHigh - toLow)) + toLow;
    }

    public static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }
}
