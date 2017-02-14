package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.util.k;
import com.xiaomi.xmpush.thrift.a;
import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.e;
import com.xiaomi.xmpush.thrift.h;
import com.xiaomi.xmpush.thrift.u;

import java.util.List;

public class l {
    private static void a(XMPushService xMPushService, h hVar, String str) {
        xMPushService.a(new q(4, xMPushService, hVar, str));
    }

    private static void a(XMPushService xMPushService, h hVar, String str, String str2) {
        xMPushService.a(new r(4, xMPushService, hVar, str, str2));
    }

    private static void a(XMPushService xMPushService, byte[] bArr, long j) {
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        h hVar = new h();
        try {
            u.a(hVar, bArr);
            if (TextUtils.isEmpty(hVar.f)) {
                b.a("receive a mipush message without package name");
                return;
            }
            Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
            intent.putExtra("mipush_payload", bArr);
            intent.putExtra("mrt", Long.toString(valueOf.longValue()));
            intent.setPackage(hVar.f);
            String a = s.a(hVar);
            k.a(xMPushService, a, j, true, System.currentTimeMillis());
            c m = hVar.m();
            if (m != null) {
                m.a("mrt", Long.toString(valueOf.longValue()));
            }
            String str;
            if (a.e == hVar.a() && i.a((Context) xMPushService).a(hVar.f) && !s.b(hVar)) {
                str = "";
                if (m != null) {
                    str = m.b();
                }
                b.a("Drop a message for unregistered, msgid=" + str);
                a(xMPushService, hVar, hVar.f);
            } else if (a.e != hVar.a() || TextUtils.equals(xMPushService.getPackageName(), "com" +
                    ".xiaomi.xmsf") || TextUtils.equals(xMPushService.getPackageName(), hVar.f)) {
                if (m != null) {
                    if (m.b() != null) {
                        b.a(String.format("receive a message, appid=%1$s, msgid= %2$s", new
                                Object[]{hVar.h(), m.b()}));
                    }
                }
                if (c(hVar) && a((Context) xMPushService, a)) {
                    c(xMPushService, hVar);
                } else if (a(hVar) && !a((Context) xMPushService, a) && !b(hVar)) {
                    d(xMPushService, hVar);
                } else if ((s.b(hVar) && b((Context) xMPushService, hVar.f)) || a((Context)
                        xMPushService, intent)) {
                    if (a.a == hVar.a()) {
                        str = hVar.j();
                        Editor edit = xMPushService.getSharedPreferences
                                ("pref_registered_pkg_names", 0).edit();
                        edit.putString(str, hVar.e);
                        edit.commit();
                    }
                    if (m == null || TextUtils.isEmpty(m.h()) || TextUtils.isEmpty(m.j()) || m.h
                            == 1 || (!s.a(m.s()) && s.a((Context) xMPushService, hVar.f))) {
                        xMPushService.sendBroadcast(intent, b.a(hVar.f));
                    } else {
                        boolean a2;
                        str = null;
                        if (m != null) {
                            if (m.j != null) {
                                str = (String) m.j.get("jobkey");
                            }
                            if (TextUtils.isEmpty(str)) {
                                str = m.b();
                            }
                            a2 = t.a(xMPushService, hVar.f, str);
                        } else {
                            a2 = false;
                        }
                        if (a2) {
                            b.a("drop a duplicate message, key=" + str);
                        } else {
                            s.a((Context) xMPushService, hVar, bArr);
                            if (!s.b(hVar)) {
                                Intent intent2 = new Intent("com.xiaomi.mipush.MESSAGE_ARRIVED");
                                intent2.putExtra("mipush_payload", bArr);
                                intent2.setPackage(hVar.f);
                                try {
                                    List queryBroadcastReceivers = xMPushService
                                            .getPackageManager().queryBroadcastReceivers(intent2,
                                                    0);
                                    if (!(queryBroadcastReceivers == null ||
                                            queryBroadcastReceivers.isEmpty())) {
                                        xMPushService.sendBroadcast(intent2, b.a(hVar.f));
                                    }
                                } catch (Exception e) {
                                    xMPushService.sendBroadcast(intent2, b.a(hVar.f));
                                }
                            }
                        }
                        b(xMPushService, hVar);
                    }
                    if (hVar.a() == a.b && !"com.xiaomi.xmsf".equals(xMPushService.getPackageName
                            ())) {
                        xMPushService.stopSelf();
                    }
                } else {
                    xMPushService.a(new m(4, xMPushService, hVar));
                }
            } else {
                b.a("Receive a message with wrong package name, expect " + xMPushService
                        .getPackageName() + ", received " + hVar.f);
                a(xMPushService, hVar, "unmatched_package", "package should be " + xMPushService
                        .getPackageName() + ", but got " + hVar.f);
            }
        } catch (Throwable e2) {
            b.a(e2);
        }
    }

