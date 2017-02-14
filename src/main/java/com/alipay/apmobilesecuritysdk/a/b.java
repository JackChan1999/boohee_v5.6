package com.alipay.apmobilesecuritysdk.a;

final class b extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ a d;

    b(a aVar, String str, String str2, String str3) {
        this.d = aVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    public final void run() {
        a.a(this.d, this.a, this.b, this.c);
    }
}
