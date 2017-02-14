package com.kitnew.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class NetworkListener extends BroadcastReceiver {
    QNApiImpl        api;
    String           appId;
    QNResultCallback callback;

    public NetworkListener(QNApiImpl api, String appId, QNResultCallback callback) {
        this.api = api;
        this.appId = appId;
        this.callback = callback;
    }

    public void onReceive(Context context, Intent intent) {
        if (!checkNetwork(context)) {
            return;
        }
        if (this.api.isAppIdReady(null)) {
            context.unregisterReceiver(this);
            if (this.callback != null) {
                this.callback.onCompete(0);
                this.callback = null;
                return;
            }
            return;
        }
        this.api.doInitSDK(this.appId, this.callback);
        if (this.api.isAppIdReady(null)) {
            context.unregisterReceiver(this);
            this.callback = null;
        }
    }

    boolean checkNetwork(Context context) {
        NetworkInfo ni = ((ConnectivityManager) context.getSystemService("connectivity"))
                .getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
