package com.boohee.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;

import com.boohee.one.R;

public class LocationUtils {
    public static final  String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";
    private static final String TAG                        = LocationUtils.class.getSimpleName();

    public static boolean isWIFIProviderAvaliable(Context context) {
        return isLocationProviderAvaliable(context.getContentResolver(), "network");
    }

    public static boolean isGPSProviderAvaliable(Context context) {
        return isLocationProviderAvaliable(context.getContentResolver(), "gps");
    }

    private static boolean isLocationProviderAvaliable(ContentResolver cr, String provider) {
        String allowedProviders = Secure.getString(cr, LOCATION_PROVIDERS_ALLOWED);
        Helper.showLog(TAG, "isLocationProviderEnabled. allowedProviders: " + allowedProviders);
        if (allowedProviders == null) {
            return false;
        }
        if (allowedProviders.equals(provider) || allowedProviders.contains("," + provider + ",")
                || allowedProviders.startsWith(provider + ",") || allowedProviders.endsWith("," +
                provider)) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService
                    ("connectivity")).getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String getProvider(Context ctx, LocationManager mLocationManager) {
        String mProviderName = "";
        if (isNetworkConnected(ctx)) {
            boolean hasGpsProvide;
            boolean hasNetWorkProvider;
            if (mLocationManager.getProvider("gps") != null) {
                hasGpsProvide = true;
            } else {
                hasGpsProvide = false;
            }
            if (mLocationManager.getProvider("network") != null) {
                hasNetWorkProvider = true;
            } else {
                hasNetWorkProvider = false;
            }
            Helper.showLog(TAG, " hasGpsProvide =" + hasGpsProvide + " hasNetWorkProvider =" +
                    hasNetWorkProvider);
            boolean isGpsEnabled = isGPSProviderAvaliable(ctx);
            boolean isWIFIEnabled = isWIFIProviderAvaliable(ctx);
            if (!hasGpsProvide && !hasNetWorkProvider) {
                Helper.showToast(ctx, (int) R.string.xw);
                return ctx.getString(R.string.xw);
            } else if (!hasGpsProvide && hasNetWorkProvider) {
                Helper.showLog(TAG, ">>>>>>>>>>>>>>>only network provider");
                if (isWIFIEnabled) {
                    mProviderName = "network";
                } else {
                    Helper.showLog(TAG, ">>>>>>>>>>>>>>>no network avaliable");
                    return null;
                }
            } else if (!hasGpsProvide || hasNetWorkProvider) {
                Helper.showLog(TAG, ">>>>>>>>>>>>>>>hasallprovider");
                if (!isGpsEnabled && !isWIFIEnabled) {
                    Helper.showLog(TAG, ">>>>>>>>>>>>>>>no GPS or NETWORK avaliable");
                    return null;
                } else if (isGpsEnabled && !isWIFIEnabled) {
                    mProviderName = "gps";
                } else if (!isGpsEnabled && isWIFIEnabled) {
                    Helper.showLog(TAG, ">>>>>>>>>>>>>>>network avaliable");
                    mProviderName = "network";
                } else if (isGpsEnabled && isWIFIEnabled) {
                    Helper.showLog(TAG, ">>>>>>>>>>>>>>>all avaliable");
                    mProviderName = "gps";
                    if (mLocationManager.getLastKnownLocation("gps") == null) {
                        Helper.showLog(TAG, ">>>>>>>>>>>>>>>all avaliable but location is null");
                        mProviderName = "network";
                    }
                }
            } else {
                Helper.showLog(TAG, ">>>>>>>>>>>>>>>only GPS provider");
                if (isGpsEnabled) {
                    mProviderName = "gps";
                } else {
                    Helper.showLog(TAG, ">>>>>>>>>>>>>>>no GPS avaliable");
                    return null;
                }
            }
        }
        Helper.showToast(ctx, ctx.getString(R.string.gu));
        return mProviderName;
    }
}
