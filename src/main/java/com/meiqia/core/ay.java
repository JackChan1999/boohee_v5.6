package com.meiqia.core;

class ay implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ ax     c;

    ay(ax axVar, String str, String str2) {
        this.c = axVar;
        this.a = str;
        this.b = str2;
    }

    public void run() {
        if (this.c.a != null) {
            this.c.a.a(this.a, this.b);
        }
    }
}
