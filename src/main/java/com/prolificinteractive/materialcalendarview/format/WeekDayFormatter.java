package com.prolificinteractive.materialcalendarview.format;

import com.prolificinteractive.materialcalendarview.CalendarUtils;

public interface WeekDayFormatter {
    public static final WeekDayFormatter DEFAULT = new CalendarWeekDayFormatter(CalendarUtils
            .getInstance());

    CharSequence format(int i);
}
