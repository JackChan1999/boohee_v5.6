package com.meiqia.core.a.a;

import com.meiqia.core.a.a.b.a;
import com.meiqia.core.a.a.b.c;
import com.meiqia.core.a.a.b.d;
import com.meiqia.core.a.a.b.f;
import com.meiqia.core.a.a.b.g;
import com.meiqia.core.a.a.b.h;
import com.meiqia.core.a.a.c.b;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.java_websocket.framing.CloseFrame;

public class e implements a {
    public static                int     a = 16384;
    public static                boolean b = false;
    public static final          List<a> c = new ArrayList(4);
    static final /* synthetic */ boolean h = (!e.class.desiredAssertionStatus());
    public       SelectionKey              d;
    public       ByteChannel               e;
    public final BlockingQueue<ByteBuffer> f;
    public final BlockingQueue<ByteBuffer> g;
    private volatile boolean i = false;
    private          b       j = b.NOT_YET_CONNECTED;
    private final f       k;
    private       List<a> l;
    private a m = null;
    private c n;
    private com.meiqia.core.a.a.d.e o = null;
    private ByteBuffer              p = ByteBuffer.allocate(0);
    private com.meiqia.core.a.a.e.a q = null;
    private String                  r = null;
    private Integer                 s = null;
    private Boolean                 t = null;
    private String                  u = null;

    static {
        c.add(new f());
        c.add(new d());
        c.add(new h());
        c.add(new g());
    }

    public e(f fVar, a aVar) {
        if (fVar == null || (aVar == null && this.n == c.SERVER)) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        this.f = new LinkedBlockingQueue();
        this.g = new LinkedBlockingQueue();
        this.k = fVar;
        this.n = c.CLIENT;
        if (aVar != null) {
            this.m = aVar.c();
        }
    }

    private void a(com.meiqia.core.a.a.e.f fVar) {
        if (b) {
            System.out.println("open using draft: " + this.m.getClass().getSimpleName());
        }
        this.j = b.OPEN;
        try {
            this.k.a(this, fVar);
        } catch (Exception e) {
            this.k.a(this, e);
        }
    }

    private void a(Collection<com.meiqia.core.a.a.d.d> collection) {
        if (b()) {
            for (com.meiqia.core.a.a.d.d a : collection) {
                a(a);
            }
            return;
        }
        throw new com.meiqia.core.a.a.c.g();
    }

    private void a(List<ByteBuffer> list) {
        for (ByteBuffer e : list) {
            e(e);
        }
    }

