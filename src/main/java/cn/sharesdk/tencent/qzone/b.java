package cn.sharesdk.tencent.qzone;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import java.util.HashMap;

class b implements PlatformActionListener {
    final /* synthetic */ PlatformActionListener a;
    final /* synthetic */ ShareParams b;
    final /* synthetic */ QZone c;

    b(QZone qZone, PlatformActionListener platformActionListener, ShareParams shareParams) {
        this.c = qZone;
        this.a = platformActionListener;
        this.b = shareParams;
    }

    public void onCancel(Platform platform, int i) {
        if (this.a != null) {
            this.a.onCancel(platform, 9);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.c.setPlatformActionListener(this.a);
        this.c.doShare(this.b);
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.a != null) {
            this.a.onError(platform, 9, th);
        }
    }
}
