package com.mob.tools;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SSDKWebViewClient extends WebViewClient {
    public static final int ERROR_AUTHENTICATION          = -4;
    public static final int ERROR_BAD_URL                 = -12;
    public static final int ERROR_CONNECT                 = -6;
    public static final int ERROR_FAILED_SSL_HANDSHAKE    = -11;
    public static final int ERROR_FILE                    = -13;
    public static final int ERROR_FILE_NOT_FOUND          = -14;
    public static final int ERROR_HOST_LOOKUP             = -2;
    public static final int ERROR_IO                      = -7;
    public static final int ERROR_PROXY_AUTHENTICATION    = -5;
    public static final int ERROR_REDIRECT_LOOP           = -9;
    public static final int ERROR_TIMEOUT                 = -8;
    public static final int ERROR_TOO_MANY_REQUESTS       = -15;
    public static final int ERROR_UNKNOWN                 = -1;
    public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
    public static final int ERROR_UNSUPPORTED_SCHEME      = -10;

    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
    }

    public void onFormResubmission(WebView webView, Message message, Message message2) {
        message.sendToTarget();
    }

    public void onLoadResource(WebView webView, String str) {
    }

    public void onPageFinished(WebView webView, String str) {
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
    }

    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler,
                                          String str, String str2) {
        httpAuthHandler.cancel();
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError
            sslError) {
        sslErrorHandler.cancel();
    }

    public void onScaleChanged(WebView webView, float f, float f2) {
    }

    public void onTooManyRedirects(WebView webView, Message message, Message message2) {
        message.sendToTarget();
    }

    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
    }

    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return false;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return false;
    }
}
