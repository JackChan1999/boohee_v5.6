package cn.sharesdk.sina.weibo;

import android.content.Intent;
import android.os.Bundle;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.FakeActivity;

public class a extends FakeActivity {
    private String a;
    private String b;
    private int c;
    private boolean d;
    private String[] e;
    private PlatformActionListener f;
    private Platform g;
    private ShareParams h;

    public a(Platform platform, String str, String str2) {
        this.a = str;
        this.g = platform;
        this.b = str2;
    }

    private void a() {
        String str = null;
        if (!(this.e == null || this.e.length == 0)) {
            str = this.e[0];
            for (int i = 1; i < this.e.length; i++) {
                str = str + "," + this.e[i];
            }
        }
        if (this.d) {
            ShareSDK.logApiEvent("com.sina.weibo.sdk.auth.sso.SsoHandler.authorize", this.c);
        } else {
            ShareSDK.logApiEvent("com.sina.weibo.sdk.auth.sso.SsoHandler.authorizeWeb", this.c);
        }
        Intent intent = new Intent(this.activity, SinaActivity.class);
        intent.putExtra("action", 1);
        intent.putExtra("appkey", this.a);
        intent.putExtra("isUserClient", this.d);
        intent.putExtra("redirectUrl", this.b);
        String str2 = "permissions";
        if (str == null) {
            str = "";
        }
        intent.putExtra(str2, str);
        SinaActivity.a(new b(this));
        this.activity.startActivity(intent);
    }

    private void b() {
        ShareSDK.logApiEvent("com.sina.weibo.sdk.api.share.IWeiboShareAPI.sendRequest", this.c);
        Intent intent = new Intent(this.activity, SinaActivity.class);
        intent.putExtra("action", 2);
        intent.putExtra("token", this.g.getDb().getToken());
        intent.putExtra("appkey", this.a);
        intent.putExtra("redirectUrl", this.b);
        intent.putExtra("isUserClient", this.d);
        AuthorizeListener cVar = new c(this);
        SinaActivity.a(this.h);
        SinaActivity.a(cVar);
        this.activity.startActivity(intent);
    }

    public void a(PlatformActionListener platformActionListener, ShareParams shareParams, boolean z) {
        this.d = true;
        this.h = shareParams;
        this.f = platformActionListener;
    }

    public void a(PlatformActionListener platformActionListener, String[] strArr, boolean z) {
        this.d = !z;
        this.e = strArr;
        this.f = platformActionListener;
    }

    public void onCreate() {
        super.onCreate();
        Bundle extras = this.activity.getIntent().getExtras();
        int i = extras.getInt("action");
        this.c = extras.getInt("platformID");
        if (1 == i) {
            a();
        } else {
            b();
        }
    }
}
