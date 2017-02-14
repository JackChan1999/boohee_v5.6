package com.baidu.location;

public abstract class BDNotifyListener {
    public int Notified = 0;
    public float differDistance = 0.0f;
    public boolean isAdded = false;
    public String mCoorType = null;
    public double mLatitude = Double.MIN_VALUE;
    public double mLatitudeC = Double.MIN_VALUE;
    public double mLongitude = Double.MIN_VALUE;
    public double mLongitudeC = Double.MIN_VALUE;
    public z mNotifyCache = null;
    public float mRadius = 0.0f;

    public void SetNotifyLocation(double d, double d2, float f, String str) {
        this.mLatitude = d;
        this.mLongitude = d2;
        if (f < 0.0f) {
            this.mRadius = 200.0f;
        } else {
            this.mRadius = f;
        }
        if (str.equals(BDGeofence.COORD_TYPE_GCJ) || str.equals(BDGeofence.COORD_TYPE_BD09) || str.equals(BDGeofence.COORD_TYPE_BD09LL) || str.equals("gps")) {
            this.mCoorType = str;
        } else {
            this.mCoorType = BDGeofence.COORD_TYPE_GCJ;
        }
        if (this.mCoorType.equals(BDGeofence.COORD_TYPE_GCJ)) {
            this.mLatitudeC = this.mLatitude;
            this.mLongitudeC = this.mLongitude;
        }
        if (this.isAdded) {
            this.Notified = 0;
            this.mNotifyCache.if(this);
        }
    }

    public void onNotify(BDLocation bDLocation, float f) {
        c.do(ax.i, "new location, not far from the destination..." + f);
    }
}
