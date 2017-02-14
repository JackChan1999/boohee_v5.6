package com.tencent.wxop.stat;

final class y implements Runnable {
    final /* synthetic */ ah O;
    final /* synthetic */ t  cl;

    y(t tVar, ah ahVar) {
        this.cl = tVar;
        this.O = ahVar;
    }

    public final void run() {
        this.cl.a(this.O);
    }
}
