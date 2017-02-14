package com.umeng.socialize.bean;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.umeng.socialize.common.SocializeConstants;

public class UMLocation implements Parcelable {
    public static final Creator<UMLocation> CREATOR = new ao();
    private double a;
    private double b;

    public UMLocation(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public double getLatitude() {
        return this.a;
    }

    public void setLatitude(double d) {
        this.a = d;
    }

    public double getLongitude() {
        return this.b;
    }

    public void setLongitude(double d) {
        this.b = d;
    }

    public String toString() {
        return SocializeConstants.OP_OPEN_PAREN + this.b + "," + this.a + SocializeConstants
                .OP_CLOSE_PAREN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.a);
        parcel.writeDouble(this.b);
    }

    private UMLocation(Parcel parcel) {
        this.a = parcel.readDouble();
        this.b = parcel.readDouble();
    }

    public static UMLocation build(String str) {
        try {
            String[] split = str.substring(1, str.length() - 1).split(",");
            return new UMLocation(Double.parseDouble(split[1]), Double.parseDouble(split[0]));
        } catch (Exception e) {
            return null;
        }
    }

    public static UMLocation build(Location location) {
        try {
            if (!(location.getLatitude() == 0.0d || location.getLongitude() == 0.0d)) {
                return new UMLocation(location.getLatitude(), location.getLongitude());
            }
        } catch (Exception e) {
        }
        return null;
    }
}
