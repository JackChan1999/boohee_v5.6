package com.meiqia.core;

class ar implements Runnable {
    final /* synthetic */ aq a;

    ar(aq aqVar) {
        this.a = aqVar;
    }

    public void run() {
        if (this.a.b != null) {
            this.a.b.onSuccess(this.a.a, 1);
        }
    }
}
