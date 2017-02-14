package com.umeng.socialize.controller.impl;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.Log;

/* compiled from: AuthServiceImpl */
class f implements UMAuthListener {
    final /* synthetic */ UMAuthListener a;
    final /* synthetic */ Activity       b;
    final /* synthetic */ a              c;
    private boolean d = false;

    f(a aVar, UMAuthListener uMAuthListener, Activity activity) {
        this.c = aVar;
        this.a = uMAuthListener;
        this.b = activity;
    }

    public void onStart(SHARE_MEDIA share_media) {
        this.a.onStart(share_media);
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        Log.e("com.umeng.socialize", "do auth by sso failed." + socializeException.toString());
        Log.e(this.c.e, "", socializeException);
        this.d = !this.d;
        if (!this.d || share_media.isCustomPlatform()) {
            this.a.onError(socializeException, share_media);
        } else {
            this.c.a(this.b, share_media, (UMAuthListener) this);
        }
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        Log.v("10.12", "bundle=" + bundle);
        this.a.onComplete(bundle, share_media);
    }

    public void onCancel(SHARE_MEDIA share_media) {
        this.a.onCancel(share_media);
    }
}
