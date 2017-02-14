package com.xiaomi.account.openauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class AuthorizeActivity extends Activity {
    private static final String                  AUTHORIZE_PATH     = (AuthorizeHelper
            .OAUTH2_HOST + "/oauth2/authorize");
    private static final String                  LOCALE_KEY_IN_URL  = "_locale";
    public static        int                     RESULT_CANCEL      = AuthorizeWebViewClient
            .RESULT_CANCEL;
    public static        int                     RESULT_FAIL        = AuthorizeWebViewClient
            .RESULT_FAIL;
    public static        int                     RESULT_SUCCESS     = AuthorizeWebViewClient
            .RESULT_SUCCESS;
    private static final String                  UTF8               = "UTF-8";
    private static       HashMap<Locale, String> locale2UrlParamMap = new HashMap();
    private WebSettings mSettings;
    private String      mUrl;
    private WebView     mWebView;

    protected interface onAuthActivityInterface {
        void onAuthorizeFailed(Bundle bundle);

        void onAuthorizeSucceeded(Bundle bundle);

        void onCancelled();
    }

    static {
        locale2UrlParamMap.put(Locale.SIMPLIFIED_CHINESE, "zh_CN");
        locale2UrlParamMap.put(Locale.CHINA, "zh_CN");
        locale2UrlParamMap.put(Locale.PRC, "zh_CN");
        locale2UrlParamMap.put(Locale.TRADITIONAL_CHINESE, "zh_TW");
        locale2UrlParamMap.put(Locale.TAIWAN, "zh_TW");
        locale2UrlParamMap.put(Locale.ENGLISH, SocializeProtocolConstants.PROTOCOL_KEY_EN);
        locale2UrlParamMap.put(Locale.UK, SocializeProtocolConstants.PROTOCOL_KEY_EN);
        locale2UrlParamMap.put(Locale.US, SocializeProtocolConstants.PROTOCOL_KEY_EN);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createView());
        this.mSettings = this.mWebView.getSettings();
        this.mSettings.setJavaScriptEnabled(true);
        this.mSettings.setSavePassword(false);
        this.mSettings.setSaveFormData(false);
        this.mUrl = AUTHORIZE_PATH + "?" + parseBundle(addLocaleIfNeeded(getIntent()
                .getBundleExtra("url_param")));
        this.mWebView.loadUrl(this.mUrl);
        this.mWebView.setWebViewClient(AuthorizeWebViewClientFactory.getInstance()
                .newWebViewClient(this));
    }

    private View createView() {
        LinearLayout linear = new LinearLayout(this);
        linear.setLayoutParams(new LayoutParams(-1, -1));
        linear.setOrientation(1);
        this.mWebView = new WebView(this);
        linear.addView(this.mWebView, new LayoutParams(-2, -2));
        return linear;
    }

    public void onBackPressed() {
        if (this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            setResultAndFinsih(RESULT_CANCEL, null);
        }
    }

    private void setResultAndFinsih(int resultCode, String url) {
        Intent intent = new Intent();
        intent.putExtras(parseUrl(url));
        setResult(resultCode, intent);
        finish();
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

    private String parseBundle(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        List<NameValuePair> list = new ArrayList();
        for (String key : parameters.keySet()) {
            String value = parameters.getString(key);
            if (!(TextUtils.isEmpty(key) || TextUtils.isEmpty(value))) {
                list.add(new BasicNameValuePair(key, value));
            }
        }
        return URLEncodedUtils.format(list, "UTF-8");
    }

    private Bundle addLocaleIfNeeded(Bundle bundle) {
        if (!(bundle == null || bundle.containsKey(LOCALE_KEY_IN_URL))) {
            Locale defaultLocale = Locale.getDefault();
            if (locale2UrlParamMap.containsKey(defaultLocale)) {
                bundle.putString(LOCALE_KEY_IN_URL, (String) locale2UrlParamMap.get(defaultLocale));
            }
        }
        return bundle;
    }
}
