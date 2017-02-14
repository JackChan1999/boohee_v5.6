package com.zxinsight.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class k {
    public static boolean a(Context context) {
        if (!o.a(context, "android.permission.INTERNET")) {
            return false;
        }
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            Object allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (l.b(allNetworkInfo)) {
                for (NetworkInfo state : allNetworkInfo) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        Toast.makeText(context, "Unable to connect to the network.", 0).show();
        return false;
    }
}
