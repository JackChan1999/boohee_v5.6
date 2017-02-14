package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: SocialServiceImpl */
class z extends UMAsyncTask<MultiStatus> {
    final /* synthetic */ MulStatusListener    a;
    final /* synthetic */ InitializeController b;
    final /* synthetic */ Context              c;
    final /* synthetic */ SNSPair              d;
    final /* synthetic */ String[]             e;
    final /* synthetic */ SHARE_MEDIA          f;
    final /* synthetic */ v                    g;

    z(v vVar, MulStatusListener mulStatusListener, InitializeController initializeController,
      Context context, SNSPair sNSPair, String[] strArr, SHARE_MEDIA share_media) {
        this.g = vVar;
        this.a = mulStatusListener;
        this.b = initializeController;
        this.c = context;
        this.d = sNSPair;
        this.e = strArr;
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
    }

    protected MultiStatus a() {
        return this.b.follow(this.c, this.d, this.e);
    }

    protected void a(MultiStatus multiStatus) {
        super.onPostExecute(multiStatus);
        if (200 != multiStatus.getStCode()) {
            SocializeUtils.errorHanding(this.c, this.f, Integer.valueOf(multiStatus.getStCode()));
        }
        if (this.a != null) {
            this.a.onComplete(multiStatus, multiStatus.getStCode(), this.g.a);
        }
    }
}
