package com.prolificinteractive.materialcalendarview.format;

import android.text.SpannableStringBuilder;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class MonthArrayTitleFormatter implements TitleFormatter {
    private final CharSequence[] monthLabels;

    public MonthArrayTitleFormatter(CharSequence[] monthLabels) {
        if (monthLabels == null) {
            throw new IllegalArgumentException("Label array cannot be null");
        } else if (monthLabels.length < 12) {
            throw new IllegalArgumentException("Label array is too short");
        } else {
            this.monthLabels = monthLabels;
        }
    }

    public CharSequence format(CalendarDay day) {
        return new SpannableStringBuilder().append(this.monthLabels[day.getMonth()]).append(" ")
                .append(String.valueOf(day.getYear()));
    }
}
