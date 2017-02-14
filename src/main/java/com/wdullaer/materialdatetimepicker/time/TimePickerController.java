package com.wdullaer.materialdatetimepicker.time;

public interface TimePickerController {
    int getAccentColor();

    boolean is24HourMode();

    boolean isThemeDark();

    void tryVibrate();
}
