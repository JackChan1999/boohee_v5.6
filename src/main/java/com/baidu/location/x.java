package com.baidu.location;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

class x implements ax, n {
    private static int fB = 0;
    private static final int fC = 1;
    private static String fE = null;
    private static final int fH = 3;
    private static final int fJ = 10000;
    private static final int fM = 5;
    private static x fN = null;
    private static File fO = new File(I, fY);
    private static final int fV = 290000;
    private static String fY = "Temp_in.dat";
    private static StringBuilder fp = null;
    private static final double fq = 1.0E-5d;
    private static final int fs = 3000;
    private static String fu;
    private long fA = 0;
    private final long fD = 1000;
    private final int fF = 3;
    private Location fG;
    private boolean fI = false;
    private long fK;
    private b fL = null;
    private boolean fP = false;
    private c fQ = null;
    private String fR = null;
    private Location fS;
    private final int fT = 1;
    private long fU = 0;
    private long fW;
    private int fX;
    private long fZ;
    private GpsStatus fg;
    private int fh;
    private Handler fi = null;
    private final int fj = 2;
    private LocationManager fk = null;
    private boolean fl = false;
    private Location fm;
    private a fn = null;
    private final int fo = 4;
    private HashMap fr;
    private long ft = 0;
    private Location fv;
    private long fw = 0;
    private long fx;
    private Location fy;
    private Context fz;

    private class a implements Listener, NmeaListener {
        private long a;
        private boolean byte;
        private List case;
        private final int char;
        private String do;
        private String for;
        long if;
        final /* synthetic */ x int;
        private String new;
        private boolean try;

        private a(x xVar) {
            this.int = xVar;
            this.if = 0;
            this.a = 0;
            this.char = 400;
            this.try = true;
            this.byte = false;
            this.case = new ArrayList();
            this.for = null;
            this.new = null;
            this.do = null;
        }

        public void a(String str) {
            if (System.currentTimeMillis() - this.a > 400 && this.byte && this.case.size() > 0) {
                try {
                    at atVar = new at(this.case, this.for, this.new, this.do);
                    if (atVar.cV()) {
                        c.al = this.int.if(atVar, this.int.fh);
                        if (c.al > 0) {
                            x.fE = String.format(Locale.CHINA, "&nmea=%.1f|%.1f&g_tp=%d", new Object[]{Double.valueOf(atVar.cT()), Double.valueOf(atVar.cW()), Integer.valueOf(c.al)});
                        }
                    } else {
                        c.al = 0;
                    }
                } catch (Exception e) {
                    c.al = 0;
                }
                this.case.clear();
                this.do = null;
                this.new = null;
                this.for = null;
                this.byte = false;
            }
            if (str.startsWith("$GPGGA")) {
                this.byte = true;
                this.for = str.trim();
            } else if (str.startsWith("$GPGSV")) {
                this.case.add(str.trim());
            } else if (str.startsWith("$GPGSA")) {
                this.do = str.trim();
            }
            this.a = System.currentTimeMillis();
        }

