package com.umeng.socialize.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/* compiled from: OauthDialog */
public class j extends Dialog {
    private static final String a = j.class.getName();
    private static final String p = "sina2/main?uid";
    private static final String q = "tenc2/main?uid";
    private static final String r = "renr2/main?uid";
    private static final String s = "douban/main?uid";
    private WebView        b;
    private UMAuthListener c;
    private View           d;
    private View           e;
    private CheckBox       f;
    private int g = 0;
    private Bundle          h;
    private UMSocialService i;
    private String j = "error";
    private Context           k;
    private Activity          l;
    private SHARE_MEDIA       m;
    private Set<String>       n;
    private MulStatusListener o;
    private Handler t = new k(this);

    /* compiled from: OauthDialog */
    private class a extends WebViewClient {
        final /* synthetic */ j b;

        private a(j jVar) {
            this.b = jVar;
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (DeviceConfig.isNetworkAvailable(this.b.k)) {
                if (str.contains("?ud_get=")) {
                    str = this.b.a(str);
                }
                if (str.contains(this.b.j)) {
                    a(str);
                }
                return super.shouldOverrideUrlLoading(webView, str);
            }
            Toast.makeText(this.b.k, "抱歉,您的网络不可用...", 0).show();
            return true;
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            Log.e(j.a, "onReceivedError: " + str2 + "\nerrCode: " + i + " description:" + str);
            if (this.b.e.getVisibility() == 0) {
                this.b.e.setVisibility(8);
            }
            super.onReceivedError(webView, i, str, str2);
            SocializeUtils.safeCloseDialog(this.b);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError
                sslError) {
            sslErrorHandler.cancel();
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (str.contains("?ud_get=")) {
                str = this.b.a(str);
            }
            if (!str.contains("access_key") || !str.contains("access_secret")) {
                super.onPageStarted(webView, str, bitmap);
            } else if (str.contains(this.b.j)) {
                a(str);
            }
        }

        public void onPageFinished(WebView webView, String str) {
            this.b.t.sendEmptyMessage(1);
            super.onPageFinished(webView, str);
            if (this.b.g == 0 && str.contains(this.b.j)) {
                a(str);
            }
        }

        private void a(String str) {
            Log.d(j.a, "OauthDialog " + str);
            this.b.g = 1;
            this.b.h = SocializeUtils.parseUrl(str);
            if (this.b.isShowing()) {
                SocializeUtils.safeCloseDialog(this.b);
            }
        }
    }

