package com.alipay.apmobilesecuritysdk.b;

import com.alipay.security.mobile.module.commonutils.LOG;

final class c implements Runnable {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public final void run() {
        try {
            this.a.a();
        } catch (Throwable th) {
            LOG.logException(th);
        }
    }
}
