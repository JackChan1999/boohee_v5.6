package u.aly;

/* compiled from: ActiveUser */
class aq$a extends di<aq> {
    private aq$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (aq) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (aq) bzVar);
    }

    public void a(cy cyVar, aq aqVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                aqVar.j();
                return;
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    aqVar.a = cyVar.z();
                    aqVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    aqVar.b = cyVar.z();
                    aqVar.b(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, aq aqVar) throws cf {
        aqVar.j();
        cyVar.a(aq.k());
        if (aqVar.a != null) {
            cyVar.a(aq.l());
            cyVar.a(aqVar.a);
            cyVar.c();
        }
        if (aqVar.b != null) {
            cyVar.a(aq.m());
            cyVar.a(aqVar.b);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
