package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class g implements OnGetMessageListCallback {
    final /* synthetic */ f a;

    g(f fVar) {
        this.a = fVar;
    }

    public void onFailure(int i, String str) {
        b.a(this.a.d, new i(this, i, str));
    }

    public void onSuccess(List<MQMessage> list) {
        b.a(this.a.d, new h(this, list));
    }
}
