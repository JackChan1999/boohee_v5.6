package com.xiaomi.measite.smack;

import com.umeng.socialize.common.SocializeConstants;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.smack.a;
import com.xiaomi.smack.d;

import java.util.Date;

class e implements d {
    final /* synthetic */ a a;

    e(a aVar) {
        this.a = aVar;
    }

    public void a(a aVar) {
        b.c("SMACK " + this.a.b.format(new Date()) + " Connection reconnected (" + this.a.c
                .hashCode() + SocializeConstants.OP_CLOSE_PAREN);
    }

    public void a(a aVar, int i, Exception exception) {
        b.c("SMACK " + this.a.b.format(new Date()) + " Connection closed (" + this.a.c.hashCode()
                + SocializeConstants.OP_CLOSE_PAREN);
    }

    public void a(a aVar, Exception exception) {
        b.c("SMACK " + this.a.b.format(new Date()) + " Reconnection failed due to an exception ("
                + this.a.c.hashCode() + SocializeConstants.OP_CLOSE_PAREN);
        exception.printStackTrace();
    }

    public void b(a aVar) {
        b.c("SMACK " + this.a.b.format(new Date()) + " Connection started (" + this.a.c.hashCode
                () + SocializeConstants.OP_CLOSE_PAREN);
    }
}
