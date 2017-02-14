package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class s implements OnGetMessageListCallback {
    final /* synthetic */ OnGetMessageListCallback a;
    final /* synthetic */ b                        b;

    s(b bVar, OnGetMessageListCallback onGetMessageListCallback) {
        this.b = bVar;
        this.a = onGetMessageListCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess(List<MQMessage> list) {
        if (this.a != null) {
            this.b.a(new t(this, list));
        }
    }
}
