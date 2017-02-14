package com.baidu.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.baidu.location.b.a.a;
import com.boohee.one.BuildConfig;
import com.tencent.connect.common.Constants;

class az implements ax, n, SensorEventListener {
    public static String iH = null;
    public static String iM = null;
    private static az iQ = null;
    int iI = -1;
    SensorManager iJ = null;
    int iK = -1;
    String iL = null;
    String iN = null;
    private Sensor iO;
    String iP = null;

    private az() {
        try {
            this.iL = ((TelephonyManager) f.getServiceContext().getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            this.iL = "NULL";
        }
        try {
            this.iP = a.if(f.getServiceContext());
        } catch (Exception e2) {
            this.iP = null;
        }
        try {
            iH = f.getServiceContext().getPackageName();
        } catch (Exception e3) {
            iH = null;
        }
    }

    public static az cn() {
        if (iQ == null) {
            iQ = new az();
        }
        return iQ;
    }

    public String char(boolean z) {
        return if(z, null);
    }

    public String cj() {
        return iH != null ? ck() + "|" + iH : ck();
    }

    public String ck() {
        return this.iP != null ? "v4.2|" + this.iP + "|" + Build.MODEL : "v4.2" + this.iL + "|" + Build.MODEL;
    }

    public String cl() {
        return "&sdk=4.2" + co();
    }

    public synchronized void cm() {
        try {
            if (ab.gv) {
                this.iJ = (SensorManager) f.getServiceContext().getSystemService("sensor");
                this.iJ.registerListener(this, this.iJ.getDefaultSensor(5), 3);
            }
        } catch (Exception e) {
        }
    }

    public String co() {
        StringBuffer stringBuffer = new StringBuffer(200);
        if (this.iP != null) {
            stringBuffer.append("&cu=");
            stringBuffer.append(this.iP);
        } else {
            stringBuffer.append("&im=");
            stringBuffer.append(this.iL);
        }
        try {
            stringBuffer.append("&mb=");
            stringBuffer.append(Build.MODEL);
        } catch (Exception e) {
        }
        stringBuffer.append("&pack=");
        try {
            stringBuffer.append(iH);
        } catch (Exception e2) {
        }
        stringBuffer.append("&sdk=");
        stringBuffer.append(n.V);
        return stringBuffer.toString();
    }

    public synchronized void cp() {
        if (this.iJ != null) {
            this.iJ.unregisterListener(this);
        }
        this.iJ = null;
    }

    public String if(boolean z, String str) {
        StringBuffer stringBuffer = new StringBuffer(256);
        stringBuffer.append("&sdk=");
        stringBuffer.append(n.V);
        if (z && c.aw.equals(BuildConfig.PLATFORM)) {
            stringBuffer.append("&addr=all");
        }
        if (z) {
            if (str == null) {
                stringBuffer.append("&coor=gcj02");
            } else {
                stringBuffer.append("&coor=");
                stringBuffer.append(str);
            }
        }
        if (this.iP == null) {
            stringBuffer.append("&im=");
            stringBuffer.append(this.iL);
        } else {
            stringBuffer.append("&cu=");
            stringBuffer.append(this.iP);
        }
        stringBuffer.append("&fw=");
        stringBuffer.append(f.getFrameVersion());
        stringBuffer.append("&lt=1");
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        if (this.iK != -1) {
            stringBuffer.append("&al=");
            stringBuffer.append(this.iK);
        }
        stringBuffer.append("&resid=");
        stringBuffer.append(Constants.VIA_REPORT_TYPE_SET_AVATAR);
        stringBuffer.append("&os=A");
        stringBuffer.append(VERSION.SDK);
        if (z) {
            stringBuffer.append("&sv=");
            String str2 = VERSION.RELEASE;
            if (str2 != null && str2.length() > 6) {
                str2 = str2.substring(0, 6);
            }
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == 5) {
            this.iK = (int) sensorEvent.values[0];
        } else if (type == 8) {
            this.iI = (int) sensorEvent.values[0];
        }
    }

    public String s(String str) {
        return if(true, str);
    }

    public void try(String str, String str2) {
        iM = str;
        iH = str2;
    }
}
