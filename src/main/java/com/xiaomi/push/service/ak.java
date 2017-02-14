package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService.e;

class ak extends e {
    final /* synthetic */ XMPushService a;

    ak(XMPushService xMPushService, int i) {
        this.a = xMPushService;
        super(i);
    }

    public void a() {
        com.xiaomi.stats.e.a().a(this.a, this.a.e);
        this.a.i();
    }

    public String b() {
        return "prepare the mi push account.";
    }
}
