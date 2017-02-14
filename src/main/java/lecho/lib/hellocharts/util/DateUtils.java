package lecho.lib.hellocharts.util;

import android.text.TextUtils;
import com.umeng.analytics.a;
import com.umeng.socialize.common.SocializeConstants;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {
    private static final int D = 2;
    private static final int[] DAYS_P_MONTH_CY = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] DAYS_P_MONTH_LY = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final String DD = "dd";
    private static final int M = 1;
    public static final String MM = "MM";
    public static final String M_D_CN = "M月d日";
    public static final String M_D_SLASH = "M/d";
    private static final int Y = 0;
    public static final String YYYY = "yyyy";
    public static final String YYYYMMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static transient int gregorianCutoverYear = 1582;

    public static Date date2date(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        try {
            return sdf.parse(sdf.format(date));
        } catch (Exception e) {
            return null;
        }
    }

    public static String date2string(Date date, String formatStr) {
        String strDate = "";
        return new SimpleDateFormat(formatStr).format(date);
    }

    public static Date string2date(String dateString, String formatStr) {
        try {
            return new SimpleDateFormat(formatStr).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Calendar string2Calendar(String dateString, String formatStr) {
        ParseException e;
        DateFormat dateFormat;
        Throwable th;
        Calendar result = Calendar.getInstance();
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            try {
                result.setTime(format.parse(dateString));
            } catch (ParseException e2) {
                e = e2;
                dateFormat = format;
                try {
                    e.printStackTrace();
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                dateFormat = format;
                throw th;
            }
        } catch (ParseException e3) {
            e = e3;
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public static String string2String(String dateString, String formatStr) {
        return date2string(string2date(dateString, "yyyy-MM-dd"), formatStr);
    }

    public static String timezoneFormat(String dateString, String outputFormat) {
        return new SimpleDateFormat(outputFormat, Locale.US).format(parseFromString(dateString, "yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static Date parseFromString(String dateString, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
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

    public static int[] splitYMD(String date) {
        date = date.replace(SocializeConstants.OP_DIVIDER_MINUS, "");
        int[] ymd = new int[]{0, 0, 0};
        ymd[0] = Integer.parseInt(date.substring(0, 4));
        ymd[1] = Integer.parseInt(date.substring(4, 6));
        ymd[2] = Integer.parseInt(date.substring(6, 8));
        return ymd;
    }

    public static boolean isLeapYear(int year) {
        return year >= gregorianCutoverYear ? year % 4 == 0 && (year % 100 != 0 || year % 400 == 0) : year % 4 == 0;
    }

    private static int[] addOneDay(int year, int month, int day) {
        if (isLeapYear(year)) {
            day++;
            if (day > DAYS_P_MONTH_LY[month - 1]) {
                month++;
                if (month > 12) {
                    year++;
                    month = 1;
                }
                day = 1;
            }
        } else {
            day++;
            if (day > DAYS_P_MONTH_CY[month - 1]) {
                month++;
                if (month > 12) {
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }
        return new int[]{year, month, day};
    }

    public static String formatMonthDay(int decimal) {
        return new DecimalFormat("00").format((long) decimal);
    }

    public static String formatYear(int decimal) {
        return new DecimalFormat("0000").format((long) decimal);
    }

    public static long countDay(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            return (format.parse(end).getTime() - format.parse(begin).getTime()) / a.h;
        } catch (ParseException e) {
            e.printStackTrace();
            return day;
        }
    }

    public static List<String> getEveryday(String beginDate, String endDate) {
        long days = countDay(beginDate, endDate);
        int[] ymd = splitYMD(beginDate);
        List<String> everyDays = new ArrayList();
        everyDays.add(beginDate);
        for (int i = 0; ((long) i) < days; i++) {
            ymd = addOneDay(ymd[0], ymd[1], ymd[2]);
            everyDays.add(formatYear(ymd[0]) + SocializeConstants.OP_DIVIDER_MINUS + formatMonthDay(ymd[1]) + SocializeConstants.OP_DIVIDER_MINUS + formatMonthDay(ymd[2]));
        }
        return everyDays;
    }

    public static ArrayList<String> getEveryMonthDay(String beginDate, String endDate) {
        long days = countDay(beginDate, endDate);
        int[] ymd = splitYMD(beginDate);
        ArrayList<String> everyDays = new ArrayList();
        if (!TextUtils.isEmpty(beginDate)) {
            everyDays.add(date2string(string2date(beginDate, "yyyy-MM-dd"), "M/d"));
        }
        for (int i = 0; ((long) i) < days; i++) {
            ymd = addOneDay(ymd[0], ymd[1], ymd[2]);
            everyDays.add(ymd[1] + "/" + ymd[2]);
        }
        return everyDays;
    }

    public static boolean isToday(String date) {
        String todayString = date2string(new Date(), "yyyy-MM-dd");
        if (todayString == null || !todayString.equals(date)) {
            return false;
        }
        return true;
    }

    public static Date getDay(String dateString, int dayNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(string2date(dateString, "yyyy-MM-dd"));
        cal.add(5, dayNum);
        return cal.getTime();
    }

    public static Date getYear(String dateString, int yearNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(string2date(dateString, "yyyy-MM-dd"));
        cal.add(1, yearNum);
        return cal.getTime();
    }

    public static Date getMonth(String dateString, int monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(string2date(dateString, "yyyy-MM-dd"));
        cal.add(2, monthNum);
        return cal.getTime();
    }

    public static Date getWeek(String dateString, int weekNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(string2date(dateString, "yyyy-MM-dd"));
        cal.add(4, weekNum);
        return cal.getTime();
    }

    public static boolean isTodayBeforeDate(String dateString) {
        try {
            if (Integer.parseInt(date2string(new Date(), "yyyyMMdd")) < Integer.parseInt(string2String(dateString, "yyyyMMdd"))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
