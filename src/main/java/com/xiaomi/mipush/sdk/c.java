package com.xiaomi.mipush.sdk;

import com.xiaomi.channel.commonutils.string.d;
import com.xiaomi.push.service.f;
import com.xiaomi.xmpush.thrift.a;
import com.xiaomi.xmpush.thrift.i;

import java.util.HashMap;

import org.apache.thrift.b;

final class c implements Runnable {
    c() {
    }

    public void run() {
        if (f.b(MiPushClient.sContext) != null) {
            b iVar = new i();
            iVar.b(a.a(MiPushClient.sContext).c());
            iVar.c("client_info_update");
            iVar.a(MiPushClient.generatePacketID());
            iVar.a(new HashMap());
            iVar.h().put("imei_md5", d.a(f.b(MiPushClient.sContext)));
            g.a(MiPushClient.sContext).a(iVar, a.Notification, false, null);
        }
    }
}
