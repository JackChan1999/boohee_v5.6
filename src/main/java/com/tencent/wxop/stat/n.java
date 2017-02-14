package com.tencent.wxop.stat;

import com.tencent.wxop.stat.a.c;

import java.lang.Thread.UncaughtExceptionHandler;

final class n implements UncaughtExceptionHandler {
    n() {
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        if (c.l() && e.aY != null) {
            if (c.x()) {
                t.s(e.aY).b(new c(e.aY, e.a(e.aY, false, null), th, thread), null, false, true);
                e.aV.debug("MTA has caught the following uncaught exception:");
                e.aV.a(th);
            }
            e.p(e.aY);
            if (e.aW != null) {
                e.aV.e("Call the original uncaught exception handler.");
                if (!(e.aW instanceof n)) {
                    e.aW.uncaughtException(thread, th);
                }
            }
        }
    }
}
