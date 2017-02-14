package u.aly;

/* compiled from: Error */
class av$a extends di<av> {
    private av$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (av) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (av) bzVar);
    }

    public void a(cy cyVar, av avVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (avVar.e()) {
                    avVar.m();
                    return;
                }
                throw new cz("Required field 'ts' was not found in serialized data! Struct: " + toString());
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    avVar.a = cyVar.x();
                    avVar.b(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    avVar.b = cyVar.z();
                    avVar.c(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    avVar.c = aw.a(cyVar.w());
                    avVar.d(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, av avVar) throws cf {
        avVar.m();
        cyVar.a(av.n());
        cyVar.a(av.o());
        cyVar.a(avVar.a);
        cyVar.c();
        if (avVar.b != null) {
            cyVar.a(av.p());
            cyVar.a(avVar.b);
            cyVar.c();
        }
        if (avVar.c != null && avVar.l()) {
            cyVar.a(av.q());
            cyVar.a(avVar.c.a());
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
