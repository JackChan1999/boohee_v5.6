package com.umeng.socialize.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.umeng.socialize.utils.DeviceConfig;

public class SocializeLocationManager {
    LocationManager mLocationManager = null;

    public void init(Context context) {
        if (DeviceConfig.checkPermission(context, "android.permission.ACCESS_FINE_LOCATION") ||
                DeviceConfig.checkPermission(context, "android.permission" +
                        ".ACCESS_COARSE_LOCATION")) {
            this.mLocationManager = (LocationManager) context.getApplicationContext()
                    .getSystemService("location");
        }
    }

    public String getBestProvider(Criteria criteria, boolean z) {
        return this.mLocationManager == null ? null : this.mLocationManager.getBestProvider
                (criteria, z);
    }

    public Location getLastKnownLocation(String str) {
        return this.mLocationManager == null ? null : this.mLocationManager.getLastKnownLocation
                (str);
    }

    public boolean isProviderEnabled(String str) {
        return this.mLocationManager == null ? false : this.mLocationManager.isProviderEnabled(str);
    }

    public void requestLocationUpdates(Activity activity, String str, long j, float f,
                                       LocationListener locationListener) {
        if (this.mLocationManager != null) {
            final String str2 = str;
            final long j2 = j;
            final float f2 = f;
            final LocationListener locationListener2 = locationListener;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    SocializeLocationManager.this.mLocationManager.requestLocationUpdates(str2,
                            j2, f2, locationListener2);
                }
            });
        }
    }

    public void removeUpdates(LocationListener locationListener) {
        if (this.mLocationManager != null) {
            this.mLocationManager.removeUpdates(locationListener);
        }
    }
}
