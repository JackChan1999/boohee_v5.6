package com.meiqia.core;

class v implements Runnable {
    final /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    public void run() {
        if (this.a.a != null) {
            this.a.a.onSuccess();
        }
    }
}
