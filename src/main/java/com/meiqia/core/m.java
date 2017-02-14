package com.meiqia.core;

class m implements Runnable {
    final /* synthetic */ l a;

    m(l lVar) {
        this.a = lVar;
    }

    public void run() {
        b.a(this.a.c).j(this.a.a);
        if (this.a.b != null) {
            this.a.b.onSuccess();
        }
    }
}
