package cn.sharesdk.tencent.qzone;

import com.mob.tools.utils.Ln;

class e extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ d b;

    e(d dVar, String str) {
        this.b = dVar;
        this.a = str;
    }

    public void run() {
        try {
            this.b.a(this.a);
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
