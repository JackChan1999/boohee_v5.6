package com.xiaomi.smack;

import com.xiaomi.push.service.XMPushService.e;

class m extends e {
    final /* synthetic */ long a;
    final /* synthetic */ l    b;

    m(l lVar, int i, long j) {
        this.b = lVar;
        this.a = j;
        super(i);
    }

    public void a() {
        if (this.b.h() && !this.b.a(this.a)) {
            this.b.v.a(22, null);
        }
    }

    public String b() {
        return "check the ping-pong.";
    }
}
