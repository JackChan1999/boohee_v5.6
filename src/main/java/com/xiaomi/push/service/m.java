package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.xmpush.thrift.h;

final class m extends e {
    final /* synthetic */ XMPushService a;
    final /* synthetic */ h             b;

    m(int i, XMPushService xMPushService, h hVar) {
        this.a = xMPushService;
        this.b = hVar;
        super(i);
    }

    public void a() {
        try {
            this.a.b(this.a.a(this.b.j(), this.b.h()));
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send app absent message.";
    }
}
