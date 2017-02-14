package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.net.y;

/* compiled from: SocialServiceImpl */
class aa extends UMAsyncTask<y> {
    final /* synthetic */ UMDataListener       a;
    final /* synthetic */ InitializeController b;
    final /* synthetic */ Context              c;
    final /* synthetic */ SNSPair              d;
    final /* synthetic */ v                    e;

    aa(v vVar, UMDataListener uMDataListener, InitializeController initializeController, Context
            context, SNSPair sNSPair) {
        this.e = vVar;
        this.a = uMDataListener;
        this.b = initializeController;
        this.c = context;
        this.d = sNSPair;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((y) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.a.onStart();
    }

    protected y a() {
        return this.b.getPlatformInfo(this.c, this.d);
    }

    protected void a(y yVar) {
        super.onPostExecute(yVar);
        if (yVar != null) {
            this.a.onComplete(yVar.mStCode, yVar.a);
        } else {
            this.a.onComplete(StatusCode.ST_CODE_SDK_NORESPONSE, null);
        }
    }
}
