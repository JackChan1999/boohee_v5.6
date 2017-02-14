package com.xiaomi.smack.util;

import com.xiaomi.channel.commonutils.misc.b.b;
import com.xiaomi.push.service.XMPushService;

import java.util.ArrayList;
import java.util.List;

final class l extends b {
    final /* synthetic */ XMPushService a;

    l(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void b() {
        List arrayList;
        synchronized (k.c) {
            arrayList = new ArrayList(k.d);
            k.d.clear();
        }
        k.b(this.a, arrayList);
    }
}
