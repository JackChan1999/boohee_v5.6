package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.smack.packet.c;

public class a extends e {
    private XMPushService a = null;
    private c[] b;

    public a(XMPushService xMPushService, c[] cVarArr) {
        super(4);
        this.a = xMPushService;
        this.b = cVarArr;
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
        return "batch send message.";
    }
}
