package com.xiaomi.push.service;

import com.xiaomi.push.protobuf.b.a;
import com.xiaomi.smack.b;
import com.xiaomi.smack.e;

import java.util.Map;

class aj extends b {
    final /* synthetic */ XMPushService a;

    aj(XMPushService xMPushService, Map map, int i, String str, e eVar) {
        this.a = xMPushService;
        super(map, i, str, eVar);
    }

    public byte[] a() {
        try {
            a aVar = new a();
            aVar.a(af.a().c());
            return aVar.b();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("getOBBString err: " + e.toString());
            return null;
        }
    }
}
