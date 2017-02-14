package cn.sharesdk.framework.authorize;

import android.app.Instrumentation;
import com.mob.tools.utils.Ln;

class i extends Thread {
    final /* synthetic */ h a;

    i(h hVar) {
        this.a = hVar;
    }

    public void run() {
        try {
            new Instrumentation().sendKeyDownUpSync(4);
        } catch (Throwable th) {
            Ln.e(th);
            AuthorizeListener authorizeListener = this.a.a.a.getAuthorizeListener();
            if (authorizeListener != null) {
                authorizeListener.onCancel();
            }
            this.a.a.finish();
        }
    }
}
