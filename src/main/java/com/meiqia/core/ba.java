package com.meiqia.core;

class ba implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ az     b;

    ba(az azVar, String str) {
        this.b = azVar;
        this.a = str;
    }

    public void run() {
        if (this.b.b != null) {
            this.b.b.onSuccess(this.a);
        }
    }
}
