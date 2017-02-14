package com.pingplusplus.android;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class g {
    Context a;
    final /* synthetic */ PaymentActivity b;

    g(PaymentActivity paymentActivity, Context context) {
        this.b = paymentActivity;
        this.a = context;
    }

    @JavascriptInterface
    public void setResult(String str) {
        if (str == null) {
            this.b.a("fail", "unknown_error");
        } else if (str.equals("success")) {
            this.b.a("success");
        } else {
            this.b.a("fail", "unknown_error");
        }
    }

    @JavascriptInterface
    public void testmodeResult(String str) {
        if (str == null) {
            this.b.a("fail", "unknown_error");
        } else if (str.equals("success")) {
            this.b.a("success");
        } else if (str.equals("cancel")) {
            this.b.a("cancel", "user_cancelled");
        } else if (str.equals("fail")) {
            this.b.a("fail", "channel_returns_fail");
        } else if (str.equals("error")) {
            this.b.a("fail", "testmode_notify_failed");
        } else {
            this.b.a("fail", "unknown_error");
        }
    }
}
