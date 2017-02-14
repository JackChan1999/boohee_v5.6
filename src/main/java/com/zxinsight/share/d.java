package com.zxinsight.share;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zxinsight.common.util.o;
import com.zxinsight.share.domain.b;

import java.lang.ref.WeakReference;

class d extends Handler {
    private WeakReference<Context> a;

    public d(Context context) {
        this.a = new WeakReference(context);
    }

    public void handleMessage(Message message) {
        Activity activity = (Activity) this.a.get();
        if (activity != null) {
            o.d();
            switch (message.what) {
                case 0:
                    a.b(activity, a.g, (b) message.obj);
                    return;
                case 1:
                    Toast.makeText(activity.getApplicationContext(), "Failed to connect " +
                            "network...", 0).show();
                    return;
                default:
                    return;
            }
        }
    }
}
