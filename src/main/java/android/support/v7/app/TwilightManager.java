package android.support.v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import com.umeng.analytics.a;
import java.util.Calendar;

class TwilightManager {
    private static final int SUNRISE = 6;
    private static final int SUNSET = 22;
    private static final String TAG = "TwilightManager";
    private static final TwilightState sTwilightState = new TwilightState();
    private final Context mContext;
    private final LocationManager mLocationManager;

    private static class TwilightState {
        boolean isNight;
        long nextUpdate;
        long todaySunrise;
        long todaySunset;
        long tomorrowSunrise;
        long yesterdaySunset;

        private TwilightState() {
        }
    }

    TwilightManager(Context context) {
        this.mContext = context;
        this.mLocationManager = (LocationManager) context.getSystemService("location");
    }

    boolean isNight() {
        TwilightState state = sTwilightState;
        if (isStateValid(state)) {
            return state.isNight;
        }
        Location location = getLastKnownLocation();
        if (location != null) {
            updateState(location);
            return state.isNight;
        }
        Log.i(TAG, "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int hour = Calendar.getInstance().get(11);
        return hour < 6 || hour >= 22;
    }

    private Location getLastKnownLocation() {
        Location coarseLoc = null;
        Location fineLoc = null;
        if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            coarseLoc = getLastKnownLocationForProvider("network");
        }
        if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            fineLoc = getLastKnownLocationForProvider("gps");
        }
        return (fineLoc == null || coarseLoc == null) ? fineLoc == null ? coarseLoc : fineLoc : fineLoc.getTime() > coarseLoc.getTime() ? fineLoc : coarseLoc;
    }

    private Location getLastKnownLocationForProvider(String provider) {
        if (this.mLocationManager != null) {
            try {
                if (this.mLocationManager.isProviderEnabled(provider)) {
                    return this.mLocationManager.getLastKnownLocation(provider);
                }
            } catch (Exception e) {
                Log.d(TAG, "Failed to get last known location", e);
            }
        }
        return null;
    }

    private boolean isStateValid(TwilightState state) {
        return state != null && state.nextUpdate > System.currentTimeMillis();
    }

    private void updateState(@NonNull Location location) {
        long nextUpdate;
        TwilightState state = sTwilightState;
        long now = System.currentTimeMillis();
        TwilightCalculator calculator = TwilightCalculator.getInstance();
        calculator.calculateTwilight(now - a.h, location.getLatitude(), location.getLongitude());
        long yesterdaySunset = calculator.sunset;
        calculator.calculateTwilight(now, location.getLatitude(), location.getLongitude());
        boolean isNight = calculator.state == 1;
        long todaySunrise = calculator.sunrise;
        long todaySunset = calculator.sunset;
        calculator.calculateTwilight(a.h + now, location.getLatitude(), location.getLongitude());
        long tomorrowSunrise = calculator.sunrise;
        if (todaySunrise == -1 || todaySunset == -1) {
            nextUpdate = now + 43200000;
        } else {
            if (now > todaySunset) {
                nextUpdate = 0 + tomorrowSunrise;
            } else if (now > todaySunrise) {
                nextUpdate = 0 + todaySunset;
            } else {
                nextUpdate = 0 + todaySunrise;
            }
            nextUpdate += 60000;
        }
        state.isNight = isNight;
        state.yesterdaySunset = yesterdaySunset;
        state.todaySunrise = todaySunrise;
        state.todaySunset = todaySunset;
        state.tomorrowSunrise = tomorrowSunrise;
        state.nextUpdate = nextUpdate;
    }
}
