package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: SocialServiceImpl */
class w extends UMAsyncTask<Integer> {
    final /* synthetic */ SocializeClientListener a;
    final /* synthetic */ InitializeController    b;
    final /* synthetic */ Context                 c;
    final /* synthetic */ v                       d;

    w(v vVar, SocializeClientListener socializeClientListener, InitializeController
            initializeController, Context context) {
        this.d = vVar;
        this.a = socializeClientListener;
        this.b = initializeController;
        this.c = context;
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
        return Integer.valueOf(this.b.actionBarInit(this.c));
    }

    protected void a(Integer num) {
        super.onPostExecute(num);
        if (200 != num.intValue()) {
            SocializeUtils.errorHanding(this.c, null, num);
        }
        if (this.a != null) {
            this.a.onComplete(num.intValue(), this.d.a);
        }
    }
}
