package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class ap implements OnGetMessageListCallback {
    final /* synthetic */ b a;

    ap(b bVar) {
        this.a = bVar;
    }

    public void onFailure(int i, String str) {
    }

    public void onSuccess(List<MQMessage> list) {
        for (MQMessage a : list) {
            a.a(this.a.e).a(a);
        }
    }
}
