package com.xiaomi.push.service;

import android.util.Base64;

import com.xiaomi.channel.commonutils.misc.b.b;
import com.xiaomi.network.HttpUtils;
import com.xiaomi.push.protobuf.a.a;
import com.xiaomi.smack.util.h;

class ag extends b {
    boolean a = false;
    final /* synthetic */ af b;

    ag(af afVar) {
        this.b = afVar;
    }

    public void b() {
        try {
            a b = a.b(Base64.decode(HttpUtils.a(h.a(), "http://resolver.msg.xiaomi.net/psc/?t=a",
                    null), 10));
            if (b != null) {
                this.b.b = b;
                this.a = true;
                this.b.g();
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("fetch config failure: " + e.getMessage());
        }
    }

    public void c() {
        this.b.c = null;
        if (this.a) {
            synchronized (this.b) {
            }
            for (af.a a : (af.a[]) this.b.a.toArray(new af.a[this.b.a.size()])) {
                a.a(this.b.b);
            }
        }
    }
}
