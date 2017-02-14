package u.aly;

import java.util.BitSet;

/* compiled from: Error */
class av$c extends dj<av> {
    private av$c() {
    }

    public void a(cy cyVar, av avVar) throws cf {
        de deVar = (de) cyVar;
        deVar.a(avVar.a);
        deVar.a(avVar.b);
        BitSet bitSet = new BitSet();
        if (avVar.l()) {
            bitSet.set(0);
        }
        deVar.a(bitSet, 1);
        if (avVar.l()) {
            deVar.a(avVar.c.a());
        }
    }

    public void b(cy cyVar, av avVar) throws cf {
        de deVar = (de) cyVar;
        avVar.a = deVar.x();
        avVar.b(true);
        avVar.b = deVar.z();
        avVar.c(true);
        if (deVar.b(1).get(0)) {
            avVar.c = aw.a(deVar.w());
            avVar.d(true);
        }
    }
}
