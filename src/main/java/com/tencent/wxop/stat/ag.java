package com.tencent.wxop.stat;

import com.tencent.wxop.stat.b.l;

import java.util.TimerTask;

final class ag extends TimerTask {
    final /* synthetic */ af de;

    ag(af afVar) {
        this.de = afVar;
    }

    public final void run() {
        if (c.k()) {
            l.av().b((Object) "TimerTask run");
        }
        e.q(this.de.h);
        cancel();
        this.de.ah();
    }
}
