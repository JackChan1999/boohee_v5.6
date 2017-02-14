package com.zxinsight.common.util;

import android.util.Log;

public class c {
    static String a;
    static String b;
    static int    c;
    static boolean d = true;

    public static boolean a() {
        return d && m.a().w().booleanValue();
    }

    private static String g(String str) {
        return "[" + a + ":" + b + ":" + c + "]" + str;
    }

    private static void a(StackTraceElement[] stackTraceElementArr) {
        a = stackTraceElementArr[1].getFileName();
        b = stackTraceElementArr[1].getMethodName();
        c = stackTraceElementArr[1].getLineNumber();
    }

    public static void a(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.e("MWSDKDebug", g(str));
        }
    }

    public static void b(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.i("MWSDKDebug", g(str));
        }
    }

    public static void c(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.d("MWSDKDebug", g(str));
        }
    }

    public static void d(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.v("MWSDKDebug", g(str));
        }
    }

    public static void e(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.i("MWSDKIntegrationDebug", a + g(str));
        }
    }

    public static void f(String str) {
        if (a()) {
            a(new Throwable().getStackTrace());
            Log.w("MWSDKIntegrationTest", a + g(str));
        }
    }
}
