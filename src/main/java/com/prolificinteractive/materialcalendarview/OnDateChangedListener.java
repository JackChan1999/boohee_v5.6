package com.prolificinteractive.materialcalendarview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface OnDateChangedListener {
    void onDateChanged(@NonNull MaterialCalendarView materialCalendarView, @Nullable CalendarDay
            calendarDay);
}
