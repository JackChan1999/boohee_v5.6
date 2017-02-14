package cn.sharesdk.tencent.qzone;

import android.app.Instrumentation;
import com.mob.tools.utils.Ln;

class k extends Thread {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public void run() {
        try {
            new Instrumentation().sendKeyDownUpSync(4);
        } catch (Throwable th) {
            Ln.e(th);
            this.a.a.finish();
            this.a.a.c.onCancel(null, 0);
        }
    }
}
