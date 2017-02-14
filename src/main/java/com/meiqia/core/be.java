package com.meiqia.core;

import com.meiqia.core.callback.OnFailureCallBack;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.core.callback.SuccessCallback;

class be implements OnInitCallback {
    final /* synthetic */ SuccessCallback   a;
    final /* synthetic */ OnFailureCallBack b;
    final /* synthetic */ MQManager         c;

    be(MQManager mQManager, SuccessCallback successCallback, OnFailureCallBack onFailureCallBack) {
        this.c = mQManager;
        this.a = successCallback;
        this.b = onFailureCallBack;
    }

    public void onFailure(int i, String str) {
        this.b.onFailure(i, str);
    }

    public void onSuccess(String str) {
        this.a.onSuccess();
    }
}
