package u.aly;

import java.util.ArrayList;

/* compiled from: InstantMsg */
class be$a extends di<be> {
    private be$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (be) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (be) bzVar);
    }

    public void a(cy cyVar, be beVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                beVar.v();
                return;
            }
            cu p;
            int i;
            ax axVar;
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    beVar.a = cyVar.z();
                    beVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    beVar.b = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        av avVar = new av();
                        avVar.a(cyVar);
                        beVar.b.add(avVar);
                    }
                    cyVar.q();
                    beVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    beVar.c = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        axVar = new ax();
                        axVar.a(cyVar);
                        beVar.c.add(axVar);
                    }
                    cyVar.q();
                    beVar.c(true);
                    break;
                case (short) 4:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    beVar.d = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        axVar = new ax();
                        axVar.a(cyVar);
                        beVar.d.add(axVar);
                    }
                    cyVar.q();
                    beVar.d(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, be beVar) throws cf {
        beVar.v();
        cyVar.a(be.w());
        if (beVar.a != null) {
            cyVar.a(be.x());
            cyVar.a(beVar.a);
            cyVar.c();
        }
        if (beVar.b != null && beVar.k()) {
            cyVar.a(be.y());
            cyVar.a(new cu((byte) 12, beVar.b.size()));
            for (av b : beVar.b) {
                b.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (beVar.c != null && beVar.p()) {
            cyVar.a(be.z());
            cyVar.a(new cu((byte) 12, beVar.c.size()));
            for (ax b2 : beVar.c) {
                b2.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (beVar.d != null && beVar.u()) {
            cyVar.a(be.A());
            cyVar.a(new cu((byte) 12, beVar.d.size()));
            for (ax b22 : beVar.d) {
                b22.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
