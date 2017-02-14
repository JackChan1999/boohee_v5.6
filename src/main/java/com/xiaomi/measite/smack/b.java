package com.xiaomi.measite.smack;

import com.xiaomi.smack.util.f;

import java.util.Date;

class b implements f {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void a(String str) {
        com.xiaomi.channel.commonutils.logger.b.c("SMACK " + this.a.b.format(new Date()) + " RCV " +
                " (" + this.a.c.hashCode() + "): " + str);
    }
}
