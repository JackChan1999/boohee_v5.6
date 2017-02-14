package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.User;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.one.R;

public class UserTargetWeightView extends FrameLayout {
    Context ctx;
    private User user;
    IntFloatWheelView wheelView;

    public UserTargetWeightView(Context context) {
        super(context);
        init();
    }

    public UserTargetWeightView(Context context, User user) {
        super(context);
        this.user = user;
        init();
    }

    public UserTargetWeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserTargetWeightView(Context context, AttributeSet attrs) {
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
                .a81) + "(Kg)");
    }

    private void setBottomPicker() {
        LinearLayout picker_layout = (LinearLayout) findViewById(R.id.picker_layout);
        if (this.user != null) {
            this.wheelView = new IntFloatWheelView(this.ctx, this.user.target_weight);
        } else {
            this.wheelView = new IntFloatWheelView(this.ctx, 48.0f);
        }
        picker_layout.addView(this.wheelView);
    }

    public float getUserTargetWeight() {
        return this.wheelView.getCurrentFloat();
    }
}
