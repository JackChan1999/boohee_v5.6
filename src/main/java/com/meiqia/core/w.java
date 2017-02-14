package com.meiqia.core;

import com.meiqia.core.callback.SimpleCallback;

class w implements SimpleCallback {
    final /* synthetic */ SimpleCallback a;
    final /* synthetic */ b              b;

    w(b bVar, SimpleCallback simpleCallback) {
        this.b = bVar;
        this.a = simpleCallback;
    }

    public void onFailure(int i, String str) {
        this.a.onFailure(i, str);
    }

    public void onSuccess() {
        this.b.a(new x(this));
    }
}
