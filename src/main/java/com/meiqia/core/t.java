package com.meiqia.core;

import java.util.List;

class t implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ s    b;

    t(s sVar, List list) {
        this.b = sVar;
        this.a = list;
    }

    public void run() {
        this.b.a.onSuccess(this.a);
    }
}
