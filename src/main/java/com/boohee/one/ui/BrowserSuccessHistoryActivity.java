package com.boohee.one.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.boohee.one.R;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;

@SuppressLint({"SetJavaScriptEnabled"})
public class BrowserSuccessHistoryActivity extends BaseActivity implements OnClickListener {
    static final        String TAG   = BrowserSuccessHistoryActivity.class.getSimpleName();
    public static final String TITLE = "title";
    public static final String URL   = "url";
    private ProgressBar mProgressBar;
    private String      mTitle;
    private String      mUrl;
    private WebView     webView;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.dh);
        this.mTitle = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(this.mTitle)) {
            setTitle(this.mTitle);
        }
        this.mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(this.mUrl)) {
            Helper.showToast((CharSequence) "无效的链接");
            finish();
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon((int) R.drawable.s7);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        initView();
    }

    private void initView() {
        findViewById(R.id.view_bottom).setVisibility(AccountUtils.isReleaseUser() ? 8 : 0);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.webView = (WebView) findViewById(R.id.wv_content);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Helper.showLog(BrowserSuccessHistoryActivity.TAG, "onPageStarted");
                BrowserSuccessHistoryActivity.this.mProgressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                Helper.showLog(BrowserSuccessHistoryActivity.TAG, "onPageFinished");
                BrowserSuccessHistoryActivity.this.mProgressBar.setVisibility(8);
                if (TextUtil.isEmpty(BrowserSuccessHistoryActivity.this.mTitle)) {
                    BrowserSuccessHistoryActivity.this.mTitle = view.getTitle();
                    if (!TextUtil.isEmpty(BrowserSuccessHistoryActivity.this.mTitle)) {
                        BrowserSuccessHistoryActivity.this.setTitle(BrowserSuccessHistoryActivity
                                .this.mTitle);
                    }
                }
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                Helper.showLog(BrowserSuccessHistoryActivity.TAG, progress + "");
                BrowserSuccessHistoryActivity.this.mProgressBar.setVisibility(0);
                BrowserSuccessHistoryActivity.this.mProgressBar.setProgress(progress);
                if (progress == 100) {
                    BrowserSuccessHistoryActivity.this.mProgressBar.setVisibility(8);
                }
            }
        });
        this.webView.loadUrl(this.mUrl);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause() {
        super.onPause();
        if (this.webView != null) {
            this.webView.onPause();
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.webView != null) {
            this.webView.stopLoading();
        }
    }

    protected void onDestroy() {
        if (this.webView != null) {
            this.webView.removeAllViews();
            this.webView.destroy();
        }
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regster:
                NewLoginAndRegisterActivity.comeOnBaby(false, this);
                return;
            case R.id.bt_login:
                NewLoginAndRegisterActivity.comeOnBaby(true, this);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (this.webView == null || !this.webView.canGoBack()) {
            super.onBackPressed();
        } else {
            this.webView.goBack();
        }
    }

    public static void comeOnBaby(Context context, String title, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent(context, BrowserSuccessHistoryActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}
