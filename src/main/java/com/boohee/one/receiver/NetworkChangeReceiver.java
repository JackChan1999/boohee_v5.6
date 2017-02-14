package com.boohee.one.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.one.http.DnspodFree;
import com.boohee.one.http.IPCheck;
import com.boohee.one.sport.DownloadService;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    static final String TAG = NetworkChangeReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Helper.showLog(TAG, "network has changed");
        DnspodFree.clearIpCache();
        IPCheck.cacheApiIpAndCheck();
        if (!HttpUtils.isWifiConnection(context)) {
            DownloadService.intentPauseAll(context);
        }
    }
}
