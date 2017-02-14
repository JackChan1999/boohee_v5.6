package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.a.d;
import com.tencent.wxop.stat.a.h;
import com.umeng.socialize.common.SocializeConstants;

final class k implements Runnable {
    final /* synthetic */ String  b;
    final /* synthetic */ f       bM;
    final /* synthetic */ Context e;

    k(Context context, String str, f fVar) {
        this.e = context;
        this.b = str;
        this.bM = fVar;
    }

    public final void run() {
        try {
            Long l;
            e.p(this.e);
            synchronized (e.aT) {
                l = (Long) e.aT.remove(this.b);
            }
            if (l != null) {
                Long valueOf = Long.valueOf((System.currentTimeMillis() - l.longValue()) / 1000);
                if (valueOf.longValue() <= 0) {
                    valueOf = Long.valueOf(1);
                }
                String O = e.aS;
                if (O != null && O.equals(this.b)) {
                    O = SocializeConstants.OP_DIVIDER_MINUS;
                }
                d hVar = new h(this.e, O, this.b, e.a(this.e, false, this.bM), valueOf, this.bM);
                if (!this.b.equals(e.aR)) {
                    e.aV.warn("Invalid invocation since previous onResume on diff page.");
                }
                new p(hVar).ah();
                e.aS = this.b;
                return;
            }
            e.aV.d("Starttime for PageID:" + this.b + " not found, lost onResume()?");
        } catch (Throwable th) {
            e.aV.b(th);
            e.a(this.e, th);
        }
    }
}
