package com.boohee.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boohee.model.CommonSport;
import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.utils.Helper;

@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class BaseWebViewActivity extends GestureActivity {
    public static final int    FROM_SPORT_LIB    = 0;
    public static final int    FROM_SPORT_REC    = 1;
    public static final int    FROM_SPORT_SEARCH = 2;
    static final        String TAG               = BaseWebViewActivity.class.getName();
    public static final String TITLE             = "title";
    public static final String URL               = "url";
    private CommonSport commonSport;
    private int         from_where;
    private Intent      intent;
    private Sport       sport;
    private String      title;
    private String      url;
    private WebView     webView;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.e7);
        setTitle(R.string.c3);
        initView();
    }

    void initView() {
        showLoading();
        this.intent = getIntent();
        this.commonSport = (CommonSport) this.intent.getSerializableExtra(CacheKey.COMMON_SPORT);
        this.sport = (Sport) this.intent.getSerializableExtra("sport");
        this.url = this.intent.getStringExtra("url");
        if (this.url == null) {
            this.url = "http://one.boohee.com/api/v1/articles/android_sport_program.html";
        }
        this.title = this.intent.getStringExtra("title");
        if (this.title != null) {
            setTitle(this.title);
        }
        Helper.showLog(TAG, "URL=" + this.url + "TITLE=" + this.title);
        this.webView = (WebView) findViewById(R.id.wv_content);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.loadUrl(this.url);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                BaseWebViewActivity.this.dismissLoading();
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.from_where = this.intent.getIntExtra("from_where", -1);
        if (this.from_where != -1 && (this.from_where == 0 || this.from_where == 1 || this
                .from_where == 2)) {
            menu.add(0, 1, 1, R.string.b7).setShowAsAction(2);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                openAddSport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPause() {
        super.onPause();
        try {
            this.webView.getClass().getMethod("onPause", new Class[0]).invoke(this.webView,
                    (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAddSport() {
    }
}
