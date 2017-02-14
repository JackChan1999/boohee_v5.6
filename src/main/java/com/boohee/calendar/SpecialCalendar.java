package com.boohee.calendar;

import java.util.Calendar;

public class SpecialCalendar {
    private int dayOfWeek   = 0;
    private int daysOfMonth = 0;

    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        }
        if (year % 100 == 0 || year % 4 != 0) {
            return false;
        }
        return true;
    }

    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                this.daysOfMonth = 31;
                break;
            case 2:
                if (!isLeapyear) {
                    this.daysOfMonth = 28;
                    break;
                }
                this.daysOfMonth = 29;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                this.daysOfMonth = 30;
                break;
        }
        return this.daysOfMonth;
    }

    public int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        this.dayOfWeek = cal.get(7) - 1;
        return this.dayOfWeek;
    }
}
