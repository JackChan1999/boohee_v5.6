package u.aly;

import java.util.ArrayList;

/* compiled from: Session */
class bn$a extends di<bn> {
    private bn$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bn) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bn) bzVar);
    }

    public void a(cy cyVar, bn bnVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!bnVar.i()) {
                    throw new cz("Required field 'start_time' was not found in serialized data! Struct: " + toString());
                } else if (!bnVar.l()) {
                    throw new cz("Required field 'end_time' was not found in serialized data! Struct: " + toString());
                } else if (bnVar.o()) {
                    bnVar.C();
                    return;
                } else {
                    throw new cz("Required field 'duration' was not found in serialized data! Struct: " + toString());
                }
            }
            cu p;
            int i;
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bnVar.a = cyVar.z();
                    bnVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bnVar.b = cyVar.x();
                    bnVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bnVar.c = cyVar.x();
                    bnVar.c(true);
                    break;
                case (short) 4:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bnVar.d = cyVar.x();
                    bnVar.d(true);
                    break;
                case (short) 5:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    bnVar.e = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        bi biVar = new bi();
                        biVar.a(cyVar);
                        bnVar.e.add(biVar);
                    }
                    cyVar.q();
                    bnVar.e(true);
                    break;
                case (short) 6:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    p = cyVar.p();
                    bnVar.f = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        bg bgVar = new bg();
                        bgVar.a(cyVar);
                        bnVar.f.add(bgVar);
                    }
                    cyVar.q();
                    bnVar.f(true);
                    break;
                case (short) 7:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bnVar.g = new bo();
                    bnVar.g.a(cyVar);
                    bnVar.g(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bn bnVar) throws cf {
        bnVar.C();
        cyVar.a(bn.D());
        if (bnVar.a != null) {
            cyVar.a(bn.E());
            cyVar.a(bnVar.a);
            cyVar.c();
        }
        cyVar.a(bn.F());
        cyVar.a(bnVar.b);
        cyVar.c();
        cyVar.a(bn.G());
        cyVar.a(bnVar.c);
        cyVar.c();
        cyVar.a(bn.H());
        cyVar.a(bnVar.d);
        cyVar.c();
        if (bnVar.e != null && bnVar.t()) {
            cyVar.a(bn.I());
            cyVar.a(new cu((byte) 12, bnVar.e.size()));
            for (bi b : bnVar.e) {
                b.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (bnVar.f != null && bnVar.y()) {
            cyVar.a(bn.J());
            cyVar.a(new cu((byte) 12, bnVar.f.size()));
            for (bg b2 : bnVar.f) {
                b2.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (bnVar.g != null && bnVar.B()) {
            cyVar.a(bn.K());
            bnVar.g.b(cyVar);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
