package com.prolificinteractive.materialcalendarview.format;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatTitleFormatter implements TitleFormatter {
    private final DateFormat dateFormat;

    public DateFormatTitleFormatter() {
        this.dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    }

    public DateFormatTitleFormatter(DateFormat format) {
        this.dateFormat = format;
    }

    public CharSequence format(CalendarDay day) {
        return this.dateFormat.format(day.getDate());
    }
}
