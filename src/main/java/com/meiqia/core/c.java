package com.meiqia.core;

import com.meiqia.core.callback.OnGetMQClientIdCallBackOn;
import com.meiqia.core.callback.OnInitCallback;

class c implements OnGetMQClientIdCallBackOn {
    final /* synthetic */ OnInitCallback a;
    final /* synthetic */ b              b;

    c(b bVar, OnInitCallback onInitCallback) {
        this.b = bVar;
        this.a = onInitCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess(String str) {
        this.b.b.k(str);
        if (this.a != null) {
            this.a.onSuccess(str);
        }
        this.b.c(str);
    }
}
