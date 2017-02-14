package com.pingplusplus.android;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

class e extends WebViewClient {
    final /* synthetic */ PaymentActivity a;

    e(PaymentActivity paymentActivity) {
        this.a = paymentActivity;
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.a.g != null && this.a.g.isShowing()) {
            this.a.g.dismiss();
        }
    }

    @TargetApi(11)
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        super.onPageStarted(webView, str, bitmap);
        Message obtainMessage = this.a.m.obtainMessage();
        obtainMessage.obj = webView;
        obtainMessage.what = 4;
        this.a.m.sendMessageDelayed(obtainMessage, 30000);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        try {
            String string = this.a.i.getString("channel");
            JSONObject jSONObject = this.a.i.getJSONObject("extra");
            CharSequence string2 = jSONObject.getString("success_url");
            CharSequence string3 = jSONObject.getString("fail_url");
            PingppLog.d("jdPay url: " + str);
            PingppLog.d("jdPay successUrl: " + string2);
            PingppLog.d("jdPay fail_url : " + string3);
            if ("jdpay_wap".equals(string)) {
                if (str.contains(string2)) {
                    this.a.h = "success";
                    PingppLog.d("jdPay success");
                } else if (str.contains(string3)) {
                    this.a.h = "fail";
                    PingppLog.d("jdPay fail");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.a.h = "fail";
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }
}
