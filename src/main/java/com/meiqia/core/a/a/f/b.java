package com.meiqia.core.a.a.f;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class b extends FilterOutputStream {
    private boolean a;
    private int     b;
    private byte[]  c;
    private int     d;
    private int     e;
    private boolean f;
    private byte[]  g;
    private boolean h;
    private int     i;
    private byte[]  j;

    public b(OutputStream outputStream, int i) {
        boolean z = true;
        super(outputStream);
        this.f = (i & 8) != 0;
        if ((i & 1) == 0) {
            z = false;
        }
        this.a = z;
        this.d = this.a ? 3 : 4;
        this.c = new byte[this.d];
        this.b = 0;
        this.e = 0;
        this.h = false;
        this.g = new byte[4];
        this.i = i;
        this.j = a.c(i);
    }

    public void a() {
        if (this.b <= 0) {
            return;
        }
        if (this.a) {
            this.out.write(a.b(this.g, this.c, this.b, this.i));
            this.b = 0;
            return;
        }
        throw new IOException("Base64 input not properly padded.");
    }

    public void close() {
        a();
        super.close();
        this.c = null;
        this.out = null;
    }

    public void write(int i) {
        if (this.h) {
            this.out.write(i);
        } else if (this.a) {
            r0 = this.c;
            r1 = this.b;
            this.b = r1 + 1;
            r0[r1] = (byte) i;
            if (this.b >= this.d) {
                this.out.write(a.b(this.g, this.c, this.d, this.i));
                this.e += 4;
                if (this.f && this.e >= 76) {
                    this.out.write(10);
                    this.e = 0;
                }
                this.b = 0;
            }
        } else if (this.j[i & 127] > (byte) -5) {
            r0 = this.c;
            r1 = this.b;
            this.b = r1 + 1;
            r0[r1] = (byte) i;
            if (this.b >= this.d) {
                this.out.write(this.g, 0, a.b(this.c, 0, this.g, 0, this.i));
                this.b = 0;
            }
        } else if (this.j[i & 127] != (byte) -5) {
            throw new IOException("Invalid character in Base64 data.");
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        if (this.h) {
            this.out.write(bArr, i, i2);
            return;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            write(bArr[i + i3]);
        }
    }
}
