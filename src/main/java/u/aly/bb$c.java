package u.aly;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map.Entry;

/* compiled from: IdTracking */
class bb$c extends dj<bb> {
    private bb$c() {
    }

    public void a(cy cyVar, bb bbVar) throws cf {
        cyVar = (de) cyVar;
        cyVar.a(bbVar.a.size());
        for (Entry entry : bbVar.a.entrySet()) {
            cyVar.a((String) entry.getKey());
            ((ba) entry.getValue()).b(cyVar);
        }
        BitSet bitSet = new BitSet();
        if (bbVar.l()) {
            bitSet.set(0);
        }
        if (bbVar.o()) {
            bitSet.set(1);
        }
        cyVar.a(bitSet, 2);
        if (bbVar.l()) {
            cyVar.a(bbVar.b.size());
            for (az b : bbVar.b) {
                b.b(cyVar);
            }
        }
        if (bbVar.o()) {
            cyVar.a(bbVar.c);
        }
    }

    public void b(cy cyVar, bb bbVar) throws cf {
        int i = 0;
        cyVar = (de) cyVar;
        cv cvVar = new cv((byte) 11, (byte) 12, cyVar.w());
        bbVar.a = new HashMap(cvVar.c * 2);
        for (int i2 = 0; i2 < cvVar.c; i2++) {
            String z = cyVar.z();
            ba baVar = new ba();
            baVar.a(cyVar);
            bbVar.a.put(z, baVar);
        }
        bbVar.a(true);
        BitSet b = cyVar.b(2);
        if (b.get(0)) {
            cu cuVar = new cu((byte) 12, cyVar.w());
            bbVar.b = new ArrayList(cuVar.b);
            while (i < cuVar.b) {
                az azVar = new az();
                azVar.a(cyVar);
                bbVar.b.add(azVar);
                i++;
            }
            bbVar.b(true);
        }
        if (b.get(1)) {
            bbVar.c = cyVar.z();
            bbVar.c(true);
        }
    }
}
