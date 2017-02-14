package com.xiaomi.smack;

import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.network.Fallback;
import com.xiaomi.network.HostManager;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.w;
import com.xiaomi.smack.packet.f;
import com.xiaomi.smack.util.i;
import com.xiaomi.stats.g;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class l extends a {
    private int A;
    public Exception n = null;
    protected Socket o;
    String p = null;
    i q;
    g r;
    private String s = null;
    private String t = "";
    private String        u;
    private XMPushService v;
    private volatile long   w = 0;
    private volatile long   x = 0;
    private final    String y = "<pf><p>t:%1$d</p></pf>";
    private volatile long   z = 0;

    public l(XMPushService xMPushService, b bVar) {
        super(xMPushService, bVar);
        this.v = xMPushService;
    }

    private void a(b bVar) {
        a(bVar.f(), bVar.e());
    }

    private void a(Exception exception) {
        if (SystemClock.elapsedRealtime() - this.z >= DeviceInfoConstant.REQUEST_LOCATE_INTERVAL) {
            this.A = 0;
        } else if (d.d(this.v)) {
            this.A++;
            if (this.A >= 2) {
                String c = c();
                b.a("max short conn time reached, sink down current host:" + c);
                a(c, 0, exception);
                this.A = 0;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.lang.String r17, int r18) {
        /*
        r16 = this;
        r4 = 0;
        r2 = 0;
        r0 = r16;
        r0.n = r2;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "get bucket for host : ";
        r2 = r2.append(r5);
        r0 = r17;
        r2 = r2.append(r0);
        r2 = r2.toString();
        r2 = com.xiaomi.channel.commonutils.logger.b.e(r2);
        r5 = r2.intValue();
        r2 = r16.c(r17);
        r5 = java.lang.Integer.valueOf(r5);
        com.xiaomi.channel.commonutils.logger.b.a(r5);
        if (r2 == 0) goto L_0x003b;
    L_0x0036:
        r3 = 1;
        r3 = r2.a(r3);
    L_0x003b:
        r5 = r3.isEmpty();
        if (r5 == 0) goto L_0x0046;
    L_0x0041:
        r0 = r17;
        r3.add(r0);
    L_0x0046:
        r6 = 0;
        r0 = r16;
        r0.z = r6;
        r0 = r16;
        r5 = r0.v;
        r10 = com.xiaomi.channel.commonutils.network.d.f(r5);
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = r3.iterator();
    L_0x005d:
        r3 = r12.hasNext();
        if (r3 == 0) goto L_0x0246;
    L_0x0063:
        r3 = r12.next();
        r3 = (java.lang.String) r3;
        r14 = java.lang.System.currentTimeMillis();
        r0 = r16;
        r5 = r0.b;
        r5 = r5 + 1;
        r0 = r16;
        r0.b = r5;
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable
         -> 0x01f2 }
        r5.<init>();	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r6 = "begin to connect to ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r5 = r5.append(r3);	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r5 = r5.toString();	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        com.xiaomi.channel.commonutils.logger.b.a(r5);	 Catch:{ IOException -> 0x0112, p ->
        0x0183, Throwable -> 0x01f2 }
        r5 = r16.u();	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r0 = r16;
        r0.o = r5;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r0 = r16;
        r5 = r0.o;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r6 = 0;
        r5.bind(r6);	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r0 = r18;
        r5 = com.xiaomi.network.Host.b(r3, r0);	 Catch:{ IOException -> 0x0112, p -> 0x0183,
        Throwable -> 0x01f2 }
        r0 = r16;
        r6 = r0.o;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r7 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r6.connect(r5, r7);	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r0 = r16;
        r5 = r0.o;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r6 = 1;
        r5.setTcpNoDelay(r6);	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r0 = r16;
        r0.u = r3;	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r16.x();	 Catch:{ IOException -> 0x0112, p -> 0x0183, Throwable -> 0x01f2 }
        r9 = 1;
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x0240, p -> 0x023d,
        Throwable -> 0x0239 }
        r4 = r4 - r14;
        r0 = r16;
        r0.c = r4;	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        if (r2 == 0) goto L_0x00d1;
    L_0x00c8:
        r0 = r16;
        r4 = r0.c;	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r6 = 0;
        r2.b(r3, r4, r6);	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
    L_0x00d1:
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ IOException -> 0x0240, p ->
        0x023d, Throwable -> 0x0239 }
        r0 = r16;
        r0.z = r4;	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable
         -> 0x0239 }
        r4.<init>();	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r5 = "connected to ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r4 = r4.append(r3);	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r5 = " in ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r0 = r16;
        r6 = r0.c;	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r4 = r4.append(r6);	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0240, p -> 0x023d, Throwable -> 0x0239 }
        com.xiaomi.channel.commonutils.logger.b.a(r4);	 Catch:{ IOException -> 0x0240, p ->
        0x023d, Throwable -> 0x0239 }
    L_0x00ff:
        r2 = com.xiaomi.network.HostManager.getInstance();
        r2.persist();
        if (r9 != 0) goto L_0x0236;
    L_0x0108:
        r2 = new com.xiaomi.smack.p;
        r3 = r11.toString();
        r2.<init>(r3);
        throw r2;
    L_0x0112:
        r8 = move-exception;
        r9 = r4;
    L_0x0114:
        if (r2 == 0) goto L_0x0120;
    L_0x0116:
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0237 }
        r4 = r4 - r14;
        r6 = 0;
        r2.b(r3, r4, r6, r8);	 Catch:{ all -> 0x0237 }
    L_0x0120:
        r0 = r16;
        r0.n = r8;	 Catch:{ all -> 0x0237 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0237 }
        r4.<init>();	 Catch:{ all -> 0x0237 }
        r5 = "SMACK: Could not connect to:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r3);	 Catch:{ all -> 0x0237 }
        r4 = r4.toString();	 Catch:{ all -> 0x0237 }
        com.xiaomi.channel.commonutils.logger.b.d(r4);	 Catch:{ all -> 0x0237 }
        r4 = "SMACK: Could not connect to ";
        r4 = r11.append(r4);	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r3);	 Catch:{ all -> 0x0237 }
        r5 = " port:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r0 = r18;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0237 }
        r5 = " ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r5 = r8.getMessage();	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r5 = "\n";
        r4.append(r5);	 Catch:{ all -> 0x0237 }
        if (r9 != 0) goto L_0x017f;
    L_0x016a:
        r0 = r16;
        r4 = r0.n;
        com.xiaomi.stats.g.a(r3, r4);
        r0 = r16;
        r3 = r0.v;
        r3 = com.xiaomi.channel.commonutils.network.d.f(r3);
        r3 = android.text.TextUtils.equals(r10, r3);
        if (r3 == 0) goto L_0x00ff;
    L_0x017f:
        r3 = r9;
    L_0x0180:
        r4 = r3;
        goto L_0x005d;
    L_0x0183:
        r8 = move-exception;
        r9 = r4;
    L_0x0185:
        if (r2 == 0) goto L_0x0191;
    L_0x0187:
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0237 }
        r4 = r4 - r14;
        r6 = 0;
        r2.b(r3, r4, r6, r8);	 Catch:{ all -> 0x0237 }
    L_0x0191:
        r0 = r16;
        r0.n = r8;	 Catch:{ all -> 0x0237 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0237 }
        r4.<init>();	 Catch:{ all -> 0x0237 }
        r5 = "SMACK: Could not connect to:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r3);	 Catch:{ all -> 0x0237 }
        r4 = r4.toString();	 Catch:{ all -> 0x0237 }
        com.xiaomi.channel.commonutils.logger.b.d(r4);	 Catch:{ all -> 0x0237 }
        r4 = "SMACK: Could not connect to ";
        r4 = r11.append(r4);	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r3);	 Catch:{ all -> 0x0237 }
        r5 = " port:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r0 = r18;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0237 }
        r5 = " ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r5 = r8.getMessage();	 Catch:{ all -> 0x0237 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0237 }
        r5 = "\n";
        r4.append(r5);	 Catch:{ all -> 0x0237 }
        if (r9 != 0) goto L_0x017f;
    L_0x01db:
        r0 = r16;
        r4 = r0.n;
        com.xiaomi.stats.g.a(r3, r4);
        r0 = r16;
        r3 = r0.v;
        r3 = com.xiaomi.channel.commonutils.network.d.f(r3);
        r3 = android.text.TextUtils.equals(r10, r3);
        if (r3 != 0) goto L_0x017f;
    L_0x01f0:
        goto L_0x00ff;
    L_0x01f2:
        r5 = move-exception;
    L_0x01f3:
        r6 = new java.lang.Exception;	 Catch:{ all -> 0x021c }
        r7 = "abnormal exception";
        r6.<init>(r7, r5);	 Catch:{ all -> 0x021c }
        r0 = r16;
        r0.n = r6;	 Catch:{ all -> 0x021c }
        com.xiaomi.channel.commonutils.logger.b.a(r5);	 Catch:{ all -> 0x021c }
        if (r4 != 0) goto L_0x0243;
    L_0x0204:
        r0 = r16;
        r5 = r0.n;
        com.xiaomi.stats.g.a(r3, r5);
        r0 = r16;
        r3 = r0.v;
        r3 = com.xiaomi.channel.commonutils.network.d.f(r3);
        r3 = android.text.TextUtils.equals(r10, r3);
        if (r3 != 0) goto L_0x0243;
    L_0x0219:
        r9 = r4;
        goto L_0x00ff;
    L_0x021c:
        r2 = move-exception;
        r9 = r4;
    L_0x021e:
        if (r9 != 0) goto L_0x0235;
    L_0x0220:
        r0 = r16;
        r4 = r0.n;
        com.xiaomi.stats.g.a(r3, r4);
        r0 = r16;
        r3 = r0.v;
        r3 = com.xiaomi.channel.commonutils.network.d.f(r3);
        r3 = android.text.TextUtils.equals(r10, r3);
        if (r3 == 0) goto L_0x00ff;
    L_0x0235:
        throw r2;
    L_0x0236:
        return;
    L_0x0237:
        r2 = move-exception;
        goto L_0x021e;
    L_0x0239:
        r4 = move-exception;
        r5 = r4;
        r4 = r9;
        goto L_0x01f3;
    L_0x023d:
        r8 = move-exception;
        goto L_0x0185;
    L_0x0240:
        r8 = move-exception;
        goto L_0x0114;
    L_0x0243:
        r3 = r4;
        goto L_0x0180;
    L_0x0246:
        r9 = r4;
        goto L_0x00ff;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.smack.l.a(java" +
                ".lang.String, int):void");
    }

    private void a(String str, long j, Exception exception) {
        Fallback fallbacksByHost = HostManager.getInstance().getFallbacksByHost(b.b(), false);
        if (fallbacksByHost != null) {
            fallbacksByHost.b(str, j, 0, exception);
            HostManager.getInstance().persist();
        }
    }

    private synchronized void x() {
        y();
        this.q = new i(this);
        this.r = new g(this);
        if (this.l.g()) {
            a(this.g.c(), null);
            if (this.g.d() != null) {
                b(this.g.d(), null);
            }
        }
        this.q.c();
        this.r.b();
    }

    private void y() {
        try {
            this.h = new BufferedReader(new InputStreamReader(this.o.getInputStream(), "UTF-8"),
                    4096);
            this.i = new BufferedWriter(new OutputStreamWriter(this.o.getOutputStream(), "UTF-8"));
            if (this.h != null && this.i != null) {
                f();
            }
        } catch (Throwable e) {
            throw new p("Error to init reader and writer", e);
        }
    }

    public void a(int i, Exception exception) {
        this.v.a(new n(this, 2, i, exception));
    }

    public synchronized void a(w.b bVar) {
        new k().a(bVar, r(), this);
    }

    public void a(com.xiaomi.smack.packet.d dVar) {
        if (this.q != null) {
            this.q.a(dVar);
            return;
        }
        throw new p("the writer is null.");
    }

    public void a(f fVar, int i, Exception exception) {
        b(fVar, i, exception);
        if (exception != null && this.z != 0) {
            a(exception);
        }
    }

    public synchronized void a(String str, String str2) {
        com.xiaomi.smack.packet.d fVar = new f(f.b.unavailable);
        fVar.l(str);
        fVar.n(str2);
        if (this.q != null) {
            this.q.a(fVar);
        }
    }

    public void a(com.xiaomi.smack.packet.d[] dVarArr) {
        for (com.xiaomi.smack.packet.d a : dVarArr) {
            a(a);
        }
    }

    protected synchronized void b(f fVar, int i, Exception exception) {
        if (n() != 2) {
            a(2, i, exception);
            this.j = "";
            if (this.r != null) {
                this.r.c();
                this.r.d();
                this.r = null;
            }
            if (this.q != null) {
                try {
                    this.q.b();
                } catch (Throwable e) {
                    b.a(e);
                }
                this.q.a();
                this.q = null;
            }
            try {
                this.o.close();
            } catch (Throwable th) {
            }
            if (this.h != null) {
                try {
                    this.h.close();
                } catch (Throwable th2) {
                }
                this.h = null;
            }
            if (this.i != null) {
                try {
                    this.i.close();
                } catch (Throwable th3) {
                }
                this.i = null;
            }
            this.w = 0;
            this.x = 0;
        }
    }

    public void b(String str) {
        this.t = str;
    }

    Fallback c(String str) {
        Fallback fallbacksByHost = HostManager.getInstance().getFallbacksByHost(str, false);
        if (!fallbacksByHost.b()) {
            i.a(new o(this, str));
        }
        this.d = 0;
        try {
            byte[] address = InetAddress.getByName(fallbacksByHost.f).getAddress();
            this.d = address[0] & 255;
            this.d |= (address[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
            this.d |= (address[2] << 16) & 16711680;
            this.d = ((address[3] << 24) & -16777216) | this.d;
        } catch (UnknownHostException e) {
        }
        return fallbacksByHost;
    }

    public String c() {
        return this.u;
    }

    public void m() {
        if (this.q != null) {
            this.q.d();
            this.v.a(new m(this, 13, System.currentTimeMillis()), 15000);
            return;
        }
        throw new p("the packetwriter is null.");
    }

    public String r() {
        return this.j;
    }

    public synchronized void s() {
        try {
            if (h() || g()) {
                b.a("WARNING: current xmpp has connected");
            } else {
                a(0, 0, null);
                a(this.l);
            }
        } catch (Throwable e) {
            throw new p(e);
        }
    }

    public String t() {
        String format = (this.x == 0 || this.w == 0) ? "" : String.format
                ("<pf><p>t:%1$d</p></pf>", new Object[]{Long.valueOf(this.x - this.w)});
        String c = g.c();
        c = c != null ? "<q>" + c + "</q>" : "";
        return String.format(this.t, new Object[]{format, c});
    }

    public Socket u() {
        return new Socket();
    }

    public void v() {
        this.w = SystemClock.uptimeMillis();
    }

    public void w() {
        this.x = SystemClock.uptimeMillis();
    }
}
