package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class f implements OnGetMessageListCallback {
    final /* synthetic */ long                     a;
    final /* synthetic */ int                      b;
    final /* synthetic */ OnGetMessageListCallback c;
    final /* synthetic */ b                        d;

    f(b bVar, long j, int i, OnGetMessageListCallback onGetMessageListCallback) {
        this.d = bVar;
        this.a = j;
        this.b = i;
        this.c = onGetMessageListCallback;
    }

    public void onFailure(int i, String str) {
        if (this.c != null) {
            this.c.onFailure(i, str);
        }
    }

    public void onSuccess(List<MQMessage> list) {
        this.d.d.b((List) list);
        MQManager.getInstance(this.d.e).getMQMessageFromDatabase(this.a, this.b, new g(this));
    }
}
