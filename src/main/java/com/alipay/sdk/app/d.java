package com.alipay.sdk.app;

import android.webkit.SslErrorHandler;

final class d implements Runnable {
    final /* synthetic */ SslErrorHandler a;
    final /* synthetic */ a b;

    d(a aVar, SslErrorHandler sslErrorHandler) {
        this.b = aVar;
        this.a = sslErrorHandler;
    }

    public final void run() {
        com.alipay.sdk.widget.d.a(this.b.a, "安全警告", "由于您的设备缺少根证书，将无法校验该访问站点的安全性，可能存在风险，请选择是否继续？", "继续", new e(this), "退出", new f(this));
    }
}