    public j(Activity activity, SocializeEntity socializeEntity, SHARE_MEDIA share_media,
             UMAuthListener uMAuthListener) {
        super(activity, ResContainer.getResourceId(activity.getApplicationContext(), ResType
                .STYLE, "umeng_socialize_popup_dialog"));
        this.k = activity.getApplicationContext();
        this.l = activity;
        this.c = uMAuthListener;
        this.m = share_media;
        this.i = UMServiceFactory.getUMSocialService(socializeEntity.mDescriptor);
        SocializeConfig config = this.i.getConfig();
        this.n = config.getFollowFids(share_media);
        this.o = config.getOauthDialogFollowListener();
        switch (s.a[share_media.ordinal()]) {
            case 1:
                this.j = p;
                break;
            case 2:
                this.j = q;
                break;
            case 3:
                this.j = r;
                break;
            case 4:
                this.j = s;
                break;
        }
        LayoutInflater layoutInflater = (LayoutInflater) this.l.getSystemService("layout_inflater");
        int resourceId = ResContainer.getResourceId(this.k, ResType.LAYOUT,
                "umeng_socialize_oauth_dialog");
        int resourceId2 = ResContainer.getResourceId(this.k, ResType.ID, "umeng_socialize_follow");
        int resourceId3 = ResContainer.getResourceId(this.k, ResType.ID,
                "umeng_socialize_follow_check");
        this.d = layoutInflater.inflate(resourceId, null);
        View findViewById = this.d.findViewById(resourceId2);
        this.f = (CheckBox) this.d.findViewById(resourceId3);
        Object obj = (this.n == null || this.n.size() <= 0) ? null : 1;
        Object obj2 = (share_media == SHARE_MEDIA.SINA || share_media == SHARE_MEDIA.TENCENT) ? 1
                : null;
        if (obj == null || obj2 == null) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
        }
        int resourceId4 = ResContainer.getResourceId(this.k, ResType.ID, "progress_bar_parent");
        resourceId = ResContainer.getResourceId(this.k, ResType.ID,
                "umeng_socialize_title_bar_leftBt");
        resourceId2 = ResContainer.getResourceId(this.k, ResType.ID,
                "umeng_socialize_title_bar_rightBt");
        resourceId3 = ResContainer.getResourceId(this.k, ResType.ID,
                "umeng_socialize_title_bar_middleTv");
        int resourceId5 = ResContainer.getResourceId(this.k, ResType.ID,
                "umeng_socialize_titlebar");
        this.e = this.d.findViewById(resourceId4);
        this.e.setVisibility(0);
        ((Button) this.d.findViewById(resourceId)).setOnClickListener(new l(this));
        this.d.findViewById(resourceId2).setVisibility(8);
        ((TextView) this.d.findViewById(resourceId3)).setText("授权" + SocialSNSHelper.getShowWord
                (this.k, share_media));
        b();
        View mVar = new m(this, this.k, findViewById, this.d.findViewById(resourceId5),
                SocializeUtils.dip2Px(this.k, 200.0f));
        mVar.addView(this.d, -1, -1);
        setContentView(mVar);
        LayoutParams attributes = getWindow().getAttributes();
        if (SocializeUtils.isFloatWindowStyle(this.k)) {
            int[] floatWindowSize = SocializeUtils.getFloatWindowSize(this.k);
            attributes.width = floatWindowSize[0];
            attributes.height = floatWindowSize[1];
            resourceId4 = ResContainer.getResourceId(getContext(), ResType.STYLE,
                    "umeng_socialize_dialog_anim_fade");
        } else {
            attributes.height = -1;
            attributes.width = -1;
            resourceId4 = ResContainer.getResourceId(getContext(), ResType.STYLE,
                    "umeng_socialize_dialog_animations");
        }
        attributes.gravity = 17;
        getWindow().getAttributes().windowAnimations = resourceId4;
    }

    private String a(SocializeEntity socializeEntity, SHARE_MEDIA share_media) {
        String str = "http://log.umsns.com/share/auth/" + SocializeUtils.getAppkey(this.k) + "/"
                + socializeEntity.mEntityKey + "/?";
        Map baseQuery = SocializeNetUtils.getBaseQuery(this.k, socializeEntity, 10);
        StringBuilder stringBuilder = new StringBuilder("via=" + share_media + com.alipay.sdk.sys
                .a.b);
        for (String str2 : baseQuery.keySet()) {
            stringBuilder.append(str2 + "=" + baseQuery.get(str2) + com.alipay.sdk.sys.a.b);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        try {
            return str + ("ud_get=" + AesHelper.encryptNoPadding(stringBuilder.toString(),
                    "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private boolean b() {
        this.b = (WebView) this.d.findViewById(ResContainer.getResourceId(this.k, ResType.ID,
                "webView"));
        this.b.setWebViewClient(c());
        this.b.setWebChromeClient(new p(this));
        this.b.requestFocusFromTouch();
        this.b.setVerticalScrollBarEnabled(false);
        this.b.setHorizontalScrollBarEnabled(false);
        this.b.setScrollBarStyle(0);
        this.b.getSettings().setCacheMode(2);
        WebSettings settings = this.b.getSettings();
        settings.setJavaScriptEnabled(true);
        if (VERSION.SDK_INT >= 8) {
            settings.setPluginState(PluginState.ON);
        }
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        if (VERSION.SDK_INT >= 8) {
            settings.setLoadWithOverviewMode(true);
            settings.setDatabaseEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setGeolocationEnabled(true);
            settings.setAppCacheEnabled(true);
        }
        if (VERSION.SDK_INT >= 11) {
            try {
                Method declaredMethod = WebSettings.class.getDeclaredMethod
                        ("setDisplayZoomControls", new Class[]{Boolean.TYPE});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(settings, new Object[]{Boolean.valueOf(false)});
            } catch (Exception e) {
            }
        }
        try {
            if (this.m == SHARE_MEDIA.RENREN) {
                CookieSyncManager.createInstance(this.k);
                CookieManager.getInstance().removeAllCookie();
            }
        } catch (Exception e2) {
        }
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    private WebViewClient c() {
        Object obj = 1;
        Object obj2 = null;
        try {
            if (WebViewClient.class.getMethod("onReceivedSslError", new Class[]{WebView.class,
                    SslErrorHandler.class, SslError.class}) == null) {
                obj = null;
            }
            obj2 = obj;
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e2) {
        }
        if (obj2 != null) {
            Log.i(a, "has method onReceivedSslError : ");
            return new q(this);
        }
        Log.i(a, "has no method onReceivedSslError : ");
        return new a();
    }

    private String a(String str) {
        try {
            String[] split = str.split("ud_get=");
            split[1] = AesHelper.decryptNoPadding(split[1], "UTF-8").trim();
            str = split[0] + split[1];
        } catch (Exception e) {
            Log.e(a, "### AuthWebViewClient解密失败");
            e.printStackTrace();
        }
        return str;
    }

    public void show() {
        super.show();
        this.h = null;
        SocializeEntity entity = this.i.getEntity();
        if (entity.mInitialized) {
            this.b.loadUrl(a(entity, this.m));
            return;
        }
        this.i.initEntity(this.k, new r(this));
    }

    public void dismiss() {
        if (this.h != null) {
            if (!TextUtils.isEmpty(this.h.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID))) {
                Log.d(a, "### dismiss ");
                if (this.c != null) {
                    this.c.onComplete(this.h, this.m);
                    if (this.f != null && this.f.isChecked()) {
                        this.t.sendEmptyMessage(2);
                    }
                }
            } else if (this.c != null) {
                this.c.onError(new SocializeException("unfetch usid..."), this.m);
            }
        } else if (this.c != null) {
            this.c.onCancel(this.m);
        }
        super.dismiss();
    }

    private void d() {
        if (this.n != null && this.n.size() > 0) {
            String[] strArr = new String[this.n.size()];
            int i = 0;
            for (String str : this.n) {
                int i2 = i + 1;
                strArr[i] = str;
                i = i2;
            }
            this.i.follow(this.k, this.m, this.o, strArr);
            e();
        }
    }

    private void e() {
        try {
            Editor edit = this.k.getSharedPreferences(ShareActivity.FOLLOW_FILE_NAME, 0).edit();
            edit.putBoolean(this.m.toString(), false);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
