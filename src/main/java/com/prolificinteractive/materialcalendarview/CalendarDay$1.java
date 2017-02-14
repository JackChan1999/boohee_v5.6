package com.prolificinteractive.materialcalendarview;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class CalendarDay$1 implements Creator<CalendarDay> {
    CalendarDay$1() {
    }

    public CalendarDay createFromParcel(Parcel source) {
        return new CalendarDay(source);
    }

    public CalendarDay[] newArray(int size) {
        return new CalendarDay[size];
    }
}