    private static boolean a(Context context, Intent intent) {
        try {
            List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers
                    (intent, 32);
            return (queryBroadcastReceivers == null || queryBroadcastReceivers.isEmpty()) ? false
                    : true;
        } catch (Exception e) {
            return true;
        }
    }

    private static boolean a(Context context, String str) {
        Intent intent = new Intent("com.xiaomi.mipush.miui.CLICK_MESSAGE");
        intent.setPackage(str);
        Intent intent2 = new Intent("com.xiaomi.mipush.miui.RECEIVE_MESSAGE");
        intent2.setPackage(str);
        PackageManager packageManager = context.getPackageManager();
        try {
            return (packageManager.queryBroadcastReceivers(intent2, 32).isEmpty() &&
                    packageManager.queryIntentServices(intent, 32).isEmpty()) ? false : true;
        } catch (Throwable e) {
            b.a(e);
            return false;
        }
    }

    private static boolean a(h hVar) {
        return "com.xiaomi.xmsf".equals(hVar.f) && hVar.m() != null && hVar.m().s() != null &&
                hVar.m().s().containsKey("miui_package_name");
    }

    private static void b(XMPushService xMPushService, h hVar) {
        xMPushService.a(new n(4, xMPushService, hVar));
    }

    private static boolean b(Context context, String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    private static boolean b(h hVar) {
        return hVar.m().s().containsKey("notify_effect");
    }

    private static void c(XMPushService xMPushService, h hVar) {
        xMPushService.a(new o(4, xMPushService, hVar));
    }

    private static boolean c(h hVar) {
        return (hVar.m() == null || hVar.m().s() == null) ? false : "1".equals(hVar.m().s().get
                ("obslete_ads_message"));
    }

    private static void d(XMPushService xMPushService, h hVar) {
        xMPushService.a(new p(4, xMPushService, hVar));
    }

    private static h e(XMPushService xMPushService, h hVar) {
        org.apache.thrift.b eVar = new e();
        eVar.b(hVar.h());
        c m = hVar.m();
        if (m != null) {
            eVar.a(m.b());
            eVar.a(m.d());
            if (!TextUtils.isEmpty(m.f())) {
                eVar.c(m.f());
            }
        }
        h a = xMPushService.a(hVar.j(), hVar.h(), eVar, a.f);
        m = hVar.m().a();
        m.a("mat", Long.toString(System.currentTimeMillis()));
        a.a(m);
        return a;
    }

    public void a(Context context, w.b bVar, boolean z, int i, String str) {
        if (!z) {
            g a = h.a(context);
            if (a != null && "token-expired".equals(str)) {
                try {
                    h.a(context, a.d, a.e, a.f);
                } catch (Throwable e) {
                    b.a(e);
                } catch (Throwable e2) {
                    b.a(e2);
                }
            }
        }
    }

    public void a(XMPushService xMPushService, d dVar, w.b bVar) {
        if (dVar instanceof com.xiaomi.smack.packet.c) {
            com.xiaomi.smack.packet.c cVar = (com.xiaomi.smack.packet.c) dVar;
            com.xiaomi.smack.packet.a p = cVar.p("s");
            if (p != null) {
                try {
                    a(xMPushService, ac.b(ac.a(bVar.i, cVar.k()), p.c()), (long) k.a(dVar.a()));
                    return;
                } catch (Throwable e) {
                    b.a(e);
                    return;
                }
            }
            return;
        }
        b.a("not a mipush message");
    }
}
