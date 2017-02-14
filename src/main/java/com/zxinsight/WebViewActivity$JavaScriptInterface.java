package com.zxinsight;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity$JavaScriptInterface {
    final /* synthetic */ WebViewActivity this$0;

    public WebViewActivity$JavaScriptInterface(WebViewActivity webViewActivity) {
        this.this$0 = webViewActivity;
    }

    @JavascriptInterface
    public void share() {
        WebViewActivity.b(this.this$0);
    }

    @JavascriptInterface
    public void share(String str) {
        WebViewActivity.a(this.this$0, str);
    }

    @JavascriptInterface
    public String getProfile() {
        return m.a().e();
    }

    @JavascriptInterface
    public String getAppId() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, o.c());
            jSONObject.put("fq", o.c());
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, m.a().d());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    @JavascriptInterface
    public int startApp(String str) {
        try {
            WebViewActivity.a(this.this$0).startActivity(new Intent("android.intent.action.VIEW",
                    Uri.parse(str)));
            return 1;
        } catch (Exception e) {
            c.a(e.getLocalizedMessage());
            return 0;
        }
    }

    @JavascriptInterface
    public void openFileSelector(String str) {
        WebViewActivity.b(this.this$0, str);
        WebViewActivity.c(this.this$0).a();
    }

    @JavascriptInterface
    public String updateImage() {
        return WebViewActivity.e(this.this$0);
    }
}
