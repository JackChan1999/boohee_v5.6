package com.pingplusplus.android;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class d extends WebViewClient {
    final /* synthetic */ PaymentActivity a;

    d(PaymentActivity paymentActivity) {
        this.a = paymentActivity;
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.a.g != null && this.a.g.isShowing()) {
            this.a.g.dismiss();
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        PingppLog.d("YEEPay open  url:" + str);
        if (str.contains("https://ok.yeepay.com/paymobile/query/pay/success?")) {
            PingppLog.d("YEEPay YEEPAY_WAP_SUCCESS_URL:https://ok.yeepay" +
                    ".com/paymobile/query/pay/success?");
            this.a.h = "success";
        } else if (str.contains("http://mobiletest.yeepay.com/paymobile/query/pay/success?")) {
            PingppLog.d("YEEPay YEEPAY_WAP_TEST_SUCCESS_URL:http://mobiletest.yeepay" +
                    ".com/paymobile/query/pay/success?");
            this.a.h = "success";
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }
}
