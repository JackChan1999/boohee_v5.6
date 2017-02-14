package com.baidu.location;

import android.location.Location;

class j implements ax, b {
    private static j kL = null;
    double kH = 0.0d;
    double kI = 0.0d;
    boolean kJ = false;
    volatile int kK = -1;
    double kM = 0.0d;
    int kN = -1;
    long kO = 0;
    double kP = 0.0d;

    private j() {
    }

    public static j cZ() {
        if (kL == null) {
            kL = new j();
        }
        return kL;
    }

    public void byte(BDLocation bDLocation) {
        if (!this.kJ || System.currentTimeMillis() - this.kO > 4000 || bDLocation == null || bDLocation.getLocType() != 161) {
            return;
        }
        if ("wf".equals(bDLocation.getNetworkLocationType()) || bDLocation.getRadius() < 300.0f) {
            this.kH = bDLocation.getLongitude();
            this.kI = bDLocation.getLatitude();
            float[] fArr = new float[1];
            Location.distanceBetween(this.kM, this.kP, this.kI, this.kH, fArr);
            this.kK = (int) fArr[0];
            this.kJ = false;
        }
    }

    public String cY() {
        Object obj = 1;
        if (this.kN < 0 && this.kK < 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(128);
        Object obj2 = null;
        if (this.kN >= 0) {
            stringBuffer.append("&osr=");
            stringBuffer.append(this.kN);
            this.kN = -1;
            obj2 = 1;
        }
        if (this.kK >= 0) {
            stringBuffer.append("&oac=");
            stringBuffer.append(this.kK);
            this.kK = -2;
        } else {
            obj = obj2;
        }
        return obj != null ? stringBuffer.toString() : null;
    }

    public void if(boolean z, boolean z2, double d, double d2) {
        if (this.kN < 0) {
            this.kN = 0;
        }
        if (z) {
            this.kN |= 1;
        }
        if (z2) {
            this.kN |= 2;
            this.kP = d;
            this.kM = d2;
            this.kJ = true;
            this.kO = System.currentTimeMillis();
        }
    }
}
