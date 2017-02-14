package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.ReportPolicy;
import com.umeng.analytics.ReportPolicy.b;
import com.umeng.analytics.ReportPolicy.c;
import com.umeng.analytics.ReportPolicy.d;
import com.umeng.analytics.ReportPolicy.e;
import com.umeng.analytics.ReportPolicy.g;
import com.umeng.analytics.ReportPolicy.i;
import com.umeng.analytics.ReportPolicy.j;
import com.umeng.analytics.ReportPolicy.k;
import com.umeng.analytics.f;
import com.umeng.analytics.h;
import java.util.List;

/* compiled from: CacheImpl */
public final class l implements q, x {
    private final long a = 28800000;
    private final int b = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    private t c = null;
    private h d = null;
    private ab e = null;
    private am f = null;
    private al g = null;
    private an h = null;
    private a i = null;
    private u.aly.g.a j = null;
    private int k = 10;
    private long l = 0;
    private int m = 0;
    private int n = 0;
    private Context o;

    /* compiled from: CacheImpl */
    public class a {
        final /* synthetic */ l a;
        private i b;
        private int c = -1;
        private int d = -1;
        private int e = -1;
        private int f = -1;

        public a(l lVar) {
            this.a = lVar;
            int[] a = lVar.j.a(-1, -1);
            this.c = a[0];
            this.d = a[1];
        }

        protected void a(boolean z) {
            int i = 1;
            int i2 = 0;
            if (this.a.f.c()) {
                i iVar;
                if (!((this.b instanceof b) && this.b.a())) {
                    i = 0;
                }
                if (i != 0) {
                    iVar = this.b;
                } else {
                    iVar = new b(this.a.e, this.a.f);
                }
                this.b = iVar;
            } else {
                if (!((this.b instanceof c) && this.b.a())) {
                    i = 0;
                }
                if (i == 0) {
                    if (z && this.a.h.a()) {
                        this.b = new c((int) this.a.h.b());
                        this.a.b((int) this.a.h.b());
                    } else if (bv.a && this.a.j.b()) {
                        bv.b("Debug: send log every 15 seconds");
                        this.b = new com.umeng.analytics.ReportPolicy.a(this.a.e);
                    } else if (this.a.g.a()) {
                        bv.b("Start A/B Test");
                        if (this.a.g.b() == 6) {
                            if (this.a.j.a()) {
                                i2 = this.a.j.d(90000);
                            } else if (this.d > 0) {
                                i2 = this.d;
                            } else {
                                i2 = this.f;
                            }
                        }
                        this.b = b(this.a.g.b(), i2);
                    } else {
                        i = this.e;
                        i2 = this.f;
                        if (this.c != -1) {
                            i = this.c;
                            i2 = this.d;
                        }
                        this.b = b(i, i2);
                    }
                }
            }
            bv.b("Report policy : " + this.b.getClass().getSimpleName());
        }

        public i b(boolean z) {
            a(z);
            return this.b;
        }

        private i b(int i, int i2) {
            switch (i) {
                case 0:
                    return this.b instanceof ReportPolicy.h ? this.b : new ReportPolicy.h();
                case 1:
                    return this.b instanceof d ? this.b : new d();
                case 4:
                    if (this.b instanceof g) {
                        return this.b;
                    }
                    return new g(this.a.e);
                case 5:
                    if (this.b instanceof j) {
                        return this.b;
                    }
                    return new j(this.a.o);
                case 6:
                    if (!(this.b instanceof e)) {
                        return new e(this.a.e, (long) i2);
                    }
                    i iVar = this.b;
                    ((e) iVar).a((long) i2);
                    return iVar;
                case 8:
                    if (this.b instanceof k) {
                        return this.b;
                    }
                    return new k(this.a.e);
                default:
                    if (this.b instanceof d) {
                        return this.b;
                    }
                    return new d();
            }
        }

        public void a(int i, int i2) {
            this.e = i;
            this.f = i2;
        }

        public void a(u.aly.g.a aVar) {
            int[] a = aVar.a(-1, -1);
            this.c = a[0];
            this.d = a[1];
        }
    }

    public l(Context context) {
        this.o = context;
        this.c = new t(context);
        this.e = new ab(context);
        this.d = h.a(context);
        this.j = g.a(context).b();
        this.i = new a(this);
        this.g = al.a(this.o);
        this.f = am.a(this.o);
        this.h = an.a(this.o, this.e);
        SharedPreferences a = y.a(this.o);
        this.l = a.getLong("thtstart", 0);
        this.m = a.getInt("gkvc", 0);
        this.n = a.getInt("ekvc", 0);
    }

