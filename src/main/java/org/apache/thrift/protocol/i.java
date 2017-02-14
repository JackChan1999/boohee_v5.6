package org.apache.thrift.protocol;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import org.apache.thrift.f;

public class i {
    private static int a = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;

    public static void a(f fVar, byte b) {
        a(fVar, b, a);
    }

    public static void a(f fVar, byte b, int i) {
        int i2 = 0;
        if (i <= 0) {
            throw new f("Maximum skip depth exceeded");
        }
        switch (b) {
            case (byte) 2:
                fVar.q();
                return;
            case (byte) 3:
                fVar.r();
                return;
            case (byte) 4:
                fVar.v();
                return;
            case (byte) 6:
                fVar.s();
                return;
            case (byte) 8:
                fVar.t();
                return;
            case (byte) 10:
                fVar.u();
                return;
            case (byte) 11:
                fVar.x();
                return;
            case (byte) 12:
                fVar.g();
                while (true) {
                    c i3 = fVar.i();
                    if (i3.b == (byte) 0) {
                        fVar.h();
                        return;
                    } else {
                        a(fVar, i3.b, i - 1);
                        fVar.j();
                    }
                }
            case (byte) 13:
                e k = fVar.k();
                while (i2 < k.c) {
                    a(fVar, k.a, i - 1);
                    a(fVar, k.b, i - 1);
                    i2++;
                }
                fVar.l();
                return;
            case (byte) 14:
                j o = fVar.o();
                while (i2 < o.b) {
                    a(fVar, o.a, i - 1);
                    i2++;
                }
                fVar.p();
                return;
            case (byte) 15:
                d m = fVar.m();
                while (i2 < m.b) {
                    a(fVar, m.a, i - 1);
                    i2++;
                }
                fVar.n();
                return;
            default:
                return;
        }
    }
}
