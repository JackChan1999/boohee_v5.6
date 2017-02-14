package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Looper;

import com.boohee.utils.MiBandHelper;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.push.service.aa;
import com.xiaomi.push.service.y;
import com.xiaomi.xmpush.thrift.a;
import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.j;
import com.xiaomi.xmpush.thrift.q;
import com.xiaomi.xmpush.thrift.u;

import java.util.ArrayList;
import java.util.Iterator;

public class g {
    private static g b;
    private static final ArrayList<a> e = new ArrayList();
    private              boolean      a = false;
    private Context c;
    private String  d;
    private Intent  f = null;
    private Integer g = null;

    private g(Context context) {
        this.c = context.getApplicationContext();
        this.d = null;
        this.a = g();
    }

    public static g a(Context context) {
        if (b == null) {
            b = new g(context);
        }
        return b;
    }

    private boolean g() {
        try {
            PackageInfo packageInfo = this.c.getPackageManager().getPackageInfo("com.xiaomi" +
                    ".xmsf", 4);
            return packageInfo != null && packageInfo.versionCode >= 105;
        } catch (Exception e) {
            return false;
        }
    }

    private Intent h() {
        Intent intent = new Intent();
        String packageName = this.c.getPackageName();
        if (!b() || "com.xiaomi.xmsf".equals(packageName)) {
            k();
            intent.setComponent(new ComponentName(this.c, "com.xiaomi.push.service.XMPushService"));
            intent.putExtra("mipush_app_package", packageName);
        } else {
            intent.setPackage("com.xiaomi.xmsf");
            intent.setClassName("com.xiaomi.xmsf", i());
            intent.putExtra("mipush_app_package", packageName);
            j();
        }
        return intent;
    }

    private String i() {
        try {
            if (this.c.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4).versionCode >=
                    106) {
                return "com.xiaomi.push.service.XMPushService";
            }
        } catch (Exception e) {
        }
        return "com.xiaomi.xmsf.push.service.XMPushService";
    }

    private void j() {
        try {
            this.c.getPackageManager().setComponentEnabledSetting(new ComponentName(this.c, "com" +
                    ".xiaomi.push.service.XMPushService"), 2, 1);
        } catch (Throwable th) {
        }
    }

    private void k() {
        try {
            this.c.getPackageManager().setComponentEnabledSetting(new ComponentName(this.c, "com" +
                    ".xiaomi.push.service.XMPushService"), 1, 1);
        } catch (Throwable th) {
        }
    }

    private boolean l() {
        String packageName = this.c.getPackageName();
        return packageName.contains("miui") || packageName.contains(MiBandHelper.KEY_DATA_SOURCE)
                || (this.c.getApplicationInfo().flags & 1) != 0;
    }

    public void a() {
        this.c.startService(h());
    }

    public void a(int i) {
        Intent h = h();
        h.setAction("com.xiaomi.mipush.CLEAR_NOTIFICATION");
        h.putExtra(y.y, this.c.getPackageName());
        h.putExtra(y.z, i);
        this.c.startService(h);
    }

    public final void a(j jVar, boolean z) {
        this.f = null;
        Intent h = h();
        byte[] a = u.a(e.a(this.c, jVar, a.Registration));
        if (a == null) {
            b.a("register fail, because msgBytes is null.");
            return;
        }
        h.setAction("com.xiaomi.mipush.REGISTER_APP");
        h.putExtra("mipush_app_id", a.a(this.c).c());
        h.putExtra("mipush_payload", a);
        h.putExtra("mipush_session", this.d);
        h.putExtra("mipush_env_chanage", z);
        h.putExtra("mipush_env_type", a.a(this.c).m());
        if (d.d(this.c) && f()) {
            this.c.startService(h);
        } else {
            this.f = h;
        }
    }

    public final void a(q qVar) {
        Intent h = h();
        byte[] a = u.a(e.a(this.c, qVar, a.UnRegistration));
        if (a == null) {
            b.a("unregister fail, because msgBytes is null.");
            return;
        }
        h.setAction("com.xiaomi.mipush.UNREGISTER_APP");
        h.putExtra("mipush_app_id", a.a(this.c).c());
        h.putExtra("mipush_payload", a);
        this.c.startService(h);
    }

    public final <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, c cVar) {
        a(t, aVar, !aVar.equals(a.Registration), cVar);
    }

    public <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, boolean z) {
        a aVar2 = new a();
        aVar2.a = t;
        aVar2.b = aVar;
        aVar2.c = z;
        synchronized (e) {
            e.add(aVar2);
            if (e.size() > 10) {
                e.remove(0);
            }
        }
    }

    public final <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, boolean z, c cVar) {
        a(t, aVar, z, true, cVar, true);
    }

    public final <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, boolean z, c cVar,
                                                              boolean z2) {
        a(t, aVar, z, true, cVar, z2);
    }

    public final <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, boolean z, boolean z2,
                                                              c cVar, boolean z3) {
        a(t, aVar, z, z2, cVar, z3, this.c.getPackageName(), a.a(this.c).c());
    }

    public final <T extends org.apache.thrift.b<T, ?>> void a(T t, a aVar, boolean z, boolean z2,
                                                              c cVar, boolean z3, String str,
                                                              String str2) {
        if (a.a(this.c).i()) {
            Intent h = h();
            org.apache.thrift.b a = e.a(this.c, t, aVar, z, str, str2);
            if (cVar != null) {
                a.a(cVar);
            }
            byte[] a2 = u.a(a);
            if (a2 == null) {
                b.a("send message fail, because msgBytes is null.");
                return;
            }
            h.setAction("com.xiaomi.mipush.SEND_MESSAGE");
            h.putExtra("mipush_payload", a2);
            h.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", z3);
            this.c.startService(h);
        } else if (z2) {
            a((org.apache.thrift.b) t, aVar, z);
        } else {
            b.a("drop the message before initialization.");
        }
    }

    public void b(int i) {
        Intent h = h();
        h.setAction("com.xiaomi.mipush.SET_NOTIFICATION_TYPE");
        h.putExtra(y.y, this.c.getPackageName());
        h.putExtra(y.A, i);
        h.putExtra(y.C, com.xiaomi.channel.commonutils.string.c.b(this.c.getPackageName() + i));
        this.c.startService(h);
    }

    public boolean b() {
        return this.a && 1 == a.a(this.c).m();
    }

    public void c() {
        if (this.f != null) {
            this.c.startService(this.f);
            this.f = null;
        }
    }

    public void d() {
        synchronized (e) {
            Iterator it = e.iterator();
            while (it.hasNext()) {
                a aVar = (a) it.next();
                a(aVar.a, aVar.b, aVar.c, false, null, true);
            }
            e.clear();
        }
    }

    public void e() {
        Intent h = h();
        h.setAction("com.xiaomi.mipush.SET_NOTIFICATION_TYPE");
        h.putExtra(y.y, this.c.getPackageName());
        h.putExtra(y.C, com.xiaomi.channel.commonutils.string.c.b(this.c.getPackageName()));
        this.c.startService(h);
    }

    public boolean f() {
        if (!b() || !l()) {
            return true;
        }
        if (this.g == null) {
            this.g = Integer.valueOf(aa.a(this.c).b());
            if (this.g.intValue() == 0) {
                this.c.getContentResolver().registerContentObserver(aa.a(this.c).c(), false, new
                        h(this, new Handler(Looper.getMainLooper())));
            }
        }
        return this.g.intValue() != 0;
    }
}
