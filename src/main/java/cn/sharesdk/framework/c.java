package cn.sharesdk.framework;

import java.util.HashMap;

class c implements PlatformActionListener {
    final /* synthetic */ PlatformActionListener a;
    final /* synthetic */ int b;
    final /* synthetic */ Object c;
    final /* synthetic */ a d;

    c(a aVar, PlatformActionListener platformActionListener, int i, Object obj) {
        this.d = aVar;
        this.a = platformActionListener;
        this.b = i;
        this.c = obj;
    }

    public void onCancel(Platform platform, int i) {
        this.d.a = this.a;
        if (this.d.a != null) {
            this.d.a.onCancel(platform, this.b);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.d.a = this.a;
        platform.afterRegister(this.b, this.c);
    }

    public void onError(Platform platform, int i, Throwable th) {
        this.d.a = this.a;
        if (this.d.a != null) {
            this.d.a.onError(platform, i, th);
        }
    }
}
