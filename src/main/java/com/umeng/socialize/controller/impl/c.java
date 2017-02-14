package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.j;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: AuthServiceImpl */
class c extends UMAsyncTask<Integer> {
    final /* synthetic */ SocializeClientListener a;
    final /* synthetic */ Context                 b;
    final /* synthetic */ SHARE_MEDIA             c;
    final /* synthetic */ a                       d;

    c(a aVar, SocializeClientListener socializeClientListener, Context context, SHARE_MEDIA
            share_media) {
        this.d = aVar;
        this.a = socializeClientListener;
        this.b = context;
        this.c = share_media;
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
        SocializeReseponse execute = new SocializeClient().execute(new j(this.b, this.d.a, this.c));
        if (execute != null) {
            return Integer.valueOf(execute.mStCode);
        }
        return Integer.valueOf(StatusCode.ST_CODE_SDK_UNKNOW);
    }

    protected void a(Integer num) {
        super.onPostExecute(num);
        if (num.intValue() == 200) {
            OauthHelper.remove(this.b, this.c);
            OauthHelper.removeTokenExpiresIn(this.b, this.c);
        } else {
            SocializeUtils.errorHanding(this.b, this.c, num);
        }
        if (this.a != null) {
            this.a.onComplete(num.intValue(), this.d.a);
        }
    }
}
