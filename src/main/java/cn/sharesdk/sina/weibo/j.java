package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;

class j implements SSOListener {
    final /* synthetic */ AuthorizeListener a;
    final /* synthetic */ i b;

    j(i iVar, AuthorizeListener authorizeListener) {
        this.b = iVar;
        this.a = authorizeListener;
    }

    public void onCancel() {
        this.a.onCancel();
    }

    public void onComplete(Bundle bundle) {
        try {
            R.parseLong(bundle.getString("expires_in"));
            this.a.onComplete(bundle);
        } catch (Throwable th) {
            onFailed(th);
        }
    }

    public void onFailed(Throwable th) {
        Ln.e(th);
        this.b.b(this.a);
    }
}
