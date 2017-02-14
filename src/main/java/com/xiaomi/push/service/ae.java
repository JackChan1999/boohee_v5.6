package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.smack.packet.d;

public class ae extends e {
    private XMPushService a = null;
    private d b;

    public ae(XMPushService xMPushService, d dVar) {
        super(4);
        this.a = xMPushService;
        this.b = dVar;
    }

    public void a() {
        try {
            this.a.a(this.b);
        } catch (Exception e) {
            b.a(e);
            this.a.a(10, e);
        }
    }

    public String b() {
        return "send a message.";
    }
}
