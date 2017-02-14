package com.xiaomi.stats;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.smack.a;

public class d implements com.xiaomi.smack.d {
    XMPushService a;
    a             b;
    private int       c;
    private Exception d;

    d(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    Exception a() {
        return this.d;
    }

    public void a(a aVar) {
        g.a(0, com.xiaomi.push.thrift.a.CONN_SUCCESS.a(), aVar.c(), aVar.i());
    }

    public void a(a aVar, int i, Exception exception) {
        if (this.c == 0 && this.d == null) {
            this.c = i;
            this.d = exception;
            g.b(aVar.c(), exception);
        }
    }

    public void a(a aVar, Exception exception) {
        g.a(0, com.xiaomi.push.thrift.a.CHANNEL_CON_FAIL.a(), 1, aVar.c(), com.xiaomi.channel
                .commonutils.network.d.d(this.a) ? 1 : 0);
    }

    public void b(a aVar) {
        this.c = 0;
        this.d = null;
        this.b = aVar;
        g.a(0, com.xiaomi.push.thrift.a.CONN_SUCCESS.a());
    }
}
