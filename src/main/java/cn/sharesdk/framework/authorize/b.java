package cn.sharesdk.framework.authorize;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import com.mob.tools.SSDKWebViewClient;

public abstract class b extends SSDKWebViewClient {
    protected g a;
    protected String b;
    protected AuthorizeListener c;

    public b(g gVar) {
        this.a = gVar;
        AuthorizeHelper a = gVar.a();
        this.b = a.getRedirectUri();
        this.c = a.getAuthorizeListener();
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        webView.stopLoading();
        AuthorizeListener authorizeListener = this.a.a().getAuthorizeListener();
        this.a.finish();
        if (authorizeListener != null) {
            authorizeListener.onError(new Throwable(str + " (" + i + "): " + str2));
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.proceed();
    }
}
