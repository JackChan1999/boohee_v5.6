package u.aly;

/* compiled from: UMEnvelope */
class bq$a extends di<bq> {
    private bq$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bq) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bq) bzVar);
    }

    public void a(cy cyVar, bq bqVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!bqVar.o()) {
                    throw new cz("Required field 'serial_num' was not found in serialized data! Struct: " + toString());
                } else if (!bqVar.r()) {
                    throw new cz("Required field 'ts_secs' was not found in serialized data! Struct: " + toString());
                } else if (bqVar.u()) {
                    bqVar.I();
                    return;
                } else {
                    throw new cz("Required field 'length' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.a = cyVar.z();
                    bqVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.b = cyVar.z();
                    bqVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.c = cyVar.z();
                    bqVar.c(true);
                    break;
                case (short) 4:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.d = cyVar.w();
                    bqVar.d(true);
                    break;
                case (short) 5:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.e = cyVar.w();
                    bqVar.e(true);
                    break;
                case (short) 6:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.f = cyVar.w();
                    bqVar.f(true);
                    break;
                case (short) 7:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.g = cyVar.A();
                    bqVar.g(true);
                    break;
                case (short) 8:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.h = cyVar.z();
                    bqVar.h(true);
                    break;
                case (short) 9:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.i = cyVar.z();
                    bqVar.i(true);
                    break;
                case (short) 10:
                    if (l.b != (byte) 8) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bqVar.j = cyVar.w();
                    bqVar.j(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bq bqVar) throws cf {
        bqVar.I();
        cyVar.a(bq.J());
        if (bqVar.a != null) {
            cyVar.a(bq.K());
            cyVar.a(bqVar.a);
            cyVar.c();
        }
        if (bqVar.b != null) {
            cyVar.a(bq.L());
            cyVar.a(bqVar.b);
            cyVar.c();
        }
        if (bqVar.c != null) {
            cyVar.a(bq.M());
            cyVar.a(bqVar.c);
            cyVar.c();
        }
        cyVar.a(bq.N());
        cyVar.a(bqVar.d);
        cyVar.c();
        cyVar.a(bq.O());
        cyVar.a(bqVar.e);
        cyVar.c();
        cyVar.a(bq.P());
        cyVar.a(bqVar.f);
        cyVar.c();
        if (bqVar.g != null) {
            cyVar.a(bq.Q());
            cyVar.a(bqVar.g);
            cyVar.c();
        }
        if (bqVar.h != null) {
            cyVar.a(bq.R());
            cyVar.a(bqVar.h);
            cyVar.c();
        }
        if (bqVar.i != null) {
            cyVar.a(bq.S());
            cyVar.a(bqVar.i);
            cyVar.c();
        }
        if (bqVar.H()) {
            cyVar.a(bq.T());
            cyVar.a(bqVar.j);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
