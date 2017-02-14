package cn.sharesdk.wechat.friends;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;

class a implements AuthorizeListener {
    final /* synthetic */ Wechat a;

    a(Wechat wechat) {
        this.a = wechat;
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
