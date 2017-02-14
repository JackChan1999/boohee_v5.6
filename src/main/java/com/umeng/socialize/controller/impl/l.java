package com.umeng.socialize.controller.impl;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.Log;

/* compiled from: AuthServiceImpl */
class l implements SocializeClientListener {
    final /* synthetic */ a a;

    l(a aVar) {
        this.a = aVar;
    }

    public void onStart() {
    }

    public void onComplete(int i, SocializeEntity socializeEntity) {
        if (this.a.b == null) {
            return;
        }
        if (i == 200) {
            this.a.b.onComplete(this.a.e, this.a.a);
            Log.v("10.13", "auth success");
            return;
        }
        this.a.b.onError(new SocializeException(i, "upload platform appkey failed."), this.a.a);
    }
}
