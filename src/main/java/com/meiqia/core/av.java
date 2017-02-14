package com.meiqia.core;

import com.meiqia.meiqiasdk.util.ErrorCode;

class av implements Runnable {
    final /* synthetic */ au a;

    av(au auVar) {
        this.a = auVar;
    }

    public void run() {
        if (this.a.a.b != null) {
            this.a.a.b.onSuccess(this.a.a.a, ErrorCode.NO_AGENT_ONLINE);
        }
    }
}
