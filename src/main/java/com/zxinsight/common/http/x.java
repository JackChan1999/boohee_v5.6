package com.zxinsight.common.http;

class x implements Runnable {
    final /* synthetic */ Request a;
    final /* synthetic */ byte[]  b;
    final /* synthetic */ w       c;

    x(w wVar, Request request, byte[] bArr) {
        this.c = wVar;
        this.a = request;
        this.b = bArr;
    }

    public void run() {
        this.a.a(this.b);
    }
}
