package u.aly;

import java.util.BitSet;

/* compiled from: UMEnvelope */
class bq$c extends dj<bq> {
    private bq$c() {
    }

    public void a(cy cyVar, bq bqVar) throws cf {
        de deVar = (de) cyVar;
        deVar.a(bqVar.a);
        deVar.a(bqVar.b);
        deVar.a(bqVar.c);
        deVar.a(bqVar.d);
        deVar.a(bqVar.e);
        deVar.a(bqVar.f);
        deVar.a(bqVar.g);
        deVar.a(bqVar.h);
        deVar.a(bqVar.i);
        BitSet bitSet = new BitSet();
        if (bqVar.H()) {
            bitSet.set(0);
        }
        deVar.a(bitSet, 1);
        if (bqVar.H()) {
            deVar.a(bqVar.j);
        }
    }

    public void b(cy cyVar, bq bqVar) throws cf {
        de deVar = (de) cyVar;
        bqVar.a = deVar.z();
        bqVar.a(true);
        bqVar.b = deVar.z();
        bqVar.b(true);
        bqVar.c = deVar.z();
        bqVar.c(true);
        bqVar.d = deVar.w();
        bqVar.d(true);
        bqVar.e = deVar.w();
        bqVar.e(true);
        bqVar.f = deVar.w();
        bqVar.f(true);
        bqVar.g = deVar.A();
        bqVar.g(true);
        bqVar.h = deVar.z();
        bqVar.h(true);
        bqVar.i = deVar.z();
        bqVar.i(true);
        if (deVar.b(1).get(0)) {
            bqVar.j = deVar.w();
            bqVar.j(true);
        }
    }
}
