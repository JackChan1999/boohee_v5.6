package com.baidu.location;

import android.location.Location;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.message.BasicNameValuePair;

class q implements ax, n {
    private static Location b9 = null;
    private static double cA = 0.1d;
    private static int cB = 1024;
    private static final String cC = (I + "/yor.dat");
    private static Location cD = null;
    private static ArrayList cE = new ArrayList();
    private static final int cG = 2048;
    private static final String cH = (I + "/yom.dat");
    private static final String cI = (I + "/yoh.dat");
    private static int cJ = 128;
    private static int cK = 512;
    private static ArrayList cL = new ArrayList();
    private static q cM = null;
    private static String cN = (I + "/yo.dat");
    private static final int ca = 2048;
    private static final int cb = 2048;
    private static double cc = 100.0d;
    private static double cd = 0.0d;
    private static int ce = 16;
    private static int cf = 8;
    private static int cg = 1024;
    private static int ch = 64;
    private static File ci = null;
    private static final int cj = 128;
    private static ArrayList ck = new ArrayList();
    private static double cl = 30.0d;
    private static int cm = 8;
    private static int co = 0;
    private static final int cp = 1040;
    private static Location cq = null;
    private static final int cr = 32;
    private static b cs = null;
    private static ArrayList ct = new ArrayList();
    private static int cu = 5;
    private static ArrayList cv = new ArrayList();
    private static final String cw = (I + "/yol.dat");
    private static int cx = 256;
    private static ArrayList cz = new ArrayList();
    long cF;
    private int cn;
    private b cy;

    private class a extends s {
        final /* synthetic */ q dk;
        private String dl;

        public a(q qVar, String str) {
            this.dk = qVar;
            this.dl = str;
            this.cT = new ArrayList();
        }

        void T() {
            this.cR = c.for();
            this.cT.add(new BasicNameValuePair("cldc[0]", this.dl));
        }

        public void ae() {
            N();
        }

        void do(boolean z) {
            if (!z || this.cS != null) {
            }
        }
    }

    private class b extends s {
        final /* synthetic */ q dm;
        boolean dn;
        private ArrayList dp;
        int dq;
        int dr;

        public b(q qVar) {
            this.dm = qVar;
            this.dn = false;
            this.dr = 0;
            this.dq = 0;
            this.dp = null;
            this.cT = new ArrayList();
        }

        void T() {
            this.cR = c.for();
            this.c0 = 2;
            if (this.dp != null) {
                for (int i = 0; i < this.dp.size(); i++) {
                    if (this.dr == 1) {
                        this.cT.add(new BasicNameValuePair("cldc[" + i + "]", (String) this.dp.get(i)));
                    } else {
                        this.cT.add(new BasicNameValuePair("cltr[" + i + "]", (String) this.dp.get(i)));
                    }
                }
                this.cT.add(new BasicNameValuePair("trtm", String.format(Locale.CHINA, "%d", new Object[]{Long.valueOf(System.currentTimeMillis())})));
            }
        }

        public void af() {
            if (!this.dn) {
                if (cY <= 4 || this.dq >= cY) {
                    this.dq = 0;
                    this.dn = true;
                    this.dr = 0;
                    if (this.dp == null || this.dp.size() < 1) {
                        if (this.dp == null) {
                            this.dp = new ArrayList();
                        }
                        this.dr = 0;
                        int i = 0;
                        do {
                            String B = this.dr < 2 ? q.B() : null;
                            if (B != null || this.dr == 1) {
                                this.dr = 1;
                            } else {
                                this.dr = 2;
                                try {
                                    if (c.aV == 0) {
                                        B = l.s();
                                        if (B == null) {
                                            B = w.aM();
                                        }
                                    } else if (c.aV == 1) {
                                        B = w.aM();
                                        if (B == null) {
                                            B = l.s();
                                        }
                                    }
                                } catch (Exception e) {
                                    B = null;
                                }
                            }
                            if (B == null) {
                                break;
                            }
                            this.dp.add(B);
                            i += B.length();
                        } while (i < ax.O);
                    }
                    if (this.dp == null || this.dp.size() < 1) {
                        this.dp = null;
                        this.dn = false;
                        return;
                    }
                    N();
                    return;
                }
                this.dq++;
            }
        }

