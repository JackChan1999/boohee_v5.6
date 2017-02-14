package com.boohee.apn;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.boohee.one.R;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utils.Helper;
import com.boohee.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"SetJavaScriptEnabled"})
public class FragmentBrowser extends Fragment {
    public static final String OUT_LINK_URL = "http://lina.elementfresh.com/boohee201508";
    public static final String TITLE        = "title";
    public static final String URL          = "url";
    protected ProgressBar mProgressBar;
    protected String      mTitle;
    public    String      mUrl;
    protected WebView     webView;

    public class FragmentWebClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            FragmentBrowser.this.mProgressBar.setVisibility(0);
            FragmentBrowser.this.mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                FragmentBrowser.this.mProgressBar.setVisibility(8);
            }
        }

        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                FragmentBrowser.this.mTitle = title;
            }
        }
    }

    public final class JSInterface {
        @JavascriptInterface
        public void set(String json) {
            try {
                JSONObject jSONObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static FragmentBrowser newInstance(String url) {
        FragmentBrowser fragment = new FragmentBrowser();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mUrl = getArguments().getString("url");
        }
        if (TextUtils.isEmpty(this.mUrl)) {
            Helper.showToast((CharSequence) "无效的链接");
        } else {
            this.mUrl = UrlUtils.handleUrl(this.mUrl);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fl, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        this.webView = (WebView) view.findViewById(R.id.wv_content);
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        settings.setUserAgentString(settings.getUserAgentString() + " App/boohee");
        settings.setDomStorageEnabled(true);
        this.webView.addJavascriptInterface(new JSInterface(), "jsObj");
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    BrowserActivity.comeOnBaby(FragmentBrowser.this.getActivity(), null, url);
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                FragmentBrowser.this.mProgressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                FragmentBrowser.this.mProgressBar.setVisibility(8);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.webView.setWebChromeClient(new FragmentWebClient());
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.webView.loadUrl(this.mUrl);
    }

    public void onPause() {
        super.onPause();
        if (this.webView != null) {
            this.webView.onPause();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.webView != null) {
            this.webView.onResume();
        }
    }

    public void onStop() {
        super.onStop();
        if (this.webView != null) {
            this.webView.stopLoading();
        }
    }

    public void onDestroy() {
        if (this.webView != null) {
            this.webView.removeAllViews();
            this.webView.destroy();
        }
        super.onDestroy();
    }

    public void reload() {
        if (this.webView != null && !TextUtils.isEmpty(this.mUrl)) {
            this.webView.loadUrl(this.mUrl);
        }
    }
}
