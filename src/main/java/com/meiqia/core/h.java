package com.meiqia.core;

import java.util.List;

class h implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ g    b;

    h(g gVar, List list) {
        this.b = gVar;
        this.a = list;
    }

    public void run() {
        if (this.b.a.c != null) {
            this.b.a.c.onSuccess(this.a);
        }
    }
}
