package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: ShareServiceImpl */
class s extends UMAsyncTask<MultiStatus> {
    final /* synthetic */ MulStatusListener a;
    final /* synthetic */ Context           b;
    final /* synthetic */ SNSPair[]         c;
    final /* synthetic */ UMShareMsg        d;
    final /* synthetic */ Map               e;
    final /* synthetic */ m                 f;

    s(m mVar, MulStatusListener mulStatusListener, Context context, SNSPair[] sNSPairArr,
      UMShareMsg uMShareMsg, Map map) {
        this.f = mVar;
        this.a = mulStatusListener;
        this.b = context;
        this.c = sNSPairArr;
        this.d = uMShareMsg;
        this.e = map;
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
    }

    protected MultiStatus a() {
        return m.a(this.f, this.b) ? m.a(this.f, this.b, this.c, this.d) : new MultiStatus
                (StatusCode.ST_CODE_SDK_INITQUEUE_FAILED);
    }

    protected void a(MultiStatus multiStatus) {
        super.onPostExecute(multiStatus);
        Map platformCode = multiStatus.getPlatformCode();
        platformCode.putAll(this.e);
        for (SHARE_MEDIA share_media : platformCode.keySet()) {
            int intValue = ((Integer) platformCode.get(share_media)).intValue();
            if (200 != intValue) {
                SocializeUtils.errorHanding(this.b, share_media, Integer.valueOf(intValue));
            }
        }
        if (this.a != null) {
            this.a.onComplete(multiStatus, multiStatus.getStCode(), this.f.a);
        }
    }
}
