package com.boohee.calendar;

import android.content.Context;
import android.widget.BaseAdapter;

import com.boohee.utils.DateHelper;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class BaseCalendarAdapter extends BaseAdapter {
    private   Date    chooseDate;
    protected Context ctx;
    protected int currentDayPosition = -1;
    protected int currentFlag        = -1;
    protected int currentMonth;
    protected int currentSelected = -1;
    protected int  currentYear;
    protected Date date;
    protected String[] dayNumber       = new String[42];
    protected int      dayOfWeek       = 0;
    protected int      daysOfMonth     = 0;
    protected boolean  isLeapyear      = false;
    protected int      lastDaysOfMonth = 0;
    public onCalendarItemSelectedListener onCalendarItemSelectedListener;
    protected int[] recordTagFlag = null;
    protected List<? extends CountDate> records;
    protected SpecialCalendar sc = null;
    protected int sys_day;
    protected int sys_month;
    protected int sys_year;

    public interface onCalendarItemSelectedListener {
        void onCalendarItemSelected(int i);
    }

    public BaseCalendarAdapter(Context context, Date date, List<? extends CountDate> records,
                               Date chooseDate) {
        this.ctx = context;
        this.sc = new SpecialCalendar();
        this.date = date;
        this.chooseDate = chooseDate;
        getSysDate();
        this.records = records;
        init();
    }

    private void init() {
        this.currentYear = DateHelper.getYear(this.date);
        this.currentMonth = DateHelper.getMonth(this.date);
        getCalendar(this.currentYear, this.currentMonth);
    }

    private void getSysDate() {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        this.sys_year = c.get(1);
        this.sys_month = c.get(2) + 1;
        this.sys_day = c.get(5);
    }

    public int getCount() {
        if (endPosition() <= 35) {
            return 35;
        }
        return this.dayNumber.length;
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public String getDateString(int position) {
        return this.currentYear + SocializeConstants.OP_DIVIDER_MINUS + String.format("%02d", new
                Object[]{Integer.valueOf(this.currentMonth)}) + SocializeConstants
                .OP_DIVIDER_MINUS + String.format("%02d", new Object[]{Integer.valueOf(Integer
                .parseInt(this.dayNumber[position]))});
    }

    public Date getDate(int position) {
        return DateHelper.parseString(getDateString(position));
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void getCalendar(int year, int month) {
        this.isLeapyear = this.sc.isLeapYear(year);
        this.daysOfMonth = this.sc.getDaysOfMonth(this.isLeapyear, month);
        this.dayOfWeek = this.sc.getWeekdayOfMonth(year, month);
        this.lastDaysOfMonth = this.sc.getDaysOfMonth(this.isLeapyear, month - 1);
        getweek(year, month);
    }

    private void getweek(int year, int month) {
        int j = 1;
        int flag = 0;
        if (this.records != null && this.records.size() > 0) {
            this.recordTagFlag = new int[this.records.size()];
        }
        for (int i = 0; i < this.dayNumber.length; i++) {
            if (i < this.dayOfWeek) {
                this.dayNumber[i] = (((this.lastDaysOfMonth - this.dayOfWeek) + 1) + i) + "";
            } else if (i < this.daysOfMonth + this.dayOfWeek) {
                int day = (i - this.dayOfWeek) + 1;
                this.dayNumber[i] = ((i - this.dayOfWeek) + 1) + "";
                if (this.sys_year == year && this.sys_month == month && this.sys_day == day) {
                    this.currentFlag = i;
                }
                if (DateHelper.getYear(this.chooseDate) == year && DateHelper.getMonth(this
                        .chooseDate) == month && DateHelper.getDay(this.chooseDate) == day) {
                    this.currentDayPosition = i;
                }
                if (this.records != null && this.records.size() > 0) {
                    for (int m = 0; m < this.records.size(); m++) {
                        CountDate record = (CountDate) this.records.get(m);
                        int matchYear = record.getYear();
                        int matchMonth = record.getMonth();
                        int matchDay = record.getDay();
                        if (matchYear == year && matchMonth == month && matchDay == day) {
                            this.recordTagFlag[flag] = i;
                            flag++;
                        }
                    }
                }
            } else {
                this.dayNumber[i] = j + "";
                j++;
            }
        }
    }

    public int startPosition() {
        return this.dayOfWeek;
    }

    public int endPosition() {
        return this.daysOfMonth + this.dayOfWeek;
    }

    public void setChangeListener(onCalendarItemSelectedListener onCalendarItemSelectedListener) {
        this.onCalendarItemSelectedListener = onCalendarItemSelectedListener;
    }
}
