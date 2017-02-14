package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: ShareServiceImpl */
class q extends UMAsyncTask<MultiStatus> {
    final /* synthetic */ SnsPostListener a;
    final /* synthetic */ String          b;
    final /* synthetic */ String          c;
    final /* synthetic */ Context         d;
    final /* synthetic */ UMShareMsg      e;
    final /* synthetic */ SHARE_MEDIA     f;
    final /* synthetic */ m               g;

    q(m mVar, SnsPostListener snsPostListener, String str, String str2, Context context,
      UMShareMsg uMShareMsg, SHARE_MEDIA share_media) {
        this.g = mVar;
        this.a = snsPostListener;
        this.b = str;
        this.c = str2;
        this.d = context;
        this.e = uMShareMsg;
        this.f = share_media;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((MultiStatus) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.a != null) {
            this.a.onStart();
        }
        if (this.g.a.isFireCallback()) {
            this.g.c.fireAllListenersOnStart(SnsPostListener.class);
        }
    }

    protected MultiStatus a() {
        SNSPair sNSPair = new SNSPair(this.b, this.c);
        MultiStatus a;
        if (m.a(this.g, this.d)) {
            a = m.a(this.g, this.d, new SNSPair[]{sNSPair}, this.e);
            return a == null ? new MultiStatus(StatusCode.ST_CODE_SDK_UNKNOW) : a;
        } else {
            a = new MultiStatus(StatusCode.ST_CODE_SDK_INITQUEUE_FAILED);
            if (this.f == null) {
                return a;
            }
            int platformStatus = a.getPlatformStatus(this.f);
            if (StatusCode.ST_CODE_SDK_UNKNOW == platformStatus) {
                return a;
            }
            a.setStCode(platformStatus);
            return a;
        }
    }

    protected void a(MultiStatus multiStatus) {
        super.onPostExecute(multiStatus);
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(this.b);
        int platformStatus = multiStatus.getPlatformStatus(convertToEmun);
        if (platformStatus != 200) {
            SocializeUtils.errorHanding(this.d, convertToEmun, Integer.valueOf(platformStatus));
        }
        if (this.a != null) {
            this.a.onComplete(convertToEmun, platformStatus, this.g.a);
        }
        if (this.g.a.isFireCallback()) {
            this.g.c.fireAllListenersOnComplete(SnsPostListener.class, convertToEmun,
                    platformStatus, this.g.a);
        }
        m.a(this.g, this.d, (InitializeController) this.g.b);
        this.g.c.cleanListeners();
        m.d(this.g);
        this.g.a.setFireCallback(false);
    }
}
