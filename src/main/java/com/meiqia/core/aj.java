package com.meiqia.core;

class aj implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ ah  b;

    aj(ah ahVar, int i) {
        this.b = ahVar;
        this.a = i;
    }

    public void run() {
        this.b.a.onProgress(this.a);
    }
}
