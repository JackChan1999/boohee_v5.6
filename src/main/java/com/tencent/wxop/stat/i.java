package com.tencent.wxop.stat;

import android.content.Context;

final class i implements Runnable {
    final /* synthetic */ Context e;

    i(Context context) {
        this.e = context;
    }

    public final void run() {
        try {
            new Thread(new o(this.e), "NetworkMonitorTask").start();
        } catch (Throwable th) {
            e.aV.b(th);
            e.a(this.e, th);
        }
    }
}
