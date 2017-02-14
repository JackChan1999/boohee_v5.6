package com.hanyou.leyusdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qiniu.android.common.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HelloWebView extends Activity {
    private static final String ANDROID_CALLBACK = "androidcallback://";
    private static final String APP_SCHEME       = "example-app:";
    private String accesstoken;
    private String appid;
    private final String hosturl = "http://m.miao.cn";
    private Intent         intent;
    private LinearLayout   layout;
    private ProgressDialog mSpinner;
    private WebView        webView;
    private final Handler xHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.layout = new LinearLayout(this);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        setContentView(this.layout);
        this.intent = getIntent();
        Bundle bundle = this.intent.getExtras();
        this.appid = bundle.getString("appid");
        this.accesstoken = bundle.getString("accesstoken");
        init();
    }

    private void init() {
        this.mSpinner = new ProgressDialog(this);
        this.mSpinner.requestWindowFeature(1);
        this.mSpinner.setMessage("Loading...");
        this.mSpinner.setCancelable(true);
        this.mSpinner.setCanceledOnTouchOutside(false);
        RelativeLayout webViewContainer = new RelativeLayout(this);
        this.webView = new WebView(this);
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        webViewContainer.addView(this.webView);
        this.layout.addView(webViewContainer, new ViewGroup.LayoutParams(-1, -1));
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setId(333);
        this.webView.loadUrl("http://m.miao.cn/action/devapi/login.html?access_token=" + this
                .accesstoken + "&n=" + new Random(100).nextInt());
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = URLDecoder.decode(url, Constants.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                if (url.startsWith(HelloWebView.ANDROID_CALLBACK)) {
                    url = url.substring(HelloWebView.ANDROID_CALLBACK.length());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("result_info", url);
                    intent.putExtras(bundle);
                    JSONObject person = null;
                    try {
                        person = (JSONObject) new JSONTokener(url).nextValue();
                    } catch (JSONException e) {
                    }
                    if (person == null) {
                        Toast.makeText(HelloWebView.this, "Service Error", 0).show();
                        HelloWebView.this.setResult(0);
                        HelloWebView.this.finish();
                    } else {
                        try {
                            LEYUApplication.saveLoginInfo(LEYUUser.fromJSONString(person.toString
                                    ()));
                            HelloWebView.this.setResult(-1, intent);
                            HelloWebView.this.finish();
                        } catch (JSONException e2) {
                            try {
                                Toast.makeText(HelloWebView.this, person.getString("error"), 1)
                                        .show();
                            } catch (JSONException e3) {
                            }
                        }
                    }
                } else {
                    view.loadUrl(new StringBuilder(String.valueOf(url)).append("?access_token=")
                            .append(HelloWebView.this.accesstoken).toString());
                }
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                HelloWebView.this.mSpinner.dismiss();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(HelloWebView.this, "网络不给力", 0).show();
                HelloWebView.this.finish();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                HelloWebView.this.mSpinner.show();
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100 && HelloWebView.this.mSpinner != null && HelloWebView.this
                        .mSpinner.isShowing()) {
                    HelloWebView.this.mSpinner.dismiss();
                }
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }
}
