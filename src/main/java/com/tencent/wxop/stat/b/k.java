package com.tencent.wxop.stat.b;

final class k extends i {
    static final /* synthetic */ boolean ad = (!h.class.desiredAssertionStatus());
    private static final         byte[]  cM = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte)
            68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75,
            (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte)
            83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90,
            (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103,
            (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110,
            (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117,
            (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49,
            (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte)
            57, (byte) 43, (byte) 47};
    private static final         byte[]  cN = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte)
            68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75,
            (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte)
            83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90,
            (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103,
            (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110,
            (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117,
            (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49,
            (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte)
            57, (byte) 45, (byte) 95};
    public final  boolean ba;
    public final  boolean bb;
    private final byte[]  cO;
    public final  boolean cP;
    private final byte[]  cQ;
    private       int     cc;
    int cp;

    public k() {
        this.cI = null;
        this.ba = true;
        this.bb = true;
        this.cP = false;
        this.cQ = cM;
        this.cO = new byte[2];
        this.cp = 0;
        this.cc = this.bb ? 19 : -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean a(byte[] r14, int r15) {
        /*
        r13 = this;
        r6 = 2;
        r12 = 13;
        r11 = 10;
        r3 = 1;
        r4 = 0;
        r7 = r13.cQ;
        r8 = r13.cI;
        r2 = r13.cc;
        r9 = r15 + 0;
        r0 = -1;
        r1 = r13.cp;
        switch(r1) {
            case 0: goto L_0x00a4;
            case 1: goto L_0x00a8;
            case 2: goto L_0x00c4;
            default: goto L_0x0015;
        };
    L_0x0015:
        r5 = r0;
        r1 = r4;
    L_0x0017:
        r0 = -1;
        if (r5 == r0) goto L_0x01ef;
    L_0x001a:
        r0 = r5 >> 18;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r4] = r0;
        r0 = r5 >> 12;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r3] = r0;
        r0 = r5 >> 6;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r6] = r0;
        r6 = 3;
        r0 = 4;
        r5 = r5 & 63;
        r5 = r7[r5];
        r8[r6] = r5;
        r2 = r2 + -1;
        if (r2 != 0) goto L_0x01eb;
    L_0x003e:
        r2 = r13.cP;
        if (r2 == 0) goto L_0x0046;
    L_0x0042:
        r2 = 4;
        r0 = 5;
        r8[r2] = r12;
    L_0x0046:
        r5 = r0 + 1;
        r8[r0] = r11;
        r0 = 19;
        r6 = r0;
    L_0x004d:
        r0 = r1 + 3;
        if (r0 > r9) goto L_0x00e2;
    L_0x0051:
        r0 = r14[r1];
        r0 = r0 & 255;
        r0 = r0 << 16;
        r2 = r1 + 1;
        r2 = r14[r2];
        r2 = r2 & 255;
        r2 = r2 << 8;
        r0 = r0 | r2;
        r2 = r1 + 2;
        r2 = r14[r2];
        r2 = r2 & 255;
        r0 = r0 | r2;
        r2 = r0 >> 18;
        r2 = r2 & 63;
        r2 = r7[r2];
        r8[r5] = r2;
        r2 = r5 + 1;
        r10 = r0 >> 12;
        r10 = r10 & 63;
        r10 = r7[r10];
        r8[r2] = r10;
        r2 = r5 + 2;
        r10 = r0 >> 6;
        r10 = r10 & 63;
        r10 = r7[r10];
        r8[r2] = r10;
        r2 = r5 + 3;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r2] = r0;
        r2 = r1 + 3;
        r1 = r5 + 4;
        r0 = r6 + -1;
        if (r0 != 0) goto L_0x01e6;
    L_0x0093:
        r0 = r13.cP;
        if (r0 == 0) goto L_0x01e3;
    L_0x0097:
        r0 = r1 + 1;
        r8[r1] = r12;
    L_0x009b:
        r5 = r0 + 1;
        r8[r0] = r11;
        r0 = 19;
        r1 = r2;
        r6 = r0;
        goto L_0x004d;
    L_0x00a4:
        r5 = r0;
        r1 = r4;
        goto L_0x0017;
    L_0x00a8:
        if (r6 > r9) goto L_0x0015;
    L_0x00aa:
        r0 = r13.cO;
        r0 = r0[r4];
        r0 = r0 & 255;
        r0 = r0 << 16;
        r1 = r14[r4];
        r1 = r1 & 255;
        r1 = r1 << 8;
        r0 = r0 | r1;
        r1 = r14[r3];
        r1 = r1 & 255;
        r0 = r0 | r1;
        r13.cp = r4;
        r5 = r0;
        r1 = r6;
        goto L_0x0017;
    L_0x00c4:
        if (r9 <= 0) goto L_0x0015;
    L_0x00c6:
        r0 = r13.cO;
        r0 = r0[r4];
        r0 = r0 & 255;
        r0 = r0 << 16;
        r1 = r13.cO;
        r1 = r1[r3];
        r1 = r1 & 255;
        r1 = r1 << 8;
        r0 = r0 | r1;
        r1 = r14[r4];
        r1 = r1 & 255;
        r0 = r0 | r1;
        r13.cp = r4;
        r5 = r0;
        r1 = r3;
        goto L_0x0017;
    L_0x00e2:
        r0 = r13.cp;
        r0 = r1 - r0;
        r2 = r9 + -1;
        if (r0 != r2) goto L_0x0146;
    L_0x00ea:
        r0 = r13.cp;
        if (r0 <= 0) goto L_0x013f;
    L_0x00ee:
        r0 = r13.cO;
        r0 = r0[r4];
        r2 = r3;
    L_0x00f3:
        r0 = r0 & 255;
        r4 = r0 << 4;
        r0 = r13.cp;
        r0 = r0 - r2;
        r13.cp = r0;
        r2 = r5 + 1;
        r0 = r4 >> 6;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r5] = r0;
        r0 = r2 + 1;
        r4 = r4 & 63;
        r4 = r7[r4];
        r8[r2] = r4;
        r2 = r13.ba;
        if (r2 == 0) goto L_0x011e;
    L_0x0112:
        r2 = r0 + 1;
        r4 = 61;
        r8[r0] = r4;
        r0 = r2 + 1;
        r4 = 61;
        r8[r2] = r4;
    L_0x011e:
        r2 = r13.bb;
        if (r2 == 0) goto L_0x0130;
    L_0x0122:
        r2 = r13.cP;
        if (r2 == 0) goto L_0x012b;
    L_0x0126:
        r2 = r0 + 1;
        r8[r0] = r12;
        r0 = r2;
    L_0x012b:
        r2 = r0 + 1;
        r8[r0] = r11;
        r0 = r2;
    L_0x0130:
        r5 = r0;
    L_0x0131:
        r0 = ad;
        if (r0 != 0) goto L_0x01ce;
    L_0x0135:
        r0 = r13.cp;
        if (r0 == 0) goto L_0x01ce;
    L_0x0139:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x013f:
        r2 = r1 + 1;
        r0 = r14[r1];
        r1 = r2;
        r2 = r4;
        goto L_0x00f3;
    L_0x0146:
        r0 = r13.cp;
        r0 = r1 - r0;
        r2 = r9 + -2;
        if (r0 != r2) goto L_0x01b6;
    L_0x014e:
        r0 = r13.cp;
        if (r0 <= r3) goto L_0x01aa;
    L_0x0152:
        r0 = r13.cO;
        r0 = r0[r4];
        r4 = r3;
    L_0x0157:
        r0 = r0 & 255;
        r10 = r0 << 10;
        r0 = r13.cp;
        if (r0 <= 0) goto L_0x01b0;
    L_0x015f:
        r0 = r13.cO;
        r2 = r4 + 1;
        r0 = r0[r4];
        r4 = r2;
    L_0x0166:
        r0 = r0 & 255;
        r0 = r0 << 2;
        r0 = r0 | r10;
        r2 = r13.cp;
        r2 = r2 - r4;
        r13.cp = r2;
        r2 = r5 + 1;
        r4 = r0 >> 12;
        r4 = r4 & 63;
        r4 = r7[r4];
        r8[r5] = r4;
        r4 = r2 + 1;
        r5 = r0 >> 6;
        r5 = r5 & 63;
        r5 = r7[r5];
        r8[r2] = r5;
        r2 = r4 + 1;
        r0 = r0 & 63;
        r0 = r7[r0];
        r8[r4] = r0;
        r0 = r13.ba;
        if (r0 == 0) goto L_0x01e1;
    L_0x0190:
        r0 = r2 + 1;
        r4 = 61;
        r8[r2] = r4;
    L_0x0196:
        r2 = r13.bb;
        if (r2 == 0) goto L_0x01a8;
    L_0x019a:
        r2 = r13.cP;
        if (r2 == 0) goto L_0x01a3;
    L_0x019e:
        r2 = r0 + 1;
        r8[r0] = r12;
        r0 = r2;
    L_0x01a3:
        r2 = r0 + 1;
        r8[r0] = r11;
        r0 = r2;
    L_0x01a8:
        r5 = r0;
        goto L_0x0131;
    L_0x01aa:
        r2 = r1 + 1;
        r0 = r14[r1];
        r1 = r2;
        goto L_0x0157;
    L_0x01b0:
        r2 = r1 + 1;
        r0 = r14[r1];
        r1 = r2;
        goto L_0x0166;
    L_0x01b6:
        r0 = r13.bb;
        if (r0 == 0) goto L_0x0131;
    L_0x01ba:
        if (r5 <= 0) goto L_0x0131;
    L_0x01bc:
        r0 = 19;
        if (r6 == r0) goto L_0x0131;
    L_0x01c0:
        r0 = r13.cP;
        if (r0 == 0) goto L_0x01df;
    L_0x01c4:
        r0 = r5 + 1;
        r8[r5] = r12;
    L_0x01c8:
        r5 = r0 + 1;
        r8[r0] = r11;
        goto L_0x0131;
    L_0x01ce:
        r0 = ad;
        if (r0 != 0) goto L_0x01da;
    L_0x01d2:
        if (r1 == r9) goto L_0x01da;
    L_0x01d4:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x01da:
        r13.g = r5;
        r13.cc = r6;
        return r3;
    L_0x01df:
        r0 = r5;
        goto L_0x01c8;
    L_0x01e1:
        r0 = r2;
        goto L_0x0196;
    L_0x01e3:
        r0 = r1;
        goto L_0x009b;
    L_0x01e6:
        r6 = r0;
        r5 = r1;
        r1 = r2;
        goto L_0x004d;
    L_0x01eb:
        r6 = r2;
        r5 = r0;
        goto L_0x004d;
    L_0x01ef:
        r6 = r2;
        r5 = r4;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.wxop.stat.b.k.a(byte[], int):boolean");
    }
}
