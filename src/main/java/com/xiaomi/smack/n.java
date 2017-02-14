package com.xiaomi.smack;

import com.xiaomi.push.service.XMPushService.e;

class n extends e {
    final /* synthetic */ int       a;
    final /* synthetic */ Exception b;
    final /* synthetic */ l         c;

    n(l lVar, int i, int i2, Exception exception) {
        this.c = lVar;
        this.a = i2;
        this.b = exception;
        super(i);
    }

    public void a() {
        this.c.v.a(this.a, this.b);
    }

    public String b() {
        return "shutdown the connection. " + this.a + ", " + this.b;
    }
}
