package com.baidu.location;

import android.support.v4.media.session.PlaybackStateCompat;
import com.qiniu.android.common.Constants;
import com.tencent.stat.DeviceInfo;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class y implements ax, n {
    public static boolean f0 = true;
    public static boolean f1 = true;
    private static final String f2 = (I + "/conlts.dat");
    public static int f4 = -1;
    public static boolean f5 = true;
    public static boolean f6 = true;
    private static final int f7 = 128;
    public static boolean f8 = true;
    private static y f9 = null;
    public static boolean gb = false;
    public static int gc = -1;
    public static int gd = 0;
    private a f3;
    private long ga;

    class a extends s {
        final /* synthetic */ y ds;
        boolean dt;
        String du;
        boolean dv;

        public a(y yVar) {
            this.ds = yVar;
            this.du = null;
            this.dt = false;
            this.dv = false;
            this.cT = new ArrayList();
        }

        void T() {
            this.cR = c.for();
            this.c0 = 2;
            String i = Jni.i(this.du);
            this.du = null;
            if (this.dt) {
                this.cT.add(new BasicNameValuePair("qt", "grid"));
            } else {
                this.cT.add(new BasicNameValuePair("qt", "conf"));
            }
            this.cT.add(new BasicNameValuePair("req", i));
        }

        void do(boolean z) {
            if (!z || this.cS == null) {
                this.ds.do(null);
            } else if (this.dt) {
                this.ds.if(this.cS);
            } else {
                this.ds.do(this.cS);
            }
            if (this.cT != null) {
                this.cT.clear();
            }
            this.dv = false;
        }

        public void if(String str, boolean z) {
            if (!this.dv) {
                this.dv = true;
                this.du = str;
                this.dt = z;
                N();
            }
        }
    }

    private y() {
        this.f3 = null;
        this.ga = 0;
        this.f3 = new a(this);
    }

    public static void a5() {
        String str = I + "/config.dat";
        int i = c.az ? 1 : 0;
        int i2 = c.aZ ? 1 : 0;
        byte[] bytes = String.format(Locale.CHINA, "{\"ver\":\"%d\",\"gps\":\"%.1f|%.1f|%.1f|%.1f|%d|%d|%d|%d|%d|%d|%d\",\"up\":\"%.1f|%.1f|%.1f|%.1f\",\"wf\":\"%d|%.1f|%d|%.1f\",\"ab\":\"%.2f|%.2f|%d|%d\",\"gpc\":\"%d|%d|%d|%d|%d|%d\",\"zxd\":\"%.1f|%.1f|%d|%d|%d\",\"shak\":\"%d|%d|%.1f\",\"dmx\":%d}", new Object[]{Integer.valueOf(c.ai), Float.valueOf(c.ae), Float.valueOf(c.aR), Float.valueOf(c.ag), Float.valueOf(c.aT), Integer.valueOf(c.aI), Integer.valueOf(c.W), Integer.valueOf(c.aJ), Integer.valueOf(c.X), Integer.valueOf(c.aa), Integer.valueOf(c.ax), Integer.valueOf(c.a4), Float.valueOf(c.be), Float.valueOf(c.bb), Float.valueOf(c.ap), Float.valueOf(c.a0), Integer.valueOf(c.aX), Float.valueOf(c.ab), Integer.valueOf(c.at), Float.valueOf(c.aU), Float.valueOf(c.bd), Float.valueOf(c.ba), Integer.valueOf(c.a8), Integer.valueOf(c.a6), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(c.av), Integer.valueOf(c.ay), Long.valueOf(c.aS), Integer.valueOf(c.aV), Float.valueOf(c.af), Float.valueOf(c.Z), Integer.valueOf(c.aq), Integer.valueOf(c.aF), Integer.valueOf(c.au), Integer.valueOf(c.aW), Integer.valueOf(c.aO), Float.valueOf(c.a2), Integer.valueOf(c.a5)}).getBytes();
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(str);
            if (!file.exists()) {
                File file2 = new File(I);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(2);
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes);
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    public static void a6() {
        try {
            File file = new File(f2);
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
                randomAccessFile.writeInt(0);
                randomAccessFile.writeInt(128);
                randomAccessFile.writeInt(0);
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    public static y a7() {
        if (f9 == null) {
            f9 = new y();
        }
        return f9;
    }

    private void ba() {
        this.f3.if("&ver=" + c.ai + "&usr=" + az.cn().ck() + "&app=" + az.iH + "&prod=" + az.iM, false);
    }

    public static void bb() {
        try {
            RandomAccessFile randomAccessFile;
            File file = new File(I + "/config.dat");
            if (!file.exists()) {
                File file2 = new File(I);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file.createNewFile()) {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.writeBoolean(false);
                    randomAccessFile.close();
                } else {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(1);
            randomAccessFile.writeBoolean(true);
            randomAccessFile.seek(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            randomAccessFile.writeDouble(c.aH);
            randomAccessFile.writeDouble(c.ac);
            randomAccessFile.writeBoolean(c.ar);
            if (c.ar && c.aB != null) {
                randomAccessFile.write(c.aB);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    public static void bc() {
        int i = 0;
        try {
            File file = new File(f2);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(4);
                int readInt = randomAccessFile.readInt();
                if (readInt > LocationClientOption.MIN_SCAN_SPAN_NETWORK) {
                    randomAccessFile.close();
                    gd = 0;
                    a6();
                    return;
                }
                int readInt2 = randomAccessFile.readInt();
                randomAccessFile.seek(128);
                byte[] bArr = new byte[readInt];
                while (i < readInt2) {
                    randomAccessFile.seek((long) ((readInt * i) + 128));
                    int readInt3 = randomAccessFile.readInt();
                    if (readInt3 > 0 && readInt3 < readInt) {
                        randomAccessFile.read(bArr, 0, readInt3);
                        if (bArr[readInt3 - 1] == (byte) 0) {
                            String str = new String(bArr, 0, readInt3 - 1);
                            az.cn();
                            if (str.equals(az.iH)) {
                                gc = randomAccessFile.readInt();
                                gd = i;
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (i == readInt2) {
                    gd = readInt2;
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
    }

    private void do(HttpEntity httpEntity) {
        String str = null;
        f4 = -1;
        if (httpEntity != null) {
            try {
                str = EntityUtils.toString(httpEntity, Constants.UTF_8);
                if (o(str)) {
                    a5();
                }
            } catch (Exception e) {
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("ctr")) {
                    f4 = Integer.parseInt(jSONObject.getString("ctr"));
                }
            } catch (Exception e2) {
            }
        }
        try {
            int i;
            bc();
            if (f4 != -1) {
                i = f4;
                new(f4);
            } else {
                i = gc != -1 ? gc : -1;
            }
            if (i != -1) {
                try(i);
            }
            r.H().I().obtainMessage(92).sendToTarget();
        } catch (Exception e3) {
        }
    }

    private void if(HttpEntity httpEntity) {
        int i = 0;
        try {
            byte[] toByteArray = EntityUtils.toByteArray(httpEntity);
            if (toByteArray != null) {
                if (toByteArray.length < 640) {
                    c.ar = false;
                    c.ac = c.an + 0.025d;
                    c.aH = c.a9 - 0.025d;
                    i = 1;
                } else {
                    c.ar = true;
                    c.aH = Double.longBitsToDouble(Long.valueOf(((((((((((long) toByteArray[7]) & 255) << 56) | ((((long) toByteArray[6]) & 255) << 48)) | ((((long) toByteArray[5]) & 255) << 40)) | ((((long) toByteArray[4]) & 255) << 32)) | ((((long) toByteArray[3]) & 255) << 24)) | ((((long) toByteArray[2]) & 255) << 16)) | ((((long) toByteArray[1]) & 255) << 8)) | (((long) toByteArray[0]) & 255)).longValue());
                    c.ac = Double.longBitsToDouble(Long.valueOf(((((((((((long) toByteArray[15]) & 255) << 56) | ((((long) toByteArray[14]) & 255) << 48)) | ((((long) toByteArray[13]) & 255) << 40)) | ((((long) toByteArray[12]) & 255) << 32)) | ((((long) toByteArray[11]) & 255) << 24)) | ((((long) toByteArray[10]) & 255) << 16)) | ((((long) toByteArray[9]) & 255) << 8)) | (((long) toByteArray[8]) & 255)).longValue());
                    c.aB = new byte[625];
                    while (i < 625) {
                        c.aB[i] = toByteArray[i + 16];
                        i++;
                    }
                    i = 1;
                }
            }
            if (i != 0) {
                bb();
            }
        } catch (Exception e) {
        }
    }

    public static void new(int i) {
        File file = new File(f2);
        if (!file.exists()) {
            a6();
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(4);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            randomAccessFile.seek((long) ((readInt * gd) + 128));
            byte[] bytes = (az.iH + '\u0000').getBytes();
            randomAccessFile.writeInt(bytes.length);
            randomAccessFile.write(bytes, 0, bytes.length);
            randomAccessFile.writeInt(i);
            if (readInt2 == gd) {
                randomAccessFile.seek(8);
                randomAccessFile.writeInt(readInt2 + 1);
            }
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    public static void try(int i) {
        boolean z = true;
        f6 = (i & 1) == 1;
        f8 = (i & 2) == 2;
        gb = (i & 4) == 4;
        f0 = (i & 8) == 8;
        f1 = (i & 65536) == 65536;
        if ((i & 131072) != 131072) {
            z = false;
        }
        f5 = z;
    }

    public void a8() {
        try {
            File file = new File(I + "/config.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(2);
                    int readInt = randomAccessFile.readInt();
                    byte[] bArr = new byte[readInt];
                    randomAccessFile.read(bArr, 0, readInt);
                    o(new String(bArr));
                }
                randomAccessFile.seek(1);
                if (randomAccessFile.readBoolean()) {
                    randomAccessFile.seek(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
                    c.aH = randomAccessFile.readDouble();
                    c.ac = randomAccessFile.readDouble();
                    c.ar = randomAccessFile.readBoolean();
                    if (c.ar) {
                        c.aB = new byte[625];
                        randomAccessFile.read(c.aB, 0, 625);
                    }
                }
                randomAccessFile.close();
            }
        } catch (Exception e) {
        }
        do(null);
    }

    public void a9() {
        if (System.currentTimeMillis() - this.ga > 72000000) {
            this.ga = System.currentTimeMillis();
            ba();
        }
    }

    public void n(String str) {
        this.f3.if(str, true);
    }

    public boolean o(String str) {
        boolean z = true;
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                int parseInt = Integer.parseInt(jSONObject.getString(DeviceInfo.TAG_VERSION));
                if (parseInt > c.ai) {
                    String[] split;
                    c.ai = parseInt;
                    if (jSONObject.has("gps")) {
                        split = jSONObject.getString("gps").split("\\|");
                        if (split.length > 10) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.ae = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.aR = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.ag = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.aT = Float.parseFloat(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                c.aI = Integer.parseInt(split[4]);
                            }
                            if (!(split[5] == null || split[5].equals(""))) {
                                c.W = Integer.parseInt(split[5]);
                            }
                            if (!(split[6] == null || split[6].equals(""))) {
                                c.aJ = Integer.parseInt(split[6]);
                            }
                            if (!(split[7] == null || split[7].equals(""))) {
                                c.X = Integer.parseInt(split[7]);
                            }
                            if (!(split[8] == null || split[8].equals(""))) {
                                c.aa = Integer.parseInt(split[8]);
                            }
                            if (!(split[9] == null || split[9].equals(""))) {
                                c.ax = Integer.parseInt(split[9]);
                            }
                            if (!(split[10] == null || split[10].equals(""))) {
                                c.a4 = Integer.parseInt(split[10]);
                            }
                        }
                    }
                    if (jSONObject.has("up")) {
                        split = jSONObject.getString("up").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.be = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.bb = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.ap = Float.parseFloat(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.a0 = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("wf")) {
                        split = jSONObject.getString("wf").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.aX = Integer.parseInt(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.ab = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.at = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.aU = Float.parseFloat(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("ab")) {
                        split = jSONObject.getString("ab").split("\\|");
                        if (split.length > 3) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.bd = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.ba = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.a8 = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.a6 = Integer.parseInt(split[3]);
                            }
                        }
                    }
                    if (jSONObject.has("zxd")) {
                        split = jSONObject.getString("zxd").split("\\|");
                        if (split.length > 4) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.af = Float.parseFloat(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.Z = Float.parseFloat(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.aq = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.aF = Integer.parseInt(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                c.au = Integer.parseInt(split[4]);
                            }
                        }
                    }
                    if (jSONObject.has("gpc")) {
                        split = jSONObject.getString("gpc").split("\\|");
                        if (split.length > 5) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                if (Integer.parseInt(split[0]) > 0) {
                                    c.az = true;
                                } else {
                                    c.az = false;
                                }
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                if (Integer.parseInt(split[1]) > 0) {
                                    c.aZ = true;
                                } else {
                                    c.aZ = false;
                                }
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.av = Integer.parseInt(split[2]);
                            }
                            if (!(split[3] == null || split[3].equals(""))) {
                                c.ay = Integer.parseInt(split[3]);
                            }
                            if (!(split[4] == null || split[4].equals(""))) {
                                int parseInt2 = Integer.parseInt(split[4]);
                                if (parseInt2 > 0) {
                                    c.aS = (long) parseInt2;
                                    c.aQ = (c.aS * 1000) * 60;
                                    c.aD = c.aQ >> 2;
                                } else {
                                    c.a7 = false;
                                }
                            }
                            if (!(split[5] == null || split[5].equals(""))) {
                                c.aV = Integer.parseInt(split[5]);
                            }
                        }
                    }
                    if (jSONObject.has("shak")) {
                        split = jSONObject.getString("shak").split("\\|");
                        if (split.length > 2) {
                            if (!(split[0] == null || split[0].equals(""))) {
                                c.aW = Integer.parseInt(split[0]);
                            }
                            if (!(split[1] == null || split[1].equals(""))) {
                                c.aO = Integer.parseInt(split[1]);
                            }
                            if (!(split[2] == null || split[2].equals(""))) {
                                c.a2 = Float.parseFloat(split[2]);
                            }
                        }
                    }
                    if (jSONObject.has("dmx")) {
                        c.a5 = jSONObject.getInt("dmx");
                    }
                    return z;
                }
            } catch (Exception e) {
                return false;
            }
        }
        z = false;
        return z;
    }
}
