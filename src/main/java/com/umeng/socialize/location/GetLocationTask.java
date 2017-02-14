package com.umeng.socialize.location;

import android.location.Location;
import android.os.AsyncTask;

import com.umeng.socialize.utils.Log;

public class GetLocationTask extends AsyncTask<Void, Void, Location> {
    private static final String LOG_TAG = "Location";
    private DefaultLocationProvider mProvider;

    public GetLocationTask(DefaultLocationProvider defaultLocationProvider) {
        this.mProvider = defaultLocationProvider;
    }

    protected Location doInBackground(Void... voidArr) {
        int i = 0;
        while (i < 20) {
            try {
                if (isCancelled()) {
                    return null;
                }
                Location locationBuffer = getLocationBuffer();
                if (locationBuffer != null) {
                    return locationBuffer;
                }
                Thread.sleep(500);
                i++;
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }

    private Location getLocationBuffer() {
        Location location = this.mProvider.getLocation();
        if (location != null) {
            return location;
        }
        Log.d(LOG_TAG, "Fetch gps info from default failed,then fetch form network..");
        this.mProvider.setProvider("network");
        location = this.mProvider.getLocation();
        this.mProvider.setProvider(null);
        return location;
    }
}
