package com.alipay.sdk.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.alipay.sdk.app.statistic.c;
import com.alipay.sdk.util.i;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class H5PayActivity extends Activity {
    private WebView a;
    private com.alipay.sdk.widget.a b;
    private Handler c;
    private boolean d;
    private boolean e;
    private Runnable f = new h(this);

    private class a extends WebViewClient {
        final /* synthetic */ H5PayActivity a;

        private a(H5PayActivity h5PayActivity) {
            this.a = h5PayActivity;
        }

        public final void onReceivedError(WebView webView, int i, String str, String str2) {
            this.a.d = true;
            super.onReceivedError(webView, i, str, str2);
        }

        public final void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            com.alipay.sdk.app.statistic.a.a(c.a, c.f, "证书错误");
            if (this.a.e) {
                sslErrorHandler.proceed();
                this.a.e = false;
                return;
            }
            this.a.runOnUiThread(new i(this, sslErrorHandler));
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.startsWith(com.alipay.sdk.cons.a.i)) {
                return false;
            }
            if (TextUtils.equals(str, com.alipay.sdk.cons.a.k) || TextUtils.equals(str, com.alipay.sdk.cons.a.l)) {
                n.a = n.a();
                this.a.finish();
                return true;
            } else if (str.startsWith(com.alipay.sdk.cons.a.j)) {
                try {
                    String substring = str.substring(str.indexOf(com.alipay.sdk.cons.a.j) + 24);
                    int parseInt = Integer.parseInt(substring.substring(substring.lastIndexOf(com.alipay.sdk.cons.a.m) + 10));
                    if (parseInt == o.SUCCEEDED.a() || parseInt == o.PAY_WAITTING.a()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        substring = URLDecoder.decode(str);
                        substring = substring.substring(substring.indexOf(com.alipay.sdk.cons.a.j) + 24, substring.lastIndexOf(com.alipay.sdk.cons.a.m));
                        if (substring.contains("&return_url=")) {
                            int indexOf = substring.indexOf("&return_url=") + 12;
                            stringBuilder.append(substring.split("&return_url=")[0]).append("&return_url=").append(substring.substring(indexOf, substring.indexOf(com.alipay.sdk.sys.a.a, indexOf))).append(substring.substring(substring.indexOf(com.alipay.sdk.sys.a.a, indexOf)));
                            substring = stringBuilder.toString();
                        }
                        o a = o.a(parseInt);
                        n.a = n.a(a.a(), a.b(), substring);
                        this.a.runOnUiThread(new l(this));
                        return true;
                    }
                    o a2 = o.a(o.FAILED.a());
                    n.a = n.a(a2.a(), a2.b(), "");
                    this.a.runOnUiThread(new l(this));
                    return true;
                } catch (Exception e) {
                    n.a = n.b();
                }
            } else {
                webView.loadUrl(str);
                return true;
            }
        }

        public final void onLoadResource(WebView webView, String str) {
        }

        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            H5PayActivity.c(this.a);
            this.a.c.postDelayed(this.a.f, 30000);
            super.onPageStarted(webView, str, bitmap);
        }

        public final void onPageFinished(WebView webView, String str) {
            H5PayActivity.f(this.a);
            this.a.c.removeCallbacks(this.a.f);
        }
    }

    static /* synthetic */ void c(H5PayActivity h5PayActivity) {
        if (h5PayActivity.b == null) {
            h5PayActivity.b = new com.alipay.sdk.widget.a(h5PayActivity, com.alipay.sdk.widget.a.a);
        }
        h5PayActivity.b.a();
    }

    static /* synthetic */ void f(H5PayActivity h5PayActivity) {
        if (h5PayActivity.b != null) {
            h5PayActivity.b.b();
        }
        h5PayActivity.b = null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                finish();
                return;
            }
            try {
                String string = extras.getString("url");
                if (i.b(string)) {
                    Method method;
                    super.requestWindowFeature(1);
                    this.c = new Handler(getMainLooper());
                    Object string2 = extras.getString("cookie");
                    if (!TextUtils.isEmpty(string2)) {
                        CookieSyncManager.createInstance(getApplicationContext()).sync();
                        CookieManager.getInstance().setCookie(string, string2);
                        CookieSyncManager.getInstance().sync();
                    }
                    View linearLayout = new LinearLayout(getApplicationContext());
                    LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                    linearLayout.setOrientation(1);
                    setContentView(linearLayout, layoutParams);
                    this.a = new WebView(getApplicationContext());
                    layoutParams.weight = 1.0f;
                    this.a.setVisibility(0);
                    linearLayout.addView(this.a, layoutParams);
                    WebSettings settings = this.a.getSettings();
                    settings.setUserAgentString(settings.getUserAgentString() + i.c(getApplicationContext()));
                    settings.setRenderPriority(RenderPriority.HIGH);
                    settings.setSupportMultipleWindows(true);
                    settings.setJavaScriptEnabled(true);
                    settings.setSavePassword(false);
                    settings.setJavaScriptCanOpenWindowsAutomatically(true);
                    settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
                    settings.setAllowFileAccess(false);
                    settings.setTextSize(TextSize.NORMAL);
                    this.a.setVerticalScrollbarOverlay(true);
                    this.a.setWebViewClient(new a());
                    this.a.loadUrl(string);
                    if (VERSION.SDK_INT >= 7) {
                        try {
                            method = this.a.getSettings().getClass().getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                            if (method != null) {
                                method.invoke(this.a.getSettings(), new Object[]{Boolean.valueOf(true)});
                            }
                        } catch (Exception e) {
                        }
                    }
                    try {
                        method = this.a.getClass().getMethod("removeJavascriptInterface", new Class[0]);
                        if (method != null) {
                            method.invoke(this.a, new Object[]{"searchBoxJavaBridge_"});
                            method.invoke(this.a, new Object[]{"accessibility"});
                            method.invoke(this.a, new Object[]{"accessibilityTraversal"});
                            return;
                        }
                        return;
                    } catch (Exception e2) {
                        return;
                    }
                }
                finish();
            } catch (Exception e3) {
                finish();
            }
        } catch (Exception e4) {
            finish();
        }
    }

    public void onBackPressed() {
        if (!this.a.canGoBack()) {
            n.a = n.a();
            finish();
        } else if (this.d) {
            o a = o.a(o.NETWORK_ERROR.a());
            n.a = n.a(a.a(), a.b(), "");
            finish();
        }
    }

    public void finish() {
        Object obj = PayTask.a;
        synchronized (obj) {
            try {
                obj.notify();
            } catch (Exception e) {
            }
        }
        super.finish();
    }

    private static void a() {
        Object obj = PayTask.a;
        synchronized (obj) {
            try {
                obj.notify();
            } catch (Exception e) {
            }
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private void b() {
        if (this.b == null) {
            this.b = new com.alipay.sdk.widget.a(this, com.alipay.sdk.widget.a.a);
        }
        this.b.a();
    }

    private void c() {
        if (this.b != null) {
            this.b.b();
        }
        this.b = null;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.a != null) {
            this.a.removeAllViews();
            try {
                this.a.destroy();
            } catch (Throwable th) {
            }
            this.a = null;
        }
    }
}
