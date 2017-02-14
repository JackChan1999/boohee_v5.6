package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.b.l;

final class ao implements Runnable {
    final /* synthetic */ f bN = null;
    final /* synthetic */ Context e;

    ao(Context context) {
        this.e = context;
    }

    public final void run() {
        if (this.e == null) {
            e.aV.error("The Context of StatService.onPause() can not be null!");
        } else {
            e.b(this.e, l.B(this.e), this.bN);
        }
    }
}
