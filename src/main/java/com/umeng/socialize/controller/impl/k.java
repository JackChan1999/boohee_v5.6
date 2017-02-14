package com.umeng.socialize.controller.impl;

import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: AuthServiceImpl */
class k implements UMAuthListener {
    final /* synthetic */ a a;

    k(a aVar) {
        this.a = aVar;
    }

    public void onStart(SHARE_MEDIA share_media) {
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        if (this.a.b != null) {
            this.a.b.onError(socializeException, share_media);
        }
    }

    private UMToken a(Bundle bundle) {
        String string = bundle.getString("access_token");
        String string2 = bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
        String string3 = bundle.getString("openid");
        String str = "";
        if (this.a.a == SHARE_MEDIA.WEIXIN_CIRCLE || this.a.a == SHARE_MEDIA.WEIXIN) {
            str = "wxsession";
        } else {
            str = this.a.a.toString();
        }
        return UMToken.buildToken(new SNSPair(str, string2), string, string3);
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        if (bundle != null && bundle.containsKey("access_token") && bundle.containsKey
                (SocializeProtocolConstants.PROTOCOL_KEY_UID)) {
            this.a.e = bundle;
            UMToken a = a(bundle);
            a.setExpireIn(bundle.getString("expires_in"));
            Object string = bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN);
            if (!TextUtils.isEmpty(string)) {
                a.setRefreshToken(string);
                a.setScope(bundle.getString("scope"));
                a.setUmengSecret(SocializeUtils.reverse(SocializeUtils.getAppkey(this.a.d)));
            }
            this.a.g.uploadToken(this.a.d, a, this.a.a());
            return;
        }
        Log.e(this.a.g.e, share_media.toString() + " authorize data is invalid.");
        if (this.a.b != null) {
            this.a.b.onError(new SocializeException("no found access_token"), share_media);
        }
    }

    public void onCancel(SHARE_MEDIA share_media) {
        if (this.a.b != null) {
            this.a.b.onCancel(share_media);
        }
    }
}
