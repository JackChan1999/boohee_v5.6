package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class q implements OnGetMessageListCallback {
    final /* synthetic */ long                     a;
    final /* synthetic */ OnGetMessageListCallback b;
    final /* synthetic */ b                        c;

    q(b bVar, long j, OnGetMessageListCallback onGetMessageListCallback) {
        this.c = bVar;
        this.a = j;
        this.b = onGetMessageListCallback;
    }

    public void onFailure(int i, String str) {
        if (this.b != null) {
            this.b.onFailure(i, str);
        }
    }

    public void onSuccess(List<MQMessage> list) {
        this.c.a((List) list, this.a);
        if (this.b != null) {
            this.c.a(new r(this, list));
        }
    }
}
