package cn.sharesdk.framework;

import com.mob.tools.utils.Ln;

class h extends Thread {
    final /* synthetic */ String[] a;
    final /* synthetic */ f b;

    h(f fVar, String[] strArr) {
        this.b = fVar;
        this.a = strArr;
    }

    public void run() {
        try {
            this.b.j();
            this.b.a.doAuthorize(this.a);
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
