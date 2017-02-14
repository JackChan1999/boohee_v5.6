package cn.sharesdk.tencent.qzone;

import android.os.Bundle;
import android.webkit.WebView;
import cn.sharesdk.framework.authorize.b;
import cn.sharesdk.framework.authorize.g;
import com.alipay.sdk.sys.a;
import com.umeng.socialize.common.SocializeConstants;
import java.net.URLDecoder;
import java.util.HashMap;

public class d extends b {
    public d(g gVar) {
        super(gVar);
    }

    private void a(HashMap<String, String> hashMap) {
        String str = (String) hashMap.get("access_token");
        String str2 = (String) hashMap.get("expires_in");
        String str3 = (String) hashMap.get("error");
        String str4 = (String) hashMap.get("error_description");
        if (str != null && str.trim().length() > 0) {
            try {
                HashMap e = f.a(this.a.a().getPlatform()).e(str);
                if (e == null || e.size() <= 0) {
                    if (this.c != null) {
                        this.c.onError(new Throwable());
                    }
                } else if (e.containsKey("openid")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("access_token", str);
                    bundle.putString("open_id", String.valueOf(e.get("openid")));
                    bundle.putString("expires_in", str2);
                    if (this.c != null) {
                        this.c.onComplete(bundle);
                    }
                } else if (this.c != null) {
                    this.c.onError(new Throwable());
                }
            } catch (Throwable th) {
                if (this.c != null) {
                    this.c.onError(th);
                }
            }
        } else if (str3 == null || str3.trim().length() <= 0) {
            this.c.onError(new Throwable());
        } else {
            str = str4 + " (" + str3 + SocializeConstants.OP_CLOSE_PAREN;
            if (this.c != null) {
                this.c.onError(new Throwable(str));
            }
        }
    }

    protected void a(String str) {
        if (str.startsWith(this.b)) {
            str = str.substring(str.indexOf(35) + 1);
        }
        String[] split = str.split(a.b);
        HashMap hashMap = new HashMap();
        for (String split2 : split) {
            String[] split3 = split2.split("=");
            if (split3.length < 2) {
                hashMap.put(URLDecoder.decode(split3[0]), "");
            } else {
                hashMap.put(URLDecoder.decode(split3[0]), URLDecoder.decode(split3[1] == null ? "" : split3[1]));
            }
        }
        a(hashMap);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str.startsWith(this.b)) {
            webView.setVisibility(4);
            webView.stopLoading();
            this.a.finish();
            new e(this, str).start();
        } else {
            webView.loadUrl(str);
        }
        return true;
    }
}
