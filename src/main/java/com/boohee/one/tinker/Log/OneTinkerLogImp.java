package com.boohee.one.tinker.Log;

import android.util.Log;

import com.tencent.tinker.lib.util.TinkerLog.TinkerLogImp;

public class OneTinkerLogImp implements TinkerLogImp {
    public static final  int    LEVEL_DEBUG   = 1;
    public static final  int    LEVEL_ERROR   = 4;
    public static final  int    LEVEL_INFO    = 2;
    public static final  int    LEVEL_NONE    = 5;
    public static final  int    LEVEL_VERBOSE = 0;
    public static final  int    LEVEL_WARNING = 3;
    private static final String TAG           = "Tinker.MyLogImp";
    private static       int    level         = 0;

    public static int getLogLevel() {
        return level;
    }

    public static void setLevel(int level) {
        level = level;
        Log.w(TAG, "new log level: " + level);
    }

    public void v(String s, String s1, Object... objects) {
        if (level <= 0) {
            Log.v(s, objects == null ? s1 : String.format(s1, objects));
        }
    }

    public void i(String s, String s1, Object... objects) {
        if (level <= 2) {
            Log.i(s, objects == null ? s1 : String.format(s1, objects));
        }
    }

    public void w(String s, String s1, Object... objects) {
        if (level <= 3) {
            Log.w(s, objects == null ? s1 : String.format(s1, objects));
        }
    }

    public void d(String s, String s1, Object... objects) {
        if (level <= 1) {
            Log.d(s, objects == null ? s1 : String.format(s1, objects));
        }
    }

    public void e(String s, String s1, Object... objects) {
        if (level <= 4) {
            Log.e(s, objects == null ? s1 : String.format(s1, objects));
        }
    }

    public void printErrStackTrace(String s, Throwable throwable, String s1, Object... objects) {
        String log = objects == null ? s1 : String.format(s1, objects);
        if (log == null) {
            log = "";
        }
        Log.e(s, log + "  " + Log.getStackTraceString(throwable));
    }
}
