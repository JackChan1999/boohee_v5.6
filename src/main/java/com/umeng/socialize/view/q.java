package com.umeng.socialize.view;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

/* compiled from: OauthDialog */
class q extends a {
    final /* synthetic */ j a;

    q(j jVar) {
        this.a = jVar;
        super();
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError
            sslError) {
        sslErrorHandler.proceed();
    }
}
