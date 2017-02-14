package u.aly;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.a;
import com.umeng.analytics.e;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* compiled from: MemoCache */
public class t {
    private List<r> a = new ArrayList();
    private ap b = null;
    private ar c = null;
    private au d = null;
    private bh e = null;
    private Context f = null;

    public t(Context context) {
        this.f = context;
    }

    public Context a() {
        return this.f;
    }

    protected boolean a(int i) {
        return true;
    }

    public synchronized int b() {
        int size;
        size = this.a.size();
        if (this.b != null) {
            size++;
        }
        return size;
    }

    public synchronized void a(r rVar) {
        this.a.add(rVar);
    }

    public void a(bp bpVar) {
        String g = aa.g(this.f);
        if (g != null) {
            synchronized (this) {
                if (this.b != null && new ab(this.f).f()) {
                    bpVar.a(this.b);
                    this.b = null;
                }
                for (r a : this.a) {
                    a.a(bpVar, g);
                }
                this.a.clear();
            }
            bpVar.a(c());
            bpVar.a(d());
            bpVar.a(e());
            bpVar.a(h());
            bpVar.a(f());
            bpVar.a(g());
            bpVar.a(j());
            bpVar.a(i());
        }
    }

    public synchronized void a(ap apVar) {
        this.b = apVar;
    }

    public synchronized ar c() {
        if (this.c == null) {
            this.c = new ar();
            a(this.f);
        }
        return this.c;
    }

    public synchronized au d() {
        if (this.d == null) {
            this.d = new au();
            b(this.f);
        }
        return this.d;
    }

    public synchronized bh e() {
        if (this.e == null) {
            this.e = new bh();
            c(this.f);
        }
        return this.e;
    }

    public bc f() {
        try {
            return g.a(this.f).a();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public bb g() {
        try {
            return e.a(this.f).b();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public as h() {
        try {
            return ab.a(this.f);
        } catch (Exception e) {
            e.printStackTrace();
            return new as();
        }
    }

    public Map<String, Integer> i() {
        return al.a(this.f).e();
    }

    public aq j() {
        String[] a = e.a(this.f);
        if (a == null || TextUtils.isEmpty(a[0]) || TextUtils.isEmpty(a[1])) {
            return null;
        }
        return new aq(a[0], a[1]);
    }

    private void a(Context context) {
        try {
            this.c.a(AnalyticsConfig.getAppkey(context));
            this.c.e(AnalyticsConfig.getChannel(context));
            if (!(AnalyticsConfig.mWrapperType == null || AnalyticsConfig.mWrapperVersion == null)) {
                this.c.f(AnalyticsConfig.mWrapperType);
                this.c.g(AnalyticsConfig.mWrapperVersion);
            }
            this.c.c(bt.v(context));
            this.c.a(bm.ANDROID);
            this.c.d(a.c);
            this.c.b(bt.d(context));
            this.c.a(Integer.parseInt(bt.c(context)));
            this.c.h(bt.w(context));
            this.c.c(AnalyticsConfig.mVerticalType);
            this.c.d(AnalyticsConfig.getSDKVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b(Context context) {
        try {
            this.d.f(bt.a());
            this.d.a(bt.f(context));
            this.d.b(bt.g(context));
            this.d.c(bt.q(context));
            this.d.e(Build.MODEL);
            this.d.g("Android");
            this.d.h(VERSION.RELEASE);
            int[] s = bt.s(context);
            if (s != null) {
                this.d.a(new bk(s[1], s[0]));
            }
            if (AnalyticsConfig.GPU_RENDERER == null || AnalyticsConfig.GPU_VENDER != null) {
                this.d.i(Build.BOARD);
                this.d.j(Build.BRAND);
                this.d.a(Build.TIME);
                this.d.k(Build.MANUFACTURER);
                this.d.l(Build.ID);
                this.d.m(Build.DEVICE);
            } else {
                this.d.i(Build.BOARD);
                this.d.j(Build.BRAND);
                this.d.a(Build.TIME);
                this.d.k(Build.MANUFACTURER);
                this.d.l(Build.ID);
                this.d.m(Build.DEVICE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void c(Context context) {
        try {
            String[] k = bt.k(context);
            if (bt.d.equals(k[0])) {
                this.e.a(ao.ACCESS_TYPE_WIFI);
            } else if (bt.c.equals(k[0])) {
                this.e.a(ao.ACCESS_TYPE_2G_3G);
            } else {
                this.e.a(ao.ACCESS_TYPE_UNKNOWN);
            }
            if (!"".equals(k[1])) {
                this.e.e(k[1]);
            }
            this.e.c(bt.t(context));
            k = bt.o(context);
            this.e.d(bt.z(context));
            this.e.b(k[0]);
            this.e.a(k[1]);
            this.e.a(bt.n(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
