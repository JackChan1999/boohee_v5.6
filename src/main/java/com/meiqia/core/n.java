package com.meiqia.core;

import com.meiqia.core.b.j;
import com.meiqia.core.callback.OnGetClientCallback;
import com.meiqia.core.callback.OnInitCallback;

class n implements OnGetClientCallback {
    final /* synthetic */ String         a;
    final /* synthetic */ OnInitCallback b;
    final /* synthetic */ b              c;

    n(b bVar, String str, OnInitCallback onInitCallback) {
        this.c = bVar;
        this.a = str;
        this.b = onInitCallback;
    }

    public void onFailure(int i, String str) {
        if (this.b != null) {
            this.b.onFailure(i, str);
        }
    }

    public void onSuccess(boolean z, String str, String str2, String str3, String str4, String
            str5, String str6) {
        this.c.b.a(this.a, str2);
        j.a(str2, this.c.b, str, str2, str3, str4, str5, str6);
        this.c.b.k(str2);
        this.c.a(new o(this, str2));
    }
}
