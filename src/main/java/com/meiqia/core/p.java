package com.meiqia.core;

import android.text.TextUtils;

import com.meiqia.core.callback.OnClientInfoCallback;

class p implements OnClientInfoCallback {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ b      d;

    p(b bVar, String str, String str2, String str3) {
        this.d = bVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
    }

    public void onFailure(int i, String str) {
    }

    public void onSuccess() {
        if (TextUtils.isEmpty(this.a)) {
            String m = this.d.b.m();
            this.d.b.k(this.b);
            this.d.b.i(this.c);
            this.d.b.k(m);
            return;
        }
        this.d.b.i(this.c);
    }
}
