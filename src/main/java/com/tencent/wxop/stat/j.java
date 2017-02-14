package com.tencent.wxop.stat;

final /* synthetic */ class j {
    static final /* synthetic */ int[] bL = new int[d.values().length];

    static {
        try {
            bL[d.INSTANT.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            bL[d.PERIOD.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            bL[d.APP_LAUNCH.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            bL[d.DEVELOPER.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            bL[d.BATCH.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        try {
            bL[d.ONLY_WIFI.ordinal()] = 6;
        } catch (NoSuchFieldError e6) {
        }
        try {
            bL[d.ONLY_WIFI_NO_CACHE.ordinal()] = 7;
        } catch (NoSuchFieldError e7) {
        }
    }
}