        public void onGpsStatusChanged(int i) {
            if (this.int.fk != null) {
                switch (i) {
                    case 2:
                        this.int.int(null);
                        this.int.new(false);
                        x.fB = 0;
                        return;
                    case 4:
                        if (this.int.fI || System.currentTimeMillis() - this.if >= 10000) {
                            if (this.int.fg == null) {
                                this.int.fg = this.int.fk.getGpsStatus(null);
                            } else {
                                this.int.fk.getGpsStatus(this.int.fg);
                            }
                            x.fp = new StringBuilder();
                            this.int.fX = 0;
                            this.int.fh = 0;
                            this.int.fr = new HashMap();
                            int i2 = 0;
                            int i3 = 0;
                            int i4 = 0;
                            for (GpsSatellite gpsSatellite : this.int.fg.getSatellites()) {
                                i3++;
                                if (gpsSatellite.usedInFix()) {
                                    i4++;
                                }
                                if (gpsSatellite.getSnr() > 0.0f) {
                                    i2++;
                                }
                                if (gpsSatellite.getSnr() >= ((float) c.aa)) {
                                    this.int.fh = this.int.fh + 1;
                                }
                                x.fp.append(this.int.if(gpsSatellite, this.int.fr));
                            }
                            x.fB = i2;
                            this.int.if(this.int.fr);
                            if (!this.int.fI && System.currentTimeMillis() - this.int.fU >= 60000) {
                                if (i4 > 3 || i3 > 15) {
                                    Location lastKnownLocation = this.int.fk.getLastKnownLocation("gps");
                                    if (lastKnownLocation != null) {
                                        this.if = System.currentTimeMillis();
                                        long currentTimeMillis = (System.currentTimeMillis() + g.e().bp) - lastKnownLocation.getTime();
                                        if (currentTimeMillis < 3500 && currentTimeMillis > -200 && q.if(lastKnownLocation, false)) {
                                            this.int.fi.sendMessage(this.int.fi.obtainMessage(3, lastKnownLocation));
                                            return;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        public void onNmeaReceived(long j, String str) {
            if (!this.int.fI) {
                return;
            }
            if (!y.f5) {
                c.al = 0;
            } else if (str != null && !str.equals("") && str.length() >= 9 && str.length() <= 150 && this.int.aR()) {
                this.int.fi.sendMessage(this.int.fi.obtainMessage(2, str));
            }
        }
    }

    private class b implements LocationListener {
        final /* synthetic */ x a;

        private b(x xVar) {
            this.a = xVar;
        }

        public void onLocationChanged(Location location) {
            this.a.fA = System.currentTimeMillis();
            this.a.new(true);
            this.a.int(location);
            this.a.fl = false;
        }

        public void onProviderDisabled(String str) {
            this.a.int(null);
            this.a.new(false);
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                    this.a.int(null);
                    this.a.new(false);
                    return;
                case 1:
                    this.a.ft = System.currentTimeMillis();
                    this.a.fl = true;
                    this.a.new(false);
                    return;
                case 2:
                    this.a.fl = false;
                    return;
                default:
                    return;
            }
        }
    }

    private class c implements LocationListener {
        final /* synthetic */ x a;
        private long if;

        private c(x xVar) {
            this.a = xVar;
            this.if = 0;
        }

        public void onLocationChanged(Location location) {
            if (!this.a.fI && location != null && location.getProvider() == "gps") {
                this.a.fU = System.currentTimeMillis();
                if (System.currentTimeMillis() - this.if >= 10000 && q.if(location, false)) {
                    this.if = System.currentTimeMillis();
                    this.a.fi.sendMessage(this.a.fi.obtainMessage(4, location));
                }
            }
        }

        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private x() {
    }

    public static x a4() {
        if (fN == null) {
            fN = new x();
        }
        return fN;
    }

    private boolean aQ() {
        return false;
    }

    private static String aX() {
        if (fp == null) {
            return null;
        }
        if (!TextUtils.isEmpty(fp.toString())) {
            fp.insert(0, "&snls=");
        }
        if (!TextUtils.isEmpty(fu)) {
            fp.append("&pogr=").append(fu);
        }
        return fp.toString();
    }

    public static String byte(Location location) {
        String str = case(location);
        return str != null ? str + "&g_tp=0" : str;
    }

    public static String case(Location location) {
        float f = -1.0f;
        if (location == null) {
            return null;
        }
        float speed = (float) (((double) location.getSpeed()) * 3.6d);
        if (!location.hasSpeed()) {
            speed = -1.0f;
        }
        int accuracy = (int) (location.hasAccuracy() ? location.getAccuracy() : -1.0f);
        double altitude = location.hasAltitude() ? location.getAltitude() : 555.0d;
        if (location.hasBearing()) {
            f = location.getBearing();
        }
        return String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d", new Object[]{Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(f), Integer.valueOf(accuracy), Integer.valueOf(fB), Double.valueOf(altitude), Long.valueOf(location.getTime() / 1000)});
    }

    private double[] do(double d, double d2) {
        return new double[]{Math.sin(Math.toRadians(d2)) * d, Math.cos(Math.toRadians(d2)) * d};
    }

    private void for(Location location) {
        this.fS = location;
        if (this.fS == null) {
            this.fR = null;
        } else {
            this.fS.setTime(System.currentTimeMillis());
            float speed = (float) (((double) this.fS.getSpeed()) * 3.6d);
            if (!this.fS.hasSpeed()) {
                speed = -1.0f;
            }
            int i = fB;
            if (i == 0) {
                try {
                    i = this.fS.getExtras().getInt("satellites");
                } catch (Exception e) {
                }
            }
            this.fR = String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_n=%d&ll_t=%d", new Object[]{Double.valueOf(this.fS.getLongitude()), Double.valueOf(this.fS.getLatitude()), Float.valueOf(speed), Float.valueOf(this.fS.getBearing()), Integer.valueOf(i), Long.valueOf(r2)});
            if(this.fS.getLongitude(), this.fS.getLatitude(), speed);
        }
        try {
            w.aK().do(this.fS);
        } catch (Exception e2) {
        }
        if (aR()) {
            k.p().byte(aP());
            if (fB > 2 && q.if(this.fS, true)) {
                ar.bW().b3();
                q.do(t.an().ak(), ar.bW().bS(), this.fS, k.p().o());
            }
        }
    }

    private double[] for(double d, double d2) {
        double d3 = 0.0d;
        if (d2 != 0.0d) {
            d3 = Math.toDegrees(Math.atan(d / d2));
        } else if (d > 0.0d) {
            d3 = 90.0d;
        } else if (d < 0.0d) {
            d3 = 270.0d;
        }
        return new double[]{Math.sqrt((d * d) + (d2 * d2)), d3};
    }

    private int if(at atVar, int i) {
        if (fB >= c.W) {
            return 1;
        }
        if (fB <= c.aI) {
            return 4;
        }
        double cT = atVar.cT();
        if (cT <= ((double) c.ae)) {
            return 1;
        }
        if (cT >= ((double) c.aR)) {
            return 4;
        }
        cT = atVar.cW();
        return cT > ((double) c.ag) ? cT >= ((double) c.aT) ? 4 : i < c.X ? i <= c.aJ ? 4 : this.fr != null ? if(this.fr) : 3 : 1 : 1;
    }

    private int if(HashMap hashMap) {
        if (this.fX > 4) {
            List arrayList = new ArrayList();
            List arrayList2 = new ArrayList();
            int i = 0;
            for (Entry value : hashMap.entrySet()) {
                int i2;
                List list = (List) value.getValue();
                if (list != null) {
                    Object obj = if(list);
                    if (obj != null) {
                        arrayList.add(obj);
                        i2 = i + 1;
                        arrayList2.add(Integer.valueOf(i));
                        i = i2;
                    }
                }
                i2 = i;
                i = i2;
            }
            if (!arrayList.isEmpty()) {
                double[] dArr;
                double[] dArr2 = new double[2];
                int size = arrayList.size();
                for (int i3 = 0; i3 < size; i3++) {
                    dArr = (double[]) arrayList.get(i3);
                    i = ((Integer) arrayList2.get(i3)).intValue();
                    dArr[0] = dArr[0] * ((double) i);
                    dArr[1] = dArr[1] * ((double) i);
                    dArr2[0] = dArr2[0] + dArr[0];
                    dArr2[1] = dArr2[1] + dArr[1];
                }
                dArr2[0] = dArr2[0] / ((double) size);
                dArr2[1] = dArr2[1] / ((double) size);
                dArr = for(dArr2[0], dArr2[1]);
                fu = String.format(Locale.CHINA, "%d,%d,%d,%d", new Object[]{Long.valueOf(Math.round(dArr2[0] * 100.0d)), Long.valueOf(Math.round(dArr2[1] * 100.0d)), Long.valueOf(Math.round(dArr[0] * 100.0d)), Long.valueOf(Math.round(dArr[1] * 100.0d))});
                if (dArr[0] <= ((double) c.ax)) {
                    return 1;
                }
                if (dArr[0] >= ((double) c.a4)) {
                    return 4;
                }
            }
        }
        return 3;
    }

    private String if(GpsSatellite gpsSatellite, HashMap hashMap) {
        int floor = (int) Math.floor((double) (gpsSatellite.getAzimuth() / 30.0f));
        float elevation = gpsSatellite.getElevation();
        int floor2 = (int) Math.floor((double) (elevation / 15.0f));
        float snr = gpsSatellite.getSnr();
        int round = Math.round(snr / 5.0f);
        if (snr >= 10.0f && elevation >= 1.0f) {
            List list = (List) hashMap.get(Integer.valueOf(round));
            if (list == null) {
                list = new ArrayList();
            }
            list.add(gpsSatellite);
            hashMap.put(Integer.valueOf(round), list);
            this.fX++;
        }
        if (floor >= 12) {
            floor = 11;
        }
        int i = floor2 >= 6 ? 5 : floor2;
        return String.format(Locale.CHINA, "%02d%d", new Object[]{Integer.valueOf((i + (floor * 6)) + 1), Integer.valueOf(round)});
    }

    private void if(double d, double d2, float f) {
        int i = 0;
        if (y.f1) {
            if (d >= 73.146973d && d <= 135.252686d && d2 <= 54.258807d && d2 >= 14.604847d && f <= 18.0f) {
                int i2 = (int) ((d - c.aH) * 1000.0d);
                int i3 = (int) ((c.ac - d2) * 1000.0d);
                if (i2 <= 0 || i2 >= 50 || i3 <= 0 || i3 >= 50) {
                    String str = String.format(Locale.CHINA, "&ll=%.5f|%.5f", new Object[]{Double.valueOf(d), Double.valueOf(d2)}) + "&im=" + az.cn().ck();
                    c.a9 = d;
                    c.an = d2;
                    y.a7().n(str);
                } else {
                    i2 += i3 * 50;
                    i3 = i2 >> 2;
                    i2 &= 3;
                    if (c.ar) {
                        i = (c.aB[i3] >> (i2 * 2)) & 3;
                    }
                }
            }
            if (c.ak != i) {
                c.ak = i;
            }
        }
    }

    private void if(String str, Location location) {
        q.do(t.an().ak(), ar.bW().bS(), location, str + k.p().o());
    }

    public static boolean if(Location location, Location location2, boolean z) {
        if (location == location2) {
            return false;
        }
        if (location == null || location2 == null) {
            return true;
        }
        float speed = location2.getSpeed();
        if (z && c.ak == 3 && speed < 5.0f) {
            return true;
        }
        float distanceTo = location2.distanceTo(location);
        return speed > c.bb ? distanceTo > c.a0 : speed > c.be ? distanceTo > c.ap : distanceTo > 5.0f;
    }

    private double[] if(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        double[] dArr = new double[2];
        for (GpsSatellite gpsSatellite : list) {
            if (gpsSatellite != null) {
                double[] dArr2 = do((double) (90.0f - gpsSatellite.getElevation()), (double) gpsSatellite.getAzimuth());
                dArr[0] = dArr[0] + dArr2[0];
                dArr[1] = dArr[1] + dArr2[1];
            }
        }
        int size = list.size();
        dArr[0] = dArr[0] / ((double) size);
        dArr[1] = dArr[1] / ((double) size);
        return dArr;
    }

    private void int(Location location) {
        this.fi.sendMessage(this.fi.obtainMessage(1, location));
    }

    public static String new(Location location) {
        String str = case(location);
        if (str != null) {
            str = str + fE;
        }
        Object aX = aX();
        return !TextUtils.isEmpty(aX) ? str + aX : str;
    }

    private void new(boolean z) {
        this.fP = z;
        if (!z || !aR()) {
        }
    }

    private void try(Location location) {
        long currentTimeMillis = System.currentTimeMillis();
        this.fG = location;
        this.fW = currentTimeMillis;
        if (this.fK != 0 && currentTimeMillis - this.fK < 290000 && this.fy != null) {
            return;
        }
        if (this.fm == null) {
            this.fm = location;
            this.fv = this.fm;
            this.fK = currentTimeMillis;
            this.fZ = this.fK;
            this.fy = null;
        } else if (currentTimeMillis - this.fK >= 10000) {
            this.fy = location;
            Object obj = q.if(t.an().ak(), ar.bW().bS(), location, k.p().o(), String.format("&dt=%.6f|%.6f|%s|%s|%s", new Object[]{Double.valueOf(this.fy.getLongitude() - this.fm.getLongitude()), Double.valueOf(this.fy.getLatitude() - this.fm.getLatitude()), Float.valueOf(this.fy.getSpeed()), Float.valueOf(this.fy.getBearing()), Long.valueOf(currentTimeMillis - this.fK)}));
            if (!TextUtils.isEmpty(obj)) {
                q.x().long(Jni.i(obj));
            }
            this.fm = null;
        }
    }

    public void a0() {
        try {
            if (this.fG != null || this.fv != null) {
                Object obj = q.if(t.an().ak(), ar.bW().bS(), this.fG, k.p().o(), String.format(Locale.CHINA, "&dt=%.6f|%.6f|%s|%s|%s", new Object[]{Double.valueOf(this.fG.getLongitude() - this.fv.getLongitude()), Double.valueOf(this.fG.getLatitude() - this.fv.getLatitude()), Float.valueOf(this.fG.getSpeed()), Float.valueOf(this.fG.getBearing()), Long.valueOf(this.fW - this.fZ)}));
                if (!TextUtils.isEmpty(obj)) {
                    q.x().long(Jni.i(obj));
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean a1() {
        return this.fk != null ? this.fk.isProviderEnabled("gps") : false;
    }

    public void a2() {
        if (!this.fI) {
            try {
                this.fL = new b();
                this.fk.requestLocationUpdates("gps", 1000, 0.0f, this.fL);
                this.fk.addNmeaListener(this.fn);
                this.fI = true;
            } catch (Exception e) {
            }
        }
    }

    public void a3() {
        if (this.fI) {
            if (this.fk != null) {
                try {
                    if (this.fL != null) {
                        this.fk.removeUpdates(this.fL);
                    }
                    if (this.fn != null) {
                        this.fk.removeNmeaListener(this.fn);
                    }
                } catch (Exception e) {
                }
            }
            c.al = 0;
            c.ak = 0;
            this.fL = null;
            this.fI = false;
            new(false);
        }
    }

    public String aP() {
        if (this.fS == null) {
            return null;
        }
        String str = "{\"result\":{\"time\":\"" + c.new() + "\",\"error\":\"61\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%d\",\"d\":\"%f\"," + "\"s\":\"%f\",\"n\":\"%d\"}}";
        int accuracy = (int) (this.fS.hasAccuracy() ? this.fS.getAccuracy() : 10.0f);
        float speed = (float) (((double) this.fS.getSpeed()) * 3.6d);
        double[] dArr = Jni.if(this.fS.getLongitude(), this.fS.getLatitude(), "gps2gcj");
        if (dArr[0] <= 0.0d && dArr[1] <= 0.0d) {
            dArr[0] = this.fS.getLongitude();
            dArr[1] = this.fS.getLatitude();
        }
        return String.format(Locale.CHINA, str, new Object[]{Double.valueOf(dArr[0]), Double.valueOf(dArr[1]), Integer.valueOf(accuracy), Float.valueOf(this.fS.getBearing()), Float.valueOf(speed), Integer.valueOf(fB)});
    }

    public boolean aR() {
        if (!aT() || System.currentTimeMillis() - this.fA > 10000) {
            return false;
        }
        return (!this.fl || System.currentTimeMillis() - this.ft >= 3000) ? this.fP : true;
    }

    public Location aS() {
        return this.fS;
    }

    public boolean aT() {
        return (this.fS == null || this.fS.getLatitude() == 0.0d || this.fS.getLongitude() == 0.0d) ? false : true;
    }

    public synchronized void aU() {
        if (ab.gv) {
            this.fz = f.getServiceContext();
            try {
                this.fk = (LocationManager) this.fz.getSystemService("location");
                this.fn = new a();
                this.fk.addGpsStatusListener(this.fn);
                this.fQ = new c();
                this.fk.requestLocationUpdates("passive", 1000, 0.0f, this.fQ);
            } catch (Exception e) {
            }
            this.fi = new Handler(this) {
                final /* synthetic */ x a;

                {
                    this.a = r1;
                }

                public void handleMessage(Message message) {
                    if (ab.gv) {
                        switch (message.what) {
                            case 1:
                                this.a.for((Location) message.obj);
                                return;
                            case 2:
                                if (this.a.fn != null) {
                                    this.a.fn.a((String) message.obj);
                                    return;
                                }
                                return;
                            case 3:
                                this.a.if("&og=1", (Location) message.obj);
                                return;
                            case 4:
                                this.a.if("&og=2", (Location) message.obj);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }
    }

    public String aV() {
        return this.fR;
    }

    public synchronized void aW() {
        a3();
        if (this.fk != null) {
            try {
                if (this.fn != null) {
                    this.fk.removeGpsStatusListener(this.fn);
                }
                this.fk.removeUpdates(this.fQ);
            } catch (Exception e) {
            }
            this.fn = null;
            this.fk = null;
        }
    }

    public String aY() {
        return (!aR() || this.fS == null) ? null : case(this.fS);
    }

    public void int(boolean z) {
        if (z) {
            a2();
        } else {
            a3();
        }
    }
}
