package com.meiqia.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.meiqia.core.b.e;

class bt extends BroadcastReceiver {
    final /* synthetic */ MeiQiaService a;
    private               boolean       b;

    private bt(MeiQiaService meiQiaService) {
        this.a = meiQiaService;
        this.b = true;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (!(((ConnectivityManager) this.a.getSystemService("connectivity"))
                    .getActiveNetworkInfo() == null || this.b)) {
                e.b("socket net reconnect");
                this.a.c();
            }
            this.b = false;
        }
    }
}
