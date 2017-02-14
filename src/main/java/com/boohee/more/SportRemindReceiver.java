package com.boohee.more;

import android.app.AlarmManager;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boohee.database.OnePreference;
import com.boohee.one.R;
import com.boohee.one.video.manager.VideoPreference;
import com.boohee.one.video.ui.NewSportPlanActivity;
import com.boohee.utils.BadgeUtils;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.umeng.analytics.a;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class SportRemindReceiver extends BroadcastReceiver {
    private static final String   TAG         = SportRemindReceiver.class.getName();
    private static final String   beginDate   = "2015-10-00";
    private static final int      noticeType  = 762;
    private static final int      requestCode = 152;
    private static final String[] tipsStr     = new String[]{"放弃可以找到一万个理由，坚持只需一个信念!",
            "哪样更痛苦？努力还是后悔!", "塑造自己的过程也许很疼，但你最终能收获一个更好的自己!", "你以为的极限弄不好只是别人的起点!",
            "不论你跑得多慢，都要比躺在沙发里的人快!", "将来的你一定会感激现在拼命的自己!", "别让人指着你说，你的程度仅此而已!",
            "感觉累，也许是因为你正处于人生的上坡路!", "今天尽力了吗？减肥没有那么容易，每块肉都有它的脾气!"};

    public void onReceive(Context context, Intent intent) {
        Helper.showLog(TAG, "onReceive start");
        if (OnePreference.getPrefSportRemind() && !new VideoPreference(context).todayIsComplete
                (DateFormat.getDateInstance().format(new Date()))) {
            Intent target = new Intent(context, NewSportPlanActivity.class);
            target.addFlags(67108864);
            setNotification(context, target);
            BadgeUtils.setIconBadge(context, 1);
        }
    }

    private void setNotification(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 268435456);
        NotificationManager nfcManager = (NotificationManager) context.getSystemService
                ("notification");
        String title = context.getString(R.string.a6a);
        Builder builder = new Builder(context);
        builder.setAutoCancel(false);
        builder.setTicker(title);
        builder.setContentTitle(title);
        builder.setContentText(tipsStr[getTips()]);
        builder.setSmallIcon(R.drawable.ja);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        nfcManager.notify(noticeType, builder.getNotification());
    }

    public static void start(Context ctx) {
        AlarmManager manager = (AlarmManager) ctx.getSystemService("alarm");
        PendingIntent pending = PendingIntent.getBroadcast(ctx, 152, new Intent(ctx,
                SportRemindReceiver.class), 134217728);
        Helper.showLog(TAG, "SportRemindReceiver start");
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.setTimeInMillis(System.currentTimeMillis());
        int remindHour = 20;
        int remindMinute = 30;
        if (!TextUtil.isEmpty(OnePreference.getPrefSportRemindTime())) {
            String[] times = OnePreference.getPrefSportRemindTime().split("#");
            if (times.length == 2) {
                remindHour = Integer.parseInt(times[0]);
                remindMinute = Integer.parseInt(times[1]);
            }
        }
        calSet.set(11, remindHour);
        calSet.set(12, remindMinute);
        calSet.set(13, 0);
        calSet.set(14, 0);
        if (calSet.compareTo(calNow) <= 0) {
            calSet.add(5, 1);
        }
        manager.setRepeating(0, calSet.getTimeInMillis(), a.h, pending);
    }

    private static int getTips() {
        return (int) (DateFormatUtils.countDay(beginDate, DateFormatUtils.date2string(new Date(),
                "yyyy-MM-dd")) % ((long) tipsStr.length));
    }
}
