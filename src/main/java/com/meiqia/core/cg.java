package com.meiqia.core;

import com.meiqia.core.callback.OnFailureCallBack;

class cg implements Runnable {
    final /* synthetic */ OnFailureCallBack a;
    final /* synthetic */ bu                b;

    cg(bu buVar, OnFailureCallBack onFailureCallBack) {
        this.b = buVar;
        this.a = onFailureCallBack;
    }

    public void run() {
        this.a.onFailure(-1, "GeneralSecurityException");
    }
}
