package com.boohee.utils;

import java.util.Date;

public class DateKnife {
    public static String display(Date now, Date origin) {
        return DateHelper.format(origin, "MM-dd HH:mm");
    }
}
