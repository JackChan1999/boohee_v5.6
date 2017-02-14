package com.meiqia.core;

import com.meiqia.core.callback.OnProgressCallback;

class al implements Runnable {
    final /* synthetic */ OnProgressCallback a;
    final /* synthetic */ b                  b;

    al(b bVar, OnProgressCallback onProgressCallback) {
        this.b = bVar;
        this.a = onProgressCallback;
    }

    public void run() {
        this.a.onFailure(20000, "download file failed");
    }
}
