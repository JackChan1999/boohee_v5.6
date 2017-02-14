package com.meiqia.core;

import com.meiqia.meiqiasdk.util.ErrorCode;

class cm implements Runnable {
    final /* synthetic */ cj a;

    cm(cj cjVar) {
        this.a = cjVar;
    }

    public void run() {
        if (this.a.a != null) {
            this.a.a.onFailure(ErrorCode.CONV_END, "conversation not found");
        }
    }
}
