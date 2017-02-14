package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnMessageSendCallback;

class aw implements dc {
    final /* synthetic */ MQMessage             a;
    final /* synthetic */ String                b;
    final /* synthetic */ OnMessageSendCallback c;
    final /* synthetic */ b                     d;

    aw(b bVar, MQMessage mQMessage, String str, OnMessageSendCallback onMessageSendCallback) {
        this.d = bVar;
        this.a = mQMessage;
        this.b = str;
        this.c = onMessageSendCallback;
    }

    public void a(String str, String str2) {
        this.a.setMedia_url(str2);
        this.a.setContent(str);
        if ("file".equals(this.b)) {
            this.a.setExtra("");
        }
        this.d.a(this.a, this.c);
    }

    public void onFailure(int i, String str) {
        this.a.setStatus("failed");
        this.d.d.a(this.a);
        if (this.c != null) {
            this.c.onFailure(this.a, i, str);
        }
    }
}
