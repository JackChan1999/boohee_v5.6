package com.zxinsight;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class ag extends WebViewClient {
    final /* synthetic */ WebViewActivity a;

    ag(WebViewActivity webViewActivity) {
        this.a = webViewActivity;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str.startsWith("unsafe:")) {
            str = str.substring(7);
        }
        try {
            this.a.e.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (Exception e) {
            webView.loadUrl(Uri.decode(str));
        }
        return true;
    }
}
