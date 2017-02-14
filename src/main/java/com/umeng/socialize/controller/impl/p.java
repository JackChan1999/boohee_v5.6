package com.umeng.socialize.controller.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: ShareServiceImpl */
class p implements UMAuthListener {
    final /* synthetic */ ProgressDialog  a;
    final /* synthetic */ Context         b;
    final /* synthetic */ SnsPostListener c;
    final /* synthetic */ Intent          d;
    final /* synthetic */ m               e;

    p(m mVar, ProgressDialog progressDialog, Context context, SnsPostListener snsPostListener,
      Intent intent) {
        this.e = mVar;
        this.a = progressDialog;
        this.b = context;
        this.c = snsPostListener;
        this.d = intent;
    }

    public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
        SocializeUtils.safeCloseDialog(this.a);
    }

    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        SocializeUtils.safeCloseDialog(this.a);
        String string = bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
        if (!TextUtils.isEmpty(string)) {
            OauthHelper.setUsid(this.b, share_media, string);
            Log.d(m.b(this.e), "do oauth successed " + share_media);
            if (m.c(this.e)) {
                m.a(this.e, false);
                this.e.postShare(this.b, string, share_media, this.c);
                return;
            }
            if (this.c != null) {
                this.e.c.registerListener(this.c);
            }
            this.b.startActivity(this.d);
        }
    }

    public void onCancel(SHARE_MEDIA share_media) {
        SocializeUtils.safeCloseDialog(this.a);
    }

    public void onStart(SHARE_MEDIA share_media) {
        SocializeUtils.safeCloseDialog(this.a);
    }
}
