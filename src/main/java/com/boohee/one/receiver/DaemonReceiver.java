package com.boohee.one.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boohee.one.pedometer.StepCounterUtil;

public class DaemonReceiver extends BroadcastReceiver {
    public static final String TAG = DaemonReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        StepCounterUtil.startStepService(context);
    }
}
