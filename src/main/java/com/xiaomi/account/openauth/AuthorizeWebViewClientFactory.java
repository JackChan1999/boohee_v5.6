package com.xiaomi.account.openauth;

import android.app.Activity;
import android.webkit.WebViewClient;

public class AuthorizeWebViewClientFactory {
    private static AuthorizeWebViewClientFactory instance = new AuthorizeWebViewClientFactory();

    public static AuthorizeWebViewClientFactory getInstance() {
        return instance;
    }

    public WebViewClient newWebViewClient(Activity activity) {
        return new AuthorizeWebViewClient(activity);
    }
}
