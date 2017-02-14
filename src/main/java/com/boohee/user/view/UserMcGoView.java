package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.boohee.myview.DatePickerWheelView;
import com.boohee.one.R;
import com.boohee.one.mine.McInitActivity;
import com.boohee.utils.DateHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserMcGoView extends FrameLayout {
    private Context             ctx;
    private TextView            hint;
    private LinearLayout        picker_layout;
    private DatePickerWheelView wheelView;

    public UserMcGoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserMcGoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserMcGoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.o0, null));
        setUserProperty();
    }

    private void setUserProperty() {
        this.picker_layout = (LinearLayout) findViewById(R.id.picker_layout);
        TextView text = (TextView) findViewById(R.id.user_property_text);
        this.hint = (TextView) findViewById(R.id.hint);
        text.setText(R.string.ov);
        ((RadioGroup) findViewById(R.id.switch_bottom)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yes:
                        UserMcGoView.this.setBottomVisible(Boolean.valueOf(false));
                        return;
                    case R.id.no:
                        UserMcGoView.this.setBottomVisible(Boolean.valueOf(true));
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private void setBottomVisible(Boolean displayBottom) {
        if (displayBottom.booleanValue()) {
            this.picker_layout.setVisibility(0);
            this.hint.setVisibility(0);
            setBottomPicker();
            return;
        }
        this.picker_layout.setVisibility(8);
        this.hint.setVisibility(8);
    }

    public void setBottomPicker() {
        this.wheelView = new DatePickerWheelView(this.ctx, DateHelper.parseString(new
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System
                .currentTimeMillis()))), McInitActivity.defaultMcCome);
        this.picker_layout.removeAllViews();
        this.picker_layout.addView(this.wheelView);
    }

    public String getLastGo() {
        if (this.wheelView != null) {
            return this.wheelView.getDateString();
        }
        return "";
    }
}
