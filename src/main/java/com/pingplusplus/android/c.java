package com.pingplusplus.android;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class c extends WebViewClient {
    final /* synthetic */ PaymentActivity a;

    c(PaymentActivity paymentActivity) {
        this.a = paymentActivity;
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.a.g != null && this.a.g.isShowing()) {
            this.a.g.dismiss();
        }
    }
}
