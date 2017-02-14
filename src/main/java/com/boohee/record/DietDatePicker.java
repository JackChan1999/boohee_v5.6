package com.boohee.record;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utils.DateHelper;
import com.umeng.socialize.common.SocializeConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class DietDatePicker extends FrameLayout {
    static final String TAG = DietDatePicker.class.getName();
    Calendar calendar = Calendar.getInstance();
    private Context   ctx;
    private WheelView date_picker_wheel;
    private final int daysCount = 60;
    private Date defaultDate;

    private class DayArrayAdapter extends AbstractWheelTextAdapter {
        protected DayArrayAdapter(Context context) {
            super(context, R.layout.f4, 0);
            setItemTextResource(R.id.monthday);
        }

        public View getItem(int index, View cachedView, ViewGroup parent) {
            int day = index - 30;
            Calendar newCalendar = (Calendar) DietDatePicker.this.calendar.clone();
            newCalendar.roll(6, day);
            View view = super.getItem(index, cachedView, parent);
            ((TextView) view.findViewById(R.id.weekday)).setText(new SimpleDateFormat("EEE")
                    .format(newCalendar.getTime()));
            TextView monthday = (TextView) view.findViewById(R.id.monthday);
            if (day == 0) {
                monthday.setText(DateHelper.format(newCalendar.getTime()) + SocializeConstants
                        .OP_OPEN_PAREN + DietDatePicker.this.ctx.getString(R.string.a9h) +
                        SocializeConstants.OP_CLOSE_PAREN);
                monthday.setTextColor(-16776976);
            } else {
                monthday.setText(DateHelper.format(newCalendar.getTime()));
                monthday.setTextColor(-15658735);
            }
            return view;
        }

        public int getItemsCount() {
            return 61;
        }

        protected CharSequence getItemText(int index) {
            return "";
        }
    }

    public DietDatePicker(Context context) {
        super(context);
        initUI();
    }

    public DietDatePicker(Context context, Date date) {
        super(context);
        this.defaultDate = date;
        initUI();
    }

    public DietDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public DietDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.f2, null);
        this.date_picker_wheel = (WheelView) view.findViewById(R.id.date_picker);
        addView(view);
        initDatePickerWheel();
    }

    private void initDatePickerWheel() {
        this.date_picker_wheel.setViewAdapter(new DayArrayAdapter(this.ctx));
        if (this.defaultDate == null) {
            this.date_picker_wheel.setCurrentItem(30);
        } else if (this.defaultDate.after(new Date())) {
            this.date_picker_wheel.setCurrentItem((30 - DateHelper.between(this.defaultDate)) + 1);
        } else {
            this.date_picker_wheel.setCurrentItem(30 - DateHelper.between(this.defaultDate));
        }
    }

    public Date getDate() {
        Calendar newCalendar = (Calendar) this.calendar.clone();
        newCalendar.roll(6, this.date_picker_wheel.getCurrentItem() - 30);
        return newCalendar.getTime();
    }

    public String getDateString() {
        return DateHelper.monthDay(getDate());
    }

    public void setScrollListener(final PickerScrollListener listener) {
        this.date_picker_wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                listener.onScroll();
            }
        });
    }
}
