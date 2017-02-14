package com.xiaomi.push.service;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.push.service.w.b;
import com.xiaomi.push.service.w.c;

import java.util.Collection;

public class j extends e {
    private XMPushService a;
    private byte[]        b;
    private String        c;
    private String        d;
    private String        f;

    public j(XMPushService xMPushService, String str, String str2, String str3, byte[] bArr) {
        super(9);
        this.a = xMPushService;
        this.c = str;
        this.b = bArr;
        this.d = str2;
        this.f = str3;
    }

    public void a() {
        g a;
        Collection c;
        b bVar;
        g a2 = h.a(this.a);
        if (a2 == null) {
            try {
                a = h.a(this.a, this.c, this.d, this.f);
            } catch (Throwable e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                a = a2;
            } catch (Throwable e2) {
                com.xiaomi.channel.commonutils.logger.b.a(e2);
            }
            if (a != null) {
                com.xiaomi.channel.commonutils.logger.b.d("no account for mipush");
                k.a(this.a, ErrorCode.ERROR_AUTHERICATION_ERROR, "no account.");
            }
            c = w.a().c("5");
            if (c.isEmpty()) {
                bVar = (b) c.iterator().next();
            } else {
                bVar = a.a(this.a);
                this.a.a(bVar);
                w.a().a(bVar);
            }
            if (this.a.e()) {
                this.a.a(true);
                return;
            }
            try {
                if (bVar.m == c.binded) {
                    this.a.a(this.c, this.b);
                    return;
                } else if (bVar.m == c.unbind) {
                    XMPushService xMPushService = this.a;
                    XMPushService xMPushService2 = this.a;
                    xMPushService2.getClass();
                    xMPushService.a(new a(xMPushService2, bVar));
                    return;
                } else {
                    return;
                }
            } catch (Exception e3) {
                com.xiaomi.channel.commonutils.logger.b.a(e3);
                this.a.a(10, e3);
                return;
            }
        }
        a = a2;
        if (a != null) {
            c = w.a().c("5");
            if (c.isEmpty()) {
                bVar = (b) c.iterator().next();
            } else {
                bVar = a.a(this.a);
                this.a.a(bVar);
                w.a().a(bVar);
            }
            if (this.a.e()) {
                this.a.a(true);
                return;
            } else if (bVar.m == c.binded) {
                this.a.a(this.c, this.b);
                return;
            } else if (bVar.m == c.unbind) {
                XMPushService xMPushService3 = this.a;
                XMPushService xMPushService22 = this.a;
                xMPushService22.getClass();
                xMPushService3.a(new a(xMPushService22, bVar));
                return;
            } else {
                return;
            }
        }
        com.xiaomi.channel.commonutils.logger.b.d("no account for mipush");
        k.a(this.a, ErrorCode.ERROR_AUTHERICATION_ERROR, "no account.");
    }

    public String b() {
        return "register app";
    }
}
