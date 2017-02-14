package u.aly;

import android.content.Context;
import com.umeng.analytics.f;
import com.umeng.analytics.g;

/* compiled from: CacheService */
public final class m implements q {
    private static m c;
    private q a = new l(this.b);
    private Context b;

    private m(Context context) {
        this.b = context.getApplicationContext();
    }

    public static synchronized m a(Context context) {
        m mVar;
        synchronized (m.class) {
            if (c == null && context != null) {
                c = new m(context);
            }
            mVar = c;
        }
        return mVar;
    }

    public void a(q qVar) {
        this.a = qVar;
    }

    public void a(final r rVar) {
        f.b(new g(this) {
            final /* synthetic */ m b;

            public void a() {
                this.b.a.a(rVar);
            }
        });
    }

    public void b(r rVar) {
        this.a.b(rVar);
    }

    public void a() {
        f.b(new g(this) {
            final /* synthetic */ m a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.a.a();
            }
        });
    }

    public void b() {
        f.b(new g(this) {
            final /* synthetic */ m a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.a.b();
            }
        });
    }

    public void c() {
        f.c(new g(this) {
            final /* synthetic */ m a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.a.c();
            }
        });
    }
}
