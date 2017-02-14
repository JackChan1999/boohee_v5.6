package com.boohee.model;

import com.boohee.calendar.CountDate;
import com.boohee.utils.DateHelper;

public class PeriodRecord implements CountDate {
    public static final String CENTER     = "center";
    public static final String LEFT       = "left";
    public static final String MC         = "mc";
    public static final String NORMAL     = "normal";
    public static final String OVIPOSIT   = "oviposit_day";
    public static final String PREDICTION = "prediction";
    public static final String PREGNACY   = "pregnancy";
    public static final String RIGHT      = "right";
    public static final String YYYYMMDD   = "yyyyMMdd";
    public String location;
    public String the_date;
    public String type;

    public PeriodRecord(String date, String type) {
        this.the_date = date;
        this.type = type;
        this.location = CENTER;
    }

    public PeriodRecord(String date, String type, String location) {
        this.the_date = date;
        this.type = type;
        this.location = location;
    }

    public int getYear() {
        return DateHelper.getYear(DateHelper.parseFromString(this.the_date, "yyyyMMdd"));
    }

    public int getMonth() {
        return DateHelper.getMonth(DateHelper.parseFromString(this.the_date, "yyyyMMdd"));
    }

    public int getDay() {
        return DateHelper.getDay(DateHelper.parseFromString(this.the_date, "yyyyMMdd"));
    }
}
