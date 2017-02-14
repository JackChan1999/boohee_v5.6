package com.xiaomi.account.openauth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class AuthorizeWebViewClient extends WebViewClient {
    public static        int    RESULT_CANCEL  = 0;
    public static        int    RESULT_FAIL    = 1;
    public static        int    RESULT_SUCCESS = -1;
    private static final String TAG            = "AuthorizeWebViewClient";
    private static final String UTF8           = "UTF-8";
    private Activity activity;

    public AuthorizeWebViewClient(Activity activity) {
        this.activity = activity;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.i(TAG, "start to load url : " + url);
        String currentUrl = new String(url);
        int index = currentUrl.indexOf(63);
        String param;
        if (index > 0) {
            param = currentUrl.substring(index + 1);
            if (param.startsWith("code=") || param.contains("&code=")) {
                setResultAndFinsih(RESULT_SUCCESS, currentUrl);
                return;
            } else if (param.startsWith("error=") || param.contains("&error=")) {
                setResultAndFinsih(RESULT_FAIL, currentUrl);
                return;
            } else {
                return;
            }
        }
        index = currentUrl.indexOf(35);
        if (index > 0) {
            param = currentUrl.substring(index + 1);
            if (param.startsWith("access_token=") || param.contains("&access_token=")) {
                setResultAndFinsih(RESULT_SUCCESS, currentUrl.replace("#", "?"));
            } else if (param.startsWith("error=") || param.contains("&error=")) {
                setResultAndFinsih(RESULT_FAIL, currentUrl.replace("#", "?"));
            }
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    private void setResultAndFinsih(int resultCode, String url) {
        Intent intent = new Intent();
        intent.putExtras(parseUrl(url));
        this.activity.setResult(resultCode, intent);
        this.activity.finish();
    }

    private Bundle parseUrl(String url) {
        Bundle b = new Bundle();
        if (url != null) {
            try {
                for (NameValuePair pair : URLEncodedUtils.parse(new URI(url), "UTF-8")) {
                    if (!(TextUtils.isEmpty(pair.getName()) || TextUtils.isEmpty(pair.getValue())
                    )) {
                        b.putString(pair.getName(), pair.getValue());
                    }
                }
            } catch (URISyntaxException e) {
                Log.e("openauth", e.getMessage());
            }
        }
        return b;
    }
}
