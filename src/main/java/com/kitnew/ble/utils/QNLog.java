package com.kitnew.ble.utils;

import android.util.Log;

public class QNLog {
    public static boolean DEBUG = true;
    static final  String  TAG   = "QN-ble";

    public static void log(Object... messages) {
        if (DEBUG) {
            Log.d(TAG, getString(messages));
        }
    }

    public static void error(Object... messages) {
        if (messages.length < 2 || !(messages[messages.length - 1] instanceof Throwable)) {
            Log.e(TAG, getString(messages));
            return;
        }
        Throwable throwable = messages[messages.length - 1];
        Object[] transformed = new Object[(messages.length - 1)];
        System.arraycopy(messages, 0, transformed, 0, transformed.length);
        Log.e(TAG, getString(transformed), throwable);
    }

    public static String getString(Object[] messages) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : messages) {
            sb.append(obj);
            sb.append(' ');
        }
        return sb.toString();
    }
}
