package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.b.l;

final class an implements Runnable {
    final /* synthetic */ Context e;

    an(Context context) {
        this.e = context;
    }

    public final void run() {
        g.r(e.aY).aa();
        l.a(this.e, true);
        t.s(this.e);
        ak.Z(this.e);
        e.aW = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new n());
        if (c.j() == d.APP_LAUNCH) {
            e.o(this.e);
        }
        if (c.k()) {
            e.aV.e("Init MTA StatService success.");
        }
    }
}
