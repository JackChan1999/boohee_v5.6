package com.umeng.socialize.bean;

import android.content.Context;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/* compiled from: InternalPlatform */
class e implements OnSnsPlatformClickListener {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public void onClick(Context context, SocializeEntity socializeEntity, SnsPostListener
            snsPostListener) {
        UMServiceFactory.getUMSocialService(socializeEntity.mDescriptor).postShare(context, this
                .a.mPlatform, snsPostListener);
    }
}
