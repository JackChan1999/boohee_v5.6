package com.meiqia.core;

import android.content.Intent;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnMessageSendCallback;

class at implements OnMessageSendCallback {
    final /* synthetic */ as a;

    at(as asVar) {
        this.a = asVar;
    }

    public void onFailure(MQMessage mQMessage, int i, String str) {
        this.a.b.onFailure(mQMessage, i, str);
    }

    public void onSuccess(MQMessage mQMessage, int i) {
        Intent intent = new Intent(b.c(this.a.c), MeiQiaService.class);
        intent.setAction("ACTION_OPEN_SOCKET");
        b.c(this.a.c).startService(intent);
        this.a.b.onSuccess(mQMessage, i);
    }
}
