package u.aly;

/* compiled from: Location */
class bg$a extends di<bg> {
    private bg$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bg) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bg) bzVar);
    }

    public void a(cy cyVar, bg bgVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                if (!bgVar.e()) {
                    throw new cz("Required field 'lat' was not found in serialized data! Struct: " + toString());
                } else if (!bgVar.i()) {
                    throw new cz("Required field 'lng' was not found in serialized data! Struct: " + toString());
                } else if (bgVar.l()) {
                    bgVar.m();
                    return;
                } else {
                    throw new cz("Required field 'ts' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 4) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bgVar.a = cyVar.y();
                    bgVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != (byte) 4) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bgVar.b = cyVar.y();
                    bgVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 10) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bgVar.c = cyVar.x();
                    bgVar.c(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bg bgVar) throws cf {
        bgVar.m();
        cyVar.a(bg.n());
        cyVar.a(bg.o());
        cyVar.a(bgVar.a);
        cyVar.c();
        cyVar.a(bg.p());
        cyVar.a(bgVar.b);
        cyVar.c();
        cyVar.a(bg.q());
        cyVar.a(bgVar.c);
        cyVar.c();
        cyVar.d();
        cyVar.b();
    }
}
