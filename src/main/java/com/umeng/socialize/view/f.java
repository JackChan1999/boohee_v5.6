package com.umeng.socialize.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

/* compiled from: LoginAgent */
class f implements UMAuthListener {
    final /* synthetic */ SHARE_MEDIA a;
    final /* synthetic */ e           b;

    f(e eVar, SHARE_MEDIA share_media) {
        this.b = eVar;
        this.a = share_media;
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        this.b.b.a.c();
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        if (TextUtils.isEmpty(bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID))) {
            this.b.b.a.c();
        } else {
            this.b.b.a(this.a);
        }
    }

    public void onCancel(SHARE_MEDIA share_media) {
        this.b.b.a.c();
    }

    public void onStart(SHARE_MEDIA share_media) {
        this.b.b.a.b();
    }
}
