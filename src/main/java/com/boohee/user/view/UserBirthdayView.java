package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.User;
import com.boohee.myview.DatePickerWheelView;
import com.boohee.one.R;
import com.boohee.utils.DateHelper;

public class UserBirthdayView extends FrameLayout {
    Context ctx;
    private User user;
    DatePickerWheelView wheelView;

    public UserBirthdayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserBirthdayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserBirthdayView(Context context) {
        super(context);
        init();
    }

    public UserBirthdayView(Context context, User user) {
        super(context);
        this.user = user;
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.nz, null));
        setUserProperty();
        setBottomPicker();
    }

    private void setUserProperty() {
        ((TextView) findViewById(R.id.user_property_text)).setText(R.string.d3);
    }

    public void setBottomPicker() {
        if (this.user == null) {
            this.wheelView = new DatePickerWheelView(this.ctx, DateHelper.parseString
                    ("1995-01-01"));
        } else {
            this.wheelView = new DatePickerWheelView(this.ctx, DateHelper.parseString(this.user
                    .birthday()));
        }
        ((LinearLayout) findViewById(R.id.picker_layout)).addView(this.wheelView);
    }

    public String getBirthday() {
        return this.wheelView.getDateString();
    }
}
