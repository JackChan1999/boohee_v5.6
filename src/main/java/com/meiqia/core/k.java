package com.meiqia.core;

class k implements Runnable {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public void run() {
        if (this.a.a != null) {
            this.a.a.onSuccess();
        }
    }
}
