package com.xiaomi.push.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class e extends Handler {
    final /* synthetic */ d a;

    e(d dVar, Looper looper) {
        this.a = dVar;
        super(looper);
    }

    public void handleMessage(Message message) {
        this.a.b = true;
        this.a.a = System.currentTimeMillis();
        if (message.obj instanceof com.xiaomi.push.service.XMPushService.e) {
            ((com.xiaomi.push.service.XMPushService.e) message.obj).c();
        }
        this.a.b = false;
    }
}
