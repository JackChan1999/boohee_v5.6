package com.meiqia.core;

class ad implements Runnable {
    final /* synthetic */ ac a;

    ad(ac acVar) {
        this.a = acVar;
    }

    public void run() {
        this.a.a.onSuccess();
    }
}
