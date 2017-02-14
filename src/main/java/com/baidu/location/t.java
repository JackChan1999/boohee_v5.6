package com.baidu.location;

import android.os.Build.VERSION;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.boohee.one.http.DnspodFree;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

class t implements ax, n {
    private static t d2 = null;
    private static String d4 = null;
    private static Method d5 = null;
    private static boolean d6 = false;
    private static Method d8 = null;
    private static long d9 = 3000;
    private static Method dZ = null;
    private static int eb = 3;
    private static Class ec = null;
    private b d0 = null;
    private List d1 = null;
    private TelephonyManager d3 = null;
    private int d7 = 0;
    private a dX = new a(this);
    private int dY = 0;
    private boolean ea = false;

    public class a {
        final /* synthetic */ t a;
        public long byte;
        public int do;
        public int for;
        public int if;
        public int int;
        public char new;
        public int try;

        public a(t tVar) {
            this.a = tVar;
            this.for = -1;
            this.try = -1;
            this.do = -1;
            this.if = -1;
            this.byte = 0;
            this.int = -1;
            this.new = '\u0000';
            this.byte = System.currentTimeMillis();
        }

        public a(t tVar, int i, int i2, int i3, int i4, char c) {
            this.a = tVar;
            this.for = -1;
            this.try = -1;
            this.do = -1;
            this.if = -1;
            this.byte = 0;
            this.int = -1;
            this.new = '\u0000';
            this.for = i;
            this.try = i2;
            this.do = i3;
            this.if = i4;
            this.new = c;
            this.byte = System.currentTimeMillis() / 1000;
        }

        public String a() {
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append(this.try + 23);
            stringBuffer.append("H");
            stringBuffer.append(this.for + 45);
            stringBuffer.append("K");
            stringBuffer.append(this.if + 54);
            stringBuffer.append("Q");
            stringBuffer.append(this.do + 203);
            return stringBuffer.toString();
        }

        public boolean a(a aVar) {
            return this.for == aVar.for && this.try == aVar.try && this.if == aVar.if;
        }

        public boolean do() {
            return System.currentTimeMillis() - this.byte < t.d9;
        }

        public boolean for() {
            return this.for > -1 && this.try > 0;
        }

        public String if() {
            StringBuffer stringBuffer = new StringBuffer(64);
            stringBuffer.append(String.format(Locale.CHINA, "cell=%d|%d|%d|%d:%d", new Object[]{Integer.valueOf(this.do), Integer.valueOf(this.if), Integer.valueOf(this.for), Integer.valueOf(this.try), Integer.valueOf(this.int)}));
            return stringBuffer.toString();
        }

        public String int() {
            try {
                List<NeighboringCellInfo> neighboringCellInfo = this.a.d3.getNeighboringCellInfo();
                if (neighboringCellInfo == null || neighboringCellInfo.isEmpty()) {
                    return null;
                }
                String str = "&nc=";
                int i = 0;
                for (NeighboringCellInfo neighboringCellInfo2 : neighboringCellInfo) {
                    String str2;
                    if (i != 0) {
                        if (i >= 8) {
                            break;
                        }
                        str2 = neighboringCellInfo2.getLac() != this.for ? str + DnspodFree.IP_SPLIT + neighboringCellInfo2.getLac() + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi() : str + DnspodFree.IP_SPLIT + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi();
                    } else {
                        str2 = neighboringCellInfo2.getLac() != this.for ? str + neighboringCellInfo2.getLac() + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi() : str + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi();
                    }
                    i++;
                    str = str2;
                }
                return str;
            } catch (Exception e) {
                return null;
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append("&nw=");
            stringBuffer.append(this.a.dX.new);
            stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d&cl_s=%d", new Object[]{Integer.valueOf(this.do), Integer.valueOf(this.if), Integer.valueOf(this.for), Integer.valueOf(this.try), Integer.valueOf(this.int)}));
            stringBuffer.append("&cl_t=");
            stringBuffer.append(this.byte);
            if (this.a.d1 != null && this.a.d1.size() > 0) {
                int size = this.a.d1.size();
                stringBuffer.append("&clt=");
                for (int i = 0; i < size; i++) {
                    a aVar = (a) this.a.d1.get(i);
                    if (aVar.do != this.do) {
                        stringBuffer.append(aVar.do);
                    }
                    stringBuffer.append("|");
                    if (aVar.if != this.if) {
                        stringBuffer.append(aVar.if);
                    }
                    stringBuffer.append("|");
                    if (aVar.for != this.for) {
                        stringBuffer.append(aVar.for);
                    }
                    stringBuffer.append("|");
                    if (aVar.try != this.try) {
                        stringBuffer.append(aVar.try);
                    }
                    stringBuffer.append("|");
                    if (i != size - 1) {
                        stringBuffer.append(aVar.byte / 1000);
                    } else {
                        stringBuffer.append((System.currentTimeMillis() - aVar.byte) / 1000);
                    }
                    stringBuffer.append(DnspodFree.IP_SPLIT);
                }
            }
            if (this.a.d7 > 100) {
                this.a.d7 = 0;
            }
            stringBuffer.append("&cs=" + ((this.a.dY << 8) + this.a.d7));
            return stringBuffer.toString();
        }
    }

    private class b extends PhoneStateListener {
        final /* synthetic */ t a;

        public b(t tVar) {
            this.a = tVar;
        }

        public void onCellLocationChanged(CellLocation cellLocation) {
            if (cellLocation != null) {
                try {
                    this.a.if(this.a.d3.getCellLocation());
                } catch (Exception e) {
                }
            }
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (this.a.dX == null) {
                return;
            }
            if (this.a.dX.new == 'g') {
                this.a.dX.int = signalStrength.getGsmSignalStrength();
            } else if (this.a.dX.new == 'c') {
                this.a.dX.int = signalStrength.getCdmaDbm();
            }
        }
    }

