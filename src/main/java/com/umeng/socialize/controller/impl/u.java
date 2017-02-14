package com.umeng.socialize.controller.impl;

import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/* compiled from: ShareServiceImpl */
class u implements SnsPostListener {
    final /* synthetic */ Context a;
    final /* synthetic */ m       b;

    u(m mVar, Context context) {
        this.b = mVar;
        this.a = context;
    }

    public void onStart() {
        if (this.b.c.isShowToast()) {
            Toast.makeText(this.a, ResContainer.getString(this.a,
                    "umeng_socialize_text_waitting_share"), 0).show();
        }
    }

    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
        if (i == 200 && this.b.c.isShowToast()) {
            Toast.makeText(this.a, "发送成功", 0).show();
        } else if (i == StatusCode.ST_CODE_ACCESS_EXPIRED || i == StatusCode
                .ST_CODE_ACCESS_EXPIRED2) {
            if (this.b.c.isShowToast()) {
                StatusCode.showErrMsg(this.a, i, "授权已过期，请重新授权...");
            }
        } else if (this.b.c.isShowToast()) {
            StatusCode.showErrMsg(this.a, i, "发送失败，请重试...");
        }
    }
}
