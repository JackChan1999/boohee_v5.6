package com.boohee.myview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.record.PickerScrollListener;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class DatePickerWheelView extends FrameLayout {
    public static final int    DEFAULT_TEXT_SIZE = 22;
    static final        String TAG               = DatePickerWheelView.class.getName();
    Calendar calendar;
    Calendar clone;
    private Context   ctx;
    private Date      date;
    private WheelView day;
    OnWheelChangedListener listener;
    private int                  maxYear;
    private int                  minYear;
    private WheelView            month;
    private OnDatePickerListener onDatePickerListener;
    private WheelView            year;

    public interface OnDatePickerListener {
        void onDatePicker(String str);
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        int     currentItem;
        int     currentValue;
        boolean isHighlight;

        public DateArrayAdapter(Context context, String[] items, int current, boolean isHighlight) {
            super(context, items);
            this.currentValue = current;
            setTextSize(24);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (this.currentItem == this.currentValue && this.isHighlight) {
                view.setTextColor(-16776976);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public View getItem(int index, View cachedView, ViewGroup parent) {
            this.currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    private class DateNumericAdapter extends NumericWheelAdapter {
        public static final int DEFAULT_MAX_YEAR = 2050;
        public static final int DEFAULT_MIN_YEAR = 1930;
        int     currentItem;
        int     currentValue;
        boolean isHighlight;

        public DateNumericAdapter(DatePickerWheelView datePickerWheelView, Context context, int
                current) {
            this(context, DEFAULT_MIN_YEAR, DEFAULT_MAX_YEAR, current, false);
        }

        public DateNumericAdapter(Context context, int minValue, int maxValue, int current,
                                  boolean isHighlight) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            this.isHighlight = isHighlight;
            setTextSize(24);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (this.currentItem == this.currentValue && this.isHighlight) {
                view.setTextColor(-16776976);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public View getItem(int index, View cachedView, ViewGroup parent) {
            this.currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    public void setOnDatePickerListener(final OnDatePickerListener onDatePickerListener) {
        this.onDatePickerListener = onDatePickerListener;
        OnWheelChangedListener changedListener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (onDatePickerListener != null) {
                    onDatePickerListener.onDatePicker(DatePickerWheelView.this.getDateString());
                }
            }
        };
        this.year.addChangingListener(changedListener);
        this.month.addChangingListener(changedListener);
        this.day.addChangingListener(changedListener);
    }

    public DatePickerWheelView(Context context) {
        this(context, new Date());
    }

    public DatePickerWheelView(Context context, Date date) {
        super(context);
        this.calendar = Calendar.getInstance();
        this.clone = (Calendar) this.calendar.clone();
        this.listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                DatePickerWheelView.this.updateDays(DatePickerWheelView.this.year,
                        DatePickerWheelView.this.month, DatePickerWheelView.this.day);
            }
        };
        this.ctx = context;
        this.date = date;
        initUI();
    }

    public DatePickerWheelView(Context context, Date date, int minYear) {
        super(context);
        this.calendar = Calendar.getInstance();
        this.clone = (Calendar) this.calendar.clone();
        this.listener = /* anonymous class already generated */;
        this.ctx = context;
        this.date = date;
        this.minYear = minYear;
        initUI();
    }

    public DatePickerWheelView(Context context, Date date, int minYear, int maxYear) {
        super(context);
        this.calendar = Calendar.getInstance();
        this.clone = (Calendar) this.calendar.clone();
        this.listener = /* anonymous class already generated */;
        this.ctx = context;
        this.date = date;
        this.minYear = minYear;
        this.maxYear = maxYear;
        initUI();
    }

    public DatePickerWheelView(Context context, AttributeSet attrs, int defStyle) {
        this(context, new Date());
    }

    public DatePickerWheelView(Context context, AttributeSet attrs) {
        this(context, new Date());
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.el, null));
        setup();
    }

    private void setup() {
        this.clone.setTime(this.date);
        setYear();
        setMonth();
        setDay();
    }

    private void setYear() {
        if (this.maxYear == 0) {
            this.maxYear = DateNumericAdapter.DEFAULT_MAX_YEAR;
        }
        this.year = (WheelView) findViewById(R.id.year);
        int curYear = this.calendar.get(1);
        if (this.minYear != 0) {
            this.year.setViewAdapter(new DateNumericAdapter(this.ctx, this.minYear, this.maxYear,
                    curYear - this.minYear, false));
            this.year.setCurrentItem(this.clone.get(1) - this.minYear);
        } else {
            this.year.setViewAdapter(new DateNumericAdapter(this, this.ctx, curYear - 1930));
            this.year.setCurrentItem(this.clone.get(1) - 1930);
        }
        this.year.addChangingListener(this.listener);
    }

    private void setMonth() {
        this.month = (WheelView) findViewById(R.id.month);
        String[] months = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月",
                "10月", "11月", "12月"};
        this.month.setViewAdapter(new DateArrayAdapter(this.ctx, months, this.calendar.get(2),
                false));
        this.month.setCurrentItem(this.clone.get(2));
        this.month.addChangingListener(this.listener);
    }

    private void setDay() {
        this.day = (WheelView) findViewById(R.id.day);
        updateDays(this.year, this.month, this.day);
        this.day.setCurrentItem(this.clone.get(5) - 1);
    }

    void updateDays(WheelView year, WheelView month, WheelView day) {
        this.calendar.clear();
        this.calendar.set(1, this.calendar.get(1) + year.getCurrentItem());
        this.calendar.set(2, month.getCurrentItem());
        int maxDays = this.calendar.getActualMaximum(5);
        day.setViewAdapter(new DateNumericAdapter(this.ctx, 1, maxDays, this.calendar.get(5) - 1,
                false));
        day.setCurrentItem(Math.min(maxDays, day.getCurrentItem() + 1) - 1, true);
    }

    public int getYear() {
        if (this.minYear == 0) {
            return this.year.getCurrentItem() + DateNumericAdapter.DEFAULT_MIN_YEAR;
        }
        return this.year.getCurrentItem() + this.minYear;
    }

    public int getMonth() {
        return this.month.getCurrentItem() + 1;
    }

    public int getDay() {
        return this.day.getCurrentItem() + 1;
    }

    public String getDateString() {
        String dateString = getYear() + SocializeConstants.OP_DIVIDER_MINUS + String.format
                ("%02d", new Object[]{Integer.valueOf(getMonth())}) + SocializeConstants
                .OP_DIVIDER_MINUS + String.format("%02d", new Object[]{Integer.valueOf(getDay())});
        Helper.showLog(TAG, dateString);
        return dateString;
    }

    public Date getDate() {
        return DateHelper.parseString(getDateString());
    }

    public void setPickNumListener(final PickerScrollListener listener) {
        this.year.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                listener.onScroll();
            }
        });
    }
}
