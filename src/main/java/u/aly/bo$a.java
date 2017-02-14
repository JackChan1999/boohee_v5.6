package u.aly;

/* compiled from: Traffic */
class bo$a extends di<bo> {
    private bo$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bo) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bo) bzVar);
    }

    public void a(cy cyVar, bo boVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!boVar.e()) {
                    throw new cz("Required field 'upload_traffic' was not found in serialized data! Struct: " + toString());
                } else if (boVar.i()) {
                    boVar.j();
                    return;
                } else {
                    throw new cz("Required field 'download_traffic' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    boVar.a = cyVar.w();
                    boVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    boVar.b = cyVar.w();
                    boVar.b(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bo boVar) throws cf {
        boVar.j();
        cyVar.a(bo.k());
        cyVar.a(bo.l());
        cyVar.a(boVar.a);
        cyVar.c();
        cyVar.a(bo.m());
        cyVar.a(boVar.b);
        cyVar.c();
        cyVar.d();
        cyVar.b();
    }
}
