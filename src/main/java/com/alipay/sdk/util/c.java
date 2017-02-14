package com.alipay.sdk.util;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class c {
    public static final String a = "mspstd";
    private static final boolean b = false;

    private static void a() {
    }

    private static void b() {
    }

    private static void a(String str) {
        Log.d(a, str);
    }

    private static void c() {
    }

    private static void d() {
    }

    private static void e() {
    }

    private static void f() {
    }

    private static void a(Object obj) {
        if (!(obj instanceof Exception)) {
        }
    }

    private static void g() {
    }

    private static String a(Throwable th) {
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
