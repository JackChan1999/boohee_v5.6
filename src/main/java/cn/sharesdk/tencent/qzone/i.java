package cn.sharesdk.tencent.qzone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.RegisterView;
import com.mob.tools.FakeActivity;
import com.mob.tools.MobUIShell;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.tencent.open.utils.SystemUtils;
import java.util.List;

public class i extends FakeActivity {
    private String a;
    private boolean b;
    private PlatformActionListener c;
    private RegisterView d;
    private WebView e;
    private String f;
    private boolean g;
    private boolean h;
    private QZoneWebShareAdapter i;

    private QZoneWebShareAdapter b() {
        try {
            String string = this.activity.getPackageManager().getActivityInfo(this.activity.getComponentName(), 128).metaData.getString("QZoneWebShareAdapter");
            if (string == null || string.length() <= 0) {
                return null;
            }
            Object newInstance = Class.forName(string).newInstance();
            return !(newInstance instanceof QZoneWebShareAdapter) ? null : (QZoneWebShareAdapter) newInstance;
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private void b(String str) {
        String str2 = str == null ? "" : new String(str);
        Bundle urlToBundle = R.urlToBundle(str);
        if (urlToBundle == null) {
            this.h = true;
            finish();
            this.c.onError(null, 0, new Throwable("failed to parse callback uri: " + str2));
            return;
        }
        String string = urlToBundle.getString("action");
        if ("share".equals(string) || SystemUtils.QZONE_SHARE_CALLBACK_ACTION.equals(string)) {
            string = urlToBundle.getString("result");
            if ("cancel".equals(string)) {
                finish();
                this.c.onCancel(null, 0);
                return;
            } else if ("complete".equals(string)) {
                String string2 = urlToBundle.getString("response");
                if (TextUtils.isEmpty(string2)) {
                    this.h = true;
                    finish();
                    this.c.onError(null, 0, new Throwable("response empty" + str2));
                    return;
                }
                this.g = true;
                finish();
                this.c.onComplete(null, 0, new Hashon().fromJson(string2));
                return;
            } else {
                this.h = true;
                finish();
                this.c.onError(null, 0, new Throwable("operation failed: " + str2));
                return;
            }
        }
        this.h = true;
        finish();
        this.c.onError(null, 0, new Throwable("action error: " + str2));
    }

    private void c() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.a));
            this.activity.startActivity(intent);
            MobUIShell.registerExecutor(this.f, this);
            finish();
        } catch (Throwable th) {
            if (this.c != null) {
                this.c.onError(null, 0, th);
            }
        }
    }

    private void c(String str) {
        List queryIntentActivities;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        try {
            queryIntentActivities = this.activity.getPackageManager().queryIntentActivities(intent, 1);
        } catch (Throwable th) {
            Ln.e(th);
            queryIntentActivities = null;
        }
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            startActivity(intent);
        }
    }

    private void d() {
        this.d = a();
        try {
            int stringRes = R.getStringRes(getContext(), "share_to_qzone");
            if (stringRes > 0) {
                this.d.c().getTvTitle().setText(stringRes);
            }
        } catch (Throwable th) {
            Ln.e(th);
            this.d.c().setVisibility(8);
        }
        this.i.setBodyView(this.d.d());
        this.i.setWebView(this.d.b());
        this.i.setTitleView(this.d.c());
        this.i.onCreate();
        this.activity.setContentView(this.d);
        if ("none".equals(DeviceHelper.getInstance(this.activity).getDetailNetworkTypeForStatic())) {
            this.h = true;
            finish();
            this.c.onError(null, 0, new Throwable("failed to load webpage, network disconnected."));
            return;
        }
        this.d.b().loadUrl(this.a);
    }

    protected RegisterView a() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.c().getChildAt(registerView.c().getChildCount() - 1).setVisibility(8);
        registerView.a().setOnClickListener(new j(this));
        this.e = registerView.b();
        WebSettings settings = this.e.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(1);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(this.activity.getDir("database", 0).getPath());
        this.e.setVerticalScrollBarEnabled(false);
        this.e.setHorizontalScrollBarEnabled(false);
        this.e.setWebViewClient(new l(this));
        return registerView;
    }

    public void a(PlatformActionListener platformActionListener) {
        this.c = platformActionListener;
    }

    public void a(String str) {
        this.f = "tencent" + str;
    }

    public void a(String str, boolean z) {
        this.a = str;
        this.b = z;
    }

    public void onCreate() {
        Intent intent = this.activity.getIntent();
        String scheme = intent.getScheme();
        if (scheme != null && scheme.startsWith(this.f)) {
            finish();
            Bundle urlToBundle = R.urlToBundle(intent.getDataString());
            scheme = String.valueOf(urlToBundle.get("result"));
            String valueOf = String.valueOf(urlToBundle.get("action"));
            if (!SystemUtils.QQ_SHARE_CALLBACK_ACTION.equals(valueOf) && !SystemUtils.QZONE_SHARE_CALLBACK_ACTION.equals(valueOf)) {
                return;
            }
            if ("complete".equals(scheme)) {
                if (this.c != null) {
                    this.c.onComplete(null, 9, new Hashon().fromJson(String.valueOf(urlToBundle.get("response"))));
                }
            } else if ("error".equals(scheme)) {
                if (this.c != null) {
                    this.c.onError(null, 9, new Throwable(String.valueOf(urlToBundle.get("response"))));
                }
            } else if (this.c != null) {
                this.c.onCancel(null, 9);
            }
        } else if (this.b) {
            c();
        } else {
            d();
        }
    }

    public void onDestroy() {
        if (!(this.b || this.h || this.g)) {
            this.c.onCancel(null, 0);
        }
        if (this.i != null) {
            this.i.onDestroy();
        }
    }

    public boolean onFinish() {
        return this.i != null ? this.i.onFinish() : super.onFinish();
    }

    public void onPause() {
        if (this.i != null) {
            this.i.onPause();
        }
    }

    public void onRestart() {
        if (this.i != null) {
            this.i.onRestart();
        }
    }

    public void onResume() {
        if (this.i != null) {
            this.i.onResume();
        }
    }

    public void onStart() {
        if (this.i != null) {
            this.i.onStart();
        }
    }

    public void onStop() {
        if (this.i != null) {
            this.i.onStop();
        }
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.i == null) {
            this.i = b();
            if (this.i == null) {
                this.i = new QZoneWebShareAdapter();
            }
        }
        this.i.setActivity(activity);
    }
}
