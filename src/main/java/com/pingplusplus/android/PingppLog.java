package com.pingplusplus.android;

import android.util.Log;

public class PingppLog {
    public static  boolean DEBUG = false;
    private static String  a     = "PING++";
    private static boolean b     = false;
    private static String  c     = "pingpp_debug";

    public static void a(Exception exception) {
        if (b) {
            if (exception != null) {
                exception.printStackTrace();
            }
        } else if (DEBUG && exception != null && exception.getMessage() != null) {
            Log.d(a, exception.getMessage());
        }
    }

    public static void a(String str) {
        if (b) {
            String str2 = c;
            if (str == null) {
                str = "null";
            }
            Log.d(str2, str);
        }
    }

    public static void d(String str) {
        if (DEBUG) {
            String str2 = a;
            if (str == null) {
                str = "null";
            }
            Log.d(str2, str);
        }
    }
}
