package com.tencent.wxop.stat;

final class ab implements Runnable {
    final /* synthetic */ int aI;
    final /* synthetic */ t   cl;

    ab(t tVar, int i) {
        this.cl = tVar;
        this.aI = i;
    }

    public final void run() {
        t.a(this.cl, this.aI, true);
        t.a(this.cl, this.aI, false);
    }
}