    private boolean b(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2;
        if (this.p.capacity() == 0) {
            byteBuffer2 = byteBuffer;
        } else {
            if (this.p.remaining() < byteBuffer.remaining()) {
                ByteBuffer allocate = ByteBuffer.allocate(this.p.capacity() + byteBuffer
                        .remaining());
                this.p.flip();
                allocate.put(this.p);
                this.p = allocate;
            }
            this.p.put(byteBuffer);
            this.p.flip();
            byteBuffer2 = this.p;
        }
        byteBuffer2.mark();
        try {
            if (this.m == null && d(byteBuffer2) == c.MATCHED) {
                try {
                    e(ByteBuffer.wrap(com.meiqia.core.a.a.f.c.a(this.k.a(this))));
                    a(-3, "");
                } catch (b e) {
                    c(CloseFrame.ABNORMAL_CLOSE, "remote peer closed connection before " +
                            "flashpolicy could be transmitted", true);
                }
                return false;
            }
            try {
                com.meiqia.core.a.a.e.f d;
                if (this.n != c.SERVER) {
                    if (this.n == c.CLIENT) {
                        this.m.a(this.n);
                        d = this.m.d(byteBuffer2);
                        if (d instanceof com.meiqia.core.a.a.e.h) {
                            d = (com.meiqia.core.a.a.e.h) d;
                            if (this.m.a(this.q, (com.meiqia.core.a.a.e.h) d) == c.MATCHED) {
                                try {
                                    this.k.a(this, this.q, d);
                                    a(d);
                                    return true;
                                } catch (b e2) {
                                    b(e2.a(), e2.getMessage(), false);
                                    return false;
                                } catch (Exception e3) {
                                    this.k.a(this, e3);
                                    b(-1, e3.getMessage(), false);
                                    return false;
                                }
                            }
                            a(1002, "draft " + this.m + " refuses handshake");
                        } else {
                            b(1002, "wrong http function", false);
                            return false;
                        }
                    }
                    return false;
                } else if (this.m == null) {
                    for (a c : this.l) {
                        a c2 = c.c();
                        try {
                            c2.a(this.n);
                            byteBuffer2.reset();
                            d = c2.d(byteBuffer2);
                            if (d instanceof com.meiqia.core.a.a.e.a) {
                                d = (com.meiqia.core.a.a.e.a) d;
                                if (c2.a((com.meiqia.core.a.a.e.a) d) == c.MATCHED) {
                                    this.u = d.a();
                                    try {
                                        a(c2.a(c2.a((com.meiqia.core.a.a.e.a) d, this.k.a(this,
                                                c2, d)), this.n));
                                        this.m = c2;
                                        a(d);
                                        return true;
                                    } catch (b e22) {
                                        b(e22.a(), e22.getMessage(), false);
                                        return false;
                                    } catch (Exception e32) {
                                        this.k.a(this, e32);
                                        b(-1, e32.getMessage(), false);
                                        return false;
                                    }
                                }
                                continue;
                            } else {
                                b(1002, "wrong http function", false);
                                return false;
                            }
                        } catch (com.meiqia.core.a.a.c.d e4) {
                        }
                    }
                    if (this.m == null) {
                        a(1002, "no draft matches");
                    }
                    return false;
                } else {
                    d = this.m.d(byteBuffer2);
                    if (d instanceof com.meiqia.core.a.a.e.a) {
                        d = (com.meiqia.core.a.a.e.a) d;
                        if (this.m.a((com.meiqia.core.a.a.e.a) d) == c.MATCHED) {
                            a(d);
                            return true;
                        }
                        a(1002, "the handshake did finaly not match");
                        return false;
                    }
                    b(1002, "wrong http function", false);
                    return false;
                }
            } catch (b e222) {
                a(e222);
            }
        } catch (com.meiqia.core.a.a.c.a e5) {
            com.meiqia.core.a.a.c.a aVar = e5;
            if (this.p.capacity() == 0) {
                byteBuffer2.reset();
                int a = aVar.a();
                if (a == 0) {
                    a = byteBuffer2.capacity() + 16;
                } else if (!h && aVar.a() < byteBuffer2.remaining()) {
                    throw new AssertionError();
                }
                this.p = ByteBuffer.allocate(a);
                this.p.put(byteBuffer);
            } else {
                this.p.position(this.p.limit());
                this.p.limit(this.p.capacity());
            }
        }
    }

    private void c(int i, String str, boolean z) {
        if (this.j != b.CLOSING && this.j != b.CLOSED) {
            if (this.j == b.OPEN) {
                if (i != CloseFrame.ABNORMAL_CLOSE) {
                    if (this.m.b() != com.meiqia.core.a.a.b.b.NONE) {
                        if (!z) {
                            try {
                                this.k.a(this, i, str);
                            } catch (Exception e) {
                                this.k.a(this, e);
                            }
                        }
                        try {
                            a(new com.meiqia.core.a.a.d.b(i, str));
                        } catch (Exception e2) {
                            this.k.a(this, e2);
                            b(CloseFrame.ABNORMAL_CLOSE, "generated frame is invalid", false);
                        }
                    }
                    b(i, str, z);
                } else if (h || !z) {
                    this.j = b.CLOSING;
                    b(i, str, false);
                    return;
                } else {
                    throw new AssertionError();
                }
            } else if (i != -3) {
                b(-1, str, false);
            } else if (h || z) {
                b(-3, str, true);
            } else {
                throw new AssertionError();
            }
            if (i == 1002) {
                b(i, str, z);
            }
            this.j = b.CLOSING;
            this.p = null;
        }
    }