        void do(boolean z) {
            if (!(!z || this.cS == null || this.dp == null)) {
                this.dp.clear();
            }
            if (this.cT != null) {
                this.cT.clear();
            }
            this.dn = false;
        }
    }

    private q() {
        this.cy = null;
        this.cn = 0;
        this.cF = 0;
        this.cy = new b(this);
        this.cn = 0;
    }

    public static String B() {
        return D();
    }

    public static void C() {
    }

    public static String D() {
        String str = null;
        for (int i = 1; i < 5; i++) {
            str = if(i);
            if (str != null) {
                return str;
            }
        }
        if(cE, ch);
        if (cE.size() > 0) {
            str = (String) cE.get(0);
            cE.remove(0);
        }
        if (str != null) {
            return str;
        }
        if(cE, co);
        if (cE.size() > 0) {
            str = (String) cE.get(0);
            cE.remove(0);
        }
        if (str != null) {
            return str;
        }
        if(cE, cJ);
        if (cE.size() <= 0) {
            return str;
        }
        str = (String) cE.get(0);
        cE.remove(0);
        return str;
    }

    public static void F() {
    }

    public static void case(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                File file2 = new File(I);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (!file.createNewFile()) {
                    file = null;
                }
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(0);
                randomAccessFile.writeInt(32);
                randomAccessFile.writeInt(2048);
                randomAccessFile.writeInt(cp);
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(0);
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public static void char(String str) {
        List list;
        int i = c.aM;
        if (i == 1) {
            list = cL;
        } else if (i == 2) {
            list = cv;
        } else if (i == 3) {
            list = cE;
        } else {
            return;
        }
        if (list != null) {
            if (list.size() <= ce) {
                list.add(str);
            }
            if (list.size() >= ce) {
                if(i, false);
            }
            while (list.size() > ce) {
                list.remove(0);
            }
        }
    }

    public static void do(com.baidu.location.t.a aVar, b bVar, Location location, String str) {
        b bVar2 = null;
        if (!y.f6) {
            return;
        }
        if (c.ak == 3 && !if(location, bVar) && !if(location, false)) {
            return;
        }
        String str2;
        if (aVar != null && aVar.do()) {
            if (!if(location, bVar)) {
                bVar = null;
            }
            str2 = c.if(aVar, bVar, location, str, 1);
            if (str2 != null) {
                goto(Jni.i(str2));
                cD = location;
                cq = location;
                if (bVar != null) {
                    cs = bVar;
                }
            }
        } else if (bVar != null && bVar.if() && if(location, bVar)) {
            com.baidu.location.t.a aVar2;
            if (if(location)) {
                aVar2 = aVar;
            }
            str2 = c.if(aVar2, bVar, location, str, 2);
            if (str2 != null) {
                else(Jni.i(str2));
                b9 = location;
                cq = location;
                if (bVar != null) {
                    cs = bVar;
                }
            }
        } else {
            if (!if(location)) {
                aVar = null;
            }
            if (if(location, bVar)) {
                bVar2 = bVar;
            }
            if (aVar != null || bVar2 != null) {
                String str3 = c.if(aVar, bVar2, location, str, 3);
                if (str3 != null) {
                    void(Jni.i(str3));
                    cq = location;
                    if (bVar2 != null) {
                        cs = bVar2;
                    }
                }
            }
        }
    }

    private static void else(String str) {
        char(str);
    }

    private static void goto(String str) {
        char(str);
    }

    private static int if(List list, int i) {
        if (list == null || i > 256 || i < 0) {
            return -1;
        }
        try {
            if (ci == null) {
                ci = new File(cN);
                if (!ci.exists()) {
                    ci = null;
                    return -2;
                }
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(ci, "rw");
            if (randomAccessFile.length() < 1) {
                randomAccessFile.close();
                return -3;
            }
            randomAccessFile.seek((long) i);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            int readInt4 = randomAccessFile.readInt();
            long readLong = randomAccessFile.readLong();
            if (!if(readInt, readInt2, readInt3, readInt4, readLong) || readInt2 < 1) {
                randomAccessFile.close();
                return -4;
            }
            byte[] bArr = new byte[cB];
            int i2 = readInt2;
            readInt2 = cf;
            while (readInt2 > 0 && i2 > 0) {
                randomAccessFile.seek(((long) ((((readInt + i2) - 1) % readInt3) * readInt4)) + readLong);
                int readInt5 = randomAccessFile.readInt();
                if (readInt5 > 0 && readInt5 < readInt4) {
                    randomAccessFile.read(bArr, 0, readInt5);
                    if (bArr[readInt5 - 1] == (byte) 0) {
                        list.add(new String(bArr, 0, readInt5 - 1));
                    }
                }
                readInt2--;
                i2--;
            }
            randomAccessFile.seek((long) i);
            randomAccessFile.writeInt(readInt);
            randomAccessFile.writeInt(i2);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.writeInt(readInt4);
            randomAccessFile.writeLong(readLong);
            randomAccessFile.close();
            return cf - readInt2;
        } catch (Exception e) {
            e.printStackTrace();
            return -5;
        }
    }

    public static String if(int i) {
        String str;
        List list;
        String str2 = null;
        if (i == 1) {
            str = cI;
            list = cL;
        } else if (i == 2) {
            str = cH;
            list = cv;
        } else if (i == 3) {
            str = cw;
            list = cE;
        } else {
            if (i == 4) {
                str = cC;
                list = cE;
            }
            return str2;
        }
        if (list != null) {
            if (list.size() < 1) {
                if(str, list);
            }
            synchronized (q.class) {
                int size = list.size();
                if (size > 0) {
                    str2 = (String) list.get(size - 1);
                    list.remove(size - 1);
                }
            }
        }
        return str2;
    }

    public static String if(com.baidu.location.t.a aVar, b bVar, Location location, String str, String str2) {
        return !y.f6 ? null : c.if(aVar, bVar, location, str) + str2;
    }

    public static void if(double d, double d2, double d3, double d4) {
        if (d <= 0.0d) {
            d = cd;
        }
        cd = d;
        cA = d2;
        if (d3 <= 20.0d) {
            d3 = cl;
        }
        cl = d3;
        cc = d4;
    }

    public static void if(int i, int i2) {
    }

    public static void if(int i, int i2, boolean z) {
    }

    public static void if(int i, boolean z) {
        String str;
        Object obj;
        String str2;
        if (i == 1) {
            str2 = cI;
            if (!z) {
                str = str2;
                List list = cL;
            } else {
                return;
            }
        } else if (i == 2) {
            str2 = cH;
            if (z) {
                str = str2;
                obj = cL;
            } else {
                str = str2;
                obj = cv;
            }
        } else if (i == 3) {
            str2 = cw;
            if (z) {
                str = str2;
                obj = cv;
            } else {
                str = str2;
                obj = cE;
            }
        } else if (i == 4) {
            str2 = cC;
            if (z) {
                str = str2;
                obj = cE;
            } else {
                return;
            }
        } else {
            return;
        }
        File file = new File(str);
        if (!file.exists()) {
            case(str);
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(4);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            int readInt4 = randomAccessFile.readInt();
            int readInt5 = randomAccessFile.readInt();
            int size = list.size();
            int i2 = readInt5;
            while (size > cm) {
                readInt5 = z ? i2 + 1 : i2;
                byte[] bytes;
                if (readInt3 >= readInt) {
                    if (!z) {
                        obj = 1;
                        i2 = readInt5;
                        break;
                    }
                    randomAccessFile.seek((long) ((readInt4 * readInt2) + 128));
                    bytes = (((String) list.get(0)) + '\u0000').getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes, 0, bytes.length);
                    list.remove(0);
                    i2 = readInt4 + 1;
                    if (i2 > readInt3) {
                        i2 = 0;
                    }
                    readInt4 = readInt3;
                } else {
                    randomAccessFile.seek((long) ((readInt2 * readInt3) + 128));
                    bytes = (((String) list.get(0)) + '\u0000').getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes, 0, bytes.length);
                    list.remove(0);
                    int i3 = readInt4;
                    readInt4 = readInt3 + 1;
                    i2 = i3;
                }
                size--;
                readInt3 = readInt4;
                readInt4 = i2;
                i2 = readInt5;
            }
            obj = null;
            randomAccessFile.seek(12);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.writeInt(readInt4);
            randomAccessFile.writeInt(i2);
            randomAccessFile.close();
            if (obj != null && i < 4) {
                if(i + 1, true);
            }
        } catch (Exception e) {
        }
    }

