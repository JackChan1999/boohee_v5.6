package u.aly;

import com.umeng.analytics.h.b;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* compiled from: Sender */
class z$1 implements b {
    final /* synthetic */ z a;

    z$1(z zVar) {
        this.a = zVar;
    }

    public void a(File file) {
    }

    public boolean b(File file) {
        Throwable th;
        InputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] b = bu.b(fileInputStream);
                try {
                    int i;
                    bu.c(fileInputStream);
                    byte[] a = z.a(this.a).a(b);
                    if (a == null) {
                        i = 1;
                    } else {
                        i = z.a(this.a, a);
                    }
                    if (i == 2 && z.b(this.a).m()) {
                        z.b(this.a).l();
                    }
                    if (!z.c(this.a) && i == 1) {
                        return false;
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } catch (Throwable th2) {
                th = th2;
                bu.c(fileInputStream);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            bu.c(fileInputStream);
            throw th;
        }
    }

    public void c(File file) {
        z.b(this.a).k();
    }
}
