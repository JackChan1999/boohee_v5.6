package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.SimpleCallback;

class br implements SimpleCallback {
    final /* synthetic */ MQMessage     a;
    final /* synthetic */ MeiQiaService b;

    br(MeiQiaService meiQiaService, MQMessage mQMessage) {
        this.b = meiQiaService;
        this.a = mQMessage;
    }

    public void onFailure(int i, String str) {
        this.b.a(this.a);
    }

    public void onSuccess() {
        this.b.a(this.a);
    }
}
