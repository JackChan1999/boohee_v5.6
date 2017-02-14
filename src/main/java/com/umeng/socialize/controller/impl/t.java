package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: ShareServiceImpl */
class t extends UMAsyncTask<Integer> {
    final /* synthetic */ SnsPostListener a;
    final /* synthetic */ SHARE_MEDIA     b;
    final /* synthetic */ String          c;
    final /* synthetic */ String          d;
    final /* synthetic */ Context         e;
    final /* synthetic */ m               f;

    t(m mVar, SnsPostListener snsPostListener, SHARE_MEDIA share_media, String str, String str2,
      Context context) {
        this.f = mVar;
        this.a = snsPostListener;
        this.b = share_media;
        this.c = str;
        this.d = str2;
        this.e = context;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((Integer) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.a != null) {
            this.a.onStart();
        }
    }

    protected Integer a() {
        SNSPair[] sNSPairArr = new SNSPair[]{new SNSPair(this.b.toString(), this.c)};
        UMShareMsg uMShareMsg = new UMShareMsg();
        uMShareMsg.mWeiBoId = this.d;
        return Integer.valueOf(m.a(this.f, this.e) ? m.a(this.f, this.e, sNSPairArr, uMShareMsg)
                .getStCode() : StatusCode.ST_CODE_SDK_INITQUEUE_FAILED);
    }

    protected void a(Integer num) {
        super.onPostExecute(num);
        if (200 != num.intValue()) {
            SocializeUtils.errorHanding(this.e, null, num);
        }
        if (this.a != null) {
            this.a.onComplete(this.b, num.intValue(), this.f.a);
        }
    }
}
