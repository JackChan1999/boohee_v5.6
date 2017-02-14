package com.tencent.open.web.security;

import android.webkit.WebView;

import com.tencent.open.a.a;
import com.tencent.open.a.f;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ProGuard */
public class c extends a {
    private static final String d = (f.d + ".SL");
    private String e;

    public c(WebView webView, long j, String str, String str2) {
        super(webView, j, str);
        this.e = str2;
    }

    public void a(Object obj) {
        f.b(d, "-->onComplete, result: " + obj);
    }

    public void a() {
        f.b(d, "-->onNoMatchMethod...");
    }

    public void a(String str) {
        f.b(d, "-->onCustomCallback, js: " + str);
        JSONObject jSONObject = new JSONObject();
        int i = 0;
        if (!com.tencent.open.c.c.a) {
            i = -4;
        }
        try {
            jSONObject.put("result", i);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_NUM, this.b);
            jSONObject.put("data", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        b(jSONObject.toString());
    }

    private void b(String str) {
        WebView webView = (WebView) this.a.get();
        if (webView != null) {
            StringBuffer stringBuffer = new StringBuffer("javascript:");
            stringBuffer.append("if(!!").append(this.e).append("){");
            stringBuffer.append(this.e);
            stringBuffer.append(SocializeConstants.OP_OPEN_PAREN);
            stringBuffer.append(str);
            stringBuffer.append(")}");
            String stringBuffer2 = stringBuffer.toString();
            f.b(f.d, "-->callback, callback: " + stringBuffer2);
            webView.loadUrl(stringBuffer2);
        }
    }
}
