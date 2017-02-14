package com.wdullaer.materialdatetimepicker.date;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateChangedListener;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter.CalendarDay;

import java.util.Calendar;

public interface DatePickerController {
    int getAccentColor();

    int getFirstDayOfWeek();

    Calendar[] getHighlightedDays();

    int getMaxYear();

    int getMinYear();

    Calendar[] getSelectableDays();

    CalendarDay getSelectedDay();

    boolean isOutOfRange(int i, int i2, int i3);

    boolean isThemeDark();

    void onDayOfMonthSelected(int i, int i2, int i3);

    void onYearSelected(int i);

    void registerOnDateChangedListener(OnDateChangedListener onDateChangedListener);

    void tryVibrate();

    void unregisterOnDateChangedListener(OnDateChangedListener onDateChangedListener);
}
