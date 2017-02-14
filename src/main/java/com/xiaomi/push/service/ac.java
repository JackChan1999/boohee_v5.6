package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.string.a;

public class ac {
    private static int    a = 8;
    private        byte[] b = new byte[256];
    private        int    c = 0;
    private        int    d = 0;
    private        int    e = -666;

    public static int a(byte b) {
        return b >= (byte) 0 ? b : b + 256;
    }

    public static String a(byte[] bArr, String str) {
        return String.valueOf(a.a(a(bArr, str.getBytes())));
    }

    private void a(int i, byte[] bArr, boolean z) {
        int i2 = 0;
        int length = bArr.length;
        for (int i3 = 0; i3 < 256; i3++) {
            this.b[i3] = (byte) i3;
        }
        this.d = 0;
        this.c = 0;
        while (this.c < i) {
            this.d = ((this.d + a(this.b[this.c])) + a(bArr[this.c % length])) % 256;
            a(this.b, this.c, this.d);
            this.c++;
        }
        if (i != 256) {
            this.e = ((this.d + a(this.b[i])) + a(bArr[i % length])) % 256;
        }
        if (z) {
            System.out.print("S_" + (i - 1) + ":");
            while (i2 <= i) {
                System.out.print(" " + a(this.b[i2]));
                i2++;
            }
            System.out.print("   j_" + (i - 1) + "=" + this.d);
            System.out.print("   j_" + i + "=" + this.e);
            System.out.print("   S_" + (i - 1) + "[j_" + (i - 1) + "]=" + a(this.b[this.d]));
            System.out.print("   S_" + (i - 1) + "[j_" + i + "]=" + a(this.b[this.e]));
            if (this.b[1] != (byte) 0) {
                System.out.print("   S[1]!=0");
            }
            System.out.println();
        }
    }

    private void a(byte[] bArr) {
        a(256, bArr, false);
    }

    private static void a(byte[] bArr, int i, int i2) {
        byte b = bArr[i];
        bArr[i] = bArr[i2];
        bArr[i2] = b;
    }

    public static byte[] a(String str, String str2) {
        int i = 0;
        byte[] a = a.a(str);
        byte[] bytes = str2.getBytes();
        byte[] bArr = new byte[((a.length + 1) + bytes.length)];
        for (int i2 = 0; i2 < a.length; i2++) {
            bArr[i2] = a[i2];
        }
        bArr[a.length] = (byte) 95;
        while (i < bytes.length) {
            bArr[(a.length + 1) + i] = bytes[i];
            i++;
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr2.length];
        ac acVar = new ac();
        acVar.a(bArr);
        acVar.b();
        for (int i = 0; i < bArr2.length; i++) {
            bArr3[i] = (byte) (bArr2[i] ^ acVar.a());
        }
        return bArr3;
    }

    private void b() {
        this.d = 0;
        this.c = 0;
    }

    public static byte[] b(byte[] bArr, String str) {
        return a(bArr, a.a(str));
    }

    byte a() {
        this.c = (this.c + 1) % 256;
        this.d = (this.d + a(this.b[this.c])) % 256;
        a(this.b, this.c, this.d);
        return this.b[(a(this.b[this.c]) + a(this.b[this.d])) % 256];
    }
}
