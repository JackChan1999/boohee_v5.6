package com.umeng.socialize.controller.impl;

import android.content.Context;
import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.OauthHelper;

/* compiled from: AuthServiceImpl */
class d implements UMAuthListener {
    final /* synthetic */ Context          a;
    final /* synthetic */ UMAuthListener   b;
    final /* synthetic */ UMAuthListener[] c;
    final /* synthetic */ a                d;

    d(a aVar, Context context, UMAuthListener uMAuthListener, UMAuthListener[] uMAuthListenerArr) {
        this.d = aVar;
        this.a = context;
        this.b = uMAuthListener;
        this.c = uMAuthListenerArr;
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        int i = 0;
        this.d.a.addOauthData(this.a, share_media, 0);
        OauthHelper.remove(this.a, share_media);
        OauthHelper.removeTokenExpiresIn(this.a, share_media);
        if (this.b != null) {
            this.b.onError(socializeException, share_media);
        }
        if (this.c != null) {
            UMAuthListener[] uMAuthListenerArr = this.c;
            int length = uMAuthListenerArr.length;
            while (i < length) {
                uMAuthListenerArr[i].onError(socializeException, share_media);
                i++;
            }
        }
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        int i = 0;
        if (bundle != null) {
            this.d.a.addOauthData(this.a, share_media, 1);
            this.d.a(this.a, share_media, bundle);
        } else {
            this.d.a.addOauthData(this.a, share_media, 0);
        }
        if (this.b != null) {
            this.b.onComplete(bundle, share_media);
        }
        if (this.c != null) {
            UMAuthListener[] uMAuthListenerArr = this.c;
            int length = uMAuthListenerArr.length;
            while (i < length) {
                uMAuthListenerArr[i].onComplete(bundle, share_media);
                i++;
            }
        }
    }

    public void onCancel(SHARE_MEDIA share_media) {
        int i = 0;
        this.d.a.addOauthData(this.a, share_media, 0);
        OauthHelper.remove(this.a, share_media);
        OauthHelper.removeTokenExpiresIn(this.a, share_media);
        if (this.b != null) {
            this.b.onCancel(share_media);
        }
        if (this.c != null) {
            UMAuthListener[] uMAuthListenerArr = this.c;
            int length = uMAuthListenerArr.length;
            while (i < length) {
                uMAuthListenerArr[i].onCancel(share_media);
                i++;
            }
        }
    }

    public void onStart(SHARE_MEDIA share_media) {
        if (this.b != null) {
            this.b.onStart(share_media);
        }
        if (this.c != null) {
            for (UMAuthListener onStart : this.c) {
                onStart.onStart(share_media);
            }
        }
    }
}
