package com.tencent.wxop.stat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

final class z extends BroadcastReceiver {
    final /* synthetic */ g cm;

    z(g gVar) {
        this.cm = gVar;
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.cm.be != null) {
            this.cm.be.a(new ae(this));
        }
    }
}
