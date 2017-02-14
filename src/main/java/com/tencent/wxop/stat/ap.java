package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.a.c;
import com.tencent.wxop.stat.a.f;

final class ap implements Runnable {
    final /* synthetic */ Throwable dn;
    final /* synthetic */ Context   e;

    ap(Context context, Throwable th) {
        this.e = context;
        this.dn = th;
    }

    public final void run() {
        try {
            if (c.l()) {
                new p(new c(this.e, e.a(this.e, false, null), this.dn, f.bw)).ah();
            }
        } catch (Throwable th) {
            e.aV.d("reportSdkSelfException error: " + th);
        }
    }
}
