package com.umeng.socialize.weixin.controller;

import android.content.Context;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

class UMWXHandler$1 implements OnSnsPlatformClickListener {
    final /* synthetic */ UMWXHandler this$0;

    UMWXHandler$1(UMWXHandler this$0) {
        this.this$0 = this$0;
    }

    public void onClick(Context context, SocializeEntity entity, SnsPostListener listener) {
        this.this$0.handleOnClick(context, this.this$0.mCustomPlatform, entity, listener);
    }
}
