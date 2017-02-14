package com.meiqia.core;

class e implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ d      c;

    e(d dVar, String str, String str2) {
        this.c = dVar;
        this.a = str;
        this.b = str2;
    }

    public void run() {
        if (this.c.a != null) {
            this.c.a.a(this.a, this.b);
        }
    }
}
