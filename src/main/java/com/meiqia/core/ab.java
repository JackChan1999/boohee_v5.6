package com.meiqia.core;

class ab implements Runnable {
    final /* synthetic */ aa a;

    ab(aa aaVar) {
        this.a = aaVar;
    }

    public void run() {
        this.a.c.onSuccess();
    }
}
