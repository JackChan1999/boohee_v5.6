package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.xmpush.thrift.h;

final class q extends e {
    final /* synthetic */ XMPushService a;
    final /* synthetic */ h             b;
    final /* synthetic */ String        c;

    q(int i, XMPushService xMPushService, h hVar, String str) {
        this.a = xMPushService;
        this.b = hVar;
        this.c = str;
        super(i);
    }

    public void a() {
        try {
            h a = l.e(this.a, this.b);
            a.m().a("absent_target_package", this.c);
            this.a.b(a);
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send app absent ack message for message.";
    }
}
