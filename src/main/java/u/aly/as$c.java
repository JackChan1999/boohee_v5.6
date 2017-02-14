package u.aly;

import java.util.BitSet;

/* compiled from: ClientStats */
class as$c extends dj<as> {
    private as$c() {
    }

    public void a(cy cyVar, as asVar) throws cf {
        de deVar = (de) cyVar;
        deVar.a(asVar.a);
        deVar.a(asVar.b);
        BitSet bitSet = new BitSet();
        if (asVar.l()) {
            bitSet.set(0);
        }
        deVar.a(bitSet, 1);
        if (asVar.l()) {
            deVar.a(asVar.c);
        }
    }

    public void b(cy cyVar, as asVar) throws cf {
        de deVar = (de) cyVar;
        asVar.a = deVar.w();
        asVar.a(true);
        asVar.b = deVar.w();
        asVar.b(true);
        if (deVar.b(1).get(0)) {
            asVar.c = deVar.w();
            asVar.c(true);
        }
    }
}
