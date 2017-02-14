package com.umeng.socialize.sso;

import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/* compiled from: CustomHandler */
class a implements OnSnsPlatformClickListener {
    final /* synthetic */ CustomHandler a;

    a(CustomHandler customHandler) {
        this.a = customHandler;
    }

    public void onClick(Context context, SocializeEntity socializeEntity, SnsPostListener
            snsPostListener) {
        if (this.a.isClientInstalled()) {
            this.a.mSocializeConfig.registerListener(snsPostListener);
            this.a.handleOnClick(this.a.mCustomPlatform, socializeEntity, snsPostListener);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("请安装");
        stringBuilder.append(this.a.mCustomPlatform.mShowWord);
        stringBuilder.append("客户端");
        Toast.makeText(this.a.mContext, stringBuilder.toString(), 0).show();
    }
}
