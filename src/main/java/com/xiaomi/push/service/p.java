package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.xmpush.thrift.h;

final class p extends e {
    final /* synthetic */ XMPushService a;
    final /* synthetic */ h             b;

    p(int i, XMPushService xMPushService, h hVar) {
        this.a = xMPushService;
        this.b = hVar;
        super(i);
    }

    public void a() {
        try {
            h a = l.e(this.a, this.b);
            a.m().a("miui_message_unrecognized", "1");
            this.a.b(a);
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send ack message for unrecognized new miui message.";
    }
}
