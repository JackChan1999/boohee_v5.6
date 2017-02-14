package com.xiaomi.push.log;

import com.xiaomi.channel.commonutils.misc.b.b;

class d extends b {
    b a;
    final /* synthetic */ b b;

    d(b bVar) {
        this.b = bVar;
    }

    public void b() {
        b bVar = (b) this.b.a.peek();
        if (bVar != null && bVar.d()) {
            this.a = (b) this.b.a.remove();
            this.a.b();
        }
    }

    public void c() {
        if (this.a != null) {
            this.a.c();
        }
    }
}
