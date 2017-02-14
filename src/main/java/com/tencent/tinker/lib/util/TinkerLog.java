package com.tencent.tinker.lib.util;

import android.util.Log;

public class TinkerLog {
    private static final String       TAG          = "Tinker.TinkerLog";
    private static       TinkerLogImp debugLog     = new TinkerLogImp() {
        public void v(String tag, String msg, Object... obj) {
            Log.v(tag, obj == null ? msg : String.format(msg, obj));
        }

        public void i(String tag, String msg, Object... obj) {
            Log.i(tag, obj == null ? msg : String.format(msg, obj));
        }

        public void d(String tag, String msg, Object... obj) {
            Log.d(tag, obj == null ? msg : String.format(msg, obj));
        }

        public void w(String tag, String msg, Object... obj) {
            Log.w(tag, obj == null ? msg : String.format(msg, obj));
        }

        public void e(String tag, String msg, Object... obj) {
            Log.e(tag, obj == null ? msg : String.format(msg, obj));
        }

        public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {
            String log = obj == null ? format : String.format(format, obj);
            if (log == null) {
                log = "";
            }
            Log.e(tag, log + "  " + Log.getStackTraceString(tr));
        }
    };
    private static       TinkerLogImp tinkerLogImp = debugLog;

    public interface TinkerLogImp {
        void d(String str, String str2, Object... objArr);

        void e(String str, String str2, Object... objArr);

        void i(String str, String str2, Object... objArr);

        void printErrStackTrace(String str, Throwable th, String str2, Object... objArr);

        void v(String str, String str2, Object... objArr);

        void w(String str, String str2, Object... objArr);
    }

    public static void setTinkerLogImp(TinkerLogImp imp) {
        tinkerLogImp = imp;
    }

    public static TinkerLogImp getImpl() {
        return tinkerLogImp;
    }

    public static void v(String tag, String msg, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.v(tag, msg, obj);
        }
    }

    public static void e(String tag, String msg, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.e(tag, msg, obj);
        }
    }

    public static void w(String tag, String msg, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.w(tag, msg, obj);
        }
    }

    public static void i(String tag, String msg, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.i(tag, msg, obj);
        }
    }

    public static void d(String tag, String msg, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.d(tag, msg, obj);
        }
    }

    public static void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {
        if (tinkerLogImp != null) {
            tinkerLogImp.printErrStackTrace(tag, tr, format, obj);
        }
    }
}
