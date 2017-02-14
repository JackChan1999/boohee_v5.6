package com.prolificinteractive.materialcalendarview.format;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatDayFormatter implements DayFormatter {
    private final DateFormat dateFormat;

    public DateFormatDayFormatter() {
        this.dateFormat = new SimpleDateFormat("d", Locale.getDefault());
    }

    public DateFormatDayFormatter(@NonNull DateFormat format) {
        this.dateFormat = format;
    }

    @NonNull
    public String format(@NonNull CalendarDay day) {
        return this.dateFormat.format(day.getDate());
    }
}
