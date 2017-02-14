package com.boohee.model.mine;

import com.boohee.calendar.CountDate;
import com.boohee.utils.DateHelper;

public class DietRecord implements CountDate {
    public float  activity_calory;
    public float  eating_calory;
    public String record_on;

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
