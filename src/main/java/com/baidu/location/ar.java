package com.baidu.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.boohee.one.http.DnspodFree;
import com.boohee.widgets.PathListView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

class ar implements ax, n {
    private static final int hN = 15;
    private static ar hW = null;
    private a h0 = null;
    private final long hM = 3000;
    private boolean hO = true;
    private long hP = 0;
    private b hQ = null;
    private Object hR = null;
    private final long hS = 3000;
    private long hT = 0;
    private final long hU = 5000;
    private WifiManager hV = null;
    private Method hX = null;
    private boolean hY = false;
    private long hZ = 0;

    private class a extends BroadcastReceiver {
        final /* synthetic */ ar a;

        private a(ar arVar) {
            this.a = arVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && intent.getAction().equals("android.net.wifi.SCAN_RESULTS")) {
                this.a.hP = System.currentTimeMillis() / 1000;
                this.a.bV();
                ab.bj().obtainMessage(41).sendToTarget();
                if (ae.bp().bs()) {
                    ae.bp().g1.obtainMessage(41).sendToTarget();
                }
            }
        }
    }

    protected class b {
        final /* synthetic */ ar a;
        private boolean do = false;
        public List for = null;
        private long if = 0;
        private long int = 0;
        private boolean new;

        public b(ar arVar, b bVar) {
            this.a = arVar;
            if (bVar != null) {
                this.for = bVar.for;
                this.if = bVar.if;
                this.int = bVar.int;
                this.do = bVar.do;
            }
        }

        public b(ar arVar, List list, long j) {
            this.a = arVar;
            this.if = j;
            this.for = list;
            this.int = System.currentTimeMillis();
            a();
            c.if(ax.i, int());
        }

        private void a() {
            if (try() >= 1) {
                Object obj = 1;
                for (int size = this.for.size() - 1; size >= 1 && r2 != null; size--) {
                    int i = 0;
                    obj = null;
                    while (i < size) {
                        Object obj2;
                        if (((ScanResult) this.for.get(i)).level < ((ScanResult) this.for.get(i + 1)).level) {
                            ScanResult scanResult = (ScanResult) this.for.get(i + 1);
                            this.for.set(i + 1, this.for.get(i));
                            this.for.set(i, scanResult);
                            obj2 = 1;
                        } else {
                            obj2 = obj;
                        }
                        i++;
                        obj = obj2;
                    }
                }
            }
        }

        public String a(int i) {
            int i2;
            if (try() < 1) {
                return null;
            }
            int i3 = 0;
            Random random = new Random();
            StringBuffer stringBuffer = new StringBuffer(512);
            String b4 = this.a.b4();
            int i4 = 0;
            int i5 = 0;
            int size = this.for.size();
            Object obj = 1;
            if (size <= i) {
                i = size;
            }
            int i6 = 0;
            while (i6 < i) {
                Object obj2;
                int i7;
                if (((ScanResult) this.for.get(i6)).level == 0) {
                    obj2 = obj;
                    i7 = i4;
                    i2 = i5;
                    i5 = i3;
                    i3 = i2;
                } else {
                    if (obj != null) {
                        obj = null;
                        stringBuffer.append("&wf=");
                    } else {
                        stringBuffer.append("|");
                    }
                    String replace = ((ScanResult) this.for.get(i6)).BSSID.replace(":", "");
                    stringBuffer.append(replace);
                    size = ((ScanResult) this.for.get(i6)).level;
                    if (size < 0) {
                        size = -size;
                    }
                    stringBuffer.append(String.format(Locale.CHINA, ";%d;", new Object[]{Integer.valueOf(size)}));
                    i4++;
                    if (b4 != null && b4.equals(replace)) {
                        this.new = this.a.q(((ScanResult) this.for.get(i6)).capabilities);
                        i5 = i4;
                    }
                    if (i3 == 0) {
                        try {
                            if (random.nextInt(10) == 2 && ((ScanResult) this.for.get(i6)).SSID != null && ((ScanResult) this.for.get(i6)).SSID.length() < 30) {
                                stringBuffer.append(((ScanResult) this.for.get(i6)).SSID);
                                size = 1;
                            }
                            size = i3;
                        } catch (Exception e) {
                            obj2 = obj;
                            i7 = i4;
                            i2 = i5;
                            i5 = i3;
                            i3 = i2;
                        }
                    } else {
                        if (i3 == 1) {
                            if (random.nextInt(20) == 1 && ((ScanResult) this.for.get(i6)).SSID != null && ((ScanResult) this.for.get(i6)).SSID.length() < 30) {
                                stringBuffer.append(((ScanResult) this.for.get(i6)).SSID);
                                size = 2;
                            }
                        }
                        size = i3;
                    }
                    i3 = i5;
                    i5 = size;
                    obj2 = obj;
                    i7 = i4;
                }
                i6++;
                i4 = i7;
                obj = obj2;
                i2 = i3;
                i3 = i5;
                i5 = i2;
            }
            if (obj != null) {
                return null;
            }
            stringBuffer.append("&wf_n=" + i5);
            stringBuffer.append("&wf_st=");
            stringBuffer.append(this.if);
            stringBuffer.append("&wf_et=");
            stringBuffer.append(this.int);
            stringBuffer.append("&wf_vt=");
            stringBuffer.append(this.a.hP);
            if (i5 > 0) {
                this.do = true;
                stringBuffer.append("&wf_en=");
                stringBuffer.append(this.new ? 1 : 0);
            }
            return stringBuffer.toString();
        }

        public boolean a(b bVar) {
            return ar.if(bVar, this, c.aU);
        }

        public String byte() {
            try {
                return a(15);
            } catch (Exception e) {
                return null;
            }
        }

        public boolean case() {
            return this.do;
        }

        public String char() {
            try {
                return a(c.aX);
            } catch (Exception e) {
                return null;
            }
        }

        public int do() {
            for (int i = 0; i < try(); i++) {
                int i2 = -((ScanResult) this.for.get(i)).level;
                if (i2 > 0) {
                    return i2;
                }
            }
            return 0;
        }

        public boolean do(b bVar) {
            if (this.for == null || bVar == null || bVar.for == null) {
                return false;
            }
            int size = this.for.size() < bVar.for.size() ? this.for.size() : bVar.for.size();
            for (int i = 0; i < size; i++) {
                String str = ((ScanResult) this.for.get(i)).BSSID;
                int i2 = ((ScanResult) this.for.get(i)).level;
                String str2 = ((ScanResult) bVar.for.get(i)).BSSID;
                int i3 = ((ScanResult) bVar.for.get(i)).level;
                if (!str.equals(str2) || i2 != i3) {
                    return false;
                }
            }
            return true;
        }

        public String else() {
            StringBuffer stringBuffer = new StringBuffer(512);
            stringBuffer.append("wifi info:");
            if (try() < 1) {
                return stringBuffer.toString();
            }
            int size = this.for.size();
            if (size > 10) {
                size = 10;
            }
            int i = 0;
            int i2 = 1;
            while (i < size) {
                int i3;
                if (((ScanResult) this.for.get(i)).level == 0) {
                    i3 = i2;
                } else if (i2 != 0) {
                    stringBuffer.append("wifi=");
                    stringBuffer.append(((ScanResult) this.for.get(i)).BSSID.replace(":", ""));
                    i3 = ((ScanResult) this.for.get(i)).level;
                    stringBuffer.append(String.format(Locale.CHINA, ";%d;", new Object[]{Integer.valueOf(i3)}));
                    i3 = 0;
                } else {
                    stringBuffer.append(DnspodFree.IP_SPLIT);
                    stringBuffer.append(((ScanResult) this.for.get(i)).BSSID.replace(":", ""));
                    i3 = ((ScanResult) this.for.get(i)).level;
                    stringBuffer.append(String.format(Locale.CHINA, ",%d;", new Object[]{Integer.valueOf(i3)}));
                    i3 = i2;
                }
                i++;
                i2 = i3;
            }
            return stringBuffer.toString();
        }

        public boolean for() {
            return System.currentTimeMillis() - this.int < 3000;
        }

        public String if(int i) {
            if (i == 0 || try() < 1) {
                return null;
            }
            int i2;
            StringBuffer stringBuffer = new StringBuffer(256);
            if (this.for.size() > c.aX) {
                i2 = c.aX;
            }
            i2 = 0;
            int i3 = 1;
            for (int i4 = 0; i4 < c.aX; i4++) {
                if ((i3 & i) != 0) {
                    if (i2 == 0) {
                        stringBuffer.append("&ssid=");
                    } else {
                        stringBuffer.append("|");
                    }
                    stringBuffer.append(((ScanResult) this.for.get(i4)).BSSID);
                    stringBuffer.append(DnspodFree.IP_SPLIT);
                    stringBuffer.append(((ScanResult) this.for.get(i4)).SSID);
                    i2++;
                }
                i3 <<= 1;
            }
            return stringBuffer.toString();
        }

        public boolean if() {
            return System.currentTimeMillis() - this.if < 3000;
        }

        public boolean if(b bVar) {
            if (this.for == null || bVar == null || bVar.for == null) {
                return false;
            }
            int size = this.for.size() < bVar.for.size() ? this.for.size() : bVar.for.size();
            for (int i = 0; i < size; i++) {
                if (!((ScanResult) this.for.get(i)).BSSID.equals(((ScanResult) bVar.for.get(i)).BSSID)) {
                    return false;
                }
            }
            return true;
        }

        public String int() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("wifi=");
            if (this.for == null) {
                return stringBuilder.toString();
            }
            for (int i = 0; i < this.for.size(); i++) {
                int i2 = ((ScanResult) this.for.get(i)).level;
                stringBuilder.append(((ScanResult) this.for.get(i)).BSSID.replace(":", ""));
                stringBuilder.append(String.format(Locale.CHINA, ",%d;", new Object[]{Integer.valueOf(i2)}));
            }
            return stringBuilder.toString();
        }

        public boolean new() {
            return System.currentTimeMillis() - this.int < 5000;
        }

        public int try() {
            return this.for == null ? 0 : this.for.size();
        }
    }

    private ar() {
    }

    public static boolean bU() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) f.getServiceContext().getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private void bV() {
        if (this.hV != null) {
            try {
                b bVar = new b(this, this.hV.getScanResults(), this.hT);
                this.hT = 0;
                if (this.hQ == null || !bVar.if(this.hQ)) {
                    this.hQ = bVar;
                }
            } catch (Exception e) {
            }
        }
    }

    public static ar bW() {
        if (hW == null) {
            hW = new ar();
        }
        return hW;
    }

    public static double if(b bVar, b bVar2) {
        if (bVar == null || bVar2 == null) {
            return 0.0d;
        }
        List list = bVar.for;
        List list2 = bVar2.for;
        if (list == list2) {
            return PathListView.NO_ZOOM;
        }
        if (list == null || list2 == null) {
            return 0.0d;
        }
        int size = list.size();
        int size2 = list2.size();
        float f = (float) (size + size2);
        if (size == 0 && size2 == 0) {
            return PathListView.NO_ZOOM;
        }
        if (size == 0 || size2 == 0) {
            return 0.0d;
        }
        int i = 0;
        int i2 = 0;
        while (i < size) {
            int i3;
            String str = ((ScanResult) list.get(i)).BSSID;
            if (str == null) {
                i3 = i2;
            } else {
                for (int i4 = 0; i4 < size2; i4++) {
                    if (str.equals(((ScanResult) list2.get(i4)).BSSID)) {
                        i3 = i2 + 1;
                        break;
                    }
                }
                i3 = i2;
            }
            i++;
            i2 = i3;
        }
        return f <= 0.0f ? 0.0d : ((double) i2) / ((double) f);
    }

    public static boolean if(b bVar, b bVar2, float f) {
        if (bVar == null || bVar2 == null) {
            return false;
        }
        List list = bVar.for;
        List list2 = bVar2.for;
        if (list == list2) {
            return true;
        }
        if (list == null || list2 == null) {
            return false;
        }
        int size = list.size();
        int size2 = list2.size();
        float f2 = (float) (size + size2);
        if (size == 0 && size2 == 0) {
            return true;
        }
        if (size == 0 || size2 == 0) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        while (i < size) {
            int i3;
            String str = ((ScanResult) list.get(i)).BSSID;
            if (str == null) {
                i3 = i2;
            } else {
                for (int i4 = 0; i4 < size2; i4++) {
                    if (str.equals(((ScanResult) list2.get(i4)).BSSID)) {
                        i3 = i2 + 1;
                        break;
                    }
                }
                i3 = i2;
            }
            i++;
            i2 = i3;
        }
        return ((float) (i2 * 2)) >= f2 * f;
    }

    private boolean q(String str) {
        return TextUtils.isEmpty(str) ? false : Pattern.compile("wpa|wep", 2).matcher(str).find();
    }

    public String b0() {
        String str = null;
        try {
            WifiInfo connectionInfo = this.hV.getConnectionInfo();
            if (connectionInfo != null) {
                str = connectionInfo.getMacAddress();
            }
        } catch (Exception e) {
        }
        return str;
    }

    public b b1() {
        return (this.hQ == null || !this.hQ.new()) ? bY() : this.hQ;
    }

    public synchronized void b2() {
        if (!this.hY) {
            if (ab.gv) {
                this.hV = (WifiManager) f.getServiceContext().getSystemService("wifi");
                this.h0 = new a();
                try {
                    f.getServiceContext().registerReceiver(this.h0, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
                } catch (Exception e) {
                }
                this.hY = true;
                try {
                    Field declaredField = Class.forName("android.net.wifi.WifiManager").getDeclaredField("mService");
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        this.hR = declaredField.get(this.hV);
                        this.hX = this.hR.getClass().getDeclaredMethod("startScan", new Class[]{Boolean.TYPE});
                        if (this.hX != null) {
                            this.hX.setAccessible(true);
                        }
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    public boolean b3() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.hZ <= 10000) {
            return false;
        }
        this.hZ = currentTimeMillis;
        return bX();
    }

    public String b4() {
        WifiInfo connectionInfo = this.hV.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        try {
            String bssid = connectionInfo.getBSSID();
            if (bssid != null) {
                bssid = bssid.replace(":", "");
                if ("000000000000".equals(bssid) || "".equals(bssid)) {
                    return null;
                }
            }
            bssid = null;
            return bssid;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean b5() {
        try {
            if (this.hV.isWifiEnabled()) {
                if (this.hX == null || this.hR == null) {
                    this.hV.startScan();
                } else {
                    try {
                        this.hX.invoke(this.hR, new Object[]{Boolean.valueOf(this.hO)});
                    } catch (Exception e) {
                        this.hV.startScan();
                    }
                }
                this.hT = System.currentTimeMillis();
                return true;
            }
            this.hT = 0;
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public b bS() {
        return (this.hQ == null || !this.hQ.for()) ? bY() : this.hQ;
    }

    public synchronized void bT() {
        if (this.hY) {
            try {
                f.getServiceContext().unregisterReceiver(this.h0);
                this.hP = 0;
            } catch (Exception e) {
            }
            this.h0 = null;
            this.hV = null;
            this.hY = false;
        }
    }

    public boolean bX() {
        return (this.hV != null && System.currentTimeMillis() - this.hT > 3000) ? b5() : false;
    }

    public b bY() {
        if (this.hV != null) {
            try {
                return new b(this, this.hV.getScanResults(), 0);
            } catch (Exception e) {
            }
        }
        return new b(this, null, 0);
    }

    public boolean bZ() {
        return this.hV.isWifiEnabled() && 3 == this.hV.getWifiState();
    }
}
