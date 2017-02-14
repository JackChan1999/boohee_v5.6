package com.tencent.mm.sdk.b;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Looper;
import android.os.Process;

public final class a {
    private static int level = 6;
    public static        d      q;
    private static       a      r;
    private static       a      s;
    private static final String t;

    public interface a {
        void e(String str, String str2);

        void f(String str, String str2);

        void g(String str, String str2);

        int h();

        void h(String str, String str2);
    }

    static {
        a bVar = new b();
        r = bVar;
        s = bVar;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VERSION.RELEASE:[" + VERSION.RELEASE);
        stringBuilder.append("] VERSION.CODENAME:[" + VERSION.CODENAME);
        stringBuilder.append("] VERSION.INCREMENTAL:[" + VERSION.INCREMENTAL);
        stringBuilder.append("] BOARD:[" + Build.BOARD);
        stringBuilder.append("] DEVICE:[" + Build.DEVICE);
        stringBuilder.append("] DISPLAY:[" + Build.DISPLAY);
        stringBuilder.append("] FINGERPRINT:[" + Build.FINGERPRINT);
        stringBuilder.append("] HOST:[" + Build.HOST);
        stringBuilder.append("] MANUFACTURER:[" + Build.MANUFACTURER);
        stringBuilder.append("] MODEL:[" + Build.MODEL);
        stringBuilder.append("] PRODUCT:[" + Build.PRODUCT);
        stringBuilder.append("] TAGS:[" + Build.TAGS);
        stringBuilder.append("] TYPE:[" + Build.TYPE);
        stringBuilder.append("] USER:[" + Build.USER + "]");
        t = stringBuilder.toString();
    }

    public static void a(String str, String str2) {
        a(str, str2, null);
    }

    public static void a(String str, String str2, Object... objArr) {
        if (s != null && s.h() <= 4) {
            String format = objArr == null ? str2 : String.format(str2, objArr);
            if (format == null) {
                format = "";
            }
            String i = i(str);
            a aVar = s;
            Process.myPid();
            Thread.currentThread().getId();
            Looper.getMainLooper().getThread().getId();
            aVar.h(i, format);
        }
    }

    public static void b(String str, String str2) {
        if (s != null && s.h() <= 3) {
            if (str2 == null) {
                str2 = "";
            }
            String i = i(str);
            a aVar = s;
            Process.myPid();
            Thread.currentThread().getId();
            Looper.getMainLooper().getThread().getId();
            aVar.g(i, str2);
        }
    }

    public static void c(String str, String str2) {
        if (s != null && s.h() <= 2) {
            if (str2 == null) {
                str2 = "";
            }
            String i = i(str);
            a aVar = s;
            Process.myPid();
            Thread.currentThread().getId();
            Looper.getMainLooper().getThread().getId();
            aVar.e(i, str2);
        }
    }

    public static void d(String str, String str2) {
        if (s != null && s.h() <= 1) {
            if (str2 == null) {
                str2 = "";
            }
            String i = i(str);
            a aVar = s;
            Process.myPid();
            Thread.currentThread().getId();
            Looper.getMainLooper().getThread().getId();
            aVar.f(i, str2);
        }
    }

    private static String i(String str) {
        return q != null ? q.i(str) : str;
    }
}