    private void c(ByteBuffer byteBuffer) {
        try {
            for (com.meiqia.core.a.a.d.d dVar : this.m.c(byteBuffer)) {
                if (b) {
                    System.out.println("matched frame: " + dVar);
                }
                com.meiqia.core.a.a.d.e f = dVar.f();
                boolean d = dVar.d();
                if (f == com.meiqia.core.a.a.d.e.CLOSING) {
                    int a;
                    String b;
                    String str = "";
                    if (dVar instanceof com.meiqia.core.a.a.d.a) {
                        com.meiqia.core.a.a.d.a aVar = (com.meiqia.core.a.a.d.a) dVar;
                        a = aVar.a();
                        b = aVar.b();
                    } else {
                        b = str;
                        a = CloseFrame.NOCODE;
                    }
                    if (this.j == b.CLOSING) {
                        a(a, b, true);
                    } else if (this.m.b() == com.meiqia.core.a.a.b.b.TWOWAY) {
                        c(a, b, true);
                    } else {
                        b(a, b, false);
                    }
                } else if (f == com.meiqia.core.a.a.d.e.PING) {
                    this.k.b(this, dVar);
                } else if (f == com.meiqia.core.a.a.d.e.PONG) {
                    this.k.c(this, dVar);
                } else if (!d || f == com.meiqia.core.a.a.d.e.CONTINUOUS) {
                    if (f != com.meiqia.core.a.a.d.e.CONTINUOUS) {
                        if (this.o != null) {
                            throw new b(1002, "Previous continuous frame sequence not completed.");
                        }
                        this.o = f;
                    } else if (d) {
                        if (this.o == null) {
                            throw new b(1002, "Continuous frame sequence was not started.");
                        }
                        this.o = null;
                    } else if (this.o == null) {
                        throw new b(1002, "Continuous frame sequence was not started.");
                    }
                    try {
                        this.k.a(this, dVar);
                    } catch (Exception e) {
                        this.k.a(this, e);
                    }
                } else if (this.o != null) {
                    throw new b(1002, "Continuous frame sequence not completed.");
                } else if (f == com.meiqia.core.a.a.d.e.TEXT) {
                    try {
                        this.k.a(this, com.meiqia.core.a.a.f.c.a(dVar.c()));
                    } catch (Exception e2) {
                        this.k.a(this, e2);
                    }
                } else if (f == com.meiqia.core.a.a.d.e.BINARY) {
                    try {
                        this.k.a(this, dVar.c());
                    } catch (Exception e22) {
                        this.k.a(this, e22);
                    }
                } else {
                    throw new b(1002, "non control or continious frame expected");
                }
            }
        } catch (b e3) {
            this.k.a(this, e3);
            a(e3);
        }
    }

    private c d(ByteBuffer byteBuffer) {
        byteBuffer.mark();
        if (byteBuffer.limit() > a.c.length) {
            return c.NOT_MATCHED;
        }
        if (byteBuffer.limit() < a.c.length) {
            throw new com.meiqia.core.a.a.c.a(a.c.length);
        }
        int i = 0;
        while (byteBuffer.hasRemaining()) {
            if (a.c[i] != byteBuffer.get()) {
                byteBuffer.reset();
                return c.NOT_MATCHED;
            }
            i++;
        }
        return c.MATCHED;
    }

    private void e(ByteBuffer byteBuffer) {
        if (b) {
            System.out.println("write(" + byteBuffer.remaining() + "): {" + (byteBuffer.remaining
                    () > 1000 ? "too big to display" : new String(byteBuffer.array())) + "}");
        }
        this.f.add(byteBuffer);
        this.k.b(this);
    }

    public InetSocketAddress a() {
        return this.k.c(this);
    }

    public void a(int i) {
        c(i, "", false);
    }

    public void a(int i, String str) {
        c(i, str, false);
    }

