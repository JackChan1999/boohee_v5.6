package com.zxinsight.common.http;

import java.util.Iterator;

class i implements Runnable {
    final /* synthetic */ g a;

    i(g gVar) {
        this.a = gVar;
    }

    public void run() {
        for (j jVar : this.a.e.values()) {
            Iterator it = jVar.e.iterator();
            while (it.hasNext()) {
                l lVar = (l) it.next();
                if (lVar.c != null) {
                    if (jVar.a() == null) {
                        lVar.b = jVar.c;
                        lVar.c.a(lVar, false);
                    } else {
                        lVar.c.a(jVar.a());
                    }
                }
            }
        }
        this.a.e.clear();
        this.a.g = null;
    }
}
