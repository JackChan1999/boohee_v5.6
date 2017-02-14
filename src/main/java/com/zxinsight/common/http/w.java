package com.zxinsight.common.http;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

class w implements Executor {
    Handler a = new Handler(Looper.getMainLooper());

    w() {
    }

    public void a(Request request, byte[] bArr) {
        execute(new x(this, request, bArr));
    }

    public void a(Request request) {
        execute(new y(this, request));
    }

    public void execute(Runnable runnable) {
        this.a.post(runnable);
    }
}
