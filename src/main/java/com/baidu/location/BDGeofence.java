package com.baidu.location;

import android.text.TextUtils;

public interface BDGeofence {
    public static final String COORD_TYPE_BD09 = "bd09";
    public static final String COORD_TYPE_BD09LL = "bd09ll";
    public static final String COORD_TYPE_GCJ = "gcj02";
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int RADIUS_TYPE_SMALL = 1;

    public static final class Builder {
        private int a;
        private String do = null;
        private double for;
        private long if = Long.MIN_VALUE;
        private String int;
        private boolean new = false;
        private double try;

        public BDGeofence build() {
            if (TextUtils.isEmpty(this.do)) {
                throw new IllegalArgumentException("BDGeofence name not set.");
            } else if (!this.new) {
                throw new IllegalArgumentException("BDGeofence region not set.");
            } else if (this.if == Long.MIN_VALUE) {
                throw new IllegalArgumentException("BDGeofence Expiration not set.");
            } else if (!TextUtils.isEmpty(this.int)) {
                return new aq(this.do, this.try, this.for, this.a, this.if, this.int);
            } else {
                throw new IllegalArgumentException("BDGeofence CoordType not set.");
            }
        }

        public Builder setCircularRegion(double d, double d2, int i) {
            this.new = true;
            this.try = d;
            this.for = d2;
            this.a = 1;
            return this;
        }

        public Builder setCoordType(String str) {
            this.int = str;
            return this;
        }

        public Builder setExpirationDruation(long j) {
            if (j < 0) {
                this.if = -1;
            } else {
                this.if = j;
            }
            return this;
        }

        public Builder setGeofenceId(String str) {
            this.do = str;
            return this;
        }
    }

    String getGeofenceId();
}
