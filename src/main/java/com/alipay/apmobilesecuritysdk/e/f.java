package com.alipay.apmobilesecuritysdk.e;

import android.content.Context;
import com.alipay.security.mobile.module.commonutils.Dbg;
import com.alipay.security.mobile.module.commonutils.LOG;
import com.umeng.analytics.a;

public final class f {
    private static String a = "";
    private static String b = "";

    public static synchronized String a() {
        String str;
        synchronized (f.class) {
            str = a;
        }
        return str;
    }

    public static synchronized void a(b bVar) {
        synchronized (f.class) {
            if (bVar != null) {
                a = bVar.a();
                b = bVar.c();
                Dbg.log("Update Token Storage. apdid = " + a + ", token = " + b);
            }
        }
    }

    public static synchronized boolean a(Context context) {
        boolean z;
        synchronized (f.class) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                long b = e.b(context);
                Dbg.log("[*]validTime=" + b);
                Dbg.log("[*]Now      =" + currentTimeMillis);
                if (Math.abs(currentTimeMillis - b) < a.h) {
                    z = true;
                }
            } catch (Throwable th) {
                LOG.logException(th);
            }
            z = false;
        }
        return z;
    }

    public static synchronized String b() {
        String str;
        synchronized (f.class) {
            str = b;
        }
        return str;
    }
}
