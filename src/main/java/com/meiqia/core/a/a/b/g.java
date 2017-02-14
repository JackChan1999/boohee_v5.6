package com.meiqia.core.a.a.b;

import com.meiqia.core.a.a.d.d;
import com.meiqia.core.a.a.d.e;
import com.meiqia.core.a.a.e.a;
import com.meiqia.core.a.a.e.b;
import com.meiqia.core.a.a.e.c;
import com.meiqia.core.a.a.e.f;
import com.meiqia.core.a.a.e.h;
import com.meiqia.core.a.a.e.i;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class g extends a {
    protected boolean f = false;
    protected List<d> g = new LinkedList();
    protected ByteBuffer h;
    private final Random i = new Random();

    public c a(a aVar) {
        return (aVar.c("Origin") && a((f) aVar)) ? c.MATCHED : c.NOT_MATCHED;
    }

    public c a(a aVar, h hVar) {
        return (aVar.b("WebSocket-Origin").equals(hVar.b("Origin")) && a((f) hVar)) ? c.MATCHED :
                c.NOT_MATCHED;
    }

    public b a(b bVar) {
        bVar.a("Upgrade", "WebSocket");
        bVar.a("Connection", "Upgrade");
        if (!bVar.c("Origin")) {
            bVar.a("Origin", "random" + this.i.nextInt());
        }
        return bVar;
    }

    public c a(a aVar, i iVar) {
        iVar.a("Web Socket Protocol Handshake");
        iVar.a("Upgrade", "WebSocket");
        iVar.a("Connection", aVar.b("Connection"));
        iVar.a("WebSocket-Origin", aVar.b("Origin"));
        iVar.a("WebSocket-Location", "ws://" + aVar.b("Host") + aVar.a());
        return iVar;
    }

    public ByteBuffer a(d dVar) {
        if (dVar.f() != e.TEXT) {
            throw new RuntimeException("only text frames supported");
        }
        ByteBuffer c = dVar.c();
        ByteBuffer allocate = ByteBuffer.allocate(c.remaining() + 2);
        allocate.put((byte) 0);
        c.mark();
        allocate.put(c);
        c.reset();
        allocate.put((byte) -1);
        allocate.flip();
        return allocate;
    }

    public List<d> a(String str, boolean z) {
        com.meiqia.core.a.a.d.c fVar = new com.meiqia.core.a.a.d.f();
        try {
            fVar.a(ByteBuffer.wrap(com.meiqia.core.a.a.f.c.a(str)));
            fVar.a(true);
            fVar.a(e.TEXT);
            fVar.b(z);
            return Collections.singletonList(fVar);
        } catch (Throwable e) {
            throw new com.meiqia.core.a.a.c.f(e);
        }
    }

    public void a() {
        this.f = false;
        this.h = null;
    }

    public b b() {
        return b.NONE;
    }

    public a c() {
        return new g();
    }

    public List<d> c(ByteBuffer byteBuffer) {
        List<d> e = e(byteBuffer);
        if (e != null) {
            return e;
        }
        throw new com.meiqia.core.a.a.c.b(1002);
    }

    public ByteBuffer d() {
        return ByteBuffer.allocate(b);
    }

    protected List<d> e(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            if (b == (byte) 0) {
                if (this.f) {
                    throw new com.meiqia.core.a.a.c.c("unexpected START_OF_FRAME");
                }
                this.f = true;
            } else if (b == (byte) -1) {
                if (this.f) {
                    if (this.h != null) {
                        this.h.flip();
                        com.meiqia.core.a.a.d.f fVar = new com.meiqia.core.a.a.d.f();
                        fVar.a(this.h);
                        fVar.a(true);
                        fVar.a(e.TEXT);
                        this.g.add(fVar);
                        this.h = null;
                        byteBuffer.mark();
                    }
                    this.f = false;
                } else {
                    throw new com.meiqia.core.a.a.c.c("unexpected END_OF_FRAME");
                }
            } else if (!this.f) {
                return null;
            } else {
                if (this.h == null) {
                    this.h = d();
                } else if (!this.h.hasRemaining()) {
                    this.h = f(this.h);
                }
                this.h.put(b);
            }
        }
        List<d> list = this.g;
        this.g = new LinkedList();
        return list;
    }

    public ByteBuffer f(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        ByteBuffer allocate = ByteBuffer.allocate(a(byteBuffer.capacity() * 2));
        allocate.put(byteBuffer);
        return allocate;
    }
}
