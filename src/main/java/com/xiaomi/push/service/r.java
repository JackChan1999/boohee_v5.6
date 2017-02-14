package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.xmpush.thrift.h;

final class r extends e {
    final /* synthetic */ XMPushService a;
    final /* synthetic */ h             b;
    final /* synthetic */ String        c;
    final /* synthetic */ String        d;

    r(int i, XMPushService xMPushService, h hVar, String str, String str2) {
        this.a = xMPushService;
        this.b = hVar;
        this.c = str;
        this.d = str2;
        super(i);
    }

    public void a() {
        try {
            h a = l.e(this.a, this.b);
            a.h.a("error", this.c);
            a.h.a("reason", this.d);
            this.a.b(a);
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send wrong message ack for message.";
    }
}
