package com.baidu.location;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.boohee.one.http.DnspodFree;
import com.boohee.widgets.PathListView;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

class l implements ax, n {
    private static final int b5 = 200;
    private static File b7 = null;
    private static File bD = null;
    private static final int bK = 800;
    public static final String bM = "com.baidu.locTest.LocationServer4.2";
    private static final int bW = 24;
    private static String bZ = (I + "/glb.dat");
    private long b0 = 0;
    private final int b1 = 200;
    private int b2 = 0;
    private int b3 = 1;
    private boolean b4 = false;
    long b6 = 0;
    private a b8 = null;
    private Handler bA = null;
    private boolean bB = false;
    private long[] bC = new long[20];
    private boolean bE = false;
    private boolean bF = false;
    private String bG = (I + "/vm.dat");
    private int bH = 0;
    private AlarmManager bI = null;
    private PendingIntent bJ = null;
    private Context bL = null;
    private String bN = null;
    private long bO = 0;
    private boolean bP = false;
    private long bQ = 0;
    private com.baidu.location.t.a bR = null;
    private long bS = c.ad;
    private final int bT = 1;
    String bU = "dlcu.dat";
    ArrayList bV = null;
    private long bX = 0;
    ArrayList bY = null;
    c bx = null;
    private final long by = 86100000;
    private String bz = "";

    public class a extends BroadcastReceiver {
        final /* synthetic */ l a;

