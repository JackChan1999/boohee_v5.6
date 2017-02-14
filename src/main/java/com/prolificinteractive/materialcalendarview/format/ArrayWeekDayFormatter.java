package com.prolificinteractive.materialcalendarview.format;

public class ArrayWeekDayFormatter implements WeekDayFormatter {
    private final CharSequence[] weekDayLabels;

    public ArrayWeekDayFormatter(CharSequence[] weekDayLabels) {
        if (weekDayLabels == null) {
            throw new IllegalArgumentException("Cannot be null");
        } else if (weekDayLabels.length != 7) {
            throw new IllegalArgumentException("Array must contain exactly 7 elements");
        } else {
            this.weekDayLabels = weekDayLabels;
        }
    }

    public CharSequence format(int dayOfWeek) {
        return this.weekDayLabels[dayOfWeek - 1];
    }
}
