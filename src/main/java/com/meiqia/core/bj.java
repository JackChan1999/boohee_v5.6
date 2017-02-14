package com.meiqia.core;

import com.meiqia.core.callback.OnGetMQClientIdCallBackOn;

class bj implements OnGetMQClientIdCallBackOn {
    final /* synthetic */ bi a;

    bj(bi biVar) {
        this.a = biVar;
    }

    public void onFailure(int i, String str) {
        this.a.b.onFailure(20003, "clientId is wrong");
    }

    public void onSuccess(String str) {
        this.a.c.setClientOnlineWithClientId(str, this.a.b);
    }
}
