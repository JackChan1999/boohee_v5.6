package com.meiqia.core;

import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;

class j implements OnRegisterDeviceTokenCallback {
    final /* synthetic */ OnRegisterDeviceTokenCallback a;
    final /* synthetic */ b                             b;

    j(b bVar, OnRegisterDeviceTokenCallback onRegisterDeviceTokenCallback) {
        this.b = bVar;
        this.a = onRegisterDeviceTokenCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess() {
        this.b.a(new k(this));
    }
}
