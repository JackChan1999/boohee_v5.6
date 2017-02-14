package cn.sharesdk.framework.authorize;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.ResizeLayout.OnResizeListener;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.umeng.socialize.common.SocializeConstants;

public class g extends a implements Callback, OnResizeListener {
    protected AuthorizeListener b;
    private AuthorizeAdapter c;
    private RegisterView d;
    private WebView e;

    private static class a implements Interpolator {
        private float[] a;

        private a() {
            this.a = new float[]{0.0f, 0.02692683f, 0.053847015f, 0.080753915f, 0.10764089f, 0.13450131f, 0.16132854f, 0.18811597f, 0.21485697f, 0.24154496f, 0.26817337f, 0.2947356f, 0.3212251f, 0.34763536f, 0.37395984f, 0.40019205f, 0.42632553f, 0.4523538f, 0.47827047f, 0.50406915f, 0.52974343f, 0.555287f, 0.5806936f, 0.60595685f, 0.6310707f, 0.65602875f, 0.68082494f, 0.70545316f, 0.72990733f, 0.75418144f, 0.7782694f, 0.8021654f, 0.8258634f, 0.8493577f, 0.8726424f, 0.89571184f, 0.9185602f, 0.94118196f, 0.9635715f, 0.9857233f, 1.0076319f, 1.0292919f, 1.0506978f, 1.0718446f, 1.0927268f, 1.1133395f, 1.1336775f, 1.1537358f, 1.1735094f, 1.1929934f, 1.1893399f, 1.1728106f, 1.1565471f, 1.1405534f, 1.1248333f, 1.1093911f, 1.0942302f, 1.0793544f, 1.0647675f, 1.050473f, 1.0364745f, 1.0227754f, 1.0093791f, 0.99628896f, 0.9835081f, 0.9710398f, 0.958887f, 0.9470527f, 0.93553996f, 0.9243516f, 0.91349024f, 0.90295863f, 0.90482706f, 0.9114033f, 0.91775465f, 0.9238795f, 0.9297765f, 0.93544406f, 0.9408808f, 0.94608533f, 0.95105654f, 0.955793f, 0.9602937f, 0.9645574f, 0.96858317f, 0.9723699f, 0.97591674f, 0.97922283f, 0.9822872f, 0.9851093f, 0.98768836f, 0.9900237f, 0.9921147f, 0.993961f, 0.99556196f, 0.9969173f, 0.9980267f, 0.99888986f, 0.99950653f, 0.9998766f, 1.0f};
        }

        public float getInterpolation(float f) {
            int i = 100;
            int i2 = (int) (100.0f * f);
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 <= 100) {
                i = i2;
            }
            return this.a[i];
        }
    }

    private AuthorizeAdapter c() {
        try {
            ActivityInfo activityInfo = this.activity.getPackageManager().getActivityInfo(this.activity.getComponentName(), 128);
            if (activityInfo.metaData == null || activityInfo.metaData.isEmpty()) {
                return null;
            }
            String string = activityInfo.metaData.getString("AuthorizeAdapter");
            if (string == null || string.length() <= 0) {
                string = activityInfo.metaData.getString("Adapter");
                if (string == null || string.length() <= 0) {
                    return null;
                }
            }
            Object newInstance = Class.forName(string).newInstance();
            return !(newInstance instanceof AuthorizeAdapter) ? null : (AuthorizeAdapter) newInstance;
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public void OnResize(int i, int i2, int i3, int i4) {
        if (this.c != null) {
            this.c.onResize(i, i2, i3, i4);
        }
    }

    public void a(AuthorizeListener authorizeListener) {
        this.b = authorizeListener;
    }

    protected RegisterView b() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.a().setOnClickListener(new h(this));
        this.e = registerView.b();
        WebSettings settings = this.e.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        this.e.setVerticalScrollBarEnabled(false);
        this.e.setHorizontalScrollBarEnabled(false);
        this.e.setWebViewClient(this.a.getAuthorizeWebviewClient(this));
        new j(this).start();
        return registerView;
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 2:
                AuthorizeListener authorizeListener;
                if (message.arg1 != 1) {
                    String str = (String) message.obj;
                    if (!TextUtils.isEmpty(str)) {
                        this.e.loadUrl(str);
                        break;
                    }
                    finish();
                    authorizeListener = this.a.getAuthorizeListener();
                    if (authorizeListener != null) {
                        authorizeListener.onError(new Throwable("Authorize URL is empty (platform: " + this.a.getPlatform().getName() + SocializeConstants.OP_CLOSE_PAREN));
                        break;
                    }
                }
                authorizeListener = this.a.getAuthorizeListener();
                if (authorizeListener != null) {
                    authorizeListener.onError(new Throwable("Network error (platform: " + this.a.getPlatform().getName() + SocializeConstants.OP_CLOSE_PAREN));
                    break;
                }
                break;
        }
        return false;
    }

    public void onCreate() {
        if (this.d == null) {
            this.d = b();
            this.d.a(this);
            this.d.a(this.c.isNotitle());
            this.c.setBodyView(this.d.d());
            this.c.setWebView(this.d.b());
            TitleLayout c = this.d.c();
            this.c.setTitleView(c);
            String name = this.a.getPlatform().getName();
            this.c.setPlatformName(this.a.getPlatform().getName());
            try {
                c.getTvTitle().setText(R.getStringRes(getContext(), name));
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
        this.c.onCreate();
        if (!(this.c == null || this.c.isPopUpAnimationDisable())) {
            Animation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
            scaleAnimation.setDuration(550);
            scaleAnimation.setInterpolator(new a());
            this.d.setAnimation(scaleAnimation);
        }
        this.activity.setContentView(this.d);
    }

    public void onDestroy() {
        if (this.c != null) {
            this.c.onDestroy();
        }
    }

    public boolean onFinish() {
        return this.c != null ? this.c.onFinish() : super.onFinish();
    }

    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (this.c != null) {
            z = this.c.onKeyEvent(i, keyEvent);
        }
        if (!z && i == 4 && keyEvent.getAction() == 0) {
            AuthorizeListener authorizeListener = this.a.getAuthorizeListener();
            if (authorizeListener != null) {
                authorizeListener.onCancel();
            }
        }
        return z ? true : super.onKeyEvent(i, keyEvent);
    }

    public void onPause() {
        if (this.c != null) {
            this.c.onPause();
        }
    }

    public void onRestart() {
        if (this.c != null) {
            this.c.onRestart();
        }
    }

    public void onResume() {
        if (this.c != null) {
            this.c.onResume();
        }
    }

    public void onStart() {
        if (this.c != null) {
            this.c.onStart();
        }
    }

    public void onStop() {
        if (this.c != null) {
            this.c.onStop();
        }
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.c == null) {
            this.c = c();
            if (this.c == null) {
                this.c = new AuthorizeAdapter();
            }
        }
        this.c.setActivity(activity);
    }
}
