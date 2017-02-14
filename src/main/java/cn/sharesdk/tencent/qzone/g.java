package cn.sharesdk.tencent.qzone;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;

class g implements SSOListener {
    final /* synthetic */ AuthorizeListener a;
    final /* synthetic */ f b;

    g(f fVar, AuthorizeListener authorizeListener) {
        this.b = fVar;
        this.a = authorizeListener;
    }

    public void onCancel() {
        this.a.onCancel();
    }

    public void onComplete(Bundle bundle) {
        this.a.onComplete(bundle);
    }

    public void onFailed(Throwable th) {
        this.b.b(this.a);
    }
}
