package com.prolificinteractive.materialcalendarview;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    public static Calendar getInstance(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        copyDateTo(calendar, calendar);
        return calendar;
    }

    @NonNull
    public static Calendar getInstance() {
        Calendar calendar = Calendar.getInstance();
        copyDateTo(calendar, calendar);
        return calendar;
    }

    public static void setToFirstDay(Calendar calendar) {
        int year = getYear(calendar);
        int month = getMonth(calendar);
        calendar.clear();
        calendar.set(year, month, 1);
        calendar.getTimeInMillis();
    }

    public static void copyDateTo(Calendar from, Calendar to) {
        int year = getYear(from);
        int month = getMonth(from);
        int day = getDay(from);
        to.clear();
        to.set(year, month, day);
        to.getTimeInMillis();
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(1);
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(2);
    }

    public static int getDay(Calendar calendar) {
        return calendar.get(5);
    }

    public static int getDayOfWeek(Calendar calendar) {
        return calendar.get(7);
    }
}
