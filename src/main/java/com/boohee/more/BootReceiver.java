package com.boohee.more;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.model.Alarm;
import com.boohee.modeldao.AlarmDao;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.utils.Helper;

import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {
    static final String TAG = BootReceiver.class.getName();

    public void onReceive(Context context, Intent intent) {
        Helper.showLog(TAG, "onReceive BootComplete");
        openRemind(context);
        StepCounterUtil.startStepService(context);
    }

    private void openRemind(Context ctx) {
        AlarmDao alarmDao = new AlarmDao(ctx);
        ArrayList<Alarm> alarms = alarmDao.getAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            RemindService.start((Alarm) alarms.get(i), ctx);
        }
        alarmDao.closeDB();
    }
}
