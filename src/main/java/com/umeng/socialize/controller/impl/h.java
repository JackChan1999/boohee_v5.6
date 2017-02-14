package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/* compiled from: AuthServiceImpl */
class h implements SocializeClientListener {
    final /* synthetic */ SocializeClientListener a;
    final /* synthetic */ UMToken                 b;
    final /* synthetic */ Context                 c;
    final /* synthetic */ a                       d;

    h(a aVar, SocializeClientListener socializeClientListener, UMToken uMToken, Context context) {
        this.d = aVar;
        this.a = socializeClientListener;
        this.b = uMToken;
        this.c = context;
    }

    public void onStart() {
        if (this.a != null) {
            this.a.onStart();
        }
    }

    public void onComplete(int i, SocializeEntity socializeEntity) {
        if (i == 200 && this.b != null) {
            socializeEntity.addStatisticsData(this.c, SHARE_MEDIA.convertToEmun(this.b.mPaltform)
                    , 13);
        }
        if (this.a != null) {
            this.a.onComplete(i, socializeEntity);
        }
    }
}
