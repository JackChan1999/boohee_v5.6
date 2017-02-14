package com.meiqia.core;

import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQClient;
import com.meiqia.core.callback.OnInitCallback;

class bh implements OnInitCallback {
    final /* synthetic */ bg a;

    bh(bg bgVar) {
        this.a = bgVar;
    }

    public void onFailure(int i, String str) {
        this.a.b.onFailure(i, str);
    }

    public void onSuccess(String str) {
        MQClient a = j.a(str, MQManager.a());
        if (!(a == null || a.getTrackId().equals(b.a.getTrackId()))) {
            MQManager.a(this.a.c);
        }
        MQManager.b().a(a);
        MQManager.a(this.a.c, this.a.b);
    }
}
