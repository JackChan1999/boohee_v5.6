package com.tencent.wxop.stat.b;

final class j extends i {
    private static final int[] cJ = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58,
            59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28,
            29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
            50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private static final int[] cK = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58,
            59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28,
            29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
            50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private              int   bf = 0;
    private final        int[] cL = cJ;
    private              int   cu = 0;

    public j(byte[] bArr) {
        this.cI = bArr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean a(byte[] r14, int r15) {
        /*
        r13 = this;
        r12 = -2;
        r11 = -1;
        r10 = 6;
        r3 = 0;
        r0 = r13.cu;
        if (r0 != r10) goto L_0x000a;
    L_0x0008:
        r0 = r3;
    L_0x0009:
        return r0;
    L_0x000a:
        r6 = r15 + 0;
        r0 = r13.cu;
        r1 = r13.bf;
        r7 = r13.cI;
        r8 = r13.cL;
        r2 = r3;
        r5 = r0;
        r0 = r3;
    L_0x0017:
        if (r2 >= r6) goto L_0x00fa;
    L_0x0019:
        if (r5 != 0) goto L_0x0060;
    L_0x001b:
        r4 = r2 + 4;
        if (r4 > r6) goto L_0x005e;
    L_0x001f:
        r1 = r14[r2];
        r1 = r1 & 255;
        r1 = r8[r1];
        r1 = r1 << 18;
        r4 = r2 + 1;
        r4 = r14[r4];
        r4 = r4 & 255;
        r4 = r8[r4];
        r4 = r4 << 12;
        r1 = r1 | r4;
        r4 = r2 + 2;
        r4 = r14[r4];
        r4 = r4 & 255;
        r4 = r8[r4];
        r4 = r4 << 6;
        r1 = r1 | r4;
        r4 = r2 + 3;
        r4 = r14[r4];
        r4 = r4 & 255;
        r4 = r8[r4];
        r1 = r1 | r4;
        if (r1 < 0) goto L_0x005e;
    L_0x0048:
        r4 = r0 + 2;
        r9 = (byte) r1;
        r7[r4] = r9;
        r4 = r0 + 1;
        r9 = r1 >> 8;
        r9 = (byte) r9;
        r7[r4] = r9;
        r4 = r1 >> 16;
        r4 = (byte) r4;
        r7[r0] = r4;
        r0 = r0 + 3;
        r2 = r2 + 4;
        goto L_0x001b;
    L_0x005e:
        if (r2 >= r6) goto L_0x00fa;
    L_0x0060:
        r4 = r2 + 1;
        r2 = r14[r2];
        r2 = r2 & 255;
        r2 = r8[r2];
        switch(r5) {
            case 0: goto L_0x006d;
            case 1: goto L_0x007b;
            case 2: goto L_0x008c;
            case 3: goto L_0x00ac;
            case 4: goto L_0x00e4;
            case 5: goto L_0x00f3;
            default: goto L_0x006b;
        };
    L_0x006b:
        r2 = r4;
        goto L_0x0017;
    L_0x006d:
        if (r2 < 0) goto L_0x0075;
    L_0x006f:
        r1 = r5 + 1;
        r5 = r1;
        r1 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x0075:
        if (r2 == r11) goto L_0x006b;
    L_0x0077:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x007b:
        if (r2 < 0) goto L_0x0085;
    L_0x007d:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r5 + 1;
        r5 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x0085:
        if (r2 == r11) goto L_0x006b;
    L_0x0087:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x008c:
        if (r2 < 0) goto L_0x0096;
    L_0x008e:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r5 + 1;
        r5 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x0096:
        if (r2 != r12) goto L_0x00a5;
    L_0x0098:
        r2 = r0 + 1;
        r5 = r1 >> 4;
        r5 = (byte) r5;
        r7[r0] = r5;
        r0 = 4;
        r5 = r0;
        r0 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x00a5:
        if (r2 == r11) goto L_0x006b;
    L_0x00a7:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x00ac:
        if (r2 < 0) goto L_0x00c8;
    L_0x00ae:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r0 + 2;
        r5 = (byte) r1;
        r7[r2] = r5;
        r2 = r0 + 1;
        r5 = r1 >> 8;
        r5 = (byte) r5;
        r7[r2] = r5;
        r2 = r1 >> 16;
        r2 = (byte) r2;
        r7[r0] = r2;
        r0 = r0 + 3;
        r2 = r4;
        r5 = r3;
        goto L_0x0017;
    L_0x00c8:
        if (r2 != r12) goto L_0x00dd;
    L_0x00ca:
        r2 = r0 + 1;
        r5 = r1 >> 2;
        r5 = (byte) r5;
        r7[r2] = r5;
        r2 = r1 >> 10;
        r2 = (byte) r2;
        r7[r0] = r2;
        r0 = r0 + 2;
        r2 = 5;
        r5 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x00dd:
        if (r2 == r11) goto L_0x006b;
    L_0x00df:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x00e4:
        if (r2 != r12) goto L_0x00ec;
    L_0x00e6:
        r2 = r5 + 1;
        r5 = r2;
        r2 = r4;
        goto L_0x0017;
    L_0x00ec:
        if (r2 == r11) goto L_0x006b;
    L_0x00ee:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x00f3:
        if (r2 == r11) goto L_0x006b;
    L_0x00f5:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x00fa:
        r2 = r1;
        switch(r5) {
            case 0: goto L_0x00fe;
            case 1: goto L_0x0105;
            case 2: goto L_0x010a;
            case 3: goto L_0x0113;
            case 4: goto L_0x0122;
            default: goto L_0x00fe;
        };
    L_0x00fe:
        r13.cu = r5;
        r13.g = r0;
        r0 = 1;
        goto L_0x0009;
    L_0x0105:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
    L_0x010a:
        r1 = r0 + 1;
        r2 = r2 >> 4;
        r2 = (byte) r2;
        r7[r0] = r2;
        r0 = r1;
        goto L_0x00fe;
    L_0x0113:
        r1 = r0 + 1;
        r3 = r2 >> 10;
        r3 = (byte) r3;
        r7[r0] = r3;
        r0 = r1 + 1;
        r2 = r2 >> 2;
        r2 = (byte) r2;
        r7[r1] = r2;
        goto L_0x00fe;
    L_0x0122:
        r13.cu = r10;
        r0 = r3;
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.wxop.stat.b.j.a(byte[], int):boolean");
    }
}
