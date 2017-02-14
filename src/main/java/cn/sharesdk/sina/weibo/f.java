package cn.sharesdk.sina.weibo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.b;
import cn.sharesdk.framework.authorize.g;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.umeng.socialize.common.SocializeConstants;

public class f extends b {
    private boolean d;

    public f(g gVar) {
        super(gVar);
    }

    private void a(Platform platform, String str) {
        new g(this, platform, str).start();
    }

    private Intent b(String str) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        intent.putExtra("sms_body", "");
        intent.setFlags(268435456);
        return intent;
    }

    protected void a(String str) {
        if (!this.d) {
            this.d = true;
            Bundle urlToBundle = R.urlToBundle(str);
            String string = urlToBundle.getString("error");
            String string2 = urlToBundle.getString("error_code");
            if (this.c == null) {
                return;
            }
            if (string == null && string2 == null) {
                Object string3 = urlToBundle.getString("code");
                if (TextUtils.isEmpty(string3)) {
                    this.c.onError(new Throwable("Authorize code is empty"));
                }
                a(this.a.a().getPlatform(), string3);
            } else if (string.equals("access_denied")) {
                this.c.onCancel();
            } else {
                int i = 0;
                try {
                    i = R.parseInt(string2);
                } catch (Throwable th) {
                    Ln.e(th);
                }
                this.c.onError(new Throwable(string + " (" + i + SocializeConstants.OP_CLOSE_PAREN));
            }
        }
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (!TextUtils.isEmpty(this.b) && str.startsWith(this.b)) {
            webView.stopLoading();
            this.a.finish();
            a(str);
        } else if (str.startsWith("sms:")) {
            String substring = str.substring(4);
            try {
                Intent b = b(substring);
                b.setPackage("com.android.mms");
                webView.getContext().startActivity(b);
            } catch (Throwable th) {
                if (this.c != null) {
                    this.c.onError(th);
                }
            }
        } else {
            super.onPageStarted(webView, str, bitmap);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!TextUtils.isEmpty(this.b) && str.startsWith(this.b)) {
            webView.stopLoading();
            this.a.finish();
            a(str);
            return true;
        } else if (!str.startsWith("sms:")) {
            return super.shouldOverrideUrlLoading(webView, str);
        } else {
            String substring = str.substring(4);
            try {
                Intent b = b(substring);
                b.setPackage("com.android.mms");
                webView.getContext().startActivity(b);
                return true;
            } catch (Throwable th) {
                if (this.c == null) {
                    return true;
                }
                this.c.onError(th);
                return true;
            }
        }
    }
}
