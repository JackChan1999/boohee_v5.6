package com.alipay.security.mobile.module.commonutils;

import android.content.Context;

public class Dbg {
    public static final String TAG = "SecurityComponent";

    public static boolean getDebugStatus(Context context) {
        return false;
    }

    public static void log(String str) {
        log(TAG, str);
    }

    public static void log(String str, String str2) {
    }
}
