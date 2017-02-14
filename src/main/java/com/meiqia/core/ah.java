package com.meiqia.core;

import com.meiqia.core.callback.OnProgressCallback;

class ah implements OnProgressCallback {
    final /* synthetic */ OnProgressCallback a;
    final /* synthetic */ b                  b;

    ah(b bVar, OnProgressCallback onProgressCallback) {
        this.b = bVar;
        this.a = onProgressCallback;
    }

    public void onFailure(int i, String str) {
        this.b.a(new ak(this, i, str));
    }

    public void onProgress(int i) {
        this.b.a(new aj(this, i));
    }

    public void onSuccess() {
        this.b.a(new ai(this));
    }
}
