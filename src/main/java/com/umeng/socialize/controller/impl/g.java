package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.t;
import com.umeng.socialize.net.u;

import java.util.Map;

/* compiled from: AuthServiceImpl */
class g extends UMAsyncTask<u> {
    final /* synthetic */ Context        a;
    final /* synthetic */ UMDataListener b;
    final /* synthetic */ Map            c;
    final /* synthetic */ a              d;

    g(a aVar, Context context, UMDataListener uMDataListener, Map map) {
        this.d = aVar;
        this.a = context;
        this.b = uMDataListener;
        this.c = map;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((u) obj);
    }

    protected u a() {
        if (!this.d.a.mInitialized) {
            new BaseController(this.d.a).a(this.a);
        }
        return (u) new SocializeClient().execute(new t(this.a, this.d.a));
    }

    protected void a(u uVar) {
        super.onPostExecute(uVar);
        this.b.onStart();
        this.b.onComplete(200, this.c);
    }
}
