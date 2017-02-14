package com.umeng.socialize.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.text.TextUtils;

import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;

public class DefaultLocationProvider implements SocializeLocationProvider {
    private static final String TAG = DefaultLocationProvider.class.getName();
    private Context mContext;
    private SocializeLocationListener mListener = null;
    private Location                 mLocation;
    private SocializeLocationManager mLocationManager;
    private String                   mProvider;

    public void init(Context context) {
        this.mContext = context;
        this.mListener = new SocializeLocationListener();
        getLocation();
    }

    public void destroy() {
        if (this.mLocationManager != null && this.mListener != null) {
            this.mLocationManager.removeUpdates(this.mListener);
        }
    }

    public Location getLocation() {
        if (this.mLocation == null) {
            if (DeviceConfig.checkPermission(this.mContext, "android.permission" +
                    ".ACCESS_FINE_LOCATION")) {
                requestLocation(this.mContext, 1);
            } else if (DeviceConfig.checkPermission(this.mContext, "android.permission" +
                    ".ACCESS_COARSE_LOCATION")) {
                requestLocation(this.mContext, 2);
            }
        }
        return this.mLocation;
    }

    private void requestLocation(Context context, int i) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(i);
        String bestProvider = this.mLocationManager.getBestProvider(criteria, true);
        if (bestProvider != null) {
            this.mProvider = bestProvider;
        }
        Log.d(TAG, "Get location from " + this.mProvider);
        try {
            if (!TextUtils.isEmpty(this.mProvider)) {
                Location lastKnownLocation = this.mLocationManager.getLastKnownLocation(this
                        .mProvider);
                if (lastKnownLocation != null) {
                    this.mLocation = lastKnownLocation;
                } else if (this.mLocationManager.isProviderEnabled(this.mProvider) && this
                        .mListener != null && (context instanceof Activity)) {
                    this.mLocationManager.requestLocationUpdates((Activity) context, this
                            .mProvider, 1, 0.0f, this.mListener);
                }
            }
        } catch (IllegalArgumentException e) {
        }
    }

    public void setLocationManager(SocializeLocationManager socializeLocationManager) {
        this.mLocationManager = socializeLocationManager;
    }

    protected SocializeLocationManager getLocationManager() {
        return this.mLocationManager;
    }

    protected void setLocation(Location location) {
        this.mLocation = location;
    }

    public void setProvider(String str) {
        this.mProvider = str;
    }
}
