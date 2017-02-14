package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.xmpush.thrift.h;

final class o extends e {
    final /* synthetic */ XMPushService a;
    final /* synthetic */ h             b;

    o(int i, XMPushService xMPushService, h hVar) {
        this.a = xMPushService;
        this.b = hVar;
        super(i);
    }

    public void a() {
        try {
            h a = l.e(this.a, this.b);
            a.m().a("message_obsleted", "1");
            this.a.b(a);
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send ack message for obsleted message.";
    }
}
