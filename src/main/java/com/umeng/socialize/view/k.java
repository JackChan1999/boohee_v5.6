package com.umeng.socialize.view;

import android.os.Handler;
import android.os.Message;

import com.umeng.socialize.utils.Log;

/* compiled from: OauthDialog */
class k extends Handler {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (message.what == 1 && this.a.e != null) {
            this.a.e.setVisibility(8);
        }
        if (message.what == 2) {
            try {
                this.a.d();
            } catch (Exception e) {
                Log.e(j.a, "follow error", e);
            }
        }
    }
}
