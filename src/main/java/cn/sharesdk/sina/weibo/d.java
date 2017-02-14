package cn.sharesdk.sina.weibo;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import java.util.HashMap;

class d implements PlatformActionListener {
    final /* synthetic */ SinaWeibo a;

    d(SinaWeibo sinaWeibo) {
        this.a = sinaWeibo;
    }

    public void onCancel(Platform platform, int i) {
        if (this.a.listener != null) {
            this.a.listener.onCancel(platform, 1);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.a.afterRegister(1, null);
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.a.listener != null) {
            this.a.listener.onError(platform, 1, th);
        }
    }
}
