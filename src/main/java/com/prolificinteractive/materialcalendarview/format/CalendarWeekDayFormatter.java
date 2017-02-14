package com.prolificinteractive.materialcalendarview.format;

import com.prolificinteractive.materialcalendarview.CalendarUtils;

import java.util.Calendar;
import java.util.Locale;

public class CalendarWeekDayFormatter implements WeekDayFormatter {
    private final Calendar calendar;

    public CalendarWeekDayFormatter(Calendar calendar) {
        this.calendar = calendar;
    }

    public CalendarWeekDayFormatter() {
        this.calendar = CalendarUtils.getInstance();
    }

    public CharSequence format(int dayOfWeek) {
        this.calendar.set(7, dayOfWeek);
        return this.calendar.getDisplayName(7, 1, Locale.getDefault());
    }
}
