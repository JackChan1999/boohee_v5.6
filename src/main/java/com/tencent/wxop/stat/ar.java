package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.a.a;
import com.tencent.wxop.stat.a.b;
import com.tencent.wxop.stat.a.d;

final class ar implements Runnable {
    final /* synthetic */ f bN = null;
    final /* synthetic */ b do;
    final /* synthetic */ Context e;

    ar(Context context, b bVar) {
        this.e = context;
        this. do=bVar;
    }

    public final void run() {
        try {
            d aVar = new a(this.e, e.a(this.e, false, this.bN), this. do.a, this.bN);
            aVar.ab().bm = this. do.bm;
            new p(aVar).ah();
        } catch (Throwable th) {
            e.aV.b(th);
            e.a(this.e, th);
        }
    }
}
