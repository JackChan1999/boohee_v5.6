package com.meiqia.core;

class o implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ n      b;

    o(n nVar, String str) {
        this.b = nVar;
        this.a = str;
    }

    public void run() {
        if (this.b.b != null) {
            this.b.b.onSuccess(this.a);
        }
    }
}
