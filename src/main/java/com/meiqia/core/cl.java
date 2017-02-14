package com.meiqia.core;

class cl implements Runnable {
    final /* synthetic */ cj a;

    cl(cj cjVar) {
        this.a = cjVar;
    }

    public void run() {
        if (this.a.a != null) {
            this.a.a.onFailure(-1, "GeneralSecurityException");
        }
    }
}
