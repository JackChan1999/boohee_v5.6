package com.meiqia.core;

class ak implements Runnable {
    final /* synthetic */ int    a;
    final /* synthetic */ String b;
    final /* synthetic */ ah     c;

    ak(ah ahVar, int i, String str) {
        this.c = ahVar;
        this.a = i;
        this.b = str;
    }

    public void run() {
        this.c.a.onFailure(this.a, this.b);
    }
}
