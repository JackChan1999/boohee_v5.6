package com.xiaomi.push.service;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.service.w.b.a;
import com.xiaomi.push.service.w.c;

class ao implements a {
    final /* synthetic */ XMPushService a;

    ao(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void a(c cVar, c cVar2, int i) {
        if (cVar2 == c.binded) {
            k.a(this.a);
            k.b(this.a);
        } else if (cVar2 == c.unbind) {
            k.a(this.a, ErrorCode.ERROR_SERVICE_UNAVAILABLE, " the push is not connected.");
        }
    }
}
