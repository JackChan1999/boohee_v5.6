package u.aly;

import android.content.Context;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.b;
import com.umeng.analytics.h;
import u.aly.g.a;

/* compiled from: ImLatent */
public class an implements x {
    private static an l = null;
    private final long a = 1296000000;
    private final long b = 129600000;
    private final int c = 1800000;
    private final int d = 10000;
    private h e;
    private ab f;
    private long g = 1296000000;
    private int h = 10000;
    private long i = 0;
    private long j = 0;
    private Context k;

    public static synchronized an a(Context context, ab abVar) {
        an anVar;
        synchronized (an.class) {
            if (l == null) {
                l = new an(context, abVar);
                l.a(g.a(context).b());
            }
            anVar = l;
        }
        return anVar;
    }

    private an(Context context, ab abVar) {
        this.k = context;
        this.e = h.a(context);
        this.f = abVar;
    }

    public boolean a() {
        if (this.e.g() || this.f.f()) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.f.o();
        if (currentTimeMillis > this.g) {
            this.i = (long) b.a(this.h, c.a(this.k));
            this.j = currentTimeMillis;
            return true;
        } else if (currentTimeMillis <= 129600000) {
            return false;
        } else {
            this.i = 0;
            this.j = currentTimeMillis;
            return true;
        }
    }

    public long b() {
        return this.i;
    }

    public long c() {
        return this.j;
    }

    public void a(a aVar) {
        this.g = aVar.a(1296000000);
        int b = aVar.b(0);
        if (b != 0) {
            this.h = b;
        } else if (AnalyticsConfig.sLatentWindow <= 0 || AnalyticsConfig.sLatentWindow > 1800000) {
            this.h = 10000;
        } else {
            this.h = AnalyticsConfig.sLatentWindow;
        }
    }
}
