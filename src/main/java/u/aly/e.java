package u.aly;

import android.content.Context;
import com.umeng.analytics.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: IdTracker */
public class e {
    public static e a;
    private final String b = "umeng_it.cache";
    private File c;
    private bb d = null;
    private long e;
    private long f;
    private Set<a> g = new HashSet();
    private a h = null;

    e(Context context) {
        this.c = new File(context.getFilesDir(), "umeng_it.cache");
        this.f = a.h;
        this.h = new a(context);
        this.h.b();
    }

    public static synchronized e a(Context context) {
        e eVar;
        synchronized (e.class) {
            if (a == null) {
                a = new e(context);
                a.a(new f(context));
                a.a(new h(context));
                a.a(new b(context));
                a.a(new k(context));
                a.a(new j(context));
                a.a(new d(context));
                a.a(new i());
                a.e();
            }
            eVar = a;
        }
        return eVar;
    }

    public boolean a(a aVar) {
        if (this.h.a(aVar.b())) {
            return this.g.add(aVar);
        }
        return false;
    }

    public void a(long j) {
        this.f = j;
    }

    public void a() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.e >= this.f) {
            Object obj = null;
            for (a aVar : this.g) {
                if (aVar.c()) {
                    if (aVar.a()) {
                        obj = 1;
                        if (!aVar.c()) {
                            this.h.b(aVar.b());
                        }
                    }
                    obj = obj;
                }
            }
            if (obj != null) {
                g();
                this.h.a();
                f();
            }
            this.e = currentTimeMillis;
        }
    }

    public bb b() {
        return this.d;
    }

    private void g() {
        bb bbVar = new bb();
        Map hashMap = new HashMap();
        List arrayList = new ArrayList();
        for (a aVar : this.g) {
            if (aVar.c()) {
                if (aVar.d() != null) {
                    hashMap.put(aVar.b(), aVar.d());
                }
                if (!(aVar.e() == null || aVar.e().isEmpty())) {
                    arrayList.addAll(aVar.e());
                }
            }
        }
        bbVar.a(arrayList);
        bbVar.a(hashMap);
        synchronized (this) {
            this.d = bbVar;
        }
    }

    public String c() {
        return null;
    }

    public void d() {
        boolean z = false;
        for (a aVar : this.g) {
            if (aVar.c()) {
                boolean z2;
                if (aVar.e() == null || aVar.e().isEmpty()) {
                    z2 = z;
                } else {
                    aVar.a(null);
                    z2 = true;
                }
                z = z2;
            }
        }
        if (z) {
            this.d.b(false);
            f();
        }
    }

    public void e() {
        bb h = h();
        if (h != null) {
            List<a> arrayList = new ArrayList(this.g.size());
            synchronized (this) {
                this.d = h;
                for (a aVar : this.g) {
                    aVar.a(this.d);
                    if (!aVar.c()) {
                        arrayList.add(aVar);
                    }
                }
                for (a aVar2 : arrayList) {
                    this.g.remove(aVar2);
                }
            }
            g();
        }
    }

    public void f() {
        if (this.d != null) {
            a(this.d);
        }
    }

    private bb h() {
        InputStream fileInputStream;
        Exception e;
        Throwable th;
        if (!this.c.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(this.c);
            try {
                byte[] b = bu.b(fileInputStream);
                bz bbVar = new bb();
                new cc().a(bbVar, b);
                bu.c(fileInputStream);
                return bbVar;
            } catch (Exception e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    bu.c(fileInputStream);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    bu.c(fileInputStream);
                    throw th;
                }
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
            e.printStackTrace();
            bu.c(fileInputStream);
            return null;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            bu.c(fileInputStream);
            throw th;
        }
    }

    private void a(bb bbVar) {
        if (bbVar != null) {
            try {
                byte[] a;
                synchronized (this) {
                    a = new ci().a(bbVar);
                }
                if (a != null) {
                    bu.a(this.c, a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
