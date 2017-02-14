package cn.sharesdk.framework;

import cn.sharesdk.framework.authorize.AuthorizeHelper;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.authorize.g;

public abstract class e implements AuthorizeHelper {
    protected Platform a;
    private AuthorizeListener b;
    private SSOListener c;

    public e(Platform platform) {
        this.a = platform;
    }

    protected void a(SSOListener sSOListener) {
        this.c = sSOListener;
        cn.sharesdk.framework.authorize.e eVar = new cn.sharesdk.framework.authorize.e();
        eVar.a(sSOListener);
        eVar.a(this);
    }

    protected void b(AuthorizeListener authorizeListener) {
        this.b = authorizeListener;
        g gVar = new g();
        gVar.a(this.b);
        gVar.a(this);
    }

    public int c() {
        return this.a.getPlatformId();
    }

    public AuthorizeListener getAuthorizeListener() {
        return this.b;
    }

    public Platform getPlatform() {
        return this.a;
    }

    public SSOListener getSSOListener() {
        return this.c;
    }

    public f getSSOProcessor(cn.sharesdk.framework.authorize.e eVar) {
        return null;
    }
}
