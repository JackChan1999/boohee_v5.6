package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService.d;
import com.xiaomi.push.service.w.a;

class al implements a {
    final /* synthetic */ XMPushService a;

    al(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void a() {
        this.a.j();
        if (w.a().c() <= 0) {
            this.a.a(new d(this.a, 12, null));
        }
    }
}
