package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import java.util.HashMap;

class c implements AuthorizeListener {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public void onCancel() {
        if (this.a.f != null) {
            this.a.f.onCancel(this.a.g, 9);
        }
        this.a.finish();
    }

    public void onComplete(Bundle bundle) {
        if (this.a.f != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("ShareParams", this.a.h);
            this.a.f.onComplete(this.a.g, 9, hashMap);
        }
        this.a.finish();
    }

    public void onError(Throwable th) {
        if (this.a.f != null) {
            this.a.f.onError(this.a.g, 9, th);
        }
        this.a.finish();
    }
}
