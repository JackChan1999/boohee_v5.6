package com.meiqia.core;

import com.meiqia.core.callback.OnClientOnlineCallback;
import com.meiqia.core.callback.SuccessCallback;

class bf extends SuccessCallback {
    final /* synthetic */ OnClientOnlineCallback a;
    final /* synthetic */ MQManager              b;

    bf(MQManager mQManager, OnClientOnlineCallback onClientOnlineCallback) {
        this.b = mQManager;
        this.a = onClientOnlineCallback;
    }

    public void onSuccess() {
        this.b.a(this.a);
    }
}
