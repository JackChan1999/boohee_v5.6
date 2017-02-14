package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.myview.IntWheelView;
import com.boohee.one.R;
import com.boohee.one.mine.McInitActivity;
import com.boohee.record.PickerScrollListener;

public class UserMcDaysView extends FrameLayout {
    static final String TAG = UserMcDaysView.class.getName();
    Context      ctx;
    int          day;
    IntWheelView wheelView;

    public UserMcDaysView(Context context, int day) {
        super(context);
        this.day = day;
        init();
    }

    public UserMcDaysView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserMcDaysView(Context context, AttributeSet attrs) {
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
        ((TextView) findViewById(R.id.user_property_text)).setText(this.ctx.getString(R.string
                .mc_days_unit));
    }

    private void setBottomPicker() {
        LinearLayout picker_layout = (LinearLayout) findViewById(R.id.picker_layout);
        if (this.day == 0) {
            this.wheelView = new IntWheelView(this.ctx, 30, 1, 5);
        } else {
            this.wheelView = new IntWheelView(this.ctx, 30, 1, this.day);
        }
        McInitActivity.defaultMcdays = this.wheelView.getCurrentNum();
        this.wheelView.setPickNumListener(new PickerScrollListener() {
            public void onScroll() {
                McInitActivity.defaultMcdays = UserMcDaysView.this.wheelView.getCurrentNum();
            }
        });
        picker_layout.addView(this.wheelView);
    }

    public int getMcDays() {
        return this.wheelView.getCurrentNum();
    }
}
