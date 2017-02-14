package com.meiqia.core;

import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQClient;
import com.meiqia.core.callback.OnInitCallback;

final class bd implements OnInitCallback {
    final /* synthetic */ OnInitCallback a;

    bd(OnInitCallback onInitCallback) {
        this.a = onInitCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess(String str) {
        MQClient a = j.a(str, MQManager.d);
        if (b.a == null) {
            MQManager.c.a(a);
        }
        MQManager.l = true;
        if (this.a != null) {
            this.a.onSuccess(str);
        }
    }
}
