package cn.sharesdk.framework;

import cn.sharesdk.framework.statistics.a;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import java.util.HashMap;

class m extends Thread {
    final /* synthetic */ a a;
    final /* synthetic */ k b;

    m(k kVar, a aVar) {
        this.b = kVar;
        this.a = aVar;
    }

    public void run() {
        try {
            String g = this.a.g(this.b.j);
            HashMap fromJson = new Hashon().fromJson(g);
            HashMap hashMap = new HashMap();
            if (this.b.a(this.a, fromJson, hashMap)) {
                this.b.b(hashMap);
            }
            this.a.a(this.b.j, g);
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
