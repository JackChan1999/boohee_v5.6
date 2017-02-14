package com.umeng.socialize.view;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/* compiled from: OauthDialog */
class p extends WebChromeClient {
    final /* synthetic */ j a;

    p(j jVar) {
        this.a = jVar;
    }

    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        if (i < 90) {
            this.a.e.setVisibility(0);
        } else {
            this.a.t.sendEmptyMessage(1);
        }
    }
}
