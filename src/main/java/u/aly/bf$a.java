package u.aly;

/* compiled from: Latent */
class bf$a extends di<bf> {
    private bf$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bf) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bf) bzVar);
    }

    public void a(cy cyVar, bf bfVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!bfVar.e()) {
                    throw new cz("Required field 'latency' was not found in serialized data! Struct: " + toString());
                } else if (bfVar.i()) {
                    bfVar.j();
                    return;
                } else {
                    throw new cz("Required field 'interval' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bfVar.a = cyVar.w();
                    bfVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bfVar.b = cyVar.x();
                    bfVar.b(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bf bfVar) throws cf {
        bfVar.j();
        cyVar.a(bf.k());
        cyVar.a(bf.l());
        cyVar.a(bfVar.a);
        cyVar.c();
        cyVar.a(bf.m());
        cyVar.a(bfVar.b);
        cyVar.c();
        cyVar.d();
        cyVar.b();
    }
}
