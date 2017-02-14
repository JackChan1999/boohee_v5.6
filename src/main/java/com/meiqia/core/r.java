package com.meiqia.core;

import java.util.List;

class r implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ q    b;

    r(q qVar, List list) {
        this.b = qVar;
        this.a = list;
    }

    public void run() {
        this.b.b.onSuccess(this.a);
    }
}
