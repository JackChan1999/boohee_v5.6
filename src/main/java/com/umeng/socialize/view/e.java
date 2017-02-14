package com.umeng.socialize.view;

import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.utils.OauthHelper;

/* compiled from: LoginAgent */
class e implements OnClickListener {
    final /* synthetic */ SnsPlatform a;
    final /* synthetic */ LoginAgent  b;

    e(LoginAgent loginAgent, SnsPlatform snsPlatform) {
        this.b = loginAgent;
        this.a = snsPlatform;
    }

    public void onClick(View view) {
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(this.a.mKeyword);
        if (OauthHelper.isAuthenticated(this.b.b, convertToEmun)) {
            this.b.a(convertToEmun);
        } else {
            this.b.c.doOauthVerify(this.b.b, convertToEmun, new f(this, convertToEmun));
        }
    }
}
