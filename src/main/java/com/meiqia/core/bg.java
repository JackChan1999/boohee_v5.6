package com.meiqia.core;

import com.meiqia.core.callback.OnClientOnlineCallback;
import com.meiqia.core.callback.SuccessCallback;

class bg extends SuccessCallback {
    final /* synthetic */ String                 a;
    final /* synthetic */ OnClientOnlineCallback b;
    final /* synthetic */ MQManager              c;

    bg(MQManager mQManager, String str, OnClientOnlineCallback onClientOnlineCallback) {
        this.c = mQManager;
        this.a = str;
        this.b = onClientOnlineCallback;
    }

    public void onSuccess() {
        MQManager.c.a(this.a, new bh(this));
    }
}
