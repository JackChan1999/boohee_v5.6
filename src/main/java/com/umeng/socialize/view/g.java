package com.umeng.socialize.view;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/* compiled from: LoginAgent */
class g implements SocializeClientListener {
    final /* synthetic */ SHARE_MEDIA a;
    final /* synthetic */ LoginAgent  b;

    g(LoginAgent loginAgent, SHARE_MEDIA share_media) {
        this.b = loginAgent;
        this.a = share_media;
    }

    public void onStart() {
        if (this.b.a != null && !this.b.a.isShowing()) {
            this.b.a.b();
        }
    }

    public void onComplete(int i, SocializeEntity socializeEntity) {
        this.b.a.c();
        if (i == 200) {
            this.b.dismissLoginDialog();
            if (this.b.f != null) {
                this.b.f.loginSuccessed(this.a, false);
            }
        } else if (this.b.f != null) {
            this.b.f.loginFailed(i);
        }
    }
}
