package com.boohee.one.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.utils.Helper;

public class ShutDownReceiver extends BroadcastReceiver {
    public static final String TAG = ShutDownReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Helper.showLog(TAG, "onReceive ShutDown");
    }
}
