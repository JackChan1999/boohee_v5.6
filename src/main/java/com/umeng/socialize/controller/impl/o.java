package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;

/* compiled from: ShareServiceImpl */
class o extends LoginListener {
    final /* synthetic */ Activity a;
    final /* synthetic */ m        b;

    o(m mVar, Activity activity) {
        this.b = mVar;
        this.a = activity;
    }

    public void loginSuccessed(SHARE_MEDIA share_media, boolean z) {
        if (z) {
            m.a(this.b).showAtLocation(this.a.getWindow().getDecorView(), 80, 0, 0);
        } else if (share_media != null) {
            this.b.postShare(this.a, share_media, null);
        } else {
            m.a(this.b).showAtLocation(this.a.getWindow().getDecorView(), 80, 0, 0);
        }
    }

    public void loginFailed(int i) {
        Toast.makeText(this.a, this.a.getResources().getString(ResContainer.getResourceId(this.a,
                ResType.STRING, "umeng_socialize_tip_loginfailed")), 0).show();
    }
}
