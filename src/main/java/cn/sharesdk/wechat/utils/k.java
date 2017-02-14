package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.utils.Hashon;
import java.util.HashMap;

public class k {
    private Platform a;
    private ShareParams b;
    private PlatformActionListener c;
    private AuthorizeListener d;
    private g e;

    public k(Platform platform) {
        this.a = platform;
    }

    public ShareParams a() {
        return this.b;
    }

    public void a(ShareParams shareParams, PlatformActionListener platformActionListener) {
        this.b = shareParams;
        this.c = platformActionListener;
    }

    public void a(AuthorizeListener authorizeListener) {
        this.d = authorizeListener;
    }

    public void a(WechatResp wechatResp) {
        HashMap hashMap;
        Throwable th;
        switch (wechatResp.f) {
            case -4:
                hashMap = new HashMap();
                hashMap.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap.put("errStr", wechatResp.g);
                hashMap.put("transaction", wechatResp.h);
                th = new Throwable(new Hashon().fromHashMap(hashMap));
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            this.d.onError(th);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case -3:
                hashMap = new HashMap();
                hashMap.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap.put("errStr", wechatResp.g);
                hashMap.put("transaction", wechatResp.h);
                th = new Throwable(new Hashon().fromHashMap(hashMap));
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            this.d.onError(th);
                            return;
                        }
                        return;
                    case 2:
                        if (this.c != null) {
                            this.c.onError(this.a, 9, th);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case -2:
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            this.d.onCancel();
                            return;
                        }
                        return;
                    case 2:
                        if (this.c != null) {
                            this.c.onCancel(this.a, 9);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case 0:
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            Bundle bundle = new Bundle();
                            wechatResp.b(bundle);
                            this.e.a(bundle, this.d);
                            return;
                        }
                        return;
                    case 2:
                        if (this.c != null) {
                            hashMap = new HashMap();
                            hashMap.put("ShareParams", this.b);
                            this.c.onComplete(this.a, 9, hashMap);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            default:
                hashMap = new HashMap();
                hashMap.put("req", wechatResp.getClass().getSimpleName());
                hashMap.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap.put("errStr", wechatResp.g);
                hashMap.put("transaction", wechatResp.h);
                new Throwable(new Hashon().fromHashMap(hashMap)).printStackTrace();
                return;
        }
    }

    public void a(g gVar) {
        this.e = gVar;
    }

    public Platform b() {
        return this.a;
    }

    public PlatformActionListener c() {
        return this.c;
    }
}