        public a(l lVar) {
            this.a = lVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(l.bM)) {
                this.a.bA.sendEmptyMessage(1);
            }
        }
    }

    class b {
        public static final double do = 0.8d;
        public static final double if = 0.7d;
        final /* synthetic */ l a;
        private double for = PathListView.NO_ZOOM;
        private HashMap int = new HashMap();

        public b(l lVar, b bVar) {
            this.a = lVar;
            if (bVar.for != null) {
                int i = 0;
                for (ScanResult scanResult : bVar.for) {
                    int abs = Math.abs(scanResult.level);
                    this.int.put(scanResult.BSSID, Integer.valueOf(abs));
                    this.for += (double) ((100 - abs) * (100 - abs));
                    int i2 = i + 1;
                    if (i2 > 16) {
                        break;
                    }
                    i = i2;
                }
                this.for = Math.sqrt(this.for);
            }
        }

        public double a() {
            return this.for;
        }

        double a(b bVar) {
            double d = 0.0d;
            for (String str : this.int.keySet()) {
                int intValue = ((Integer) this.int.get(str)).intValue();
                Integer num = (Integer) bVar.if().get(str);
                if (num != null) {
                    d = ((double) ((100 - num.intValue()) * (100 - intValue))) + d;
                }
            }
            return d / (this.for * bVar.a());
        }

        public HashMap if() {
            return this.int;
        }
    }

    class c extends BroadcastReceiver {
        final /* synthetic */ l a;
        boolean if = false;

        public c(l lVar) {
            this.a = lVar;
            a(f.getServiceContext());
        }

        public void a(Context context) {
            if (!this.if) {
                this.if = true;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.SCREEN_OFF");
                intentFilter.addAction("android.intent.action.SCREEN_ON");
                context.registerReceiver(this, intentFilter);
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!action.equals("android.intent.action.SCREEN_ON") && action.equals("android.intent.action.SCREEN_OFF")) {
                ad.cM().cK();
            }
        }
    }

    public l(Context context) {
        this.bL = context;
        this.b6 = 0;
        try {
            this.bx = new c(this);
        } catch (Exception e) {
            this.bx = null;
        }
        synchronized (this) {
            this.bA = new Handler(this) {
                final /* synthetic */ l a;

                {
                    this.a = r1;
                }

                public void handleMessage(Message message) {
                    if (ab.gv) {
                        switch (message.what) {
                            case 1:
                                try {
                                    this.a.v();
                                    return;
                                } catch (Exception e) {
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                }
            };
            this.bX = System.currentTimeMillis();
            this.bI = (AlarmManager) context.getSystemService("alarm");
            this.b8 = new a(this);
            context.registerReceiver(this.b8, new IntentFilter(bM));
            this.bJ = PendingIntent.getBroadcast(context, 0, new Intent(bM), 134217728);
            this.bI.set(0, System.currentTimeMillis() + 1000, this.bJ);
            this.bS = c.ad;
            this.bY = new ArrayList();
            this.bV = new ArrayList();
            t();
            this.bE = true;
        }
    }

    private void if(boolean z) {
        String str = c.byte();
        if (str != null) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(str + File.separator + "baidu/tempdata/" + this.bU, "rw");
                if (z) {
                    randomAccessFile.seek(0);
                    randomAccessFile.writeLong(System.currentTimeMillis());
                    randomAccessFile.writeInt(2125);
                    this.b2 = 0;
                    this.bQ = System.currentTimeMillis();
                } else {
                    randomAccessFile.seek(12);
                }
                randomAccessFile.writeInt(this.b2);
                randomAccessFile.writeInt(2125);
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static void q() {
        try {
            if (bZ != null) {
                b7 = new File(bZ);
                if (!b7.exists()) {
                    File file = new File(I);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    b7.createNewFile();
                    RandomAccessFile randomAccessFile = new RandomAccessFile(b7, "rw");
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.writeLong(0);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                    return;
                }
                return;
            }
            b7 = null;
        } catch (Exception e) {
            b7 = null;
        }
    }

    public static String s() {
        return null;
    }

    private void t() {
        String str = c.byte();
        if (str != null) {
            long readLong;
            int readInt;
            int i;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(str + File.separator + "baidu/tempdata/" + this.bU, "r");
                randomAccessFile.seek(0);
                readLong = randomAccessFile.readLong();
                try {
                    if (randomAccessFile.readInt() == 2125) {
                        readInt = randomAccessFile.readInt();
                        try {
                            i = randomAccessFile.readInt() == 2125 ? 1 : 0;
                        } catch (Exception e) {
                            i = 0;
                        }
                    } else {
                        readInt = 0;
                        i = 0;
                    }
                    try {
                        randomAccessFile.close();
                    } catch (Exception e2) {
                    }
                } catch (Exception e3) {
                    readInt = 0;
                    i = 0;
                }
            } catch (Exception e4) {
                readLong = 0;
                readInt = 0;
                i = 0;
            }
            if (i != 0) {
                this.b2 = readInt;
                this.bQ = readLong;
                return;
            }
            this.b2 = 0;
            this.bQ = 0;
        }
    }

    boolean if(double d, double d2) {
        return ((-2.1971522d * d) + (-0.70587059d * d2)) + 0.8428018d > 0.0d;
    }

    public boolean r() {
        return ((KeyguardManager) this.bL.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    public synchronized void u() {
        this.bE = false;
        if (this.b8 != null) {
            this.bL.unregisterReceiver(this.b8);
        }
        this.b8 = null;
        if (!(this.bI == null || this.bJ == null)) {
            this.bI.cancel(this.bJ);
        }
        this.bI = null;
        this.bJ = null;
        bD = null;
        this.bY.clear();
        this.bV.clear();
        this.bY = null;
        this.bV = null;
        this.b6 = 0;
        this.b0 = 0;
        this.bz = "";
        this.bB = false;
    }

    void v() {
        if (this.bE) {
            long currentTimeMillis = this.b0 != 0 ? (System.currentTimeMillis() - this.b0) + 30000 : 0;
            this.b0 = System.currentTimeMillis();
            String str = c.byte();
            if (str == null) {
                this.bI.set(0, System.currentTimeMillis() + c.aQ, this.bJ);
                return;
            }
            com.baidu.location.t.a ak = t.an().ak();
            if (ak == null) {
                this.bI.set(0, System.currentTimeMillis() + c.aQ, this.bJ);
                return;
            }
            int size;
            b b1 = ar.bW().b1();
            Object obj = null;
            if (this.b6 == 0) {
                obj = 1;
                this.bY.clear();
                this.bV.clear();
            }
            Object obj2 = obj;
            int i = 0;
            if (obj2 == null) {
                size = this.bV.size();
                if (size > 0 && ak.a((com.baidu.location.t.a) this.bV.get(size - 1)) && this.bY.size() >= size) {
                    b bVar = (b) this.bY.get(size - 1);
                    if (!if(ar.if(b1, bVar), new b(this, bVar).a(new b(this, b1)))) {
                        i = -1;
                    }
                }
            }
            obj = null;
            if (i < 0) {
                obj = 1;
            }
            if (obj == null) {
                if (System.currentTimeMillis() - this.bQ > com.umeng.analytics.a.h || System.currentTimeMillis() - this.bQ < 0) {
                    this.b2 = 0;
                    if(true);
                } else {
                    this.b2++;
                    if(false);
                }
                if (this.b2 > c.a5) {
                    this.bO = (this.bQ + com.umeng.analytics.a.h) - System.currentTimeMillis();
                }
            }
            if (this.bO > 900000) {
                this.bS = this.bO;
                this.bI.set(0, System.currentTimeMillis() + this.bS, this.bJ);
                this.bO = 0;
            } else if (i < 0) {
                this.bS += c.ao;
                if (b1 == null || b1.for == null || b1.for.size() == 0) {
                    if (this.bS > c.aK) {
                        this.bS = c.aK;
                    }
                } else if (this.bS > c.aQ) {
                    this.bS = c.aQ;
                }
                this.bI.set(0, System.currentTimeMillis() + this.bS, this.bJ);
                this.bB = true;
            } else {
                this.bS = c.ad;
                this.bI.set(0, System.currentTimeMillis() + this.bS, this.bJ);
                if (System.currentTimeMillis() - this.b6 > 840000) {
                    this.bY.clear();
                    this.bV.clear();
                }
            }
            this.b6 = System.currentTimeMillis();
            if (obj != null) {
                q.x().z();
                return;
            }
            String str2;
            StringBuffer stringBuffer = new StringBuffer(200);
            if (obj2 != null) {
                stringBuffer.append("s");
            }
            stringBuffer.append("v");
            stringBuffer.append(4);
            int currentTimeMillis2 = (int) (System.currentTimeMillis() >> 15);
            stringBuffer.append("t");
            stringBuffer.append(currentTimeMillis2);
            if (ak.for()) {
                if (ak.do == 460) {
                    stringBuffer.append("x,");
                } else {
                    stringBuffer.append("x");
                    stringBuffer.append(ak.do);
                    stringBuffer.append(",");
                }
                stringBuffer.append(ak.if);
                stringBuffer.append(",");
                stringBuffer.append(ak.for);
                stringBuffer.append(",");
                stringBuffer.append(ak.try);
            }
            String b4 = ar.bW().b4();
            int i2 = 0;
            obj2 = null;
            String str3 = null;
            if (!(b1 == null || b1.for == null)) {
                int i3;
                size = 0;
                while (size < b1.for.size()) {
                    Object obj3;
                    String replace = ((ScanResult) b1.for.get(size)).BSSID.replace(":", "");
                    int i4 = ((ScanResult) b1.for.get(size)).level;
                    int i5 = i4 < 0 ? -i4 : i4;
                    String str4;
                    if (i2 >= 3) {
                        obj = obj2;
                        i3 = i2;
                        if (i3 > 2) {
                            break;
                        }
                        str4 = str3;
                        obj3 = obj;
                        str2 = str4;
                    } else if (size < 2 || obj2 != null || b4 == null || b4.equals(replace)) {
                        if (size == 0) {
                            stringBuffer.append("w");
                        } else {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append(replace);
                        if (b4 != null && b4.equals(replace)) {
                            obj = ((ScanResult) b1.for.get(size)).capabilities;
                            if (TextUtils.isEmpty(obj)) {
                                stringBuffer.append("j");
                            } else {
                                str2 = obj.toUpperCase(Locale.CHINA);
                                if (str2.contains("WEP") || str2.contains("WPA")) {
                                    stringBuffer.append("l");
                                } else {
                                    stringBuffer.append("j");
                                }
                            }
                            obj2 = 1;
                        }
                        stringBuffer.append(DnspodFree.IP_SPLIT + i5);
                        obj = obj2;
                        i3 = i2 + 1;
                        if (i3 > 2) {
                            break;
                        }
                        str4 = str3;
                        obj3 = obj;
                        str2 = str4;
                    } else if (str3 == null) {
                        str2 = "," + replace + DnspodFree.IP_SPLIT + i5;
                        obj3 = obj2;
                        i3 = i2;
                    } else {
                        str2 = str3;
                        obj3 = obj2;
                        i3 = i2;
                    }
                    size++;
                    i2 = i3;
                    obj2 = obj3;
                    str3 = str2;
                }
                i3 = i2;
                if (i3 < 3 && str3 != null) {
                    stringBuffer.append(str3);
                }
            }
            try {
                if (r()) {
                    str2 = "y2";
                } else {
                    str2 = "y1";
                    ad.cM().goto(currentTimeMillis2);
                }
            } catch (Exception e) {
                str2 = "y";
            }
            if (aw.do().a() != null) {
                str2 = str2 + aw.do().a();
            }
            stringBuffer.append(str2);
            if (this.bB) {
                if (currentTimeMillis > 0) {
                    this.bz = "r" + (currentTimeMillis / 60000);
                    stringBuffer.append(this.bz);
                    this.bz = "";
                }
                this.bB = false;
            }
            System.currentTimeMillis();
            Jni.int(str, stringBuffer.toString());
            this.bY.add(b1);
            while (this.bY.size() > 3) {
                this.bY.remove(0);
            }
            this.bV.add(ak);
            while (this.bV.size() > 3) {
                this.bV.remove(0);
            }
            q.x().z();
        }
    }
}
