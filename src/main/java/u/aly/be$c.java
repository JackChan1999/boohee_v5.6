package u.aly;

import java.util.ArrayList;
import java.util.BitSet;

/* compiled from: InstantMsg */
class be$c extends dj<be> {
    private be$c() {
    }

    public void a(cy cyVar, be beVar) throws cf {
        de deVar = (de) cyVar;
        deVar.a(beVar.a);
        BitSet bitSet = new BitSet();
        if (beVar.k()) {
            bitSet.set(0);
        }
        if (beVar.p()) {
            bitSet.set(1);
        }
        if (beVar.u()) {
            bitSet.set(2);
        }
        deVar.a(bitSet, 3);
        if (beVar.k()) {
            deVar.a(beVar.b.size());
            for (av b : beVar.b) {
                b.b(deVar);
            }
        }
        if (beVar.p()) {
            deVar.a(beVar.c.size());
            for (ax b2 : beVar.c) {
                b2.b(deVar);
            }
        }
        if (beVar.u()) {
            deVar.a(beVar.d.size());
            for (ax b22 : beVar.d) {
                b22.b(deVar);
            }
        }
    }

    public void b(cy cyVar, be beVar) throws cf {
        int i;
        int i2 = 0;
        de deVar = (de) cyVar;
        beVar.a = deVar.z();
        beVar.a(true);
        BitSet b = deVar.b(3);
        if (b.get(0)) {
            cu cuVar;
            cuVar = new cu((byte) 12, deVar.w());
            beVar.b = new ArrayList(cuVar.b);
            for (i = 0; i < cuVar.b; i++) {
                av avVar = new av();
                avVar.a(deVar);
                beVar.b.add(avVar);
            }
            beVar.b(true);
        }
        if (b.get(1)) {
            cuVar = new cu((byte) 12, deVar.w());
            beVar.c = new ArrayList(cuVar.b);
            for (i = 0; i < cuVar.b; i++) {
                ax axVar = new ax();
                axVar.a(deVar);
                beVar.c.add(axVar);
            }
            beVar.c(true);
        }
        if (b.get(2)) {
            cu cuVar2 = new cu((byte) 12, deVar.w());
            beVar.d = new ArrayList(cuVar2.b);
            while (i2 < cuVar2.b) {
                ax axVar2 = new ax();
                axVar2.a(deVar);
                beVar.d.add(axVar2);
                i2++;
            }
            beVar.d(true);
        }
    }
}
