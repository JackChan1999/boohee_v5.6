package com.meiqia.core;

class i implements Runnable {
    final /* synthetic */ int    a;
    final /* synthetic */ String b;
    final /* synthetic */ g      c;

    i(g gVar, int i, String str) {
        this.c = gVar;
        this.a = i;
        this.b = str;
    }

    public void run() {
        if (this.c.a.c != null) {
            this.c.a.c.onFailure(this.a, this.b);
        }
    }
}
