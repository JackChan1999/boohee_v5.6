package com.meiqia.core;

import com.meiqia.core.b.i;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.util.ErrorCode;

class aq implements da {
    final /* synthetic */ MQMessage             a;
    final /* synthetic */ OnMessageSendCallback b;
    final /* synthetic */ b                     c;

    aq(b bVar, MQMessage mQMessage, OnMessageSendCallback onMessageSendCallback) {
        this.c = bVar;
        this.a = mQMessage;
        this.b = onMessageSendCallback;
    }

    public void a(String str, long j) {
        long a = i.a(str);
        long id = this.a.getId();
        this.a.setCreated_on(a);
        this.a.setId(j);
        this.a.setStatus("arrived");
        if (this.c.e() != null) {
            this.a.setAgent_nickname(this.c.e().getNickname());
        }
        this.c.d.a(this.a, id);
        this.c.a(new ar(this));
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case ErrorCode.CONV_END /*19997*/:
                this.c.a(null);
                this.c.a(null);
                this.c.b(this.a, this.b);
                return;
            default:
                this.a.setStatus("failed");
                this.c.d.a(this.a);
                if (this.b != null) {
                    this.b.onFailure(this.a, i, str);
                    return;
                }
                return;
        }
    }
}
