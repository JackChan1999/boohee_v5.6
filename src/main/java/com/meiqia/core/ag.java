package com.meiqia.core;

import com.meiqia.core.callback.OnProgressCallback;

class ag implements Runnable {
    final /* synthetic */ OnProgressCallback a;
    final /* synthetic */ b                  b;

    ag(b bVar, OnProgressCallback onProgressCallback) {
        this.b = bVar;
        this.a = onProgressCallback;
    }

    public void run() {
        this.a.onFailure(20005, "sdcard_is_not_available");
    }
}
