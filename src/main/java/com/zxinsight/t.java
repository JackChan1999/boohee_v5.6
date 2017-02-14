package com.zxinsight;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class t extends WebChromeClient {
    final /* synthetic */ ProgressWebView a;

    public t(ProgressWebView progressWebView) {
        this.a = progressWebView;
    }

    public void onProgressChanged(WebView webView, int i) {
        if (i == 100) {
            this.a.d.setVisibility(8);
        } else {
            if (this.a.d.getVisibility() == 8) {
                this.a.d.setVisibility(0);
            }
            this.a.d.setProgress(i);
        }
        super.onProgressChanged(webView, i);
    }
}
