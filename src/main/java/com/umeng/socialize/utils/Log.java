package com.umeng.socialize.utils;

public class Log {
    public static final  boolean ENCRYPT_LOG = false;
    public static        boolean LOG         = true;
    private static final String  TAG         = "umengsocial";

    public static void i(String str, String str2) {
        if (LOG) {
            android.util.Log.i(str, str2);
        }
    }

    public static void i(String str, String str2, Exception exception) {
        if (LOG) {
            android.util.Log.i(str, exception.toString() + ":  [" + str2 + "]");
        }
    }

    public static void e(String str, String str2) {
        android.util.Log.e(str, str2);
    }

    public static void e(String str, String str2, Exception exception) {
        android.util.Log.e(str, exception.toString() + ":  [" + str2 + "]");
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            android.util.Log.e(str, "        at\t " + stackTraceElement.toString());
        }
    }

    public static void d(String str, String str2) {
        if (LOG) {
            android.util.Log.d(str, str2);
        }
    }

    public static void d(String str, String str2, Exception exception) {
        if (LOG) {
            android.util.Log.d(str, exception.toString() + ":  [" + str2 + "]");
        }
    }

    public static void v(String str, String str2) {
        if (LOG) {
            android.util.Log.v(str, str2);
        }
    }

    public static void v(String str, String str2, Exception exception) {
        if (LOG) {
            android.util.Log.v(str, exception.toString() + ":  [" + str2 + "]");
        }
    }

    public static void w(String str, String str2) {
        if (LOG) {
            android.util.Log.w(str, str2);
        }
    }

    public static void w(String str, String str2, Exception exception) {
        if (LOG) {
            android.util.Log.w(str, exception.toString() + ":  [" + str2 + "]");
            for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                android.util.Log.w(str, "        at\t " + stackTraceElement.toString());
            }
        }
    }

    public static void i(String str) {
        if (LOG) {
            android.util.Log.i(TAG, str);
        }
    }

    public static void e(String str) {
        android.util.Log.e(TAG, str);
    }

    public static void d(String str) {
        if (LOG) {
            android.util.Log.d(TAG, str);
        }
    }

    public static void v(String str) {
        if (LOG) {
            android.util.Log.v(TAG, str);
        }
    }

    public static void w(String str) {
        if (LOG) {
            android.util.Log.w(TAG, str);
        }
    }
}
