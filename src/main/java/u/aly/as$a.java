package u.aly;

/* compiled from: ClientStats */
class as$a extends di<as> {
    private as$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (as) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (as) bzVar);
    }

    public void a(cy cyVar, as asVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!asVar.e()) {
                    throw new cz("Required field 'successful_requests' was not found in serialized data! Struct: " + toString());
                } else if (asVar.i()) {
                    asVar.m();
                    return;
                } else {
                    throw new cz("Required field 'failed_requests' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    asVar.a = cyVar.w();
                    asVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    asVar.b = cyVar.w();
                    asVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    asVar.c = cyVar.w();
                    asVar.c(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, as asVar) throws cf {
        asVar.m();
        cyVar.a(as.n());
        cyVar.a(as.o());
        cyVar.a(asVar.a);
        cyVar.c();
        cyVar.a(as.p());
        cyVar.a(asVar.b);
        cyVar.c();
        if (asVar.l()) {
            cyVar.a(as.q());
            cyVar.a(asVar.c);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
