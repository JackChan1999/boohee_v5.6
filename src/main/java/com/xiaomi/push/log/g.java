package com.xiaomi.push.log;

import android.util.Log;

import com.xiaomi.channel.commonutils.file.c;
import com.xiaomi.channel.commonutils.misc.b.b;

class g extends b {
    final /* synthetic */ f a;

    g(f fVar) {
        this.a = fVar;
    }

    public void b() {
        if (!f.f.isEmpty()) {
            try {
                if (c.d()) {
                    this.a.b();
                } else {
                    Log.w(this.a.d, "SDCard is unavailable.");
                }
            } catch (Throwable e) {
                Log.e(this.a.d, "", e);
            }
        }
    }
}
