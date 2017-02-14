package com.alipay.security.mobile.module.commonutils.crypto;

import java.io.OutputStream;

public class HexEncoder {
    protected final byte[] a = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102};
    protected final byte[] b = new byte[128];

    public HexEncoder() {
        a();
    }

    private void a() {
        for (int i = 0; i < this.a.length; i++) {
            this.b[this.a[i]] = (byte) i;
        }
        this.b[65] = this.b[97];
        this.b[66] = this.b[98];
        this.b[67] = this.b[99];
        this.b[68] = this.b[100];
        this.b[69] = this.b[101];
        this.b[70] = this.b[102];
    }

    private static boolean a(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    public int decode(String str, OutputStream outputStream) {
        int i = 0;
        int length = str.length();
        while (length > 0 && a(str.charAt(length - 1))) {
            length--;
        }
        int i2 = 0;
        while (i < length) {
            int i3 = i;
            while (i3 < length && a(str.charAt(i3))) {
                i3++;
            }
            i = i3 + 1;
            byte b = this.b[str.charAt(i3)];
            while (i < length && a(str.charAt(i))) {
                i++;
            }
            i3 = i + 1;
            outputStream.write(this.b[str.charAt(i)] | (b << 4));
            i2++;
            i = i3;
        }
        return i2;
    }

    public int decode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        int i3 = i + i2;
        while (i3 > i && a((char) bArr[i3 - 1])) {
            i3--;
        }
        int i4 = 0;
        int i5 = i;
        while (i5 < i3) {
            int i6 = i5;
            while (i6 < i3 && a((char) bArr[i6])) {
                i6++;
            }
            i5 = i6 + 1;
            byte b = this.b[bArr[i6]];
            while (i5 < i3 && a((char) bArr[i5])) {
                i5++;
            }
            i = i5 + 1;
            outputStream.write(this.b[bArr[i5]] | (b << 4));
            i4++;
            i5 = i;
        }
        return i4;
    }

    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) {
        for (int i3 = i; i3 < i + i2; i3++) {
            int i4 = bArr[i3] & 255;
            outputStream.write(this.a[i4 >>> 4]);
            outputStream.write(this.a[i4 & 15]);
        }
        return i2 * 2;
    }
}
