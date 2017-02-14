package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;

class an extends e {
    final /* synthetic */ String        a;
    final /* synthetic */ byte[]        b;
    final /* synthetic */ XMPushService c;

    an(XMPushService xMPushService, int i, String str, byte[] bArr) {
        this.c = xMPushService;
        this.a = str;
        this.b = bArr;
        super(i);
    }

    public void a() {
        try {
            this.c.a(this.a, this.b);
        } catch (Exception e) {
            b.a(e);
            this.c.a(10, e);
        }
    }

    public String b() {
        return "send mi push message";
    }
}
