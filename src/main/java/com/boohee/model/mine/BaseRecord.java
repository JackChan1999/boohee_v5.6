package com.boohee.model.mine;

import com.boohee.utils.DateHelper;

import java.io.Serializable;

public class BaseRecord implements Serializable {
    private static final long serialVersionUID = 3429400890940214781L;
    public int    id;
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
