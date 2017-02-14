package com.xiaomi.stats;

import android.support.v4.view.ViewCompat;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.w;
import com.xiaomi.push.thrift.b;
import com.xiaomi.xmpush.thrift.u;

import java.util.Hashtable;

public class g {
    private static final int a = com.xiaomi.push.thrift.a.PING_RTT.a();

    static class a {
        static Hashtable<Integer, Long> a = new Hashtable();
    }

    public static void a() {
        a(0, a);
    }

    public static void a(int i) {
        b f = e.a().f();
        f.a(com.xiaomi.push.thrift.a.CHANNEL_STATS_COUNTER.a());
        f.c(i);
        e.a().a(f);
    }

    public static synchronized void a(int i, int i2) {
        synchronized (g.class) {
            if (i2 < ViewCompat.MEASURED_SIZE_MASK) {
                a.a.put(Integer.valueOf((i << 24) | i2), Long.valueOf(System.currentTimeMillis()));
            } else {
                com.xiaomi.channel.commonutils.logger.b.d("stats key should less than 16777215");
            }
        }
    }

    public static void a(int i, int i2, int i3, String str, int i4) {
        b f = e.a().f();
        f.a((byte) i);
        f.a(i2);
        f.b(i3);
        f.b(str);
        f.c(i4);
        e.a().a(f);
    }

    public static synchronized void a(int i, int i2, String str, int i3) {
        synchronized (g.class) {
            long currentTimeMillis = System.currentTimeMillis();
            int i4 = (i << 24) | i2;
            if (a.a.containsKey(Integer.valueOf(i4))) {
                b f = e.a().f();
                f.a(i2);
                f.b((int) (currentTimeMillis - ((Long) a.a.get(Integer.valueOf(i4))).longValue()));
                f.b(str);
                if (i3 > -1) {
                    f.c(i3);
                }
                e.a().a(f);
                a.a.remove(Integer.valueOf(i2));
            } else {
                com.xiaomi.channel.commonutils.logger.b.d("stats key not found");
            }
        }
    }

    public static void a(XMPushService xMPushService, w.b bVar) {
        new a(xMPushService, bVar).a();
    }

    public static void a(String str, int i, Exception exception) {
        b f = e.a().f();
        if (i > 0) {
            f.a(com.xiaomi.push.thrift.a.GSLB_REQUEST_SUCCESS.a());
            f.b(str);
            f.b(i);
            e.a().a(f);
            return;
        }
        try {
            a a = c.a(exception);
            f.a(a.a.a());
            f.c(a.b);
            f.b(str);
            e.a().a(f);
        } catch (NullPointerException e) {
        }
    }

    public static void a(String str, Exception exception) {
        try {
            a b = c.b(exception);
            b f = e.a().f();
            f.a(b.a.a());
            f.c(b.b);
            f.b(str);
            e.a().a(f);
        } catch (NullPointerException e) {
        }
    }

    public static void b() {
        a(0, a, null, -1);
    }

    public static void b(String str, Exception exception) {
        try {
            a d = c.d(exception);
            b f = e.a().f();
            f.a(d.a.a());
            f.c(d.b);
            f.b(str);
            e.a().a(f);
        } catch (NullPointerException e) {
        }
    }

    public static String c() {
        org.apache.thrift.b e = e.a().e();
        if (e == null) {
            return null;
        }
        byte[] a = u.a(e);
        if (a == null) {
            return null;
        }
        String str = new String(com.xiaomi.channel.commonutils.string.a.a(a));
        com.xiaomi.channel.commonutils.logger.b.a("stat encoded size = " + str.length());
        com.xiaomi.channel.commonutils.logger.b.c(str);
        return str;
    }
}
