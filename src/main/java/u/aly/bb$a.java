package u.aly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/* compiled from: IdTracking */
class bb$a extends di<bb> {
    private bb$a() {
    }

    public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
        b(cyVar, (bb) bzVar);
    }

    public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
        a(cyVar, (bb) bzVar);
    }

    public void a(cy cyVar, bb bbVar) throws cf {
        cyVar.j();
        while (true) {
            ct l = cyVar.l();
            if (l.b == (byte) 0) {
                cyVar.k();
                bbVar.p();
                return;
            }
            int i;
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 13) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    cv n = cyVar.n();
                    bbVar.a = new HashMap(n.c * 2);
                    for (i = 0; i < n.c; i++) {
                        String z = cyVar.z();
                        ba baVar = new ba();
                        baVar.a(cyVar);
                        bbVar.a.put(z, baVar);
                    }
                    cyVar.o();
                    bbVar.a(true);
                    break;
                case (short) 2:
                    if (l.b != df.m) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    cu p = cyVar.p();
                    bbVar.b = new ArrayList(p.b);
                    for (i = 0; i < p.b; i++) {
                        az azVar = new az();
                        azVar.a(cyVar);
                        bbVar.b.add(azVar);
                    }
                    cyVar.q();
                    bbVar.b(true);
                    break;
                case (short) 3:
                    if (l.b != (byte) 11) {
                        db.a(cyVar, l.b);
                        break;
                    }
                    bbVar.c = cyVar.z();
                    bbVar.c(true);
                    break;
                default:
                    db.a(cyVar, l.b);
                    break;
            }
            cyVar.m();
        }
    }

    public void b(cy cyVar, bb bbVar) throws cf {
        bbVar.p();
        cyVar.a(bb.q());
        if (bbVar.a != null) {
            cyVar.a(bb.r());
            cyVar.a(new cv((byte) 11, (byte) 12, bbVar.a.size()));
            for (Entry entry : bbVar.a.entrySet()) {
                cyVar.a((String) entry.getKey());
                ((ba) entry.getValue()).b(cyVar);
            }
            cyVar.e();
            cyVar.c();
        }
        if (bbVar.b != null && bbVar.l()) {
            cyVar.a(bb.s());
            cyVar.a(new cu((byte) 12, bbVar.b.size()));
            for (az b : bbVar.b) {
                b.b(cyVar);
            }
            cyVar.f();
            cyVar.c();
        }
        if (bbVar.c != null && bbVar.o()) {
            cyVar.a(bb.t());
            cyVar.a(bbVar.c);
            cyVar.c();
        }
        cyVar.d();
        cyVar.b();
    }
}
