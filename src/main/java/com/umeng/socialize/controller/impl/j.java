package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.c;
import com.umeng.socialize.net.d;

import java.util.Map;

/* compiled from: AuthServiceImpl */
class j extends UMAsyncTask<d> {
    final /* synthetic */ UMDataListener a;
    final /* synthetic */ Context        b;
    final /* synthetic */ SHARE_MEDIA[]  c;
    final /* synthetic */ a              d;

    j(a aVar, UMDataListener uMDataListener, Context context, SHARE_MEDIA[] share_mediaArr) {
        this.d = aVar;
        this.a = uMDataListener;
        this.b = context;
        this.c = share_mediaArr;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((d) obj);
    }

    protected void onPreExecute() {
        if (this.a != null) {
            this.a.onStart();
        }
    }

    protected d a() {
        return (d) new SocializeClient().execute(new c(this.b, this.d.a, this.c));
    }

    protected void a(d dVar) {
        Map map = null;
        int i = StatusCode.ST_CODE_SDK_UNKNOW;
        if (dVar != null) {
            map = dVar.a;
            i = dVar.mStCode;
        }
        if (this.a != null) {
            this.a.onComplete(i, map);
        }
    }
}
