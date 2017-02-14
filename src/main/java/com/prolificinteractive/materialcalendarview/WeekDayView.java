package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import java.util.Calendar;

@SuppressLint({"ViewConstructor"})
class WeekDayView extends TextView {
    private int dayOfWeek;
    private WeekDayFormatter formatter = WeekDayFormatter.DEFAULT;

    public WeekDayView(Context context, int dayOfWeek) {
        super(context);
        setGravity(17);
        if (VERSION.SDK_INT >= 17) {
            setTextAlignment(4);
        }
        setDayOfWeek(dayOfWeek);
    }

    public void setWeekDayFormatter(WeekDayFormatter formatter) {
        if (formatter == null) {
            formatter = WeekDayFormatter.DEFAULT;
        }
        this.formatter = formatter;
        setDayOfWeek(this.dayOfWeek);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        setText(this.formatter.format(dayOfWeek));
    }

    public void setDayOfWeek(Calendar calendar) {
        setDayOfWeek(CalendarUtils.getDayOfWeek(calendar));
    }
}
