package com.meiqia.core.a.a.b;

import com.meiqia.core.a.a.c.b;
import com.meiqia.core.a.a.c.d;
import com.meiqia.core.a.a.d.e;
import com.meiqia.core.a.a.e.f;
import com.meiqia.core.a.a.e.h;
import com.meiqia.core.a.a.e.i;
import com.meiqia.core.a.a.f.c;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public abstract class a {
    public static       int                   a = 1000;
    public static       int                   b = 64;
    public static final byte[]                c = c.a("<policy-file-request/>\u0000");
    protected           com.meiqia.core.a.a.c d = null;
    protected           e                     e = null;

    public static com.meiqia.core.a.a.e.c a(ByteBuffer byteBuffer, com.meiqia.core.a.a.c cVar) {
        String b = b(byteBuffer);
        if (b == null) {
            throw new com.meiqia.core.a.a.c.a(byteBuffer.capacity() + 128);
        }
        String[] split = b.split(" ", 3);
        if (split.length != 3) {
            throw new d();
        }
        com.meiqia.core.a.a.e.c eVar;
        if (cVar == com.meiqia.core.a.a.c.CLIENT) {
            eVar = new com.meiqia.core.a.a.e.e();
            i iVar = (i) eVar;
            iVar.a(Short.parseShort(split[1]));
            iVar.a(split[2]);
        } else {
            eVar = new com.meiqia.core.a.a.e.d();
            eVar.a(split[1]);
        }
        b = b(byteBuffer);
        while (b != null && b.length() > 0) {
            String[] split2 = b.split(":", 2);
            if (split2.length != 2) {
                throw new d("not an http header");
            }
            eVar.a(split2[0], split2[1].replaceFirst("^ +", ""));
            b = b(byteBuffer);
        }
        if (b != null) {
            return eVar;
        }
        throw new com.meiqia.core.a.a.c.a();
    }

    public static ByteBuffer a(ByteBuffer byteBuffer) {
        ByteBuffer allocate = ByteBuffer.allocate(byteBuffer.remaining());
        byte b = (byte) 48;
        while (byteBuffer.hasRemaining()) {
            byte b2 = byteBuffer.get();
            allocate.put(b2);
            if (b == (byte) 13 && b2 == (byte) 10) {
                allocate.limit(allocate.position() - 2);
                allocate.position(0);
                return allocate;
            }
            b = b2;
        }
        byteBuffer.position(byteBuffer.position() - allocate.position());
        return null;
    }

    public static String b(ByteBuffer byteBuffer) {
        ByteBuffer a = a(byteBuffer);
        return a == null ? null : c.a(a.array(), 0, a.limit());
    }

    public int a(int i) {
        if (i >= 0) {
            return i;
        }
        throw new b(1002, "Negative count");
    }

    public abstract c a(com.meiqia.core.a.a.e.a aVar);

    public abstract c a(com.meiqia.core.a.a.e.a aVar, h hVar);

    public abstract com.meiqia.core.a.a.e.b a(com.meiqia.core.a.a.e.b bVar);

    public abstract com.meiqia.core.a.a.e.c a(com.meiqia.core.a.a.e.a aVar, i iVar);

    public abstract ByteBuffer a(com.meiqia.core.a.a.d.d dVar);

    public List<ByteBuffer> a(f fVar, com.meiqia.core.a.a.c cVar) {
        return a(fVar, cVar, true);
    }

    public List<ByteBuffer> a(f fVar, com.meiqia.core.a.a.c cVar, boolean z) {
        StringBuilder stringBuilder = new StringBuilder(100);
        if (fVar instanceof com.meiqia.core.a.a.e.a) {
            stringBuilder.append("GET ");
            stringBuilder.append(((com.meiqia.core.a.a.e.a) fVar).a());
            stringBuilder.append(" HTTP/1.1");
        } else if (fVar instanceof h) {
            stringBuilder.append("HTTP/1.1 101 " + ((h) fVar).a());
        } else {
            throw new RuntimeException("unknow role");
        }
        stringBuilder.append("\r\n");
        Iterator b = fVar.b();
        while (b.hasNext()) {
            String str = (String) b.next();
            String b2 = fVar.b(str);
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(b2);
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("\r\n");
        byte[] b3 = c.b(stringBuilder.toString());
        byte[] c = z ? fVar.c() : null;
        ByteBuffer allocate = ByteBuffer.allocate((c == null ? 0 : c.length) + b3.length);
        allocate.put(b3);
        if (c != null) {
            allocate.put(c);
        }
        allocate.flip();
        return Collections.singletonList(allocate);
    }

    public abstract List<com.meiqia.core.a.a.d.d> a(String str, boolean z);

    public abstract void a();

    public void a(com.meiqia.core.a.a.c cVar) {
        this.d = cVar;
    }

    protected boolean a(f fVar) {
        return fVar.b("Upgrade").equalsIgnoreCase("websocket") && fVar.b("Connection")
                .toLowerCase(Locale.ENGLISH).contains("upgrade");
    }

    public abstract b b();

    public abstract a c();

    public abstract List<com.meiqia.core.a.a.d.d> c(ByteBuffer byteBuffer);

    public f d(ByteBuffer byteBuffer) {
        return a(byteBuffer, this.d);
    }
}
