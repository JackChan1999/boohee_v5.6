package com.baidu.location;

import android.net.wifi.ScanResult;
import android.os.Environment;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class am implements ax, n {
    private static am hE = null;
    private static String[] hF = null;
    private static final String hG = "loc_cache.dat";
    private static final String hH = ";";
    private static final String hJ = ",";
    private static final int hK = 5;
    private static final double hL = 121.314d;
    private String[] hI = null;

    public class a {
        final /* synthetic */ am a;
        public int do;
        public boolean for;
        public double if;
        public double int;
        public long new;
        public double try;

        public a(am amVar) {
            this.a = amVar;
        }
    }

    private double bJ() {
        return (this.hI == null || this.hI.length <= 2) ? 0.0d : Double.valueOf(this.hI[2]).doubleValue();
    }

    private double bK() {
        return (this.hI == null || this.hI.length <= 1) ? 0.0d : Double.valueOf(this.hI[1]).doubleValue() - hL;
    }

    private long bL() {
        return (this.hI == null || this.hI.length < 3) ? 0 : Long.valueOf(this.hI[3]).longValue();
    }

    private boolean bM() {
        com.baidu.location.t.a ak = t.an().ak();
        return !TextUtils.isEmpty(hF[1]) && hF[1].equals(String.format("%s|%s|%s|%s", new Object[]{Integer.valueOf(ak.do), Integer.valueOf(ak.if), Integer.valueOf(ak.for), Integer.valueOf(ak.try)}));
    }

    private void bO() {
        if (this.hI == null && hF != null) {
            Object obj = hF[0];
            if (!TextUtils.isEmpty(obj)) {
                this.hI = obj.split(",");
            }
        }
    }

    private double bP() {
        return (this.hI == null || this.hI.length <= 0) ? 0.0d : Double.valueOf(this.hI[0]).doubleValue() - hL;
    }

    public static am bQ() {
        if (hE == null) {
            hE = new am();
        }
        return hE;
    }

    public a bN() {
        byte[] bArr = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File file = new File(I + File.separator + hG);
            if (file.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bArr2 = new byte[128];
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while (true) {
                        int read = fileInputStream.read(bArr2);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr2, 0, read);
                    }
                    bArr = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    fileInputStream.close();
                } catch (Exception e) {
                }
            }
        }
        hF = new String(bArr).split(";");
        bO();
        a aVar = new a(this);
        aVar.int = bP();
        aVar.try = bK();
        aVar.if = bJ();
        aVar.for = bM();
        aVar.do = bR();
        aVar.new = bL();
        return aVar;
    }

    public int bR() {
        String[] split = hF[2] != null ? hF[2].split(",") : null;
        b bS = ar.bW().bS();
        if (bS == null) {
            return 0;
        }
        List list = bS.for;
        if (list == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < 5) {
            int i3;
            ScanResult scanResult = (ScanResult) list.get(i);
            if (scanResult != null) {
                String replace = scanResult.BSSID.replace(":", "");
                for (Object equals : split) {
                    if (replace.equals(equals)) {
                        i3 = i2 + 1;
                        break;
                    }
                }
            }
            i3 = i2;
            i++;
            i2 = i3;
        }
        return i2;
    }

    public void new(BDLocation bDLocation) {
        int i = 0;
        if (bDLocation.getLocType() == 161) {
            String format;
            String format2 = String.format("%s,%s,%s,%d", new Object[]{Double.valueOf(bDLocation.getLongitude() + hL), Double.valueOf(bDLocation.getLatitude() + hL), Float.valueOf(bDLocation.getRadius()), Long.valueOf(System.currentTimeMillis())});
            if (t.an().ak().for()) {
                format = String.format("%s|%s|%s|%s", new Object[]{Integer.valueOf(t.an().ak().do), Integer.valueOf(t.an().ak().if), Integer.valueOf(t.an().ak().for), Integer.valueOf(t.an().ak().try)});
            } else {
                format = null;
            }
            String str = null;
            b bS = ar.bW().bS();
            if (bS != null) {
                List list = bS.for;
                if (list != null) {
                    Iterable arrayList = new ArrayList();
                    while (i < 5) {
                        ScanResult scanResult = (ScanResult) list.get(i);
                        if (scanResult != null) {
                            arrayList.add(scanResult.BSSID.replace(":", ""));
                        }
                        i++;
                    }
                    str = TextUtils.join(",", arrayList);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(format2).append(";").append(format).append(";").append(str);
            str = stringBuilder.toString();
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(I + File.separator + hG);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
