package com.umeng.socialize.view;

import com.umeng.socialize.view.abs.SocialPopupDialog.SwitchListener;

/* compiled from: LoginAgent */
class d implements SwitchListener {
    final /* synthetic */ LoginAgent a;

    d(LoginAgent loginAgent) {
        this.a = loginAgent;
    }

    public void a() {
    }

    public void b() {
        if (this.a.f != null) {
            this.a.f.dissmiss();
        }
    }
}
