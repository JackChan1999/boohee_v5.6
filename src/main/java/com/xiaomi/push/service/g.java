package com.xiaomi.push.service;

import android.content.Context;

import com.xiaomi.push.service.w.b;

import java.util.Locale;

public class g {
    public final    String a;
    protected final String b;
    protected final String c;
    protected final String d;
    protected final String e;
    protected final String f;
    protected final int    g;

    public g(String str, String str2, String str3, String str4, String str5, String str6, int i) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
        this.g = i;
    }

    private static boolean a(Context context) {
        return context.getPackageName().equals("com.xiaomi.xmsf");
    }

    public b a(XMPushService xMPushService) {
        b bVar = new b(xMPushService);
        bVar.a = xMPushService.getPackageName();
        bVar.b = this.a;
        bVar.i = this.c;
        bVar.c = this.b;
        bVar.h = "5";
        bVar.d = "XMPUSH-PASS";
        bVar.e = false;
        bVar.f = "sdk_ver:8";
        String str = a((Context) xMPushService) ? "1000271" : this.d;
        bVar.g = String.format("%1$s:%2$s,%3$s:%4$s", new Object[]{"appid", str, "locale", Locale
                .getDefault().toString()});
        bVar.k = xMPushService.d();
        return bVar;
    }
}
