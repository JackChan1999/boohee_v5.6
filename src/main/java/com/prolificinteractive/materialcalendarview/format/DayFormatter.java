package com.prolificinteractive.materialcalendarview.format;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public interface DayFormatter {
    public static final DayFormatter DEFAULT = new DateFormatDayFormatter();

    @NonNull
    String format(@NonNull CalendarDay calendarDay);
}
