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

public class UserTargetDateView extends FrameLayout {
    Context ctx;
    private User user;
    DatePickerWheelView wheelView;

    public UserTargetDateView(Context context) {
        super(context);
        init();
    }

    public UserTargetDateView(Context context, User user) {
        super(context);
        this.user = user;
        init();
    }

    public UserTargetDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserTargetDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.nz, null));
        setUserProperty();
        setBottomPicker();
    }

    private void setUserProperty() {
        ((TextView) findViewById(R.id.user_property_text)).setText(R.string.a7z);
    }

    public void setBottomPicker() {
        this.wheelView = new DatePickerWheelView(this.ctx, DateHelper.defaultTargetDate());
        ((LinearLayout) findViewById(R.id.picker_layout)).addView(this.wheelView);
    }

    public String getTargetDate() {
        return this.wheelView.getDateString();
    }
}
