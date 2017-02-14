package u.aly;

/* compiled from: Page */
class bi$a extends di<bi> {
    private bi$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bi) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bi) bzVar);
    }

    public void a(cy cyVar, bi biVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (biVar.i()) {
                    biVar.j();
                    return;
                }
                throw new cz("Required field 'duration' was not found in serialized data! Struct: " + toString());
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    biVar.a = cyVar.z();
                    biVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    biVar.b = cyVar.x();
                    biVar.b(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bi biVar) throws cf {
        biVar.j();
        cyVar.a(bi.k());
        if (biVar.a != null) {
            cyVar.a(bi.l());
            cyVar.a(biVar.a);
            cyVar.c();
        }
        cyVar.a(bi.m());
        cyVar.a(biVar.b);
        cyVar.c();
        cyVar.d();
        cyVar.b();
    }
}