    public void a() {
        if (bt.m(this.o)) {
            e();
        } else {
            bv.b("network is unavailable");
        }
    }

    public void a(r rVar) {
        if (rVar != null) {
            this.c.a(rVar);
        }
        a(rVar instanceof bn);
    }

    public void b(r rVar) {
        this.c.a(rVar);
    }

    public void b() {
        if (this.c.b() > 0) {
            try {
                byte[] b = b(a(new int[0]));
                if (b != null) {
                    this.d.a(b);
                }
            } catch (Throwable th) {
                if (th instanceof OutOfMemoryError) {
                    this.d.f();
                }
                if (th != null) {
                    th.printStackTrace();
                }
            }
        }
        y.a(this.o).edit().putLong("thtstart", this.l).putInt("gkvc", this.m).putInt("ekvc", this.n).commit();
    }

    public void c() {
        a(a(new int[0]));
    }

    private void a(boolean z) {
        boolean f = this.e.f();
        if (f) {
            this.c.a(new ap(this.e.n()));
        }
        if (b(z)) {
            e();
        } else if (f || d()) {
            b();
        }
    }

    private void a(int i) {
        int currentTimeMillis = (int) (System.currentTimeMillis() - this.e.o());
        a(a(i, currentTimeMillis));
        f.a(new com.umeng.analytics.g(this) {
            final /* synthetic */ l a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.a();
            }
        }, (long) i);
    }

    private void a(bp bpVar) {
        if (bpVar != null) {
            e a = e.a(this.o);
            a.a();
            bpVar.a(a.b());
            byte[] b = b(d(bpVar));
            if (b != null) {
                c b2;
                if (f()) {
                    b2 = c.b(this.o, AnalyticsConfig.getAppkey(this.o), b);
                } else {
                    b2 = c.a(this.o, AnalyticsConfig.getAppkey(this.o), b);
                }
                b = b2.c();
                h a2 = h.a(this.o);
                a2.f();
                a2.b(b);
                a.d();
            }
        }
    }

    protected bp a(int... iArr) {
        Object obj = null;
        try {
            if (TextUtils.isEmpty(AnalyticsConfig.getAppkey(this.o))) {
                bv.e("Appkey is missing ,Please check AndroidManifest.xml");
                return null;
            }
            byte[] e = h.a(this.o).e();
            bp a = e == null ? null : a(e);
            if (a == null && this.c.b() == 0) {
                return null;
            }
            bp bpVar;
            if (a == null) {
                bpVar = new bp();
            } else {
                bpVar = a;
            }
            this.c.a(bpVar);
            if (bv.a && bpVar.B()) {
                for (bn p : bpVar.z()) {
                    Object obj2;
                    if (p.p() > 0) {
                        obj2 = 1;
                    } else {
                        obj2 = obj;
                    }
                    obj = obj2;
                }
                if (obj == null) {
                    bv.d("missing Activities or PageViews");
                }
            }
            a = this.f.a(this.o, bpVar);
            if (iArr == null || iArr.length != 2) {
                return a;
            }
            at atVar = new at();
            atVar.a(new bf(iArr[0] / 1000, (long) iArr[1]));
            a.a(atVar);
            return a;
        } catch (Throwable e2) {
            bv.e("Fail to construct message ...", e2);
            h.a(this.o).f();
            return null;
        }
    }

    private bp a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            bz bpVar = new bp();
            new cc().a(bpVar, bArr);
            return bpVar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] b(bp bpVar) {
        if (bpVar == null) {
            return null;
        }
        try {
            byte[] a = new ci().a(bpVar);
            bv.b(bpVar.toString());
            return a;
        } catch (Throwable e) {
            bv.e("Fail to serialize log ...", e);
            return null;
        }
    }

    private boolean b(boolean z) {
        if (!bt.m(this.o)) {
            bv.b("network is unavailable");
            return false;
        } else if (this.e.f()) {
            return true;
        } else {
            return this.i.b(z).a(z);
        }
    }

    private boolean d() {
        return this.c.b() > this.k;
    }

    private void e() {
        try {
            if (this.d.g()) {
                z zVar = new z(this.o, this.e);
                zVar.a((x) this);
                if (this.f.c()) {
                    zVar.b(true);
                }
                zVar.a();
                return;
            }
            bp a = a(new int[0]);
            if (c(a)) {
                z zVar2 = new z(this.o, this.e);
                zVar2.a((x) this);
                if (this.f.c()) {
                    zVar2.b(true);
                }
                zVar2.a(d(a));
                zVar2.a(f());
                zVar2.a();
                return;
            }
            bv.e(" not legitimate!");
        } catch (Throwable th) {
            if (th instanceof OutOfMemoryError) {
                if (th != null) {
                    th.printStackTrace();
                }
            } else if (th != null) {
                th.printStackTrace();
            }
        }
    }

    private boolean c(bp bpVar) {
        if (bpVar == null) {
            bv.e("No data to report");
            return false;
        } else if (bpVar.c() == null || bpVar.f() == null || bpVar.j() == null || bpVar.m() == null) {
            return false;
        } else {
            return true;
        }
    }

    private bp d(bp bpVar) {
        int i;
        int i2;
        int i3;
        long currentTimeMillis;
        int i4 = 0;
        int i5 = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        List u = bpVar.u();
        if (u != null) {
            int size = u.size();
            if (size > 0) {
                i = 0;
                i2 = 0;
                for (i3 = 0; i3 < size; i3++) {
                    i2 += ((be) u.get(i3)).q();
                    i += ((be) u.get(i3)).l();
                }
                i4 = i;
                i = i2;
                currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.l <= 28800000) {
                    i2 = i - 5000;
                    i3 = i4 - 5000;
                    if (i2 > 0 || i3 > 0) {
                        a(i2, i3, u);
                    }
                    if (i2 > 0) {
                        i = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
                    }
                    this.m = i;
                    if (i3 <= 0) {
                        i5 = i4;
                    }
                    this.n = i5;
                    this.l = currentTimeMillis;
                } else {
                    i2 = this.m <= BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT ? i : (this.m + i) - 5000;
                    i3 = this.n <= BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT ? i4 : (this.n + i4) - 5000;
                    if (i2 > 0 || i3 > 0) {
                        a(i2, i3, u);
                    }
                    this.m = i2 <= 0 ? BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT : i + this.m;
                    if (i3 <= 0) {
                        i5 = this.n + i4;
                    }
                    this.n = i5;
                }
                return bpVar;
            }
        }
        i = 0;
        currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.l <= 28800000) {
            if (this.m <= BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT) {
            }
            if (this.n <= BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT) {
            }
            a(i2, i3, u);
            if (i2 <= 0) {
            }
            this.m = i2 <= 0 ? BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT : i + this.m;
            if (i3 <= 0) {
                i5 = this.n + i4;
            }
            this.n = i5;
        } else {
            i2 = i - 5000;
            i3 = i4 - 5000;
            a(i2, i3, u);
            if (i2 > 0) {
                i = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
            }
            this.m = i;
            if (i3 <= 0) {
                i5 = i4;
            }
            this.n = i5;
            this.l = currentTimeMillis;
        }
        return bpVar;
    }

    private void a(int i, int i2, List<be> list) {
        int i3;
        int size;
        int size2 = list.size();
        if (i > 0) {
            i3 = size2 - 1;
            while (i3 >= 0) {
                List s = ((be) list.get(i3)).s();
                if (s.size() >= i) {
                    i3 = s.size() - i;
                    for (size = s.size() - 1; size >= i3; size--) {
                        s.remove(size);
                    }
                } else {
                    i -= s.size();
                    s.clear();
                    i3--;
                }
            }
        }
        if (i2 > 0) {
            for (i3 = size2 - 1; i3 >= 0; i3--) {
                List n = ((be) list.get(i3)).n();
                if (n.size() >= i2) {
                    i3 = n.size() - i2;
                    for (size = n.size() - 1; size >= i3; size--) {
                        n.remove(size);
                    }
                    return;
                }
                i2 -= n.size();
                n.clear();
            }
        }
    }

    private boolean f() {
        switch (this.j.c(-1)) {
            case -1:
                return AnalyticsConfig.sEncrypt;
            case 1:
                return true;
            default:
                return false;
        }
    }

    private void b(int i) {
        a(i);
    }

    public void a(u.aly.g.a aVar) {
        this.g.a(aVar);
        this.f.a(aVar);
        this.h.a(aVar);
        this.i.a(aVar);
    }
}
