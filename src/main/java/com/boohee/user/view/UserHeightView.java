package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.User;
import com.boohee.myview.IntWheelView;
import com.boohee.one.R;

public class UserHeightView extends FrameLayout {
    static final String TAG = UserHeightView.class.getName();
    Context ctx;
    private User user;
    IntWheelView wheelView;

    public UserHeightView(Context context) {
        super(context);
        init();
    }

    public UserHeightView(Context context, User user) {
        super(context);
        this.user = user;
        init();
    }

    public UserHeightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserHeightView(Context context, AttributeSet attrs) {
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
                .o6) + "(cm)");
    }

    private void setBottomPicker() {
        LinearLayout picker_layout = (LinearLayout) findViewById(R.id.picker_layout);
        if (this.user != null) {
            this.wheelView = new IntWheelView(this.ctx, (int) this.user.height);
        } else {
            this.wheelView = new IntWheelView(this.ctx);
        }
        picker_layout.addView(this.wheelView);
    }

    public int getUserHeight() {
        return this.wheelView.getIntNum();
    }
}
