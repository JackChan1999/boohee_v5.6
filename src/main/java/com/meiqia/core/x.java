package com.meiqia.core;

class x implements Runnable {
    final /* synthetic */ w a;

    x(w wVar) {
        this.a = wVar;
    }

    public void run() {
        this.a.a.onSuccess();
    }
}
