package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class ao implements OnGetMessageListCallback {
    final /* synthetic */ an a;

    ao(an anVar) {
        this.a = anVar;
    }

    public void onFailure(int i, String str) {
        this.a.a.onFailure(i, str);
    }

    public void onSuccess(List<MQMessage> list) {
        this.a.a.a(b.d(this.a.b), b.e(this.a.b), list);
    }
}
