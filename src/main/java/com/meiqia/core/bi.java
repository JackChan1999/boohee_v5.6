package com.meiqia.core;

import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQClient;
import com.meiqia.core.callback.OnClientOnlineCallback;
import com.meiqia.core.callback.SuccessCallback;

class bi extends SuccessCallback {
    final /* synthetic */ String                 a;
    final /* synthetic */ OnClientOnlineCallback b;
    final /* synthetic */ MQManager              c;

    bi(MQManager mQManager, String str, OnClientOnlineCallback onClientOnlineCallback) {
        this.c = mQManager;
        this.a = str;
        this.b = onClientOnlineCallback;
    }

    public void onSuccess() {
        MQClient a = j.a(this.a, MQManager.d);
        if (!(a == null || a.getTrackId().equals(b.a.getTrackId()))) {
            this.c.c();
        }
        if (a != null) {
            MQManager.c.a(a);
            this.c.a(this.b);
            return;
        }
        MQManager.c.a(this.a, new bj(this));
    }
}
