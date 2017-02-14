package com.boohee.model;

import com.boohee.calendar.CountDate;
import com.boohee.utils.DateHelper;

public class CheckRecord implements CountDate {
    public String the_date;

    public int getYear() {
        return DateHelper.getYear(DateHelper.parseString(this.the_date));
    }

    public int getMonth() {
        return DateHelper.getMonth(DateHelper.parseString(this.the_date));
    }

    public int getDay() {
        return DateHelper.getDay(DateHelper.parseString(this.the_date));
    }
}
