package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchFriendsListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.l;
import com.umeng.socialize.utils.Log;

/* compiled from: SocialServiceImpl */
class y extends UMAsyncTask<l> {
    final /* synthetic */ FetchFriendsListener a;
    final /* synthetic */ InitializeController b;
    final /* synthetic */ Context              c;
    final /* synthetic */ SHARE_MEDIA          d;
    final /* synthetic */ String               e;
    final /* synthetic */ v                    f;

    y(v vVar, FetchFriendsListener fetchFriendsListener, InitializeController
            initializeController, Context context, SHARE_MEDIA share_media, String str) {
        this.f = vVar;
        this.a = fetchFriendsListener;
        this.b = initializeController;
        this.c = context;
        this.d = share_media;
        this.e = str;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((l) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.a != null) {
            this.a.onStart();
        }
    }

    protected l a() {
        l lVar = null;
        try {
            lVar = this.b.getFriends(this.c, this.d, this.e);
        } catch (NullPointerException e) {
            Log.e(v.a(), e.toString());
        } catch (SocializeException e2) {
            Log.e(v.a(), e2.toString());
        }
        return lVar;
    }

    protected void a(l lVar) {
        super.onPostExecute(lVar);
        if (this.a == null) {
            return;
        }
        if (lVar != null) {
            this.a.onComplete(lVar.mStCode, lVar.a);
        } else {
            this.a.onComplete(StatusCode.ST_CODE_SDK_UNKNOW, null);
        }
    }
}
