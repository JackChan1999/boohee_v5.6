package org.apache.thrift.protocol;

import com.umeng.socialize.common.SocializeConstants;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.apache.thrift.f;
import org.apache.thrift.transport.d;
import u.aly.df;

public final class b extends f {
    private static final k d = new k("");
    private static final c f = new c("", (byte) 0, (short) 0);
    private static final byte[] g = new byte[16];
    byte[] a = new byte[5];
    byte[] b = new byte[10];
    byte[] c = new byte[1];
    private org.apache.thrift.a h = new org.apache.thrift.a(15);
    private short i = (short) 0;
    private c j = null;
    private Boolean k = null;
    private byte[] l = new byte[1];

    public static class a implements h {
        public f a(d dVar) {
            return new b(dVar);
        }
    }

    static {
        g[0] = (byte) 0;
        g[2] = (byte) 1;
        g[3] = (byte) 3;
        g[6] = (byte) 4;
        g[8] = (byte) 5;
        g[10] = (byte) 6;
        g[4] = (byte) 7;
        g[11] = (byte) 8;
        g[15] = (byte) 9;
        g[14] = (byte) 10;
        g[13] = (byte) 11;
        g[12] = (byte) 12;
    }

    public b(d dVar) {
        super(dVar);
    }

    private long A() {
        long j = null;
        long j2 = 0;
        if (this.e.c() >= 10) {
            int i;
            byte[] a = this.e.a();
            int b = this.e.b();
            long j3 = 0;
            while (true) {
                byte b2 = a[b + i];
                j2 |= ((long) (b2 & 127)) << j3;
                if ((b2 & 128) != 128) {
                    break;
                }
                j3 += 7;
                i++;
            }
            this.e.a(i + 1);
        } else {
            while (true) {
                byte r = r();
                j2 |= ((long) (r & 127)) << j;
                if ((r & 128) != 128) {
                    break;
                }
                j += 7;
            }
        }
        return j2;
    }

    private long a(byte[] bArr) {
        return ((((((((((long) bArr[7]) & 255) << 56) | ((((long) bArr[6]) & 255) << 48)) | ((((long) bArr[5]) & 255) << 40)) | ((((long) bArr[4]) & 255) << 32)) | ((((long) bArr[3]) & 255) << 24)) | ((((long) bArr[2]) & 255) << 16)) | ((((long) bArr[1]) & 255) << 8)) | (((long) bArr[0]) & 255);
    }

    private void a(c cVar, byte b) {
        if (b == (byte) -1) {
            b = e(cVar.b);
        }
        if (cVar.c <= this.i || cVar.c - this.i > 15) {
            b(b);
            a(cVar.c);
        } else {
            d(((cVar.c - this.i) << 4) | b);
        }
        this.i = cVar.c;
    }

    private void a(byte[] bArr, int i, int i2) {
        b(i2);
        this.e.b(bArr, i, i2);
    }

    private void b(byte b) {
        this.l[0] = b;
        this.e.b(this.l);
    }

    private void b(int i) {
        int i2 = 0;
        while ((i & -128) != 0) {
            int i3 = i2 + 1;
            this.a[i2] = (byte) ((i & 127) | 128);
            i >>>= 7;
            i2 = i3;
        }
        int i4 = i2 + 1;
        this.a[i2] = (byte) i;
        this.e.b(this.a, 0, i4);
    }

    private void b(long j) {
        int i = 0;
        while ((-128 & j) != 0) {
            int i2 = i + 1;
            this.b[i] = (byte) ((int) ((127 & j) | 128));
            j >>>= 7;
            i = i2;
        }
        int i3 = i + 1;
        this.b[i] = (byte) ((int) j);
        this.e.b(this.b, 0, i3);
    }

    private int c(int i) {
        return (i << 1) ^ (i >> 31);
    }

    private long c(long j) {
        return (j << 1) ^ (j >> 63);
    }

    private boolean c(byte b) {
        int i = b & 15;
        return i == 1 || i == 2;
    }

    private byte d(byte b) {
        switch ((byte) (b & 15)) {
            case (byte) 0:
                return (byte) 0;
            case (byte) 1:
            case (byte) 2:
                return (byte) 2;
            case (byte) 3:
                return (byte) 3;
            case (byte) 4:
                return (byte) 6;
            case (byte) 5:
                return (byte) 8;
            case (byte) 6:
                return (byte) 10;
            case (byte) 7:
                return (byte) 4;
            case (byte) 8:
                return (byte) 11;
            case (byte) 9:
                return df.m;
            case (byte) 10:
                return df.l;
            case (byte) 11:
                return (byte) 13;
            case (byte) 12:
                return (byte) 12;
            default:
                throw new g("don't know what type: " + ((byte) (b & 15)));
        }
    }

