package com.boohee.one.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boohee.one.MyApplication;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import de.greenrobot.event.EventBus;

@SuppressLint({"SetJavaScriptEnabled"})
public class JumpBrowserActivity extends BrowserActivity {
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        if (!MyApplication.getIsMainOpened()) {
            setSwipeBackEnable(false);
        }
    }

    protected WebViewClient createChromeClient() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Helper.showLog(BrowserActivity.TAG, "url-->" + url);
                    Uri uri = Uri.parse(url);
                    Intent intent;
                    if (url.contains(ShareConstants.PATCH_SUFFIX) || url.contains("http://lina" +
                            ".elementfresh.com/boohee201508")) {
                        try {
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            JumpBrowserActivity.this.startActivity(intent);
                        } catch (Exception e) {
                        }
                    } else if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains
                            ("https://")) {
                        BrowserActivity.comeOnBaby(JumpBrowserActivity.this.activity, "", url);
                        if (url.contains("user_diets.html")) {
                            EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                        }
                    } else if (!(BooheeScheme.handleUrl(JumpBrowserActivity.this, url) || uri ==
                            null)) {
                        try {
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            JumpBrowserActivity.this.startActivity(intent);
                        } catch (Exception e2) {
                        }
                    }
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                JumpBrowserActivity.this.mProgressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                JumpBrowserActivity.this.mProgressBar.setVisibility(8);
                String tmpTitle = JumpBrowserActivity.this.webView.getTitle();
                if (!TextUtils.isEmpty(tmpTitle) && !TextUtils.equals(JumpBrowserActivity.this
                        .mTitle, tmpTitle)) {
                    JumpBrowserActivity.this.mTitle = tmpTitle;
                    if (!TextUtil.isEmpty(JumpBrowserActivity.this.mTitle)) {
                        JumpBrowserActivity.this.setTitle(JumpBrowserActivity.this.mTitle);
                    }
                }
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        };
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

    public static void comeOnBaby(Context context, String title, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent(context, JumpBrowserActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}
