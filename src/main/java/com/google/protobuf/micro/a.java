package com.google.protobuf.micro;

import java.io.InputStream;
import java.util.Vector;

public final class a {
    private final byte[]      a;
    private       int         b;
    private       int         c;
    private       int         d;
    private final InputStream e;
    private       int         f;
    private       int         g;
    private       int         h;
    private       int         i;
    private       int         j;

    private a(InputStream inputStream) {
        this.h = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.i = 64;
        this.j = 67108864;
        this.a = new byte[4096];
        this.b = 0;
        this.d = 0;
        this.e = inputStream;
    }

    private a(byte[] bArr, int i, int i2) {
        this.h = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.i = 64;
        this.j = 67108864;
        this.a = bArr;
        this.b = i + i2;
        this.d = i;
        this.e = null;
    }

    public static a a(InputStream inputStream) {
        return new a(inputStream);
    }

    public static a a(byte[] bArr, int i, int i2) {
        return new a(bArr, i, i2);
    }

    private boolean a(boolean z) {
        if (this.d < this.b) {
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        } else if (this.g + this.b != this.h) {
            this.g += this.b;
            this.d = 0;
            this.b = this.e == null ? -1 : this.e.read(this.a);
            if (this.b == 0 || this.b < -1) {
                throw new IllegalStateException("InputStream#read(byte[]) returned invalid " +
                        "result: " + this.b + "\nThe InputStream implementation is buggy.");
            } else if (this.b == -1) {
                this.b = 0;
                if (!z) {
                    return false;
                }
                throw c.a();
            } else {
                l();
                int i = (this.g + this.b) + this.c;
                if (i <= this.j && i >= 0) {
                    return true;
                }
                throw c.g();
            }
        } else if (!z) {
            return false;
        } else {
            throw c.a();
        }
    }

    private void l() {
        this.b += this.c;
        int i = this.g + this.b;
        if (i > this.h) {
            this.c = i - this.h;
            this.b -= this.c;
            return;
        }
        this.c = 0;
    }

    public int a() {
        if (j()) {
            this.f = 0;
            return 0;
        }
        this.f = g();
        if (this.f != 0) {
            return this.f;
        }
        throw c.d();
    }

    public void a(int i) {
        if (this.f != i) {
            throw c.e();
        }
    }

    public void b() {
        int a;
        do {
            a = a();
            if (a == 0) {
                return;
            }
        } while (b(a));
    }

    public boolean b(int i) {
        switch (e.a(i)) {
            case 0:
                c();
                return true;
            case 1:
                i();
                return true;
            case 2:
                d(g());
                return true;
            case 3:
                b();
                a(e.a(e.b(i), 4));
                return true;
            case 4:
                return false;
            case 5:
                h();
                return true;
            default:
                throw c.f();
        }
    }

    public int c() {
        return g();
    }

    public byte[] c(int i) {
        if (i < 0) {
            throw c.b();
        } else if ((this.g + this.d) + i > this.h) {
            d((this.h - this.g) - this.d);
            throw c.a();
        } else if (i <= this.b - this.d) {
            Object obj = new byte[i];
            System.arraycopy(this.a, this.d, obj, 0, i);
            this.d += i;
            return obj;
        } else if (i < 4096) {
            Object obj2 = new byte[i];
            r0 = this.b - this.d;
            System.arraycopy(this.a, this.d, obj2, 0, r0);
            this.d = this.b;
            a(true);
            while (i - r0 > this.b) {
                System.arraycopy(this.a, 0, obj2, r0, this.b);
                r0 += this.b;
                this.d = this.b;
                a(true);
            }
            System.arraycopy(this.a, 0, obj2, r0, i - r0);
            this.d = i - r0;
            return obj2;
        } else {
            int read;
            int i2 = this.d;
            int i3 = this.b;
            this.g += this.b;
            this.d = 0;
            this.b = 0;
            r0 = i - (i3 - i2);
            Vector vector = new Vector();
            int i4 = r0;
            while (i4 > 0) {
                Object obj3 = new byte[Math.min(i4, 4096)];
                r0 = 0;
                while (r0 < obj3.length) {
                    read = this.e == null ? -1 : this.e.read(obj3, r0, obj3.length - r0);
                    if (read == -1) {
                        throw c.a();
                    }
                    this.g += read;
                    r0 += read;
                }
                r0 = i4 - obj3.length;
                vector.addElement(obj3);
                i4 = r0;
            }
            Object obj4 = new byte[i];
            r0 = i3 - i2;
            System.arraycopy(this.a, i2, obj4, 0, r0);
            int i5 = r0;
            for (read = 0; read < vector.size(); read++) {
                byte[] bArr = (byte[]) vector.elementAt(read);
                System.arraycopy(bArr, 0, obj4, i5, bArr.length);
                i5 += bArr.length;
            }
            return obj4;
        }
    }

    public void d(int i) {
        if (i < 0) {
            throw c.b();
        } else if ((this.g + this.d) + i > this.h) {
            d((this.h - this.g) - this.d);
            throw c.a();
        } else if (i <= this.b - this.d) {
            this.d += i;
        } else {
            int i2 = this.b - this.d;
            this.g += this.b;
            this.d = 0;
            this.b = 0;
            int i3 = i2;
            while (i3 < i) {
                i2 = this.e == null ? -1 : (int) this.e.skip((long) (i - i3));
                if (i2 <= 0) {
                    throw c.a();
                }
                i3 += i2;
                this.g = i2 + this.g;
            }
        }
    }

    public boolean d() {
        return g() != 0;
    }

    public String e() {
        int g = g();
        if (g > this.b - this.d || g <= 0) {
            return new String(c(g), "UTF-8");
        }
        String str = new String(this.a, this.d, g, "UTF-8");
        this.d = g + this.d;
        return str;
    }

    public int f() {
        return g();
    }

    public int g() {
        byte k = k();
        if (k >= (byte) 0) {
            return k;
        }
        int i = k & 127;
        byte k2 = k();
        if (k2 >= (byte) 0) {
            return i | (k2 << 7);
        }
        i |= (k2 & 127) << 7;
        k2 = k();
        if (k2 >= (byte) 0) {
            return i | (k2 << 14);
        }
        i |= (k2 & 127) << 14;
        k2 = k();
        if (k2 >= (byte) 0) {
            return i | (k2 << 21);
        }
        i |= (k2 & 127) << 21;
        k2 = k();
        i |= k2 << 28;
        if (k2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (k() >= (byte) 0) {
                return i;
            }
        }
        throw c.c();
    }

    public int h() {
        return (((k() & 255) | ((k() & 255) << 8)) | ((k() & 255) << 16)) | ((k() & 255) << 24);
    }

    public long i() {
        byte k = k();
        byte k2 = k();
        return ((((((((((long) k2) & 255) << 8) | (((long) k) & 255)) | ((((long) k()) & 255) <<
                16)) | ((((long) k()) & 255) << 24)) | ((((long) k()) & 255) << 32)) | ((((long)
                k()) & 255) << 40)) | ((((long) k()) & 255) << 48)) | ((((long) k()) & 255) << 56);
    }

    public boolean j() {
        return this.d == this.b && !a(false);
    }

    public byte k() {
        if (this.d == this.b) {
            a(true);
        }
        byte[] bArr = this.a;
        int i = this.d;
        this.d = i + 1;
        return bArr[i];
    }
}
