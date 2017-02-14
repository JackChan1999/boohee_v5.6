package com.boohee.record;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Event;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

public class ScaleIntroActivity extends SwipeBackActivity {
    public static        int    REQUEST = 17;
    private static final String URL     = "http://shop.boohee.com/store/pages/scales";
    @InjectView(2131427709)
    TextView tvBind;
    @InjectView(2131427707)
    WebView  webView;

    public static void startActivity(Activity context) {
        MobclickAgent.onEvent(context, Event.bingo_viewScalsePage);
        context.startActivityForResult(new Intent(context, ScaleIntroActivity.class), REQUEST);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs);
        ButterKnife.inject((Activity) this);
        initView();
    }

    private void initView() {
        this.tvBind.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MobclickAgent.onEvent(ScaleIntroActivity.this, Event.bingo_clickBindingScalse);
                ScaleBindActivity.startActivity(ScaleIntroActivity.this);
            }
        });
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setUserAgentString(this.webView.getSettings()
                .getUserAgentString() + " App/boohee");
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(createWebViewClient());
        this.webView.loadUrl(UrlUtils.handleUrl(URL));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            setResult(-1, data);
            finish();
        }
    }

    private WebViewClient createWebViewClient() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains("https://")) {
                    view.loadUrl(url);
                } else if (!BooheeScheme.handleUrl(ScaleIntroActivity.this, url)) {
                    Uri uri = Uri.parse(url);
                    if (uri != null) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        ScaleIntroActivity.this.startActivity(intent);
                    }
                }
                return true;
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
