package com.umeng.socialize.view;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.exception.SocializeException;

/* compiled from: OauthDialog */
class r implements SocializeClientListener {
    final /* synthetic */ j a;

    r(j jVar) {
        this.a = jVar;
    }

    public void onStart() {
    }

    public void onComplete(int i, SocializeEntity socializeEntity) {
        if (i == 200) {
            this.a.b.loadUrl(this.a.a(socializeEntity, this.a.m));
            return;
        }
        if (this.a.c != null) {
            this.a.c.onError(new SocializeException("can`t initlized entity.."), this.a.m);
        }
        this.a.b.loadData("Error:502  Please make sure your network is available.", "text/html",
                "UTF-8");
    }
}
