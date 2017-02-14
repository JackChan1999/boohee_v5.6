package com.boohee.more;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.database.OnePreference;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.CheckView;
import com.umeng.analytics.a;

import java.util.Calendar;
import java.util.Random;

public class DailySignReceiver extends BroadcastReceiver {
    static final         String   TAG         = DailySignReceiver.class.getName();
    private static final int      noticeType  = 723;
    private static final int      requestCode = 425;
    private              String[] tipsStr     = new String[]{"少侠，请留步！你忘记打卡啦",
            "有了薄荷菌的提醒，妈妈再也不用担心我忘记打卡啦", "今天运动了吗？别忘记打个卡，有钻石奖励哦", "卡是谁？为什么我们每天都要打TA？"};

    public void onReceive(Context context, Intent intent) {
        Helper.showLog(TAG, "PrefSignRecord isToday : " + (!DateFormatUtils.isToday(OnePreference
                .getPrefSignRecord())));
        Helper.showLog(TAG, "getPrefDiamondSignRemind : " + OnePreference
                .getPrefDiamondSignRemind());
        if (!DateFormatUtils.isToday(OnePreference.getPrefSignRecord()) && OnePreference
                .getPrefDiamondSignRemind()) {
            showDialog(context);
            OnePreference.setPrefSignRecord();
        }
    }

    private void showDialog(Context context) {
        CheckView checkView = new CheckView(context);
        checkView.setMsg(this.tipsStr[new Random().nextInt(4)]);
        checkView.attachToWindow();
    }

    public static void start(Context ctx) {
        AlarmManager manager = (AlarmManager) ctx.getSystemService("alarm");
        PendingIntent pending = PendingIntent.getBroadcast(ctx, requestCode, new Intent(ctx,
                DailySignReceiver.class), 134217728);
        Helper.showLog(TAG, "DailySignReceiver start");
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        calSet.set(11, 12);
        calSet.set(12, 0);
        calSet.set(13, 0);
        calSet.set(14, 0);
        if (calSet.compareTo(calNow) <= 0) {
            calSet.add(5, 1);
        }
        manager.setRepeating(0, calSet.getTimeInMillis(), a.h, pending);
    }
}
