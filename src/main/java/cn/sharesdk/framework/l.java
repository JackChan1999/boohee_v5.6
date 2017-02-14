package cn.sharesdk.framework;

import com.mob.tools.utils.Ln;
import java.util.HashMap;

class l extends Thread {
    final /* synthetic */ k a;

    l(k kVar) {
        this.a = kVar;
    }

    public void run() {
        try {
            HashMap hashMap = new HashMap();
            if (!this.a.f() && this.a.a(hashMap)) {
                this.a.b(hashMap);
            }
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
