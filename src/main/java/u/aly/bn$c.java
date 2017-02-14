package u.aly;

import java.util.ArrayList;
import java.util.BitSet;

/* compiled from: Session */
class bn$c extends dj<bn> {
    private bn$c() {
    }

    public void a(cy cyVar, bn bnVar) throws cf {
        de deVar = (de) cyVar;
        deVar.a(bnVar.a);
        deVar.a(bnVar.b);
        deVar.a(bnVar.c);
        deVar.a(bnVar.d);
        BitSet bitSet = new BitSet();
        if (bnVar.t()) {
            bitSet.set(0);
        }
        if (bnVar.y()) {
            bitSet.set(1);
        }
        if (bnVar.B()) {
            bitSet.set(2);
        }
        deVar.a(bitSet, 3);
        if (bnVar.t()) {
            deVar.a(bnVar.e.size());
            for (bi b : bnVar.e) {
                b.b(deVar);
            }
        }
        if (bnVar.y()) {
            deVar.a(bnVar.f.size());
            for (bg b2 : bnVar.f) {
                b2.b(deVar);
            }
        }
        if (bnVar.B()) {
            bnVar.g.b(deVar);
        }
    }

    public void b(cy cyVar, bn bnVar) throws cf {
        int i = 0;
        de deVar = (de) cyVar;
        bnVar.a = deVar.z();
        bnVar.a(true);
        bnVar.b = deVar.x();
        bnVar.b(true);
        bnVar.c = deVar.x();
        bnVar.c(true);
        bnVar.d = deVar.x();
        bnVar.d(true);
        BitSet b = deVar.b(3);
        if (b.get(0)) {
            cu cuVar = new cu((byte) 12, deVar.w());
            bnVar.e = new ArrayList(cuVar.b);
            for (int i2 = 0; i2 < cuVar.b; i2++) {
                bi biVar = new bi();
                biVar.a(deVar);
                bnVar.e.add(biVar);
            }
            bnVar.e(true);
        }
        if (b.get(1)) {
            cu cuVar2 = new cu((byte) 12, deVar.w());
            bnVar.f = new ArrayList(cuVar2.b);
            while (i < cuVar2.b) {
                bg bgVar = new bg();
                bgVar.a(deVar);
                bnVar.f.add(bgVar);
                i++;
            }
            bnVar.f(true);
        }
        if (b.get(2)) {
            bnVar.g = new bo();
            bnVar.g.a(deVar);
            bnVar.g(true);
        }
    }
}
