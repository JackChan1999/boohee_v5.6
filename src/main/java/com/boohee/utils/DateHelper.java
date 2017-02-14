package com.boohee.utils;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.model.User;
import com.boohee.one.R;
import com.umeng.analytics.a;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    public static final  Calendar   CALENDAR              = Calendar.getInstance();
    public static final  String     DD                    = "dd";
    public static final  DateFormat DEFAULT_FORMAT        = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());
    public static final  String     MM                    = "MM";
    public static final  String     M_D                   = "M-d";
    public static final  String     M_D_CN                = "M月d日";
    public static final  String     M_D_SLASH             = "M/d";
    public static final  String     YYYY                  = "yyyy";
    public static final  String     YYYYMM                = "yyyyMM";
    public static final  String     YYYYMMDD              = "yyyyMMdd";
    public static final  String     YYYY_MM_DD            = "yyyy-MM-dd";
    public static final  String     YYYY_MM_DD_HH_MM      = "yyyy-MM-dd HH:mm";
    public static final  String     YYYY_MM_DD_HH_MM_SS   = "yyyy-MM-dd HH:mm:ss";
    public static final  String     YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final  String     YYYY_M_CN             = "yyyy年M月";
    public static final  String     YYYY_M_D_CN           = "yyyy年M月d日";
    private static final String[]   meals                 = new String[]{"加餐", "早餐", "午餐", "晚餐",
            "", "", "上午加餐", "下午加餐", "晚上加餐"};

    public static String getCurrentDateTime() {
        return format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTimeMills() {
        return format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String monthDay() {
        return format(new Date(), "MMMdd日");
    }

    public static String monthDay(Date date) {
        return format(date, "MMMdd日");
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, days);
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, months);
        return cal.getTime();
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String timezoneFormat(String dateString, String outputFormat) {
        return new SimpleDateFormat(outputFormat, Locale.US).format(parseFromString(dateString,
                "yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static String format(Date date, String format) {
        String dateString = "";
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return df.format(date);
        } catch (Exception e) {
            return df.format(new Date());
        }
    }

    public static String formatString(String dateString, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(parseFromString
                (dateString, "yyyy-MM-dd"));
    }

    public static Date getFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(7, cal.getFirstDayOfWeek());
        return cal.getTime();
    }

    public static Date getFirstDay(Date date, int start) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(7, start);
        return cal.getTime();
    }

    public static Date parseFromString(String dateString, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseString(String dateString) {
        try {
            return DEFAULT_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getWeekOfDate(Date date, Context context) {
        String[] weekDaysName = context.getResources().getStringArray(R.array.f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return weekDaysName[calendar.get(7) - 1];
    }

    public static int between(Date newDate, Date oldDate) {
        return (int) ((newDate.getTime() - oldDate.getTime()) / a.h);
    }

    public static int between(Date oldDate) {
        return (int) ((new Date().getTime() - oldDate.getTime()) / a.h);
    }

    public static int between(String oldDate) {
        return between(parseString(oldDate));
    }

    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("date cannot after today");
        }
        int yearNow = cal.get(1);
        int monthNow = cal.get(2) + 1;
        int dayOfMonthNow = cal.get(5);
        cal.setTime(birthDay);
        int yearBirth = cal.get(1);
        int monthBirth = cal.get(2);
        int dayOfMonthBirth = cal.get(5);
        int age = yearNow - yearBirth;
        if (monthNow > monthBirth) {
            return age;
        }
        if (monthNow != monthBirth) {
            return age - 1;
        }
        if (dayOfMonthNow < dayOfMonthBirth) {
            return age - 1;
        }
        return age;
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, 1);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, c.getActualMaximum(5));
        return c.getTime();
    }

    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = CALENDAR;
        c.setTime(date);
        return c.get(1);
    }

    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = CALENDAR;
        c.setTime(date);
        return c.get(2) + 1;
    }

    public static String getYearMonth(Date date) {
        return getYear(date) + String.format("%02d", new Object[]{Integer.valueOf(getMonth(date))});
    }

    public static int getDay(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = CALENDAR;
        c.setTime(date);
        return c.get(5);
    }

    public static Date defaultTargetDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(2, cal.get(2) + 1);
        cal.set(5, cal.get(5) - 1);
        return cal.getTime();
    }

    public static String parseStringFromZone(String dateStringIn, Boolean isTime) {
        String dateStringOut = "";
        SimpleDateFormat ISO8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.getDefault());
        String dateFormatIn = dateStringIn.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
        if (isTime.booleanValue()) {
            try {
                dateStringOut = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
                        .format(ISO8601DateFormat.parse(dateFormatIn));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dateStringOut = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format
                        (ISO8601DateFormat.parse(dateFormatIn));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        return dateStringOut;
    }

    public static String getTimeFiled() {
        int hour = Calendar.getInstance().get(11);
        if (hour >= 5 && hour < 9) {
            return "早餐";
        }
        if (hour >= 9 && hour < 11) {
            return "上午加餐";
        }
        if (hour >= 11 && hour < 13) {
            return "午餐";
        }
        if (hour >= 13 && hour < 18) {
            return "下午加餐";
        }
        if (hour < 18 || hour >= 21) {
            return "晚上加餐";
        }
        return "晚餐";
    }

    public static String getMealName(int time_type) {
        return (time_type >= meals.length || time_type < 0) ? "加餐" : meals[time_type];
    }

    public static int mealName2TimeType(String mealName) {
        int i = 0;
        while (i < meals.length) {
            if (meals[i].equals(mealName)) {
                return i;
            }
            i++;
        }
        return i >= meals.length ? 0 : i;
    }

    public static String today() {
        return nextDay(new Date(), 0);
    }

    public static String tomorrow() {
        return nextDay(new Date(), 1);
    }

    public static String yesterday() {
        return previousDay(new Date(), -1);
    }

    public static String nextDay(Date date, int step) {
        return format(addDays(date, step));
    }

    public static String nextDay(String date, int step) {
        return nextDay(parseString(date), step);
    }

    public static String previousDay(Date date, int step) {
        return format(addDays(date, step));
    }

    public static String previousDay(String date, int step) {
        return previousDay(parseString(date), step);
    }

    public static boolean showResetPlan(User user) {
        if (user == null) {
            return true;
        }
        if (user.target_weight == -1.0f) {
            return false;
        }
        if (TextUtils.isEmpty(user.target_date)) {
            return false;
        }
        if (new Date().after(DateFormatUtils.string2date(user.target_date, "yyyy-MM-dd"))) {
            return true;
        }
        return false;
    }

    public static int getOffsetFromUtc() {
        return TimeZone.getDefault().getOffset(new Date().getTime()) / 1000;
    }

    public static boolean isAfterToday(String targetDate, String currentDate) {
        if (currentDate == null || targetDate == null || targetDate.compareTo(currentDate) > 0) {
            return true;
        }
        return false;
    }
}
