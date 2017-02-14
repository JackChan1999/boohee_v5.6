package com.tencent.wxop.stat;

import android.content.Context;

final class as implements Runnable {
    final /* synthetic */ String  a;
    final /* synthetic */ f       bM;
    final /* synthetic */ Context co;

    as(String str, Context context, f fVar) {
        this.a = str;
        this.co = context;
        this.bM = fVar;
    }

    public final void run() {
        try {
            synchronized (e.aT) {
                if (e.aT.size() >= c.v()) {
                    e.aV.error("The number of page events exceeds the maximum value " + Integer
                            .toString(c.v()));
                    return;
                }
                e.aR = this.a;
                if (e.aT.containsKey(e.aR)) {
                    e.aV.d("Duplicate PageID : " + e.aR + ", onResume() repeated?");
                    return;
                }
                e.aT.put(e.aR, Long.valueOf(System.currentTimeMillis()));
                e.a(this.co, true, this.bM);
            }
        } catch (Throwable th) {
            e.aV.b(th);
            e.a(this.co, th);
        }
    }
}
