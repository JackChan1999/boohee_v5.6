package u.aly;

import java.util.BitSet;

/* compiled from: ControlPolicy */
class at$c extends dj<at> {
    private at$c() {
    }

    public void a(cy cyVar, at atVar) throws cf {
        de deVar = (de) cyVar;
        BitSet bitSet = new BitSet();
        if (atVar.e()) {
            bitSet.set(0);
        }
        deVar.a(bitSet, 1);
        if (atVar.e()) {
            atVar.a.b(deVar);
        }
    }

    public void b(cy cyVar, at atVar) throws cf {
        de deVar = (de) cyVar;
        if (deVar.b(1).get(0)) {
            atVar.a = new bf();
            atVar.a.a(deVar);
            atVar.a(true);
        }
    }
}
