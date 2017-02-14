package com.tencent.wxop.stat;

import java.util.List;

final class v implements Runnable {
    final /* synthetic */ boolean bR = true;
    final /* synthetic */ List    bc;
    final /* synthetic */ boolean ch;
    final /* synthetic */ t       ci;

    v(t tVar, List list, boolean z) {
        this.ci = tVar;
        this.bc = list;
        this.ch = z;
    }

    public final void run() {
        this.ci.a(this.bc, this.ch);
        if (this.bR) {
            this.bc.clear();
        }
    }
}
