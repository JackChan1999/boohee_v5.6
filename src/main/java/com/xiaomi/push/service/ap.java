package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService.e;
import com.xiaomi.smack.packet.f;
import com.xiaomi.smack.packet.f.b;

class ap extends e {
    final /* synthetic */ XMPushService a;

    ap(XMPushService xMPushService, int i) {
        this.a = xMPushService;
        super(i);
    }

    public void a() {
        if (this.a.f != null) {
            this.a.f.a(new f(b.unavailable), 15, null);
            this.a.f = null;
        }
    }

    public String b() {
        return "disconnect for service destroy.";
    }
}
