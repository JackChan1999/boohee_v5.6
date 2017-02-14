package com.boohee.one.pedometer;

import com.boohee.calendar.CountDate;
import com.boohee.utils.DateHelper;

import java.io.Serializable;

public class StepModel implements Serializable, CountDate {
    public int    distance;
    public String record_on;
    public int    step;

    public int getYear() {
        return DateHelper.getYear(DateHelper.parseString(this.record_on));
    }

    public int getMonth() {
        return DateHelper.getMonth(DateHelper.parseString(this.record_on));
    }

    public int getDay() {
        return DateHelper.getDay(DateHelper.parseString(this.record_on));
    }
}
