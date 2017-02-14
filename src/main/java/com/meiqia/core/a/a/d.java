package com.meiqia.core.a.a;

import com.meiqia.core.a.a.b.a;
import com.meiqia.core.a.a.d.f;
import com.meiqia.core.a.a.e.e;
import com.meiqia.core.a.a.e.h;
import com.meiqia.core.a.a.e.i;

import java.net.InetSocketAddress;

public abstract class d implements f {
    public i a(a aVar, a aVar2, com.meiqia.core.a.a.e.a aVar3) {
        return new e();
    }

    public String a(a aVar) {
        InetSocketAddress a = aVar.a();
        if (a == null) {
            throw new com.meiqia.core.a.a.c.d("socket not bound");
        }
        StringBuffer stringBuffer = new StringBuffer(90);
        stringBuffer.append("<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"");
        stringBuffer.append(a.getPort());
        stringBuffer.append("\" /></cross-domain-policy>\u0000");
        return stringBuffer.toString();
    }

    public void a(a aVar, com.meiqia.core.a.a.d.d dVar) {
    }

    public void a(a aVar, com.meiqia.core.a.a.e.a aVar2) {
    }

    public void a(a aVar, com.meiqia.core.a.a.e.a aVar2, h hVar) {
    }

    public void b(a aVar, com.meiqia.core.a.a.d.d dVar) {
        com.meiqia.core.a.a.d.d fVar = new f(dVar);
        fVar.a(com.meiqia.core.a.a.d.e.e);
        aVar.a(fVar);
    }

    public void c(a aVar, com.meiqia.core.a.a.d.d dVar) {
    }
}
