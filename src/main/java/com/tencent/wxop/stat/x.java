package com.tencent.wxop.stat;

import com.tencent.wxop.stat.a.d;

final class x implements Runnable {
    final /* synthetic */ d       bP;
    final /* synthetic */ boolean bR;
    final /* synthetic */ boolean ba;
    final /* synthetic */ t       cg;
    final /* synthetic */ aj      ck;

    x(t tVar, d dVar, aj ajVar, boolean z, boolean z2) {
        this.cg = tVar;
        this.bP = dVar;
        this.ck = ajVar;
        this.bR = z;
        this.ba = z2;
    }

    public final void run() {
        this.cg.a(this.bP, this.ck, this.bR, this.ba);
    }
}