    private t() {
    }

    private boolean al() {
        if (d4 == null || d4.length() < 10) {
            return false;
        }
        try {
            char[] toCharArray = d4.toCharArray();
            int i = 0;
            while (i < 10) {
                if (toCharArray[i] > '9' || toCharArray[i] < '0') {
                    return false;
                }
                i++;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static t an() {
        if (d2 == null) {
            d2 = new t();
        }
        return d2;
    }

    private void if(CellLocation cellLocation) {
        if (cellLocation != null && this.d3 != null) {
            int intValue;
            if (!d6) {
                d4 = this.d3.getDeviceId();
                d6 = al();
            }
            a aVar = new a(this);
            aVar.byte = System.currentTimeMillis();
            try {
                String networkOperator = this.d3.getNetworkOperator();
                if (networkOperator != null && networkOperator.length() > 0) {
                    if (networkOperator.length() >= 3) {
                        intValue = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                        if (intValue < 0) {
                            intValue = this.dX.do;
                        }
                        aVar.do = intValue;
                    }
                    networkOperator = networkOperator.substring(3);
                    if (networkOperator != null) {
                        char[] toCharArray = networkOperator.toCharArray();
                        intValue = 0;
                        while (intValue < toCharArray.length && Character.isDigit(toCharArray[intValue])) {
                            intValue++;
                        }
                    } else {
                        intValue = 0;
                    }
                    intValue = Integer.valueOf(networkOperator.substring(0, intValue)).intValue();
                    if (intValue < 0) {
                        intValue = this.dX.if;
                    }
                    aVar.if = intValue;
                }
                this.d7 = this.d3.getSimState();
            } catch (Exception e) {
                this.dY = 1;
            }
            if (cellLocation instanceof GsmCellLocation) {
                aVar.for = ((GsmCellLocation) cellLocation).getLac();
                aVar.try = ((GsmCellLocation) cellLocation).getCid();
                aVar.new = 'g';
            } else if (cellLocation instanceof CdmaCellLocation) {
                aVar.new = 'c';
                if (Integer.parseInt(VERSION.SDK) >= 5) {
                    if (ec == null) {
                        try {
                            ec = Class.forName("android.telephony.cdma.CdmaCellLocation");
                            d8 = ec.getMethod("getBaseStationId", new Class[0]);
                            d5 = ec.getMethod("getNetworkId", new Class[0]);
                            dZ = ec.getMethod("getSystemId", new Class[0]);
                        } catch (Exception e2) {
                            ec = null;
                            this.dY = 2;
                            return;
                        }
                    }
                    if (ec != null && ec.isInstance(cellLocation)) {
                        try {
                            intValue = ((Integer) dZ.invoke(cellLocation, new Object[0])).intValue();
                            if (intValue < 0) {
                                intValue = this.dX.if;
                            }
                            aVar.if = intValue;
                            aVar.try = ((Integer) d8.invoke(cellLocation, new Object[0])).intValue();
                            aVar.for = ((Integer) d5.invoke(cellLocation, new Object[0])).intValue();
                        } catch (Exception e3) {
                            this.dY = 3;
                            return;
                        }
                    }
                }
                return;
            }
            if (!aVar.for()) {
                return;
            }
            if (this.dX == null || !this.dX.a(aVar)) {
                this.dX = aVar;
                if (aVar.for()) {
                    if (this.d1 == null) {
                        this.d1 = new LinkedList();
                    }
                    intValue = this.d1.size();
                    a aVar2 = intValue == 0 ? null : (a) this.d1.get(intValue - 1);
                    if (aVar2 == null || aVar2.try != this.dX.try || aVar2.for != this.dX.for) {
                        if (aVar2 != null) {
                            aVar2.byte = this.dX.byte - aVar2.byte;
                        }
                        this.d1.add(this.dX);
                        if (this.d1.size() > eb) {
                            this.d1.remove(0);
                        }
                    }
                } else if (this.d1 != null) {
                    this.d1.clear();
                }
            }
        }
    }

    public a ak() {
        if (!((this.dX != null && this.dX.do() && this.dX.for()) || this.d3 == null)) {
            try {
                if(this.d3.getCellLocation());
            } catch (Exception e) {
            }
        }
        return this.dX;
    }

    public synchronized void am() {
        if (!this.ea) {
            if (ab.gv) {
                this.d3 = (TelephonyManager) f.getServiceContext().getSystemService("phone");
                this.d1 = new LinkedList();
                this.d0 = new b(this);
                if (!(this.d3 == null || this.d0 == null)) {
                    try {
                        this.d3.listen(this.d0, 272);
                    } catch (Exception e) {
                    }
                    d6 = al();
                    c.if(ax.i, "i:" + d4);
                    this.ea = true;
                }
            }
        }
    }

    public String ao() {
        return d4;
    }

    public int ap() {
        return this.d3 == null ? 0 : this.d3.getNetworkType();
    }

    public int aq() {
        String subscriberId = ((TelephonyManager) f.getServiceContext().getSystemService("phone")).getSubscriberId();
        if (subscriberId != null) {
            if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002") || subscriberId.startsWith("46007")) {
                return 1;
            }
            if (subscriberId.startsWith("46001")) {
                return 2;
            }
            if (subscriberId.startsWith("46003")) {
                return 3;
            }
        }
        return 0;
    }

    public synchronized void as() {
        if (this.ea) {
            if (!(this.d0 == null || this.d3 == null)) {
                this.d3.listen(this.d0, 0);
            }
            this.d0 = null;
            this.d3 = null;
            this.d1.clear();
            this.d1 = null;
            this.ea = false;
        }
    }
}
