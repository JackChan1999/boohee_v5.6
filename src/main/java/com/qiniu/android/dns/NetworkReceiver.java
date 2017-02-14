package com.qiniu.android.dns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.boohee.utility.Const;
import com.qiniu.android.dns.NetworkInfo.NetSatus;

import java.util.Locale;

public final class NetworkReceiver extends BroadcastReceiver {
    private static final Uri PREFERRED_APN_URI = Uri.parse
            ("content://telephony/carriers/preferapn");
    private static DnsManager mdnsManager;

    public static NetworkInfo createNetInfo(NetworkInfo info, Context context) {
        if (info == null) {
            return NetworkInfo.noNetwork;
        }
        NetSatus net;
        int provider = 0;
        if (info.getType() == 1) {
            net = NetSatus.WIFI;
            provider = 0;
        } else {
            net = NetSatus.MOBILE;
            Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null,
                    null);
            if (c != null) {
                c.moveToFirst();
                String user = c.getString(c.getColumnIndex(Const.USER));
                if (!TextUtils.isEmpty(user) && (user.startsWith("ctwap") || user.startsWith
                        ("ctnet"))) {
                    provider = 1;
                }
            }
            c.close();
            if (provider != 1) {
                String netMode = info.getExtraInfo();
                if (netMode != null) {
                    netMode = netMode.toLowerCase(Locale.getDefault());
                    if (netMode.equals("cmwap") || netMode.equals("cmnet")) {
                        provider = 3;
                    } else if (netMode.equals("3gnet") || netMode.equals("uninet") || netMode
                            .equals("3gwap") || netMode.equals("uniwap")) {
                        provider = 2;
                    }
                }
            }
        }
        return new NetworkInfo(net, provider);
    }

    public static void setDnsManager(DnsManager dnsManager) {
        mdnsManager = dnsManager;
    }

    public void onReceive(Context context, Intent intent) {
        if (mdnsManager != null) {
            mdnsManager.onNetworkChange(createNetInfo(((ConnectivityManager) context
                    .getSystemService("connectivity")).getActiveNetworkInfo(), context));
        }
    }
}
