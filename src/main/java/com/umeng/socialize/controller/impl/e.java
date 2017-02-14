package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

/* compiled from: AuthServiceImpl */
class e implements UMAuthListener {
    Context a = this.b.getApplicationContext();
    final /* synthetic */ Activity       b;
    final /* synthetic */ UMAuthListener c;
    final /* synthetic */ a              d;

    e(a aVar, Activity activity, UMAuthListener uMAuthListener) {
        this.d = aVar;
        this.b = activity;
        this.c = uMAuthListener;
    }

    public void onStart(SHARE_MEDIA share_media) {
        if (this.c != null) {
            this.c.onStart(share_media);
        }
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        Toast.makeText(this.a, "授权失败,请重试！", 1).show();
        if (this.c != null) {
            this.c.onError(socializeException, share_media);
        }
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        if (TextUtils.isEmpty(bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID))) {
            Toast.makeText(this.a, "授权失败,请重试！", 1).show();
        }
        if (this.c != null) {
            this.c.onComplete(bundle, share_media);
        }
    }

    public void onCancel(SHARE_MEDIA share_media) {
        if (this.c != null) {
            this.c.onCancel(share_media);
        }
    }
}
