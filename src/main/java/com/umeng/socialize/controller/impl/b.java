package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.net.GetPlatformKeyResponse;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.e;
import com.umeng.socialize.utils.SocializeUtils;

/* compiled from: AuthServiceImpl */
class b extends UMAsyncTask<GetPlatformKeyResponse> {
    final /* synthetic */ UMDataListener a;
    final /* synthetic */ Context        b;
    final /* synthetic */ a              c;

    b(a aVar, UMDataListener uMDataListener, Context context) {
        this.c = aVar;
        this.a = uMDataListener;
        this.b = context;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((GetPlatformKeyResponse) obj);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.a.onStart();
    }

    protected GetPlatformKeyResponse a() {
        BaseController baseController = new BaseController(this.c.a);
        if (!baseController.a(this.b)) {
            baseController.actionBarInit(this.b);
        }
        return (GetPlatformKeyResponse) new SocializeClient().execute(new e(this.b, this.c.a));
    }

    protected void a(GetPlatformKeyResponse getPlatformKeyResponse) {
        if (getPlatformKeyResponse != null) {
            this.c.c = getPlatformKeyResponse.mSecrets;
            this.c.d = getPlatformKeyResponse.mData;
            String share_media = SHARE_MEDIA.QQ.toString();
            String share_media2 = SHARE_MEDIA.QZONE.toString();
            this.c.c.put(share_media, this.c.c.get(share_media2));
            this.c.d.put(share_media, this.c.d.get(share_media2));
            SocializeUtils.savePlatformKey(this.b, this.c.d);
            SocializeUtils.savePlatformSecret(this.b, this.c.c);
            if (this.a != null) {
                this.a.onComplete(getPlatformKeyResponse.mStCode, this.c.d);
                return;
            }
            return;
        }
        this.a.onComplete(StatusCode.ST_CODE_SDK_UNKNOW, null);
    }
}
