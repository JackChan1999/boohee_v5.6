package com.umeng.socialize.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class SocializeLocationListener implements LocationListener {
    private DefaultLocationProvider mLocationProvider;

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onProviderDisabled(String str) {
    }

    public void onLocationChanged(Location location) {
        if (this.mLocationProvider != null) {
            this.mLocationProvider.setLocation(location);
            this.mLocationProvider.getLocationManager().removeUpdates(this);
        }
    }

    public void setLocationProvider(DefaultLocationProvider defaultLocationProvider) {
        this.mLocationProvider = defaultLocationProvider;
    }
}
