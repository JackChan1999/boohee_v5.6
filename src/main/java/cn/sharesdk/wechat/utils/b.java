package cn.sharesdk.wechat.utils;

import android.os.Bundle;

public class b extends WechatResp {
    public String a;
    public String b;
    public int c;
    public String d;
    public String e;

    public b(Bundle bundle) {
        super(bundle);
    }

    public int a() {
        return 1;
    }

    public void a(Bundle bundle) {
        super.a(bundle);
        this.a = bundle.getString("_wxapi_sendauth_resp_userName");
        this.b = bundle.getString("_wxapi_sendauth_resp_token");
        this.c = bundle.getInt("_wxapi_sendauth_resp_expireDate", 0);
        this.d = bundle.getString("_wxapi_sendauth_resp_state");
        this.e = bundle.getString("_wxapi_sendauth_resp_url");
    }

    public void b(Bundle bundle) {
        super.b(bundle);
        bundle.putString("_wxapi_sendauth_resp_userName", this.a);
        bundle.putString("_wxapi_sendauth_resp_token", this.b);
        bundle.putInt("_wxapi_sendauth_resp_expireDate", this.c);
        bundle.putString("_wxapi_sendauth_resp_state", this.d);
        bundle.putString("_wxapi_sendauth_resp_url", this.e);
    }
}
