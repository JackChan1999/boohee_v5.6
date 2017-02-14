package com.baidu.location;

import com.boohee.one.BuildConfig;

public final class LocationClientOption {
    public static final int MIN_SCAN_SPAN = 1000;
    public static final int MIN_SCAN_SPAN_NETWORK = 3000;
    protected static final int byte = 1;
    protected static final int try = 3;
    protected static final int void = 2;
    protected boolean a = true;
    protected boolean b = false;
    protected float c = 500.0f;
    protected String case = "com.baidu.location.service_v2.9";
    protected boolean char = false;
    protected int d = ax.F;
    protected String do = BDGeofence.COORD_TYPE_GCJ;
    protected boolean e = false;
    protected String else = "detail";
    protected LocationMode f;
    protected boolean for = false;
    protected boolean g = false;
    protected boolean goto = false;
    protected int h = 1;
    protected String if = "SDK2.0";
    protected int int = 0;
    protected int long = 3;
    protected boolean new = false;

    public enum LocationMode {
        Hight_Accuracy,
        Battery_Saving,
        Device_Sensors
    }

    public LocationClientOption(LocationClientOption locationClientOption) {
        this.do = locationClientOption.do;
        this.else = locationClientOption.else;
        this.for = locationClientOption.for;
        this.int = locationClientOption.int;
        this.d = locationClientOption.d;
        this.if = locationClientOption.if;
        this.h = locationClientOption.h;
        this.goto = locationClientOption.goto;
        this.e = locationClientOption.e;
        this.c = locationClientOption.c;
        this.long = locationClientOption.long;
        this.case = locationClientOption.case;
        this.a = locationClientOption.a;
        this.b = locationClientOption.b;
        this.char = locationClientOption.char;
        this.g = locationClientOption.g;
        this.f = locationClientOption.f;
    }

    public void SetIgnoreCacheException(boolean z) {
        this.b = z;
    }

    protected boolean a() {
        return this.a;
    }

    public boolean equals(LocationClientOption locationClientOption) {
        return this.do.equals(locationClientOption.do) && this.else.equals(locationClientOption.else) && this.for == locationClientOption.for && this.int == locationClientOption.int && this.d == locationClientOption.d && this.if.equals(locationClientOption.if) && this.goto == locationClientOption.goto && this.h == locationClientOption.h && this.long == locationClientOption.long && this.e == locationClientOption.e && this.c == locationClientOption.c && this.a == locationClientOption.a && this.b == locationClientOption.b && this.char == locationClientOption.char && this.g == locationClientOption.g && this.f == locationClientOption.f;
    }

    public String getAddrType() {
        return this.else;
    }

    public String getCoorType() {
        return this.do;
    }

    public LocationMode getLocationMode() {
        return this.f;
    }

    public String getProdName() {
        return this.if;
    }

    public int getScanSpan() {
        return this.int;
    }

    public int getTimeOut() {
        return this.d;
    }

    public boolean isLocationNotify() {
        return this.goto;
    }

    public boolean isOpenGps() {
        return this.for;
    }

    public void setCoorType(String str) {
        String toLowerCase = str.toLowerCase();
        if (toLowerCase.equals(BDGeofence.COORD_TYPE_GCJ) || toLowerCase.equals(BDGeofence.COORD_TYPE_BD09) || toLowerCase.equals(BDGeofence.COORD_TYPE_BD09LL)) {
            this.do = toLowerCase;
        }
    }

    public void setIgnoreKillProcess(boolean z) {
        this.char = z;
    }

    public void setIsNeedAddress(boolean z) {
        if (z) {
            this.else = BuildConfig.PLATFORM;
            this.h = 1;
        }
    }

    public void setLocationMode(LocationMode locationMode) {
        switch (locationMode) {
            case Hight_Accuracy:
                this.for = true;
                break;
            case Battery_Saving:
                this.for = false;
                break;
            case Device_Sensors:
                this.h = 3;
                this.for = true;
                break;
            default:
                throw new IllegalArgumentException("Illegal this mode : " + locationMode);
        }
        this.f = locationMode;
    }

    public void setLocationNotify(boolean z) {
        this.goto = z;
    }

    public void setNeedDeviceDirect(boolean z) {
        this.g = z;
    }

    public void setOpenGps(boolean z) {
        this.for = z;
    }

    public void setProdName(String str) {
        if (str.length() > 64) {
            str = str.substring(0, 64);
        }
        this.if = str;
    }

    public void setScanSpan(int i) {
        this.int = i;
    }

    public void setTimeOut(int i) {
        this.d = i;
    }
}
