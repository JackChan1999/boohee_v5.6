package com.xiaomi.mipush.sdk;

import android.database.ContentObserver;
import android.os.Handler;

import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.push.service.aa;

class h extends ContentObserver {
    final /* synthetic */ g a;

    h(g gVar, Handler handler) {
        this.a = gVar;
        super(handler);
    }

    public void onChange(boolean z) {
        g.a(this.a, Integer.valueOf(aa.a(g.a(this.a)).b()));
        if (g.b(this.a).intValue() != 0) {
            g.a(this.a).getContentResolver().unregisterContentObserver(this);
            if (d.d(g.a(this.a))) {
                this.a.c();
            }
        }
    }
}
