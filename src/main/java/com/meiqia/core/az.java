package com.meiqia.core;

import com.meiqia.core.b.j;
import com.meiqia.core.callback.OnGetClientCallback;
import com.meiqia.core.callback.OnGetMQClientIdCallBackOn;

class az implements OnGetClientCallback {
    final /* synthetic */ b                         a;
    private               OnGetMQClientIdCallBackOn b;

    public az(b bVar, OnGetMQClientIdCallBackOn onGetMQClientIdCallBackOn) {
        this.a = bVar;
        this.b = onGetMQClientIdCallBackOn;
    }

    public void onFailure(int i, String str) {
        if (this.b != null) {
            this.b.onFailure(i, str);
        }
    }

    public void onSuccess(boolean z, String str, String str2, String str3, String str4, String
            str5, String str6) {
        j.a(str2, this.a.b, str, str2, str3, str4, str5, str6);
        this.a.a(new ba(this, str2));
    }
}