    private long d(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    private void d(int i) {
        b((byte) i);
    }

    private byte e(byte b) {
        return g[b];
    }

    private byte[] e(int i) {
        if (i == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i];
        this.e.d(bArr, 0, i);
        return bArr;
    }

    private int f(int i) {
        return (i >>> 1) ^ (-(i & 1));
    }

    private int z() {
        int i = 0;
        int i2;
        if (this.e.c() >= 5) {
            byte[] a = this.e.a();
            int b = this.e.b();
            i2 = 0;
            int i3 = 0;
            while (true) {
                byte b2 = a[b + i];
                i3 |= (b2 & 127) << i2;
                if ((b2 & 128) != 128) {
                    this.e.a(i + 1);
                    return i3;
                }
                i2 += 7;
                i++;
            }
        } else {
            i2 = 0;
            while (true) {
                byte r = r();
                i2 |= (r & 127) << i;
                if ((r & 128) != 128) {
                    return i2;
                }
                i += 7;
            }
        }
    }

    public void a() {
        this.i = this.h.a();
    }

    public void a(byte b) {
        b(b);
    }

    protected void a(byte b, int i) {
        if (i <= 14) {
            d((i << 4) | e(b));
            return;
        }
        d(e(b) | SocializeConstants.MASK_USER_CENTER_HIDE_AREA);
        b(i);
    }

    public void a(int i) {
        b(c(i));
    }

    public void a(long j) {
        b(c(j));
    }

    public void a(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            a(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new f("UTF-8 not supported!");
        }
    }

    public void a(ByteBuffer byteBuffer) {
        a(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), (byteBuffer.limit() - byteBuffer.position()) - byteBuffer.arrayOffset());
    }

    public void a(c cVar) {
        if (cVar.b == (byte) 2) {
            this.j = cVar;
        } else {
            a(cVar, (byte) -1);
        }
    }

    public void a(d dVar) {
        a(dVar.a, dVar.b);
    }

    public void a(e eVar) {
        if (eVar.c == 0) {
            d(0);
            return;
        }
        b(eVar.c);
        d((e(eVar.a) << 4) | e(eVar.b));
    }

    public void a(j jVar) {
        a(jVar.a, jVar.b);
    }

    public void a(k kVar) {
        this.h.a(this.i);
        this.i = (short) 0;
    }

    public void a(short s) {
        b(c((int) s));
    }

    public void a(boolean z) {
        byte b = (byte) 1;
        if (this.j != null) {
            c cVar = this.j;
            if (!z) {
                b = (byte) 2;
            }
            a(cVar, b);
            this.j = null;
            return;
        }
        if (!z) {
            b = (byte) 2;
        }
        b(b);
    }

    public void b() {
    }

    public void c() {
        b((byte) 0);
    }

    public void d() {
    }

    public void e() {
    }

    public void f() {
    }

    public k g() {
        this.h.a(this.i);
        this.i = (short) 0;
        return d;
    }

    public void h() {
        this.i = this.h.a();
    }

    public c i() {
        byte r = r();
        if (r == (byte) 0) {
            return f;
        }
        short s = (short) ((r & SocializeConstants.MASK_USER_CENTER_HIDE_AREA) >> 4);
        c cVar = new c("", d((byte) (r & 15)), s == (short) 0 ? s() : (short) (s + this.i));
        if (c(r)) {
            this.k = ((byte) (r & 15)) == (byte) 1 ? Boolean.TRUE : Boolean.FALSE;
        }
        this.i = cVar.c;
        return cVar;
    }

    public void j() {
    }

    public e k() {
        int z = z();
        int r = z == 0 ? 0 : r();
        return new e(d((byte) (r >> 4)), d((byte) (r & 15)), z);
    }

    public void l() {
    }

    public d m() {
        byte r = r();
        int i = (r >> 4) & 15;
        if (i == 15) {
            i = z();
        }
        return new d(d(r), i);
    }

    public void n() {
    }

    public j o() {
        return new j(m());
    }

    public void p() {
    }

    public boolean q() {
        if (this.k == null) {
            return r() == (byte) 1;
        } else {
            boolean booleanValue = this.k.booleanValue();
            this.k = null;
            return booleanValue;
        }
    }

    public byte r() {
        if (this.e.c() > 0) {
            byte b = this.e.a()[this.e.b()];
            this.e.a(1);
            return b;
        }
        this.e.d(this.c, 0, 1);
        return this.c[0];
    }

    public short s() {
        return (short) f(z());
    }

    public int t() {
        return f(z());
    }

    public long u() {
        return d(A());
    }

    public double v() {
        byte[] bArr = new byte[8];
        this.e.d(bArr, 0, 8);
        return Double.longBitsToDouble(a(bArr));
    }

    public String w() {
        int z = z();
        if (z == 0) {
            return "";
        }
        try {
            if (this.e.c() < z) {
                return new String(e(z), "UTF-8");
            }
            String str = new String(this.e.a(), this.e.b(), z, "UTF-8");
            this.e.a(z);
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new f("UTF-8 not supported!");
        }
    }

    public ByteBuffer x() {
        int z = z();
        if (z == 0) {
            return ByteBuffer.wrap(new byte[0]);
        }
        byte[] bArr = new byte[z];
        this.e.d(bArr, 0, z);
        return ByteBuffer.wrap(bArr);
    }

    public void y() {
        this.h.b();
        this.i = (short) 0;
    }
}
