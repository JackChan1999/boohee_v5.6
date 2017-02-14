package com.zxinsight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.common.SocializeConstants;
import com.zxinsight.analytics.domain.UploadImg;
import com.zxinsight.common.base.a;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.e;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.j;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;

import java.io.File;

public class WebViewActivity implements a {
    private ViewGroup       a;
    private ProgressWebView b;
    private WebView         c;
    private String          d;
    private Activity        e;
    private String          f;
    private String g = "";

    public WebViewActivity(Activity activity) {
        this.e = activity;
        Intent intent = activity.getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (!(data == null || TextUtils.isEmpty(data.getQueryParameter("key")))) {
                this.d = data.getQueryParameter("key").trim();
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("mw_key"))) {
                this.d = intent.getStringExtra("mw_key");
            }
        }
    }

    public void onCreate() {
        this.e.requestWindowFeature(1);
        this.a = c();
        this.e.setContentView(this.a);
        this.e.getWindow().setSoftInputMode(18);
        c.a("MW WebViewActivity onCreate");
    }

    public void onBackPressed() {
    }

    public void onPause() {
        if (this.b != null && VERSION.SDK_INT >= 11) {
            this.b.onPause();
        }
        if (this.c != null && VERSION.SDK_INT >= 11) {
            this.c.onPause();
        }
        if (m.a().y()) {
            MWConfiguration.getContext().sendBroadcast(new Intent("com.magicwindow.webview.pause" +
                    ".MW_MESSAGE"));
        }
        TrackAgent.currentEvent().onWebviewPause(this.e, this.d);
    }

    public void onResume() {
        if (this.b != null && VERSION.SDK_INT >= 11) {
            this.b.onResume();
        }
        if (this.c != null && VERSION.SDK_INT >= 11) {
            this.c.onResume();
        }
        if (m.a().y()) {
            MWConfiguration.getContext().sendBroadcast(new Intent("com.magicwindow.webview.resume" +
                    ".MW_MESSAGE"));
        }
        TrackAgent.currentEvent().onWebviewResume(this.e, this.d);
    }

    private void a() {
        if (this.a != null) {
            this.a.removeAllViews();
        }
        if (this.b != null) {
            this.b.getSettings().setJavaScriptEnabled(false);
            this.b.destroy();
            this.b = null;
        }
        if (this.c != null) {
            this.c.getSettings().setJavaScriptEnabled(false);
            this.c.destroy();
        }
    }

    public void onDestroy() {
        a();
    }

    public void onRestart() {
    }

    public void onNewIntent(Intent intent) {
    }

    private void b() {
        a(null);
    }

    private void a(String str) {
        ShareHelper.share(this.e, this.d, str);
    }

    @SuppressLint({"AddJavascriptInterface"})
    private ViewGroup c() {
        View relativeLayout;
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        ViewGroup relativeLayout2 = new RelativeLayout(this.e);
        relativeLayout2.setLayoutParams(layoutParams);
        String str = "mw_title_layout_" + this.d.toLowerCase();
        int a = j.a(this.e, "layout", "mw_title_layout");
        int a2 = j.a(this.e, "layout", str);
        View textView;
        if (!m.a().x() || (a == 0 && a2 == 0)) {
            relativeLayout = new RelativeLayout(this.e);
            relativeLayout.setVerticalGravity(14);
            relativeLayout.setBackgroundColor(CustomStyle.getWebViewTopBar(this.d));
            relativeLayout.setId(1000001);
            textView = new TextView(this.e);
            LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams2.addRule(9);
            layoutParams2.addRule(15, -1);
            layoutParams2.leftMargin = o.a(this.e, 8.0f);
            textView.setText(o.a("关闭", "Done"));
            textView.setTextColor(CustomStyle.textColor(CustomStyle.getWebViewLeftMenuNormal(this
                    .d), CustomStyle.getWebViewLeftMenuPressed(this.d)));
            textView.setTextSize(16.0f);
            View textView2 = new TextView(this.e);
            LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams3.addRule(11);
            layoutParams3.addRule(15, -1);
            layoutParams3.rightMargin = o.a(this.e, 8.0f);
            textView2.setText(o.a("分享", "Share"));
            textView2.setTextColor(CustomStyle.textColor(CustomStyle.getWebViewRightMenuNormal
                    (this.d), CustomStyle.getWebViewRightMenuPressed(this.d)));
            textView2.setTextSize(16.0f);
            relativeLayout.addView(textView, layoutParams2);
            if (MarketingHelper.currentMarketing(MWConfiguration.getContext()).checkShare(this.d)) {
                relativeLayout.addView(textView2, layoutParams3);
            }
            textView.setOnClickListener(new ad(this));
            textView2.setOnClickListener(new ae(this));
        } else {
            LayoutInflater from = LayoutInflater.from(this.e);
            if (a2 != 0) {
                relativeLayout = (RelativeLayout) from.inflate(a2, null);
            } else {
                relativeLayout = (RelativeLayout) from.inflate(a, null);
            }
            relativeLayout.setId(1000001);
            a2 = j.a(this.e, "id", "mw_left_menu");
            if (a2 != 0) {
                textView = relativeLayout.findViewById(a2);
                if (textView != null) {
                    textView.setOnClickListener(new aa(this));
                }
            }
            a2 = j.a(this.e, "id", "mw_right_menu");
            if (a2 != 0) {
                textView = relativeLayout.findViewById(a2);
                if (!MarketingHelper.currentMarketing(MWConfiguration.getContext()).checkShare
                        (this.d)) {
                    textView.setVisibility(8);
                }
                if (textView != null) {
                    textView.setOnClickListener(new ac(this));
                }
            }
            a2 = j.a(this.e, "id", "mw_title");
            if (a2 != 0) {
                TextView textView3 = (TextView) relativeLayout.findViewById(a2);
                if (textView3 != null) {
                    textView3.setText(MarketingHelper.currentMarketing(MWConfiguration.getContext
                            ()).getWebviewTitle(this.d));
                }
            }
        }
        layoutParams = new RelativeLayout.LayoutParams(-1, o.a(this.e, 42.0f));
        layoutParams.addRule(10);
        relativeLayout2.addView(relativeLayout, layoutParams);
        this.b = new ProgressWebView(this.e);
        this.b.setBackgroundColor(-1);
        this.b.setVerticalScrollBarEnabled(false);
        this.b.addJavascriptInterface(new JavaScriptInterface(this), "JSInterface");
        str = MarketingHelper.currentMarketing(this.e).getWebviewURL(this.d);
        c.b("MW WebViewActivity" + str);
        this.b.postDelayed(new af(this, str), 500);
        layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(3, relativeLayout.getId());
        relativeLayout2.addView(this.b, layoutParams);
        if (l.b(MarketingHelper.currentMarketing(this.e).getFloatWebURL(this.d))) {
            this.c = new WebView(this.e);
            this.c.setWebViewClient(new ag(this));
            this.c.getSettings().setJavaScriptEnabled(true);
            this.c.getSettings().setSupportZoom(true);
            this.c.getSettings().setBuiltInZoomControls(false);
            this.c.getSettings().setUseWideViewPort(true);
            this.c.getSettings().setDomStorageEnabled(true);
            this.c.getSettings().setLoadWithOverviewMode(true);
            this.c.setVerticalScrollBarEnabled(false);
            this.c.setHorizontalScrollBarEnabled(false);
            this.c.setOnTouchListener(new ah(this));
            this.c.setOnLongClickListener(new ai(this));
            this.c.loadUrl(MarketingHelper.currentMarketing(this.e).getFloatWebURL(this.d));
            layoutParams = new RelativeLayout.LayoutParams(-1, o.a(this.e, 62.0f));
            if ("0".equalsIgnoreCase(MarketingHelper.currentMarketing(this.e).getFloatWebPosition
                    (this.d))) {
                layoutParams.addRule(12, -1);
                this.b.setOnScrollChangedCallback(new aj(this));
            } else {
                layoutParams.addRule(3, 1000001);
                this.b.setOnScrollChangedCallback(new ab(this));
            }
            relativeLayout2.addView(this.c, layoutParams);
        }
        relativeLayout2.bringChildToFront(relativeLayout);
        return relativeLayout2;
    }

    private void d() {
        a(this.e, new File(this.b.a));
    }

    private void a(Context context, File file) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("mime_type", "image/jpeg");
        context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    private Uri a(Intent intent) {
        Cursor query = this.e.getContentResolver().query(intent.getData(), new String[]{"_data"},
                null, null, null);
        if (query == null) {
            return null;
        }
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        String string = query.getString(columnIndexOrThrow);
        if (string != null && (string.endsWith(".png") || string.endsWith(".PNG") || string
                .endsWith(".jpg") || string.endsWith(".JPG"))) {
            return Uri.fromFile(e.b(string, this.b.c));
        }
        query.close();
        return null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String str = null;
        if (i2 == -1) {
            Uri uri;
            if (i == 2) {
                d();
                uri = this.b.b;
            } else {
                uri = i == 3 ? a(intent) : null;
            }
            if (uri != null) {
                str = uri.getPath();
            }
            if (l.b(str)) {
                Bitmap c = e.c(str);
                str = e.d(str);
                this.g = com.zxinsight.common.util.a.a(c);
                int length = this.g.length();
                UploadImg uploadImg = new UploadImg();
                uploadImg.length = length;
                uploadImg.type = str;
                this.b.loadUrl("javascript:" + this.f + SocializeConstants.OP_OPEN_PAREN + h.a
                        (uploadImg) + SocializeConstants.OP_CLOSE_PAREN);
            }
        }
    }

    public static TranslateAnimation a(float f, float f2) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0.0f, 0, 0.0f, 1, f, 1,
                f2);
        translateAnimation.setDuration(500);
        return translateAnimation;
    }
}
