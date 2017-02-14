package com.meiqia.meiqiasdk.util;

import android.content.Context;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.model.BaseMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MQTimeUtils {
    private static final String HOURS_MINUTE        = "H:mm";
    private static final String MONTH_DAY           = "M-d";
    private static final long   TIME_INTERNAL_LIMIT = 120000;
    private static       String TODAY               = "today";
    private static       String YESTERDAY           = "yesterday";

    public static void init(Context context) {
        TODAY = context.getResources().getString(R.string.mq_timeline_today);
        YESTERDAY = context.getResources().getString(R.string.mq_timeline_yesterday);
    }

    public static void refreshMQTimeItem(List<BaseMessage> mcMessageList) {
        for (int i = mcMessageList.size() - 1; i >= 0; i--) {
            if (((BaseMessage) mcMessageList.get(i)).getItemViewType() == 2) {
                mcMessageList.remove(i);
            }
        }
        addMQTimeItem(mcMessageList);
    }

    private static void addMQTimeItem(List<BaseMessage> mcMessageList) {
        int i = mcMessageList.size() - 1;
        while (i >= 0) {
            if (i != 0) {
                long currentMsgTime = ((BaseMessage) mcMessageList.get(i)).getCreatedOn();
                if (currentMsgTime - ((BaseMessage) mcMessageList.get(i - 1)).getCreatedOn() >
                        TIME_INTERNAL_LIMIT && ((BaseMessage) mcMessageList.get(i))
                        .getItemViewType() != 2) {
                    BaseMessage timeItem = new BaseMessage();
                    timeItem.setItemViewType(2);
                    timeItem.setCreatedOn(currentMsgTime);
                    mcMessageList.add(i, timeItem);
                }
            }
            i--;
        }
    }

    public static String parseTime(long time) {
        Date curDates = new Date(time);
        String timeStr = new SimpleDateFormat(HOURS_MINUTE, Locale.getDefault()).format(curDates);
        if (time > getTodayZeroTime()) {
            return TODAY + " " + timeStr;
        }
        if (time > getYesterdayZeroTime() && time < getTodayZeroTime()) {
            return YESTERDAY + " " + timeStr;
        }
        return new SimpleDateFormat("M-d", Locale.getDefault()).format(curDates) + " " + timeStr;
    }

    private static long getTodayZeroTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    private static long getYesterdayZeroTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, -24);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    public static long parseTimeToLong(String time) {
        if (time == null) {
            return System.currentTimeMillis();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
}
