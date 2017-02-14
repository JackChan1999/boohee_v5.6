package com.zxinsight.common.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class q extends Handler {
    private final WeakReference<Context> a;

    public q(Context context) {
        this.a = new WeakReference(context);
    }

    public void handleMessage(Message message) {
        if (this.a.get() != null) {
        }
    }
}