    protected synchronized void a(int i, String str, boolean z) {
        if (this.j != b.CLOSED) {
            if (this.d != null) {
                this.d.cancel();
            }
            if (this.e != null) {
                try {
                    this.e.close();
                } catch (Exception e) {
                    this.k.a(this, e);
                }
            }
            try {
                this.k.a(this, i, str, z);
            } catch (Exception e2) {
                this.k.a(this, e2);
            }
            if (this.m != null) {
                this.m.a();
            }
            this.q = null;
            this.j = b.CLOSED;
            this.f.clear();
        }
    }

    protected void a(int i, boolean z) {
        a(i, "", z);
    }

    public void a(b bVar) {
        c(bVar.a(), bVar.getMessage(), false);
    }

    public void a(com.meiqia.core.a.a.d.d dVar) {
        if (b) {
            System.out.println("send frame: " + dVar);
        }
        e(this.m.a(dVar));
    }

    public void a(com.meiqia.core.a.a.e.b bVar) {
        if (h || this.j != b.CONNECTING) {
            this.q = this.m.a(bVar);
            this.u = bVar.a();
            if (h || this.u != null) {
                try {
                    this.k.a(this, this.q);
                    a(this.m.a(this.q, this.n));
                    return;
                } catch (b e) {
                    throw new com.meiqia.core.a.a.c.d("Handshake data rejected by client.");
                } catch (Exception e2) {
                    this.k.a(this, e2);
                    throw new com.meiqia.core.a.a.c.d("rejected because of" + e2);
                }
            }
            throw new AssertionError();
        }
        throw new AssertionError("shall only be called once");
    }

    public void a(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
        }
        a(this.m.a(str, this.n == c.CLIENT));
    }

    public void a(ByteBuffer byteBuffer) {
        if (h || byteBuffer.hasRemaining()) {
            if (b) {
                System.out.println("process(" + byteBuffer.remaining() + "): {" + (byteBuffer
                        .remaining() > 1000 ? "too big to display" : new String(byteBuffer.array
                        (), byteBuffer.position(), byteBuffer.remaining())) + "}");
            }
            if (this.j != b.NOT_YET_CONNECTED) {
                c(byteBuffer);
            } else if (b(byteBuffer)) {
                if (!h && this.p.hasRemaining() == byteBuffer.hasRemaining() && byteBuffer
                        .hasRemaining()) {
                    throw new AssertionError();
                } else if (byteBuffer.hasRemaining()) {
                    c(byteBuffer);
                } else if (this.p.hasRemaining()) {
                    c(this.p);
                }
            }
            if (!h && !d() && !e() && byteBuffer.hasRemaining()) {
                throw new AssertionError();
            }
            return;
        }
        throw new AssertionError();
    }

    public void b(int i, String str) {
        a(i, str, false);
    }

    protected synchronized void b(int i, String str, boolean z) {
        if (!this.i) {
            this.s = Integer.valueOf(i);
            this.r = str;
            this.t = Boolean.valueOf(z);
            this.i = true;
            this.k.b(this);
            try {
                this.k.b(this, i, str, z);
            } catch (Exception e) {
                this.k.a(this, e);
            }
            if (this.m != null) {
                this.m.a();
            }
            this.q = null;
        }
    }

    public boolean b() {
        if (h || this.j != b.OPEN || !this.i) {
            return this.j == b.OPEN;
        } else {
            throw new AssertionError();
        }
    }

    public void c() {
        if (g() == b.NOT_YET_CONNECTED) {
            a(-1, true);
        } else if (this.i) {
            a(this.s.intValue(), this.r, this.t.booleanValue());
        } else if (this.m.b() == com.meiqia.core.a.a.b.b.NONE) {
            a(1000, true);
        } else if (this.m.b() != com.meiqia.core.a.a.b.b.ONEWAY) {
            a((int) CloseFrame.ABNORMAL_CLOSE, true);
        } else if (this.n == c.SERVER) {
            a((int) CloseFrame.ABNORMAL_CLOSE, true);
        } else {
            a(1000, true);
        }
    }

    public boolean d() {
        return this.j == b.CLOSING;
    }

    public boolean e() {
        return this.i;
    }

    public boolean f() {
        return this.j == b.CLOSED;
    }

    public b g() {
        return this.j;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }
}
