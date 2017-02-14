package com.meiqia.core;

import com.meiqia.core.callback.OnClientInfoCallback;

class l implements OnClientInfoCallback {
    final /* synthetic */ String               a;
    final /* synthetic */ OnClientInfoCallback b;
    final /* synthetic */ b                    c;

    l(b bVar, String str, OnClientInfoCallback onClientInfoCallback) {
        this.c = bVar;
        this.a = str;
        this.b = onClientInfoCallback;
    }

    public void onFailure(int i, String str) {
        if (this.b != null) {
            this.b.onFailure(i, str);
        }
    }

    public void onSuccess() {
        this.c.a(new m(this));
    }
}
