package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: AuthServiceImpl */
class i extends UMAsyncTask<Integer> {
    final /* synthetic */ SocializeClientListener a;
    final /* synthetic */ Context                 b;
    final /* synthetic */ UMToken                 c;
    final /* synthetic */ a                       d;

    i(a aVar, SocializeClientListener socializeClientListener, Context context, UMToken uMToken) {
        this.d = aVar;
        this.a = socializeClientListener;
        this.b = context;
        this.c = uMToken;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((Integer) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.a.onStart();
    }

    protected Integer a() {
        return Integer.valueOf(this.d.a(this.b, this.c));
    }

    protected void a(Integer num) {
        super.onPostExecute(num);
        if (200 != num.intValue()) {
            SocializeUtils.errorHanding(this.b, null, num);
        }
        this.a.onComplete(num.intValue(), this.d.a);
    }
}
