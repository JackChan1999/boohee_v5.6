package com.umeng.socialize.view;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.utils.LoginInfoHelp;

/* compiled from: LoginAgent */
class h implements SocializeClientListener {
    final /* synthetic */ LoginAgent a;

    h(LoginAgent loginAgent) {
        this.a = loginAgent;
    }

    public void onStart() {
        this.a.a.b();
    }

    public void onComplete(int i, SocializeEntity socializeEntity) {
        this.a.a.c();
        if (i == 200) {
            LoginInfoHelp.setGuest(this.a.b, true);
            this.a.dismissLoginDialog();
            if (this.a.f != null) {
                this.a.f.loginSuccessed(null, false);
            }
        } else if (this.a.f != null) {
            this.a.f.loginFailed(i);
        }
    }
}
