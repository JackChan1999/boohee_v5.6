package com.boohee.one.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.pedometer.BindClingActivity;
import com.boohee.record.ScaleBindActivity;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.UrlUtils;

public class HardwareIntroActivity extends SwipeBackActivity {
    public static final String BUY_URL       = "buy_url";
    public static final int    REQUEST_INTRO = 100;
    public static final String TYPE          = "type";
    public static final int    TYPE_CLING    = 1;
    public static final int    TYPE_SCALE    = 0;
    public static final String WEB_URL       = "web_url";
    private String buyUrl;
    @InjectView(2131427709)
    TextView tvBind;
    @InjectView(2131427708)
    TextView tvBuy;
    private int    type;
    private String webUrl;
    @InjectView(2131427707)
    WebView webView;

    public static void startActivity(Activity context, String webUrl, String buyUrl, int type) {
        Intent i = new Intent(context, HardwareIntroActivity.class);
        i.putExtra(WEB_URL, webUrl);
        i.putExtra(BUY_URL, buyUrl);
        i.putExtra("type", type);
        context.startActivityForResult(i, 100);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bh);
        ButterKnife.inject((Activity) this);
        initParam();
        initView();
    }

    private void initParam() {
        this.webUrl = getIntent().getStringExtra(WEB_URL);
        this.buyUrl = getIntent().getStringExtra(BUY_URL);
        this.type = getIntent().getIntExtra("type", 0);
    }

    private void initView() {
        this.tvBind.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (HardwareIntroActivity.this.type == 0) {
                    ScaleBindActivity.startActivity(HardwareIntroActivity.this);
                } else if (HardwareIntroActivity.this.type == 1) {
                    BindClingActivity.startActivity(HardwareIntroActivity.this);
                }
            }
        });
        this.tvBuy.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BooheeScheme.handleUrl(HardwareIntroActivity.this, HardwareIntroActivity.this
                        .buyUrl);
            }
        });
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setUserAgentString(this.webView.getSettings()
                .getUserAgentString() + " App/boohee");
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(createWebViewClient());
        this.webView.setWebChromeClient(createWebChromeClient());
        this.webView.loadUrl(UrlUtils.handleUrl(this.webUrl));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            setResult(-1);
            finish();
        }
    }

    private WebViewClient createWebViewClient() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains("https://")) {
                    view.loadUrl(url);
                } else if (!BooheeScheme.handleUrl(HardwareIntroActivity.this, url)) {
                    Uri uri = Uri.parse(url);
                    if (uri != null) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        HardwareIntroActivity.this.startActivity(intent);
                    }
                }
                return true;
            }
        };
    }

    private WebChromeClient createWebChromeClient() {
        return new WebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    HardwareIntroActivity.this.setTitle(title);
                }
            }
        };
    }

    public void onBackPressed() {
        if (this.webView == null || !this.webView.canGoBack()) {
            super.onBackPressed();
        } else {
            this.webView.goBack();
        }
    }
}
