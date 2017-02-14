package com.tencent.wxop.stat.b;

public class h {
    static final /* synthetic */ boolean cH = (!h.class.desiredAssertionStatus());

    private h() {
    }

    public static byte[] d(byte[] bArr) {
        int length = bArr.length;
        j jVar = new j(new byte[((length * 3) / 4)]);
        if (!jVar.a(bArr, length)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (jVar.g == jVar.cI.length) {
            return jVar.cI;
        } else {
            Object obj = new byte[jVar.g];
            System.arraycopy(jVar.cI, 0, obj, 0, jVar.g);
            return obj;
        }
    }

    public static byte[] e(byte[] bArr) {
        int length = bArr.length;
        k kVar = new k();
        int i = (length / 3) * 4;
        if (!kVar.ba) {
            switch (length % 3) {
                case 0:
                    break;
                case 1:
                    i += 2;
                    break;
                case 2:
                    i += 3;
                    break;
                default:
                    break;
            }
        } else if (length % 3 > 0) {
            i += 4;
        }
        if (kVar.bb && length > 0) {
            i += (kVar.cP ? 2 : 1) * (((length - 1) / 57) + 1);
        }
        kVar.cI = new byte[i];
        kVar.a(bArr, length);
        if (cH || kVar.g == i) {
            return kVar.cI;
        }
        throw new AssertionError();
    }
}
