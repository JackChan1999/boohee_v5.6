package com.tencent.wxop.stat;

import java.util.List;

final class am implements Runnable {
    final /* synthetic */ List bc;
    final /* synthetic */ aj   ck;
    final /* synthetic */ ak   dm;

    am(ak akVar, List list, aj ajVar) {
        this.dm = akVar;
        this.bc = list;
        this.ck = ajVar;
    }

    public final void run() {
        this.dm.a(this.bc, this.ck);
    }
}
