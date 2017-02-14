package u.aly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/* compiled from: UALogEntry */
class bp$a extends di<bp> {
    private bp$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bp) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bp) bzVar);
    }

    public void a(cy cyVar, bp bpVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                bpVar.S();
                return;
            }
            cu p;
            int i;
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.a = new as();
                    bpVar.a.a(cyVar);
                    bpVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.b = new ar();
                    bpVar.b.a(cyVar);
                    bpVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.c = new au();
                    bpVar.c.a(cyVar);
                    bpVar.c(true);
                    break;
                case (short) 4:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.d = new bh();
                    bpVar.d.a(cyVar);
                    bpVar.d(true);
                    break;
                case (short) 5:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.e = new ap();
                    bpVar.e.a(cyVar);
                    bpVar.e(true);
                    break;
                case (short) 6:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    bpVar.f = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        be beVar = new be();
                        beVar.a(cyVar);
                        bpVar.f.add(beVar);
                    }
                    cyVar.q();
                    bpVar.f(true);
                    break;
                case (short) 7:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    bpVar.g = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        bn bnVar = new bn();
                        bnVar.a(cyVar);
                        bpVar.g.add(bnVar);
                    }
                    cyVar.q();
                    bpVar.g(true);
                    break;
                case (short) 8:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.h = new bc();
                    bpVar.h.a(cyVar);
                    bpVar.h(true);
                    break;
                case (short) 9:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.i = new bb();
                    bpVar.i.a(cyVar);
                    bpVar.i(true);
                    break;
                case (short) 10:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.j = new aq();
                    bpVar.j.a(cyVar);
                    bpVar.j(true);
                    break;
                case (short) 11:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bpVar.k = new at();
                    bpVar.k.a(cyVar);
                    bpVar.k(true);
                    break;
                case (short) 12:
                    if (l.b != (byte) 13) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    cv n = cyVar.n();
                    bpVar.l = new HashMap(n.c * 2);
                    for (i = 0; i < n.c; i++) {
                        bpVar.l.put(cyVar.z(), Integer.valueOf(cyVar.w()));
                    }
                    cyVar.o();
                    bpVar.l(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bp bpVar) throws cf {
        bpVar.S();
        cyVar.a(bp.T());
        if (bpVar.a != null) {
            cyVar.a(bp.U());
            bpVar.a.b(cyVar);
            cyVar.c();
        }
        if (bpVar.b != null) {
            cyVar.a(bp.V());
            bpVar.b.b(cyVar);
            cyVar.c();
        }
        if (bpVar.c != null) {
            cyVar.a(bp.W());
            bpVar.c.b(cyVar);
            cyVar.c();
        }
        if (bpVar.d != null) {
            cyVar.a(bp.X());
            bpVar.d.b(cyVar);
            cyVar.c();
        }
        if (bpVar.e != null && bpVar.r()) {
            cyVar.a(bp.Y());
            bpVar.e.b(cyVar);
            cyVar.c();
        }
        if (bpVar.f != null && bpVar.w()) {
            cyVar.a(bp.Z());
            cyVar.a(new cu((byte) 12, bpVar.f.size()));
            for (be b : bpVar.f) {
                b.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (bpVar.g != null && bpVar.B()) {
            cyVar.a(bp.aa());
            cyVar.a(new cu((byte) 12, bpVar.g.size()));
            for (bn b2 : bpVar.g) {
                b2.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (bpVar.h != null && bpVar.E()) {
            cyVar.a(bp.ab());
            bpVar.h.b(cyVar);
            cyVar.c();
        }
        if (bpVar.i != null && bpVar.H()) {
            cyVar.a(bp.ac());
            bpVar.i.b(cyVar);
            cyVar.c();
        }
        if (bpVar.j != null && bpVar.K()) {
            cyVar.a(bp.ad());
            bpVar.j.b(cyVar);
            cyVar.c();
        }
        if (bpVar.k != null && bpVar.N()) {
            cyVar.a(bp.ae());
            bpVar.k.b(cyVar);
            cyVar.c();
        }
        if (bpVar.l != null && bpVar.R()) {
            cyVar.a(bp.af());
            cyVar.a(new cv((byte) 11, (byte) 8, bpVar.l.size()));
            for (Entry entry : bpVar.l.entrySet()) {
                cyVar.a((String) entry.getKey());
                cyVar.a(((Integer) entry.getValue()).intValue());
            }
            cyVar.e();
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
