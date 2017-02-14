package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.a.a;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.HashMap;

public class g {
    private String a;
    private String b;
    private a c = a.a();
    private Platform d;
    private int e;

    public g(Platform platform, int i) {
        this.d = platform;
        this.e = i;
    }

    private void a(String str) {
        Ln.d("wechat getAuthorizeToken ==>>" + str, new Object[0]);
        HashMap fromJson = new Hashon().fromJson(str);
        String valueOf = String.valueOf(fromJson.get("access_token"));
        String valueOf2 = String.valueOf(fromJson.get(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN));
        String valueOf3 = String.valueOf(fromJson.get("expires_in"));
        this.d.getDb().put("openid", String.valueOf(fromJson.get("openid")));
        this.d.getDb().putExpiresIn(Long.valueOf(valueOf3).longValue());
        this.d.getDb().putToken(valueOf);
        this.d.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN, valueOf2);
    }

    public void a(Bundle bundle, AuthorizeListener authorizeListener) {
        String string = bundle.getString("_wxapi_sendauth_resp_url");
        if (!TextUtils.isEmpty(string)) {
            int indexOf = string.indexOf("://oauth?");
            if (indexOf >= 0) {
                string = string.substring(indexOf + 1);
            }
            try {
                a(R.urlToBundle(string).getString("code"), authorizeListener);
            } catch (Throwable th) {
                Ln.e(th);
                if (authorizeListener != null) {
                    authorizeListener.onError(th);
                }
            }
        } else if (authorizeListener != null) {
            authorizeListener.onError(null);
        }
    }

    public void a(PlatformActionListener platformActionListener) {
        new i(this, platformActionListener).start();
    }

    public void a(String str, AuthorizeListener authorizeListener) {
        Ln.e("getAuthorizeToken ==>> " + str, new Object[0]);
        new h(this, str, authorizeListener).start();
    }

    public void a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }
}
