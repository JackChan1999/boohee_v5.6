package com.boohee.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

public class HttpUtils {

    public enum NetWorkType {
        UnKnown(-1),
        Wifi(1),
        Net2G(2),
        Net3G(3),
        Net4G(4);

        public int value;

        private NetWorkType(int value) {
            this.value = value;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            NetworkInfo[] info = ((ConnectivityManager) context.getSystemService("connectivity"))
                    .getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiConnection(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService
                ("connectivity")).getNetworkInfo(1);
        if (localNetworkInfo == null || !localNetworkInfo.isConnected()) {
            return false;
        }
        Helper.showLog("", "wifi is connected!");
        return true;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    public static int getConnectedTypeINT(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        if (net != null) {
            return net.getType();
        }
        return -1;
    }

    public static NetWorkType getNetworkType(Context context) {
        switch (getConnectedTypeINT(context)) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                switch (getTelephonyManager(context).getNetworkType()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return NetWorkType.Net2G;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return NetWorkType.Net3G;
                    case 13:
                        return NetWorkType.Net4G;
                    default:
                        return NetWorkType.UnKnown;
                }
            case 1:
                return NetWorkType.Wifi;
            default:
                return NetWorkType.UnKnown;
        }
    }
}
