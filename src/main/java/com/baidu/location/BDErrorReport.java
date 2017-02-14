package com.baidu.location;

import android.os.Bundle;
import java.util.Locale;

public abstract class BDErrorReport {
    private String a = null;

    public BDErrorReport(BDErrorReport bDErrorReport) {
        this.a = bDErrorReport.a;
    }

    public Bundle getErrorInfo() {
        if (this.a == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("errInfo", this.a);
        return bundle;
    }

    public abstract void onReportResult(boolean z);

    public int setErrorInfo(boolean z, double d, double d2, boolean z2, String str, double d3, double d4, String str2, String str3) {
        StringBuffer stringBuffer = new StringBuffer(1024);
        stringBuffer.append("&report=1");
        if (z) {
            stringBuffer.append(String.format(Locale.CHINA, "&ell=%.5f|%.5f", new Object[]{Double.valueOf(d), Double.valueOf(d2)}));
        }
        if (z2) {
            if (str == null || (!str.equals(BDGeofence.COORD_TYPE_GCJ) && !str.equals(BDGeofence.COORD_TYPE_BD09) && !str.equals(BDGeofence.COORD_TYPE_BD09LL) && !str.equals("gps"))) {
                return 1;
            }
            stringBuffer.append(String.format(Locale.CHINA, "&ugc=%.5f|%.5f", new Object[]{Double.valueOf(d3), Double.valueOf(d4)}));
            stringBuffer.append("&ucoord=");
            stringBuffer.append(str);
        }
        if (str2 != null) {
            if (str2.length() > 60) {
                str2 = str2.substring(0, 60);
            }
            stringBuffer.append("&ver=");
            stringBuffer.append(str2);
        }
        if (str3 != null) {
            if (str3.length() > 512) {
                str3 = str3.substring(0, 512);
            }
            stringBuffer.append("&erInfo=");
            stringBuffer.append(str3);
        }
        this.a = stringBuffer.toString();
        return 0;
    }
}