    public static void if(String str, int i) {
    }

    public static void if(String str, int i, boolean z) {
    }

    private static boolean if(int i, int i2, int i3, int i4, long j) {
        return i >= 0 && i < i3 && i2 >= 0 && i2 <= i3 && i3 >= 0 && i3 <= 1024 && i4 >= 128 && i4 <= 1024;
    }

    private static boolean if(Location location) {
        if (location == null) {
            return false;
        }
        if (cD == null || cq == null) {
            cD = location;
            return true;
        }
        double distanceTo = (double) location.distanceTo(cD);
        return ((double) location.distanceTo(cq)) > ((distanceTo * ((double) c.ba)) + ((((double) c.bd) * distanceTo) * distanceTo)) + ((double) c.a8);
    }

    private static boolean if(Location location, b bVar) {
        if (location == null || bVar == null || bVar.for == null || bVar.for.isEmpty() || bVar.do(cs)) {
            return false;
        }
        if (b9 != null) {
            return true;
        }
        b9 = location;
        return true;
    }

    public static boolean if(Location location, boolean z) {
        return x.if(cq, location, z);
    }

    public static boolean if(String str, List list) {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(8);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            byte[] bArr = new byte[cB];
            int i = readInt2;
            readInt2 = cm + 1;
            boolean z = false;
            while (readInt2 > 0 && i > 0) {
                if (i < readInt3) {
                    readInt3 = 0;
                }
                try {
                    randomAccessFile.seek((long) (((i - 1) * readInt) + 128));
                    int readInt4 = randomAccessFile.readInt();
                    if (readInt4 > 0 && readInt4 < readInt) {
                        randomAccessFile.read(bArr, 0, readInt4);
                        if (bArr[readInt4 - 1] == (byte) 0) {
                            list.add(0, new String(bArr, 0, readInt4 - 1));
                            z = true;
                        }
                    }
                    readInt2--;
                    i--;
                } catch (Exception e) {
                    return z;
                }
            }
            randomAccessFile.seek(12);
            randomAccessFile.writeInt(i);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.close();
            return z;
        } catch (Exception e2) {
            return false;
        }
    }

    private static void void(String str) {
        char(str);
    }

    public static void w() {
        cm = 0;
        if(1, false);
        if(2, false);
        if(3, false);
        cm = 8;
    }

    public static q x() {
        if (cM == null) {
            cM = new q();
        }
        return cM;
    }

    public static String y() {
        RandomAccessFile randomAccessFile;
        int readInt;
        File file = new File(cH);
        if (file.exists()) {
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(20);
                readInt = randomAccessFile.readInt();
                if (readInt > 128) {
                    String str = "&p1=" + readInt;
                    randomAccessFile.seek(20);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                    return str;
                }
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }
        file = new File(cw);
        if (file.exists()) {
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(20);
                readInt = randomAccessFile.readInt();
                if (readInt > 256) {
                    str = "&p2=" + readInt;
                    randomAccessFile.seek(20);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                    return str;
                }
                randomAccessFile.close();
            } catch (Exception e2) {
            }
        }
        file = new File(cC);
        if (!file.exists()) {
            return null;
        }
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(20);
            readInt = randomAccessFile.readInt();
            if (readInt > 512) {
                str = "&p3=" + readInt;
                randomAccessFile.seek(20);
                randomAccessFile.writeInt(0);
                randomAccessFile.close();
                return str;
            }
            randomAccessFile.close();
            return null;
        } catch (Exception e3) {
            return null;
        }
    }

    public void A() {
        this.cF = System.currentTimeMillis();
        if (!o.ac().ab()) {
            this.cn++;
            if (this.cn > 1) {
                this.cn = 0;
                E();
            }
        }
    }

    public void E() {
        if (ar.bU()) {
            this.cy.af();
        }
    }

    public void long(String str) {
        new a(this, str).ae();
    }

    public void z() {
        if (aw.do().int() && this.cF != 0 && System.currentTimeMillis() - this.cF > 1200000) {
            A();
        }
    }
}
