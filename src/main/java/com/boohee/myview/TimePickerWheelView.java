package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.boohee.one.R;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class TimePickerWheelView extends FrameLayout {
    static final String TAG = TimePickerWheelView.class.getName();
    private Context   ctx;
    private int       default_hour;
    private int       default_minute;
    private WheelView hours;
    private WheelView minutes;

    public TimePickerWheelView(Context context) {
        super(context);
        initUI();
    }

    public TimePickerWheelView(Context context, int hour, int minute) {
        super(context);
        this.default_hour = hour;
        this.default_minute = minute;
        initUI();
    }

    public TimePickerWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public TimePickerWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.mz, null));
        findView();
    }

    private void findView() {
        this.hours = (WheelView) findViewById(R.id.hour);
        this.hours.setViewAdapter(new NumericWheelAdapter(this.ctx, 0, 23));
        this.hours.setCurrentItem(this.default_hour);
        this.minutes = (WheelView) findViewById(R.id.mins);
        this.minutes.setViewAdapter(new NumericWheelAdapter(this.ctx, 0, 59, "%02d"));
        this.minutes.setCyclic(true);
        this.minutes.setCurrentItem(this.default_minute);
    }

    public void setHourViewAdapter(NumericWheelAdapter adapter) {
        this.hours.setViewAdapter(adapter);
    }

    public void setMinuteViewAdapter(AbstractWheelTextAdapter adapter) {
        this.minutes.setViewAdapter(adapter);
    }

    public int getHour() {
        return this.hours.getCurrentItem();
    }

    public int getMinute() {
        return this.minutes.getCurrentItem();
    }
}
