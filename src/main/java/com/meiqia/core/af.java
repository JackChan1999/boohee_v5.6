package com.meiqia.core;

class af implements Runnable {
    final /* synthetic */ ae a;

    af(ae aeVar) {
        this.a = aeVar;
    }

    public void run() {
        this.a.a.onSuccess();
    }
}
