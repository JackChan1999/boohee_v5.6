package u.aly;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map.Entry;

/* compiled from: UALogEntry */
class bp$c extends dj<bp> {
    private bp$c() {
    }

    public void a(cy cyVar, bp bpVar) throws cf {
        de deVar = (de) cyVar;
        bpVar.a.b(deVar);
        bpVar.b.b(deVar);
        bpVar.c.b(deVar);
        bpVar.d.b(deVar);
        BitSet bitSet = new BitSet();
        if (bpVar.r()) {
            bitSet.set(0);
        }
        if (bpVar.w()) {
            bitSet.set(1);
        }
        if (bpVar.B()) {
            bitSet.set(2);
        }
        if (bpVar.E()) {
            bitSet.set(3);
        }
        if (bpVar.H()) {
            bitSet.set(4);
        }
        if (bpVar.K()) {
            bitSet.set(5);
        }
        if (bpVar.N()) {
            bitSet.set(6);
        }
        if (bpVar.R()) {
            bitSet.set(7);
        }
        deVar.a(bitSet, 8);
        if (bpVar.r()) {
            bpVar.e.b(deVar);
        }
        if (bpVar.w()) {
            deVar.a(bpVar.f.size());
            for (be b : bpVar.f) {
                b.b(deVar);
            }
        }
        if (bpVar.B()) {
            deVar.a(bpVar.g.size());
            for (bn b2 : bpVar.g) {
                b2.b(deVar);
            }
        }
        if (bpVar.E()) {
            bpVar.h.b(deVar);
        }
        if (bpVar.H()) {
            bpVar.i.b(deVar);
        }
        if (bpVar.K()) {
            bpVar.j.b(deVar);
        }
        if (bpVar.N()) {
            bpVar.k.b(deVar);
        }
        if (bpVar.R()) {
            deVar.a(bpVar.l.size());
            for (Entry entry : bpVar.l.entrySet()) {
                deVar.a((String) entry.getKey());
                deVar.a(((Integer) entry.getValue()).intValue());
            }
        }
    }

    public void b(cy cyVar, bp bpVar) throws cf {
        int i;
        int i2 = 0;
        de deVar = (de) cyVar;
        bpVar.a = new as();
        bpVar.a.a(deVar);
        bpVar.a(true);
        bpVar.b = new ar();
        bpVar.b.a(deVar);
        bpVar.b(true);
        bpVar.c = new au();
        bpVar.c.a(deVar);
        bpVar.c(true);
        bpVar.d = new bh();
        bpVar.d.a(deVar);
        bpVar.d(true);
        BitSet b = deVar.b(8);
        if (b.get(0)) {
            bpVar.e = new ap();
            bpVar.e.a(deVar);
            bpVar.e(true);
        }
        if (b.get(1)) {
            cu cuVar;
            cuVar = new cu((byte) 12, deVar.w());
            bpVar.f = new ArrayList(cuVar.b);
            for (i = 0; i < cuVar.b; i++) {
                be beVar = new be();
                beVar.a(deVar);
                bpVar.f.add(beVar);
            }
            bpVar.f(true);
        }
        if (b.get(2)) {
            cuVar = new cu((byte) 12, deVar.w());
            bpVar.g = new ArrayList(cuVar.b);
            for (i = 0; i < cuVar.b; i++) {
                bn bnVar = new bn();
                bnVar.a(deVar);
                bpVar.g.add(bnVar);
            }
            bpVar.g(true);
        }
        if (b.get(3)) {
            bpVar.h = new bc();
            bpVar.h.a(deVar);
            bpVar.h(true);
        }
        if (b.get(4)) {
            bpVar.i = new bb();
            bpVar.i.a(deVar);
            bpVar.i(true);
        }
        if (b.get(5)) {
            bpVar.j = new aq();
            bpVar.j.a(deVar);
            bpVar.j(true);
        }
        if (b.get(6)) {
            bpVar.k = new at();
            bpVar.k.a(deVar);
            bpVar.k(true);
        }
        if (b.get(7)) {
            cv cvVar = new cv((byte) 11, (byte) 8, deVar.w());
            bpVar.l = new HashMap(cvVar.c * 2);
            while (i2 < cvVar.c) {
                bpVar.l.put(deVar.z(), Integer.valueOf(deVar.w()));
                i2++;
            }
            bpVar.l(true);
        }
    }
}
