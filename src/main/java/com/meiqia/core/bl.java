package com.meiqia.core;

import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.core.callback.SimpleCallback;

class bl implements OnInitCallback {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ MQManager      b;

    bl(MQManager mQManager, SimpleCallback simpleCallback) {
        this.b = mQManager;
        this.a = simpleCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess(String str) {
        this.b.a(str);
        if (this.a != null) {
            this.a.onSuccess();
        }
    }
}
