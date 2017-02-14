package cn.sharesdk.wechat.moments;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;

class a implements AuthorizeListener {
    final /* synthetic */ WechatMoments a;

    a(WechatMoments wechatMoments) {
        this.a = wechatMoments;
    }

    public void onCancel() {
        if (this.a.listener != null) {
            this.a.listener.onCancel(this.a, 1);
        }
    }

    public void onComplete(Bundle bundle) {
        this.a.afterRegister(1, null);
    }

    public void onError(Throwable th) {
        if (this.a.listener != null) {
            this.a.listener.onError(this.a, 1, th);
        }
    }
}
