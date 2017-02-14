package com.meiqia.core;

class cn implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ cj  b;

    cn(cj cjVar, int i) {
        this.b = cjVar;
        this.a = i;
    }

    public void run() {
        if (this.b.a != null) {
            this.b.a.onFailure(this.a, "success = false");
        }
    }
}
