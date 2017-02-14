package com.zxinsight;

import com.zxinsight.common.util.c;
import com.zxinsight.share.b.a;
import com.zxinsight.share.e;

final class y extends a {
    final /* synthetic */ String a;

    y(String str) {
        this.a = str;
    }

    public void a(String str) {
        c.b("share success !");
    }

    public void b(String str) {
        TrackAgent.currentEvent().onSocialShare(this.a, str);
    }

    public void a(e eVar) {
        c.b("share failed !");
    }

    public void a() {
        c.b("share cancelled !");
    }
}
