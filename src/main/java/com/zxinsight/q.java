package com.zxinsight;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxinsight.common.util.c;

class q extends WebViewClient {
    final /* synthetic */ ProgressWebView a;

    q(ProgressWebView progressWebView) {
        this.a = progressWebView;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str.startsWith("unsafe:")) {
            str = str.substring(7);
        }
        if (str.startsWith("http:") || str.startsWith("https:")) {
            webView.loadUrl(Uri.decode(str));
            return false;
        }
        try {
            this.a.g.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (Exception e) {
            c.a(e.getLocalizedMessage());
        }
        return true;
    }
}
