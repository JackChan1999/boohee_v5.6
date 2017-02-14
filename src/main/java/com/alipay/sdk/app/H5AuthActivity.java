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

public class H5AuthActivity extends Activity {
    private WebView a;
    private com.alipay.sdk.widget.a b;
    private Handler c;
    private boolean d;
    private boolean e;
    private Runnable f = new c(this);

    private class a extends WebViewClient {
        final /* synthetic */ H5AuthActivity a;

        private a(H5AuthActivity h5AuthActivity) {
            this.a = h5AuthActivity;
        }

        public final void onReceivedError(WebView webView, int i, String str, String str2) {
            this.a.e = true;
            super.onReceivedError(webView, i, str, str2);
        }

        public final void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            com.alipay.sdk.app.statistic.a.a(c.a, c.g, "证书错误");
            if (this.a.d) {
                sslErrorHandler.proceed();
                this.a.d = false;
                return;
            }
            this.a.runOnUiThread(new d(this, sslErrorHandler));
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (TextUtils.equals(str, com.alipay.sdk.cons.a.k) || TextUtils.equals(str, com.alipay.sdk.cons.a.l)) {
                n.a = n.a();
                this.a.finish();
            } else if (str.startsWith(com.alipay.sdk.cons.a.j)) {
                try {
                    String substring = str.substring(str.indexOf(com.alipay.sdk.cons.a.j) + 24);
                    int parseInt = Integer.parseInt(substring.substring(substring.lastIndexOf(com.alipay.sdk.cons.a.m) + 10));
                    o a;
                    if (parseInt == o.SUCCEEDED.a()) {
                        String decode = URLDecoder.decode(str);
                        decode = decode.substring(decode.indexOf(com.alipay.sdk.cons.a.j) + 24, decode.lastIndexOf(com.alipay.sdk.cons.a.m));
                        a = o.a(parseInt);
                        n.a = n.a(a.a(), a.b(), decode);
                    } else {
                        a = o.a(o.FAILED.a());
                        n.a = n.a(a.a(), a.b(), "");
                    }
                } catch (Exception e) {
                    n.a = n.b();
                }
                this.a.runOnUiThread(new g(this));
            } else {
                webView.loadUrl(str);
            }
            return true;
        }

        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            H5AuthActivity.c(this.a);
            this.a.c.postDelayed(this.a.f, 30000);
            super.onPageStarted(webView, str, bitmap);
        }

        public final void onPageFinished(WebView webView, String str) {
            H5AuthActivity.f(this.a);
            this.a.c.removeCallbacks(this.a.f);
        }
    }

    static /* synthetic */ void c(H5AuthActivity h5AuthActivity) {
        if (h5AuthActivity.b == null) {
            h5AuthActivity.b = new com.alipay.sdk.widget.a(h5AuthActivity, com.alipay.sdk.widget.a.a);
        }
        try {
            h5AuthActivity.b.a();
        } catch (Exception e) {
            h5AuthActivity.b = null;
        }
    }

    static /* synthetic */ void f(H5AuthActivity h5AuthActivity) {
        if (h5AuthActivity.b != null) {
            h5AuthActivity.b.b();
        }
        h5AuthActivity.b = null;
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
                    this.a.setDownloadListener(new b(this));
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
        } else if (this.e) {
            o a = o.a(o.NETWORK_ERROR.a());
            n.a = n.a(a.a(), a.b(), "");
            finish();
        }
    }

    public void finish() {
        Object obj = AuthTask.a;
        synchronized (obj) {
            try {
                obj.notify();
            } catch (Exception e) {
            }
        }
        super.finish();
    }

    private static void a() {
        Object obj = AuthTask.a;
        synchronized (obj) {
            try {
                obj.notify();
            } catch (Exception e) {
            }
        }
    }

    private void b() {
        if (this.b == null) {
            this.b = new com.alipay.sdk.widget.a(this, com.alipay.sdk.widget.a.a);
        }
        try {
            this.b.a();
        } catch (Exception e) {
            this.b = null;
        }
    }

    private void c() {
        if (this.b != null) {
            this.b.b();
        }
        this.b = null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
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
            this.a = null;
        }
    }
}
