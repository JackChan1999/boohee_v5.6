package cn.sharesdk.tencent.qzone;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import java.util.HashMap;

class c implements PlatformActionListener {
    final /* synthetic */ ShareParams a;
    final /* synthetic */ QZone b;

    c(QZone qZone, ShareParams shareParams) {
        this.b = qZone;
        this.a = shareParams;
    }

    public void onCancel(Platform platform, int i) {
        if (this.b.listener != null) {
            this.b.listener.onCancel(this.b, 9);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (this.b.listener != null) {
            hashMap.put("ShareParams", this.a);
            this.b.listener.onComplete(this.b, 9, hashMap);
        }
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.b.listener != null) {
            this.b.listener.onError(this.b, 9, th);
        }
    }
}
