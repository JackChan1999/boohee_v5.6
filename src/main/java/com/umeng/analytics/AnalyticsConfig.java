package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;

import u.aly.bt;
import u.aly.bv;

public class AnalyticsConfig {
    public static  boolean  ACTIVITY_DURATION_OPEN = true;
    public static  boolean  CATCH_EXCEPTION        = true;
    public static  boolean  COMPRESS_DATA          = true;
    public static  boolean  ENABLE_MEMORY_BUFFER   = true;
    public static  String   GPU_RENDERER           = "";
    public static  String   GPU_VENDER             = "";
    private static String   a                      = null;
    private static String   b                      = null;
    private static double[] c                      = null;
    public static  long     kContinueSessionMillis = 30000;
    public static int mVerticalType;
    public static String  mWrapperType    = null;
    public static String  mWrapperVersion = null;
    public static boolean sEncrypt        = false;
    public static int sLatentWindow;

    public static void enableEncrypt(boolean z) {
        sEncrypt = z;
    }

    public static void setAppkey(Context context, String str) {
        if (context == null) {
            a = str;
            return;
        }
        String p = bt.p(context);
        if (TextUtils.isEmpty(p)) {
            Object c = h.a(context).c();
            if (TextUtils.isEmpty(c)) {
                h.a(context).a(str);
            } else if (!c.equals(str)) {
                bv.d("Appkey和上次配置的不一致 ");
                h.a(context).a(str);
            }
            a = str;
            return;
        }
        a = p;
        if (!p.equals(str)) {
            bv.d("Appkey和AndroidManifest.xml中配置的不一致 ");
        }
    }

    public static void setChannel(String str) {
        b = str;
    }

    public static String getAppkey(Context context) {
        if (TextUtils.isEmpty(a)) {
            a = bt.p(context);
            if (TextUtils.isEmpty(a)) {
                a = h.a(context).c();
            }
        }
        return a;
    }

    public static String getChannel(Context context) {
        if (TextUtils.isEmpty(b)) {
            b = bt.u(context);
        }
        return b;
    }

    public static String getSDKVersion() {
        if (mVerticalType == 1) {
            return a.d;
        }
        return a.c;
    }

    public static double[] getLocation() {
        return c;
    }

    public static void setLocation(double d, double d2) {
        if (c == null) {
            c = new double[2];
        }
        c[0] = d;
        c[1] = d2;
    }

    public static void setLatencyWindow(long j) {
        sLatentWindow = ((int) j) * 1000;
    }
}
