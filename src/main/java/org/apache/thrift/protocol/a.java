package org.apache.thrift.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.apache.thrift.f;
import org.apache.thrift.transport.d;

public class a extends f {
    private static final k f = new k();
    protected boolean a = false;
    protected boolean b = true;
    protected int c;
    protected boolean d = false;
    private byte[] g = new byte[1];
    private byte[] h = new byte[2];
    private byte[] i = new byte[4];
    private byte[] j = new byte[8];
    private byte[] k = new byte[1];
    private byte[] l = new byte[2];
    private byte[] m = new byte[4];
    private byte[] n = new byte[8];

    public static class a implements h {
        protected boolean a;
        protected boolean b;
        protected int c;

        public a() {
            this(false, true);
        }

        public a(boolean z, boolean z2) {
            this(z, z2, 0);
        }

        public a(boolean z, boolean z2, int i) {
            this.a = false;
            this.b = true;
            this.a = z;
            this.b = z2;
            this.c = i;
        }

        public f a(d dVar) {
            f aVar = new a(dVar, this.a, this.b);
            if (this.c != 0) {
                aVar.c(this.c);
            }
            return aVar;
        }
    }

    public a(d dVar, boolean z, boolean z2) {
        super(dVar);
        this.a = z;
        this.b = z2;
    }

    private int a(byte[] bArr, int i, int i2) {
        d(i2);
        return this.e.d(bArr, i, i2);
    }

    public void a() {
    }

    public void a(byte b) {
        this.g[0] = b;
        this.e.b(this.g, 0, 1);
    }

    public void a(int i) {
        this.i[0] = (byte) ((i >> 24) & 255);
        this.i[1] = (byte) ((i >> 16) & 255);
        this.i[2] = (byte) ((i >> 8) & 255);
        this.i[3] = (byte) (i & 255);
        this.e.b(this.i, 0, 4);
    }

    public void a(long j) {
        this.j[0] = (byte) ((int) ((j >> 56) & 255));
        this.j[1] = (byte) ((int) ((j >> 48) & 255));
        this.j[2] = (byte) ((int) ((j >> 40) & 255));
        this.j[3] = (byte) ((int) ((j >> 32) & 255));
        this.j[4] = (byte) ((int) ((j >> 24) & 255));
        this.j[5] = (byte) ((int) ((j >> 16) & 255));
        this.j[6] = (byte) ((int) ((j >> 8) & 255));
        this.j[7] = (byte) ((int) (255 & j));
        this.e.b(this.j, 0, 8);
    }

    public void a(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            a(bytes.length);
            this.e.b(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new f("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public void a(ByteBuffer byteBuffer) {
        int limit = (byteBuffer.limit() - byteBuffer.position()) - byteBuffer.arrayOffset();
        a(limit);
        this.e.b(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), limit);
    }

    public void a(c cVar) {
        a(cVar.b);
        a(cVar.c);
    }

    public void a(d dVar) {
        a(dVar.a);
        a(dVar.b);
    }

    public void a(e eVar) {
        a(eVar.a);
        a(eVar.b);
        a(eVar.c);
    }

    public void a(j jVar) {
        a(jVar.a);
        a(jVar.b);
    }

    public void a(k kVar) {
    }

    public void a(short s) {
        this.h[0] = (byte) ((s >> 8) & 255);
        this.h[1] = (byte) (s & 255);
        this.e.b(this.h, 0, 2);
    }

    public void a(boolean z) {
        a(z ? (byte) 1 : (byte) 0);
    }

    public String b(int i) {
        try {
            d(i);
            byte[] bArr = new byte[i];
            this.e.d(bArr, 0, i);
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new f("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public void b() {
    }

    public void c() {
        a((byte) 0);
    }

    public void c(int i) {
        this.c = i;
        this.d = true;
    }

    public void d() {
    }

    protected void d(int i) {
        if (i < 0) {
            throw new f("Negative length: " + i);
        } else if (this.d) {
            this.c -= i;
            if (this.c < 0) {
                throw new f("Message length exceeded: " + i);
            }
        }
    }

    public void e() {
    }

    public void f() {
    }

    public k g() {
        return f;
    }

    public void h() {
    }

    public c i() {
        byte r = r();
        return new c("", r, r == (byte) 0 ? (short) 0 : s());
    }

    public void j() {
    }

    public e k() {
        return new e(r(), r(), t());
    }

    public void l() {
    }

    public d m() {
        return new d(r(), t());
    }

    public void n() {
    }

    public j o() {
        return new j(r(), t());
    }

    public void p() {
    }

    public boolean q() {
        return r() == (byte) 1;
    }

    public byte r() {
        if (this.e.c() >= 1) {
            byte b = this.e.a()[this.e.b()];
            this.e.a(1);
            return b;
        }
        a(this.k, 0, 1);
        return this.k[0];
    }

    public short s() {
        int i = 0;
        byte[] bArr = this.l;
        if (this.e.c() >= 2) {
            bArr = this.e.a();
            i = this.e.b();
            this.e.a(2);
        } else {
            a(this.l, 0, 2);
        }
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    public int t() {
        int i = 0;
        byte[] bArr = this.m;
        if (this.e.c() >= 4) {
            bArr = this.e.a();
            i = this.e.b();
            this.e.a(4);
        } else {
            a(this.m, 0, 4);
        }
        return (bArr[i + 3] & 255) | ((((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8));
    }

    public long u() {
        int i = 0;
        byte[] bArr = this.n;
        if (this.e.c() >= 8) {
            bArr = this.e.a();
            i = this.e.b();
            this.e.a(8);
        } else {
            a(this.n, 0, 8);
        }
        return ((long) (bArr[i + 7] & 255)) | (((((((((long) (bArr[i] & 255)) << 56) | (((long) (bArr[i + 1] & 255)) << 48)) | (((long) (bArr[i + 2] & 255)) << 40)) | (((long) (bArr[i + 3] & 255)) << 32)) | (((long) (bArr[i + 4] & 255)) << 24)) | (((long) (bArr[i + 5] & 255)) << 16)) | (((long) (bArr[i + 6] & 255)) << 8));
    }

    public double v() {
        return Double.longBitsToDouble(u());
    }

    public String w() {
        int t = t();
        if (this.e.c() < t) {
            return b(t);
        }
        try {
            String str = new String(this.e.a(), this.e.b(), t, "UTF-8");
            this.e.a(t);
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new f("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    public ByteBuffer x() {
        int t = t();
        d(t);
        if (this.e.c() >= t) {
            ByteBuffer wrap = ByteBuffer.wrap(this.e.a(), this.e.b(), t);
            this.e.a(t);
            return wrap;
        }
        byte[] bArr = new byte[t];
        this.e.d(bArr, 0, t);
        return ByteBuffer.wrap(bArr);
    }
}
