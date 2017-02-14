package com.meiqia.core;

class ai implements Runnable {
    final /* synthetic */ ah a;

    ai(ah ahVar) {
        this.a = ahVar;
    }

    public void run() {
        this.a.a.onSuccess();
    }
}
