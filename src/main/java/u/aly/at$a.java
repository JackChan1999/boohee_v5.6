package u.aly;

/* compiled from: ControlPolicy */
class at$a extends di<at> {
    private at$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (at) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (at) bzVar);
    }

    public void a(cy cyVar, at atVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                atVar.f();
                return;
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 12) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    atVar.a = new bf();
                    atVar.a.a(cyVar);
                    atVar.a(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, at atVar) throws cf {
        atVar.f();
        cyVar.a(at.h());
        if (atVar.a != null && atVar.e()) {
            cyVar.a(at.i());
            atVar.a.b(cyVar);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
