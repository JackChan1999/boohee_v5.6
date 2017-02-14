package com.xiaomi.push.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.network.Fallback;
import com.xiaomi.network.HostManager;
import com.xiaomi.smack.p;
import com.xiaomi.xmpush.thrift.u;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class XMPushService extends Service implements com.xiaomi.smack.d {
    public static int a = 1;
    private com.xiaomi.smack.b b;
    private ad                 c;
    private long d = 0;
    private com.xiaomi.smack.l e;
    private com.xiaomi.smack.a f;
    private b                  g;
    private PacketSync                       h = null;
    private com.xiaomi.push.service.timers.a i = null;
    private d                                j = null;
    private com.xiaomi.smack.f               k = new ah(this);

    public static abstract class e {
        protected int e;

        public e(int i) {
            this.e = i;
        }

        public abstract void a();

        public abstract String b();

        public void c() {
            if (!(this.e == 4 || this.e == 8)) {
                com.xiaomi.channel.commonutils.logger.b.a("JOB: " + b());
            }
            a();
        }
    }

    class a extends e {
        com.xiaomi.push.service.w.b a = null;
        final /* synthetic */ XMPushService b;

        public a(XMPushService xMPushService, com.xiaomi.push.service.w.b bVar) {
            this.b = xMPushService;
            super(9);
            this.a = bVar;
        }

        public void a() {
            try {
                if (this.b.e()) {
                    com.xiaomi.push.service.w.b b = w.a().b(this.a.h, this.a.b);
                    if (b == null) {
                        com.xiaomi.channel.commonutils.logger.b.a("ignore bind because the " +
                                "channel " + this.a.h + " is removed ");
                        return;
                    } else if (b.m == com.xiaomi.push.service.w.c.unbind) {
                        b.a(com.xiaomi.push.service.w.c.binding, 0, 0, null, null);
                        this.b.f.a(b);
                        com.xiaomi.stats.g.a(this.b, b);
                        return;
                    } else {
                        com.xiaomi.channel.commonutils.logger.b.a("trying duplicate bind, ingore!" +
                                " " + b.m);
                        return;
                    }
                }
                com.xiaomi.channel.commonutils.logger.b.d("trying bind while the connection is " +
                        "not created, quit!");
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                this.b.a(10, e);
            }
        }

        public String b() {
            return "bind the client. " + this.a.h + ", " + this.a.b;
        }
    }

    static class b extends e {
        private final com.xiaomi.push.service.w.b a;

        public b(com.xiaomi.push.service.w.b bVar) {
            super(12);
            this.a = bVar;
        }

        public void a() {
            this.a.a(com.xiaomi.push.service.w.c.unbind, 1, 21, null, null);
        }

        public String b() {
            return "bind time out. chid=" + this.a.h;
        }

        public boolean equals(Object obj) {
            return !(obj instanceof b) ? false : TextUtils.equals(((b) obj).a.h, this.a.h);
        }

        public int hashCode() {
            return this.a.h.hashCode();
        }
    }

    public class c extends e {
        final /* synthetic */ XMPushService a;

        c(XMPushService xMPushService) {
            this.a = xMPushService;
            super(1);
        }

        public void a() {
            if (this.a.a()) {
                this.a.k();
            } else {
                com.xiaomi.channel.commonutils.logger.b.a("should not connect. quit the job.");
            }
        }

        public String b() {
            return "do reconnect..";
        }
    }

    public class d extends e {
        public                int           a;
        public                Exception     b;
        final /* synthetic */ XMPushService c;

        d(XMPushService xMPushService, int i, Exception exception) {
            this.c = xMPushService;
            super(2);
            this.a = i;
            this.b = exception;
        }

        public void a() {
            this.c.a(this.a, this.b);
        }

        public String b() {
            return "disconnect the connection.";
        }
    }

    class f extends e {
        final /* synthetic */ XMPushService a;

        public f(XMPushService xMPushService) {
            this.a = xMPushService;
            super(5);
        }

        public void a() {
            this.a.j.quit();
        }

        public String b() {
            return "ask the job queue to quit";
        }
    }

    public class g extends Binder {
        final /* synthetic */ XMPushService a;

        public g(XMPushService xMPushService) {
            this.a = xMPushService;
        }
    }

    class h extends e {
        final /* synthetic */ XMPushService a;
        private com.xiaomi.smack.packet.d b = null;

        public h(XMPushService xMPushService, com.xiaomi.smack.packet.d dVar) {
            this.a = xMPushService;
            super(8);
            this.b = dVar;
        }

        public void a() {
            this.a.h.a(this.b);
        }

        public String b() {
            return "receive a message.";
        }
    }

    class i extends e {
        final /* synthetic */ XMPushService a;

        public i(XMPushService xMPushService) {
            this.a = xMPushService;
            super(4);
        }

        public void a() {
            if (this.a.e()) {
                try {
                    com.xiaomi.stats.g.a();
                    this.a.f.m();
                } catch (Exception e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    this.a.a(10, e);
                }
            }
        }

        public String b() {
            return "send ping..";
        }
    }

    class j extends e {
        com.xiaomi.push.service.w.b a = null;
        final /* synthetic */ XMPushService b;

        public j(XMPushService xMPushService, com.xiaomi.push.service.w.b bVar) {
            this.b = xMPushService;
            super(4);
            this.a = bVar;
        }

        public void a() {
            try {
                this.a.a(com.xiaomi.push.service.w.c.unbind, 1, 16, null, null);
                this.b.f.a(this.a.h, this.a.b);
                this.a.a(com.xiaomi.push.service.w.c.binding, 1, 16, null, null);
                this.b.f.a(this.a);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                this.b.a(10, e);
            }
        }

        public String b() {
            return "bind the client. " + this.a.h + ", " + this.a.b;
        }
    }

    class k extends e {
        final /* synthetic */ XMPushService a;

        k(XMPushService xMPushService) {
            this.a = xMPushService;
            super(3);
        }

        public void a() {
            this.a.a(11, null);
            if (this.a.a()) {
                this.a.k();
            }
        }

        public String b() {
            return "reset the connection.";
        }
    }

    class l extends e {
        com.xiaomi.push.service.w.b a = null;
        int    b;
        String c;
        String d;
        final /* synthetic */ XMPushService f;

        public l(XMPushService xMPushService, com.xiaomi.push.service.w.b bVar, int i, String
                str, String str2) {
            this.f = xMPushService;
            super(9);
            this.a = bVar;
            this.b = i;
            this.c = str;
            this.d = str2;
        }

        public void a() {
            if (!(this.a.m == com.xiaomi.push.service.w.c.unbind || this.f.f == null)) {
                try {
                    this.f.f.a(this.a.h, this.a.b);
                } catch (Exception e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    this.f.a(10, e);
                }
            }
            this.a.a(com.xiaomi.push.service.w.c.unbind, this.b, 0, this.d, this.c);
        }

        public String b() {
            return "unbind the channel. " + this.a.h + ", " + this.a.b;
        }
    }

    static {
        HostManager.addReservedHost("app.chat.xiaomi.net", "42.62.94.2");
        HostManager.addReservedHost("app.chat.xiaomi.net", "114.54.23.2");
        HostManager.addReservedHost("app.chat.xiaomi.net", "111.13.142.2");
        HostManager.addReservedHost("app.chat.xiaomi.net", "111.206.200.2");
        HostManager.addReservedHost("app.chat.xiaomi.net", "app.chat.xiaomi.net");
        com.xiaomi.smack.l.a = true;
    }

    private com.xiaomi.smack.packet.c a(com.xiaomi.smack.packet.c cVar, String str) {
        byte[] a = ac.a(str, cVar.k());
        com.xiaomi.smack.packet.c cVar2 = new com.xiaomi.smack.packet.c();
        cVar2.n(cVar.n());
        cVar2.m(cVar.m());
        cVar2.k(cVar.k());
        cVar2.l(cVar.l());
        cVar2.b(true);
        String a2 = ac.a(a, com.xiaomi.smack.util.g.c(cVar.a()));
        com.xiaomi.smack.packet.a aVar = new com.xiaomi.smack.packet.a("s", null, (String[])
                null, (String[]) null);
        aVar.b(a2);
        cVar2.a(aVar);
        return cVar2;
    }

    private com.xiaomi.smack.packet.d a(com.xiaomi.smack.packet.d dVar, String str, String str2,
                                        boolean z) {
        w a = w.a();
        List b = a.b(str);
        if (b.isEmpty()) {
            com.xiaomi.channel.commonutils.logger.b.a("open channel should be called first before" +
                    " sending a packet, pkg=" + str);
        } else {
            dVar.o(str);
            String l = dVar.l();
            if (TextUtils.isEmpty(l)) {
                l = (String) b.get(0);
                dVar.l(l);
            }
            com.xiaomi.push.service.w.b b2 = a.b(l, dVar.n());
            if (!e()) {
                com.xiaomi.channel.commonutils.logger.b.a("drop a packet as the channel is not " +
                        "connected, chid=" + l);
            } else if (b2 == null || b2.m != com.xiaomi.push.service.w.c.binded) {
                com.xiaomi.channel.commonutils.logger.b.a("drop a packet as the channel is not " +
                        "opened, chid=" + l);
            } else if (TextUtils.equals(str2, b2.j)) {
                return ((dVar instanceof com.xiaomi.smack.packet.c) && z) ? a((com.xiaomi.smack
                        .packet.c) dVar, b2.i) : dVar;
            } else {
                com.xiaomi.channel.commonutils.logger.b.a("invalid session. " + str2);
            }
        }
        return null;
    }

    private String a(String str) {
        return "<iq to='" + str + "' id='0' chid='0' type='get'><ping " +
                "xmlns='urn:xmpp:ping'>%1$s%2$s</ping></iq>";
    }

    private void a(String str, int i) {
        Collection<com.xiaomi.push.service.w.b> c = w.a().c(str);
        if (c != null) {
            for (com.xiaomi.push.service.w.b bVar : c) {
                if (bVar != null) {
                    a(new l(this, bVar, i, null, null));
                }
            }
        }
        w.a().a(str);
    }

    private boolean a(String str, Intent intent) {
        com.xiaomi.push.service.w.b b = w.a().b(str, intent.getStringExtra(y.p));
        boolean z = false;
        if (b == null || str == null) {
            return false;
        }
        Object stringExtra = intent.getStringExtra(y.B);
        String stringExtra2 = intent.getStringExtra(y.u);
        if (!(TextUtils.isEmpty(b.j) || TextUtils.equals(stringExtra, b.j))) {
            com.xiaomi.channel.commonutils.logger.b.a("session changed. old session=" + b.j + ", " +
                    "new session=" + stringExtra);
            z = true;
        }
        if (stringExtra2.equals(b.i)) {
            return z;
        }
        com.xiaomi.channel.commonutils.logger.b.a("security changed. ");
        return true;
    }

    private com.xiaomi.push.service.w.b b(String str, Intent intent) {
        com.xiaomi.push.service.w.b b = w.a().b(str, intent.getStringExtra(y.p));
        if (b == null) {
            b = new com.xiaomi.push.service.w.b(this);
        }
        b.h = intent.getStringExtra(y.q);
        b.b = intent.getStringExtra(y.p);
        b.c = intent.getStringExtra(y.s);
        b.a = intent.getStringExtra(y.y);
        b.f = intent.getStringExtra(y.w);
        b.g = intent.getStringExtra(y.x);
        b.e = intent.getBooleanExtra(y.v, false);
        b.i = intent.getStringExtra(y.u);
        b.j = intent.getStringExtra(y.B);
        b.d = intent.getStringExtra(y.t);
        b.k = this.g;
        b.l = getApplicationContext();
        w.a().a(b);
        return b;
    }

    private void i() {
        if (h.a(getApplicationContext()) != null) {
            com.xiaomi.push.service.w.b a = h.a(getApplicationContext()).a(this);
            a(a);
            w.a().a(a);
            if (com.xiaomi.channel.commonutils.network.d.d(getApplicationContext())) {
                a(true);
            }
        }
    }

    private void j() {
        if (!a()) {
            this.i.a();
        } else if (!this.i.b()) {
            this.i.a(true);
        }
    }

    private void k() {
        if (this.f != null && this.f.g()) {
            com.xiaomi.channel.commonutils.logger.b.d("try to connect while connecting.");
        } else if (this.f == null || !this.f.h()) {
            this.b.b(com.xiaomi.channel.commonutils.network.d.f(this));
            l();
            if (this.f == null) {
                w.a().a((Context) this);
                sendBroadcast(new Intent("miui.intent.action.NETWORK_BLOCKED"));
                return;
            }
            sendBroadcast(new Intent("miui.intent.action.NETWORK_CONNECTED"));
        } else {
            com.xiaomi.channel.commonutils.logger.b.d("try to connect while is connected.");
        }
    }

    private void l() {
        try {
            this.e.a(this.k, new aq(this));
            this.e.s();
            this.f = this.e;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("fail to create xmpp connection", e);
            this.e.a(new com.xiaomi.smack.packet.f(com.xiaomi.smack.packet.f.b.unavailable), 3, e);
        }
    }

    public com.xiaomi.smack.l a(com.xiaomi.smack.b bVar) {
        return new com.xiaomi.smack.l(this, bVar);
    }

    public com.xiaomi.smack.packet.c a(com.xiaomi.xmpush.thrift.h hVar) {
        try {
            com.xiaomi.smack.packet.c cVar = new com.xiaomi.smack.packet.c();
            cVar.l("5");
            cVar.m("xiaomi.com");
            cVar.n(h.a(this).a);
            cVar.b(true);
            cVar.f("push");
            cVar.o(hVar.f);
            String str = h.a(this).a;
            hVar.g.b = str.substring(0, str.indexOf("@"));
            hVar.g.d = str.substring(str.indexOf("/") + 1);
            String valueOf = String.valueOf(com.xiaomi.channel.commonutils.string.a.a(ac.a(ac.a(h
                    .a(this).c, cVar.k()), u.a(hVar))));
            com.xiaomi.smack.packet.a aVar = new com.xiaomi.smack.packet.a("s", null, (String[])
                    null, (String[]) null);
            aVar.b(valueOf);
            cVar.a(aVar);
            com.xiaomi.channel.commonutils.logger.b.a("try send mi push message. packagename:" +
                    hVar.f + " action:" + hVar.a);
            return cVar;
        } catch (Throwable e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return null;
        }
    }

    public com.xiaomi.smack.packet.c a(byte[] bArr) {
        com.xiaomi.xmpush.thrift.h hVar = new com.xiaomi.xmpush.thrift.h();
        try {
            u.a(hVar, bArr);
            return a(hVar);
        } catch (Throwable e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return null;
        }
    }

    public com.xiaomi.xmpush.thrift.h a(String str, String str2) {
        org.apache.thrift.b iVar = new com.xiaomi.xmpush.thrift.i();
        iVar.b(str2);
        iVar.c("package uninstalled");
        iVar.a(com.xiaomi.smack.packet.d.j());
        iVar.a(false);
        return a(str, str2, iVar, com.xiaomi.xmpush.thrift.a.i);
    }

    public <T extends org.apache.thrift.b<T, ?>> com.xiaomi.xmpush.thrift.h a(String str, String
            str2, T t, com.xiaomi.xmpush.thrift.a aVar) {
        byte[] a = u.a(t);
        com.xiaomi.xmpush.thrift.h hVar = new com.xiaomi.xmpush.thrift.h();
        com.xiaomi.xmpush.thrift.d dVar = new com.xiaomi.xmpush.thrift.d();
        dVar.a = 5;
        dVar.b = "fakeid";
        hVar.a(dVar);
        hVar.a(ByteBuffer.wrap(a));
        hVar.a(aVar);
        hVar.c(true);
        hVar.b(str);
        hVar.a(false);
        hVar.a(str2);
        return hVar;
    }

    public void a(int i) {
        this.j.a(i);
    }

    public void a(int i, Exception exception) {
        com.xiaomi.channel.commonutils.logger.b.a("disconnect " + hashCode() + ", " + (this.f ==
                null ? null : Integer.valueOf(this.f.hashCode())));
        if (this.f != null) {
            this.f.a(new com.xiaomi.smack.packet.f(com.xiaomi.smack.packet.f.b.unavailable), i,
                    exception);
            this.f = null;
        }
        a(7);
        a(4);
        w.a().a((Context) this, i);
    }

    public void a(e eVar) {
        a(eVar, 0);
    }

    public void a(e eVar, long j) {
        this.j.a(eVar, j);
    }

    public void a(com.xiaomi.push.service.w.b bVar) {
        bVar.a(new ao(this));
    }

    public void a(com.xiaomi.smack.a aVar) {
        this.c.a();
        Iterator it = w.a().b().iterator();
        while (it.hasNext()) {
            a(new a(this, (com.xiaomi.push.service.w.b) it.next()));
        }
    }

    public void a(com.xiaomi.smack.a aVar, int i, Exception exception) {
        a(false);
    }

    public void a(com.xiaomi.smack.a aVar, Exception exception) {
        a(false);
    }

    public void a(com.xiaomi.smack.packet.d dVar) {
        if (this.f != null) {
            this.f.a(dVar);
            return;
        }
        throw new p("try send msg while connection is null.");
    }

    public void a(String str, String str2, int i, String str3, String str4) {
        com.xiaomi.push.service.w.b b = w.a().b(str, str2);
        if (b != null) {
            a(new l(this, b, i, str4, str3));
        }
        w.a().a(str, str2);
    }

    public void a(String str, byte[] bArr) {
        if (this.f != null) {
            com.xiaomi.smack.packet.d a = a(bArr);
            if (a != null) {
                this.f.a(a);
                return;
            } else {
                k.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, "not a valid message");
                return;
            }
        }
        throw new p("try send msg while connection is null.");
    }

    public void a(boolean z) {
        this.c.a(z);
    }

    public void a(byte[] bArr, String str) {
        if (bArr == null) {
            k.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, "null payload");
            com.xiaomi.channel.commonutils.logger.b.a("register request without payload");
            return;
        }
        com.xiaomi.xmpush.thrift.h hVar = new com.xiaomi.xmpush.thrift.h();
        try {
            u.a(hVar, bArr);
            if (hVar.a == com.xiaomi.xmpush.thrift.a.a) {
                com.xiaomi.xmpush.thrift.j jVar = new com.xiaomi.xmpush.thrift.j();
                try {
                    u.a(jVar, hVar.f());
                    k.a(hVar.j(), bArr);
                    a(new j(this, hVar.j(), jVar.d(), jVar.h(), bArr));
                    return;
                } catch (Throwable e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    k.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " data action error.");
                    return;
                }
            }
            k.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " registration action required.");
            com.xiaomi.channel.commonutils.logger.b.a("register request with invalid payload");
        } catch (Throwable e2) {
            com.xiaomi.channel.commonutils.logger.b.a(e2);
            k.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " data container error.");
        }
    }

    public void a(com.xiaomi.smack.packet.d[] dVarArr) {
        if (this.f != null) {
            this.f.a(dVarArr);
            return;
        }
        throw new p("try send msg while connection is null.");
    }

    public boolean a() {
        return com.xiaomi.channel.commonutils.network.d.d(this) && w.a().c() > 0 && !b();
    }

    public void b(e eVar) {
        this.j.a(eVar.e, (Object) eVar);
    }

    public void b(com.xiaomi.push.service.w.b bVar) {
        if (bVar != null) {
            long a = bVar.a();
            com.xiaomi.channel.commonutils.logger.b.a("schedule rebind job in " + (a / 1000));
            a(new a(this, bVar), a);
        }
    }

    public void b(com.xiaomi.smack.a aVar) {
        com.xiaomi.channel.commonutils.logger.b.c("begin to connect...");
    }

    public void b(com.xiaomi.xmpush.thrift.h hVar) {
        if (this.f != null) {
            com.xiaomi.smack.packet.d a = a(hVar);
            if (a != null) {
                this.f.a(a);
                return;
            }
            return;
        }
        throw new p("try send msg while connection is null.");
    }

    public boolean b() {
        try {
            Class cls = Class.forName("miui.os.Build");
            return cls.getField("IS_CM_CUSTOMIZATION_TEST").getBoolean(null) || cls.getField
                    ("IS_CU_CUSTOMIZATION_TEST").getBoolean(null);
        } catch (Throwable th) {
            return false;
        }
    }

    public boolean b(int i) {
        return this.j.b(i);
    }

    public b c() {
        return new b();
    }

    public b d() {
        return this.g;
    }

    public boolean e() {
        return this.f != null && this.f.h();
    }

    public boolean f() {
        return this.f != null && this.f.g();
    }

    public com.xiaomi.smack.a g() {
        return this.f;
    }

    public void h() {
        a(new ai(this, 10), 120000);
    }

    public IBinder onBind(Intent intent) {
        return new g(this);
    }

    public void onCreate() {
        super.onCreate();
        com.xiaomi.smack.util.h.a(this);
        g a = h.a(this);
        if (a != null) {
            com.xiaomi.channel.commonutils.misc.a.a(a.g);
        }
        z.a(this);
        this.b = new aj(this, null, 5222, "xiaomi.com", null);
        this.b.a(true);
        this.e = a(this.b);
        this.e.b(a("xiaomi.com"));
        Fallback fallback = new Fallback("mibind.chat.gslb.mi-idc.com");
        this.g = c();
        try {
            if (TextUtils.equals((String) Class.forName("android.os.SystemProperties").getMethod
                    ("get", new Class[]{String.class}).invoke(null, new Object[]{"sys" +
                    ".boot_completed"}), "1")) {
                this.g.a((Context) this);
            }
        } catch (Throwable e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
        this.i = new com.xiaomi.push.service.timers.a(this);
        this.e.a((com.xiaomi.smack.d) this);
        this.h = new PacketSync(this);
        this.c = new ad(this);
        new c().a();
        this.j = new d("Connection Controller Thread");
        this.j.start();
        a(new ak(this, 11));
        w a2 = w.a();
        a2.e();
        a2.a(new al(this));
    }

    public void onDestroy() {
        this.j.a();
        a(new ap(this, 2));
        a(new f(this));
        w.a().e();
        w.a().a((Context) this, 15);
        w.a().d();
        this.e.b((com.xiaomi.smack.d) this);
        af.a().b();
        this.i.a();
        super.onDestroy();
        com.xiaomi.channel.commonutils.logger.b.a("Service destroyed");
    }

    public void onStart(Intent intent, int i) {
        com.xiaomi.push.service.w.b bVar = null;
        boolean z = true;
        boolean z2 = false;
        if (intent == null) {
            com.xiaomi.channel.commonutils.logger.b.d("onStart() with intent NULL");
        } else {
            com.xiaomi.channel.commonutils.logger.b.c(String.format("onStart() with intent.Action" +
                    " = %s, chid = %s", new Object[]{intent.getAction(), intent.getStringExtra(y
                    .q)}));
        }
        w a = w.a();
        if (intent != null && intent.getAction() != null) {
            String stringExtra;
            if (y.d.equalsIgnoreCase(intent.getAction()) || y.j.equalsIgnoreCase(intent.getAction
                    ())) {
                stringExtra = intent.getStringExtra(y.q);
                if (TextUtils.isEmpty(intent.getStringExtra(y.u))) {
                    com.xiaomi.channel.commonutils.logger.b.a("security is empty. ignore.");
                } else if (stringExtra != null) {
                    boolean a2 = a(stringExtra, intent);
                    com.xiaomi.push.service.w.b b = b(stringExtra, intent);
                    if (!com.xiaomi.channel.commonutils.network.d.d(this)) {
                        this.g.a(this, b, false, 2, null);
                    } else if (!e()) {
                        a(true);
                    } else if (b.m == com.xiaomi.push.service.w.c.unbind) {
                        a(new a(this, b));
                    } else if (a2) {
                        a(new j(this, b));
                    } else if (b.m == com.xiaomi.push.service.w.c.binding) {
                        com.xiaomi.channel.commonutils.logger.b.a(String.format("the client is " +
                                "binding. %1$s %2$s.", new Object[]{b.h, b.b}));
                    } else if (b.m == com.xiaomi.push.service.w.c.binded) {
                        this.g.a(this, b, true, 0, null);
                    }
                } else {
                    com.xiaomi.channel.commonutils.logger.b.d("channel id is empty, do nothing!");
                }
            } else if (y.i.equalsIgnoreCase(intent.getAction())) {
                stringExtra = intent.getStringExtra(y.y);
                r2 = intent.getStringExtra(y.q);
                Object stringExtra2 = intent.getStringExtra(y.p);
                if (TextUtils.isEmpty(r2)) {
                    for (String stringExtra3 : a.b(stringExtra3)) {
                        a(stringExtra3, 2);
                    }
                } else if (TextUtils.isEmpty(stringExtra2)) {
                    a(r2, 2);
                } else {
                    a(r2, stringExtra2, 2, null, null);
                }
            } else if (y.e.equalsIgnoreCase(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.y);
                r1 = intent.getStringExtra(y.B);
                Bundle bundleExtra = intent.getBundleExtra("ext_packet");
                com.xiaomi.smack.packet.d a3 = a(new com.xiaomi.smack.packet.c(bundleExtra),
                        stringExtra3, r1, intent.getBooleanExtra("ext_encrypt", true));
                if (a3 != null) {
                    a(new ae(this, a3));
                }
            } else if (y.g.equalsIgnoreCase(intent.getAction())) {
                r1 = intent.getStringExtra(y.y);
                r2 = intent.getStringExtra(y.B);
                Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("ext_packets");
                com.xiaomi.smack.packet.c[] cVarArr = new com.xiaomi.smack.packet
                        .c[parcelableArrayExtra.length];
                boolean booleanExtra = intent.getBooleanExtra("ext_encrypt", true);
                while (r3 < parcelableArrayExtra.length) {
                    cVarArr[r3] = new com.xiaomi.smack.packet.c((Bundle) parcelableArrayExtra[r3]);
                    cVarArr[r3] = (com.xiaomi.smack.packet.c) a(cVarArr[r3], r1, r2, booleanExtra);
                    if (cVarArr[r3] != null) {
                        r3++;
                    } else {
                        return;
                    }
                }
                a(new a(this, cVarArr));
            } else if (y.f.equalsIgnoreCase(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.y);
                r1 = intent.getStringExtra(y.B);
                r4 = new com.xiaomi.smack.packet.b(intent.getBundleExtra("ext_packet"));
                if (a(r4, stringExtra3, r1, false) != null) {
                    a(new ae(this, r4));
                }
            } else if (y.h.equalsIgnoreCase(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.y);
                r1 = intent.getStringExtra(y.B);
                r4 = new com.xiaomi.smack.packet.f(intent.getBundleExtra("ext_packet"));
                if (a(r4, stringExtra3, r1, false) != null) {
                    a(new ae(this, r4));
                }
            } else if ("com.xiaomi.push.timer".equalsIgnoreCase(intent.getAction()) || ("com" +
                    ".xiaomi.push.check_alive").equalsIgnoreCase(intent.getAction())) {
                if ("com.xiaomi.push.timer".equalsIgnoreCase(intent.getAction())) {
                    com.xiaomi.channel.commonutils.logger.b.a("Service called on timer");
                } else if (System.currentTimeMillis() - this.d >= 30000) {
                    this.d = System.currentTimeMillis();
                    com.xiaomi.channel.commonutils.logger.b.a("Service called on check alive.");
                } else {
                    return;
                }
                if (this.j.b()) {
                    com.xiaomi.channel.commonutils.logger.b.d("ERROR, the job controller is " +
                            "blocked.");
                    w.a().a((Context) this, 14);
                    stopSelf();
                } else if (e()) {
                    if (this.f.p()) {
                        a(new i(this));
                    } else {
                        a(new d(this, 17, null));
                    }
                } else if ("com.xiaomi.push.timer".equalsIgnoreCase(intent.getAction())) {
                    a(false);
                } else {
                    a(true);
                }
            } else if ("com.xiaomi.push.network_status_changed".equalsIgnoreCase(intent.getAction
                    ())) {
                NetworkInfo activeNetworkInfo;
                try {
                    activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity"))
                            .getActiveNetworkInfo();
                } catch (Throwable e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    activeNetworkInfo = null;
                }
                if (activeNetworkInfo != null) {
                    com.xiaomi.channel.commonutils.logger.b.a("network changed, " +
                            activeNetworkInfo.toString());
                } else {
                    com.xiaomi.channel.commonutils.logger.b.a("network changed, no active network");
                }
                this.e.q();
                if (com.xiaomi.channel.commonutils.network.d.d(this)) {
                    if (!(e() || f())) {
                        this.j.a(1);
                        a(new c(this));
                    }
                    com.xiaomi.push.log.b.a((Context) this).a();
                } else {
                    a(new d(this, 2, null));
                }
                j();
            } else if (y.k.equals(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.q);
                if (stringExtra3 != null) {
                    b(stringExtra3, intent);
                }
                a(new k(this));
            } else if (y.l.equals(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.y);
                List b2 = a.b(stringExtra3);
                if (b2.isEmpty()) {
                    com.xiaomi.channel.commonutils.logger.b.a("open channel should be called " +
                            "first before update info, pkg=" + stringExtra3);
                    return;
                }
                stringExtra3 = intent.getStringExtra(y.q);
                Object stringExtra4 = intent.getStringExtra(y.p);
                if (TextUtils.isEmpty(stringExtra3)) {
                    stringExtra3 = (String) b2.get(0);
                }
                if (TextUtils.isEmpty(stringExtra4)) {
                    r0 = a.c(stringExtra3);
                    if (!(r0 == null || r0.isEmpty())) {
                        bVar = (com.xiaomi.push.service.w.b) r0.iterator().next();
                    }
                } else {
                    bVar = a.b(stringExtra3, stringExtra4);
                }
                if (bVar != null) {
                    if (intent.hasExtra(y.w)) {
                        bVar.f = intent.getStringExtra(y.w);
                    }
                    if (intent.hasExtra(y.x)) {
                        bVar.g = intent.getStringExtra(y.x);
                    }
                }
            } else if ("com.xiaomi.mipush.REGISTER_APP".equals(intent.getAction())) {
                if (aa.a(getApplicationContext()).a() && aa.a(getApplicationContext()).b() == 0) {
                    com.xiaomi.channel.commonutils.logger.b.a("register without being provisioned" +
                            ". " + intent.getStringExtra("mipush_app_package"));
                    return;
                }
                byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
                String stringExtra5 = intent.getStringExtra("mipush_app_package");
                boolean booleanExtra2 = intent.getBooleanExtra("mipush_env_chanage", false);
                r3 = intent.getIntExtra("mipush_env_type", 1);
                i.a((Context) this).c(stringExtra5);
                if (!booleanExtra2 || "com.xiaomi.xmsf".equals(getPackageName())) {
                    a(byteArrayExtra, stringExtra5);
                } else {
                    a(new am(this, 14, r3, byteArrayExtra, stringExtra5));
                }
            } else if ("com.xiaomi.mipush.SEND_MESSAGE".equals(intent.getAction()) || ("com.xiaomi" +
                    ".mipush.UNREGISTER_APP").equals(intent.getAction())) {
                r1 = intent.getStringExtra("mipush_app_package");
                byte[] byteArrayExtra2 = intent.getByteArrayExtra("mipush_payload");
                z2 = intent.getBooleanExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
                r0 = w.a().c("5");
                if ("com.xiaomi.mipush.UNREGISTER_APP".equals(intent.getAction())) {
                    i.a((Context) this).b(r1);
                }
                if (r0.isEmpty()) {
                    if (z2) {
                        k.b(r1, byteArrayExtra2);
                    }
                } else if (((com.xiaomi.push.service.w.b) r0.iterator().next()).m == com.xiaomi
                        .push.service.w.c.binded) {
                    a(new an(this, 4, r1, byteArrayExtra2));
                } else if (z2) {
                    k.b(r1, byteArrayExtra2);
                }
            } else if (ab.a.equals(intent.getAction())) {
                stringExtra3 = intent.getStringExtra("uninstall_pkg_name");
                if (stringExtra3 != null && !TextUtils.isEmpty(stringExtra3.trim())) {
                    try {
                        getPackageManager().getPackageInfo(stringExtra3, 256);
                        z = false;
                    } catch (NameNotFoundException e2) {
                    }
                    if ("com.xiaomi.channel".equals(stringExtra3) && !w.a().c("1").isEmpty() &&
                            r9) {
                        a("1", 0);
                        com.xiaomi.channel.commonutils.logger.b.a("close the miliao channel as " +
                                "the app is uninstalled.");
                        return;
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences
                            ("pref_registered_pkg_names", 0);
                    r2 = sharedPreferences.getString(stringExtra3, null);
                    if (!TextUtils.isEmpty(r2) && r9) {
                        Editor edit = sharedPreferences.edit();
                        edit.remove(stringExtra3);
                        edit.commit();
                        if (s.e(this, stringExtra3)) {
                            s.d(this, stringExtra3);
                        }
                        s.b(this, stringExtra3);
                        if (e() && r2 != null) {
                            try {
                                b(a(stringExtra3, r2));
                                com.xiaomi.channel.commonutils.logger.b.a("uninstall " +
                                        stringExtra3 + " msg sent");
                            } catch (Exception e3) {
                                com.xiaomi.channel.commonutils.logger.b.d("Fail to send Message: " +
                                        "" + e3.getMessage());
                                a(10, e3);
                            }
                        }
                    }
                }
            } else if ("com.xiaomi.mipush.CLEAR_NOTIFICATION".equals(intent.getAction())) {
                stringExtra3 = intent.getStringExtra(y.y);
                r1 = intent.getIntExtra(y.z, 0);
                if (!TextUtils.isEmpty(stringExtra3)) {
                    if (r1 >= 0) {
                        s.a((Context) this, stringExtra3, r1);
                    } else if (r1 == -1) {
                        s.b(this, stringExtra3);
                    }
                }
            } else if ("com.xiaomi.mipush.SET_NOTIFICATION_TYPE".equals(intent.getAction())) {
                r2 = intent.getStringExtra(y.y);
                CharSequence stringExtra6 = intent.getStringExtra(y.C);
                CharSequence b3;
                if (intent.hasExtra(y.A)) {
                    r1 = intent.getIntExtra(y.A, 0);
                    b3 = com.xiaomi.channel.commonutils.string.c.b(r2 + r1);
                } else {
                    b3 = com.xiaomi.channel.commonutils.string.c.b(r2);
                    r1 = 0;
                    z2 = true;
                }
                if (TextUtils.isEmpty(r2) || !TextUtils.equals(stringExtra6, r0)) {
                    com.xiaomi.channel.commonutils.logger.b.d("invalid notification for " + r2);
                } else if (z2) {
                    s.d(this, r2);
                } else {
                    s.b((Context) this, r2, r1);
                }
            }
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        onStart(intent, i2);
        return a;
    }
}
