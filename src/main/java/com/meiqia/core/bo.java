package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class bo implements OnGetMessageListCallback {
    final /* synthetic */ OnGetMessageListCallback a;
    final /* synthetic */ MQManager                b;

    bo(MQManager mQManager, OnGetMessageListCallback onGetMessageListCallback) {
        this.b = mQManager;
        this.a = onGetMessageListCallback;
    }

    public void onFailure(int i, String str) {
        this.a.onFailure(i, str);
    }

    public void onSuccess(List<MQMessage> list) {
        this.a.onSuccess(list);
    }
}
