package com.meiqia.core;

import com.meiqia.meiqiasdk.util.ErrorCode;

import java.io.IOException;

class ck implements Runnable {
    final /* synthetic */ IOException a;
    final /* synthetic */ cj          b;

    ck(cj cjVar, IOException iOException) {
        this.b = cjVar;
        this.a = iOException;
    }

    public void run() {
        String str = "IOException";
        if (this.a != null) {
            str = this.a.getMessage();
        }
        if (this.b.a != null) {
            this.b.a.onFailure(ErrorCode.NET_NOT_WORK, str);
        }
    }
}
