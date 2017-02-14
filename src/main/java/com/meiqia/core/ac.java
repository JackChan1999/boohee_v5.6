package com.meiqia.core;

import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.SimpleCallback;

class ac implements OnClientInfoCallback {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ b              b;

    ac(b bVar, SimpleCallback simpleCallback) {
        this.b = bVar;
        this.a = simpleCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess() {
        if (this.a != null) {
            this.b.a(new ad(this));
        }
    }
}
